package net.sakuragame.eternal.dragoncore.network.receiver.nbt;

import net.minecraft.server.v1_12_R1.*;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.sakuragame.eternal.dragoncore.network.PacketSenderWithNBTEdit;
import net.sakuragame.eternal.dragoncore.network.receiver.IMessage;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

public class MessageEntityUpdate implements IMessage {

    private int entityID;
    private NBTTagCompound nbt;

    @Override
    public void read(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.nbt = PacketSenderWithNBTEdit.readNBT(buffer);
    }

    @Override
    public void onMessage(Player player) {
        if (!player.hasPermission(CommandPerms.ADMIN.getNode())) return;

        World world = player.getWorld();
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer handle = craftWorld.getHandle();
        Entity entity = handle.getEntity(entityID);
        if (entity == null) {
            player.sendMessage(" §7当前修改实体不存在");
            return;
        }

        try {
            EnumGamemode preGameType = null;
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                preGameType = entityPlayer.playerInteractManager.getGameMode();
            }
            entity.f(nbt);
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                entityPlayer.updateInventory(entityPlayer.defaultContainer);
                EnumGamemode type = entityPlayer.playerInteractManager.getGameMode();
                if (preGameType != type) {
                    entityPlayer.a(type);
                }
                ((EntityPlayer) entity).playerConnection.sendPacket(new PacketPlayOutUpdateHealth(
                        entityPlayer.getHealth(), entityPlayer.getFoodData().getFoodLevel(), entityPlayer.getFoodData().getSaturationLevel()
                ));
                ((EntityPlayer) entity).playerConnection.sendPacket(new PacketPlayOutExperience(
                        entityPlayer.exp, entityPlayer.expTotal, entityPlayer.expLevel
                ));
                entityPlayer.updateAbilities();
                entityPlayer.setPosition(entityPlayer.locX, entityPlayer.locY, entityPlayer.locZ);
            }
            player.sendMessage(" §7修改实体NBT完成");
        }
        catch (Throwable t) {
            player.sendMessage(" §7修改失败，请查看后台报错");
            t.printStackTrace();
        }
    }
}
