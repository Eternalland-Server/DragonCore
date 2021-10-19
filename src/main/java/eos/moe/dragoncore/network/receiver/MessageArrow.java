package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.network.PacketBuffer;
import eos.moe.dragoncore.util.Utils;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MovingObjectPosition;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessageArrow implements IMessage {
    private int arrow;
    private int target;

    @Override
    public void read(PacketBuffer buffer) {
        this.arrow = buffer.readInt();
        this.target = buffer.readInt();
    }

    @Override
    public void onMessage(Player p) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity arrow = worldserver.getEntity(this.arrow);
        Entity target = worldserver.getEntity(this.target);

        if (arrow == null || target == null || !(target.getBukkitEntity() instanceof LivingEntity)) {
            return;
        }
        CraftEntity bukkitEntity = arrow.getBukkitEntity();
        if (!(bukkitEntity instanceof Projectile)) {
            return;
        }
        if (!p.equals(((Projectile) bukkitEntity).getShooter())) {
            return;
        }
        Method a1 = Utils.getDeclaredMethod(arrow.getClass(), "a", MovingObjectPosition.class);
        if (a1 != null) {
            a1.setAccessible(true);
            try {
                a1.invoke(arrow, new MovingObjectPosition(target));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
