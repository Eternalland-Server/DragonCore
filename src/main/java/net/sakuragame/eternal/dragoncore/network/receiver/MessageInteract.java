package net.sakuragame.eternal.dragoncore.network.receiver;

import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class MessageInteract implements IMessage {
    private int entityId;
    private int hand;
    private Vector vector;

    @Override
    public void read(PacketBuffer buffer) {
        entityId = buffer.readInt();
        hand = buffer.readInt();
        if (hand != -1) {
            vector = new Vector(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    }

    @Override
    public void onMessage(Player player) {
        if (hand == -1)
            // 手攻击
            attack(player, entityId);
        else {
            if (hand == 0)
                interactAt(player, entityId, EnumHand.MAIN_HAND, vector);
            else if (hand == 1)
                interactAt(player, entityId, EnumHand.MAIN_HAND, vector);
        }
    }

    public void attack(Player p, int entityId) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity entity = worldserver.getEntity(entityId);
        if (entity != null) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityExperienceOrb) &&
                    !(entity instanceof EntityArrow) && (entity != player || player.isSpectator())) {
                player.attack(entity);
            }
        }
    }

    public void interactAt(Player p, int entityId, EnumHand hand, Vector vector) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity entity = worldserver.getEntity(entityId);
        if (entity != null) {

            PlayerInteractEntityEvent event = new PlayerInteractAtEntityEvent(p, entity.getBukkitEntity(), vector,
                    hand == EnumHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);

            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }
}
