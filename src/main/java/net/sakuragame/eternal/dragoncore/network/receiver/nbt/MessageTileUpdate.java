package net.sakuragame.eternal.dragoncore.network.receiver.nbt;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.WorldServer;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.sakuragame.eternal.dragoncore.network.PacketSenderWithNBTEdit;
import net.sakuragame.eternal.dragoncore.network.receiver.IMessage;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

public class MessageTileUpdate implements IMessage {

    private BlockPosition blockPosition;
    private NBTTagCompound nbt;

    @Override
    public void read(PacketBuffer buffer) {
        blockPosition = BlockPosition.fromLong(buffer.readLong());
        nbt = PacketSenderWithNBTEdit.readNBT(buffer);
    }

    @Override
    public void onMessage(Player player) {
        if (!player.hasPermission(CommandPerms.ADMIN.getNode())) return;

        World world = player.getWorld();
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer handle = craftWorld.getHandle();

        TileEntity tileEntityAt = handle.getWorld().getTileEntityAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (tileEntityAt != null) {
            try {
                tileEntityAt.load(nbt);
                tileEntityAt.update();
                if (tileEntityAt.getWorld() != null && tileEntityAt.getWorld() instanceof WorldServer) {
                    WorldServer tileEntityAtWorld = (WorldServer) tileEntityAt.getWorld();
                    tileEntityAtWorld.getPlayerChunkMap().flagDirty(blockPosition);
                }
                player.sendMessage(" §7修改方块NBT完成");
            } catch (Throwable e) {
                player.sendMessage(" §7修改失败，请查看后台报错");
                e.printStackTrace();
            }
        } else {
            player.sendMessage(" §7当前修改方块不存在");
        }
    }

}
