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

public class MessageTileRequest implements IMessage {

    private BlockPosition blockPosition;

    @Override
    public void read(PacketBuffer buffer) {
        blockPosition = BlockPosition.fromLong(buffer.readLong());
    }

    @Override
    public void onMessage(Player player) {
        if (!player.hasPermission(CommandPerms.ADMIN.getNode())) return;

        World world = player.getWorld();
        CraftWorld world1 = (CraftWorld) world;
        WorldServer handle = world1.getHandle();

        TileEntity tileEntityAt = handle.getWorld().getTileEntityAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (tileEntityAt != null) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            tileEntityAt.save(nbtTagCompound);
            PacketSenderWithNBTEdit.sendTileNBTPacket(player, blockPosition, nbtTagCompound);
        } else {
            player.sendMessage(" §7当前所指方块无NBT数据");
        }
    }
}
