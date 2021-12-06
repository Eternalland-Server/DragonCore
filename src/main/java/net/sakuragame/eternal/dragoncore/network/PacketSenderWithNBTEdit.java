package net.sakuragame.eternal.dragoncore.network;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTReadLimiter;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.entity.Player;

import java.io.DataOutput;
import java.io.IOException;

public class PacketSenderWithNBTEdit extends PluginMessageSender {
    
    public static void sendMouseOverPacket(Player player) {
        PluginMessageSender.sendPluginMessage(player, 1001, null);
    }

    public static void sendTileNBTPacket(Player player, BlockPosition blockPosition, NBTTagCompound nbt) {
        PluginMessageSender.sendPluginMessage(player, 1002, buffer -> {
            buffer.writeLong(blockPosition.asLong());
            writeNBT(buffer, nbt);
        });
    }

    public static void sendEntityNBTPacket(Player player, int entityId, NBTTagCompound nbt) {
        PluginMessageSender.sendPluginMessage(player, 1003, buffer -> {
            buffer.writeInt(entityId);
            writeNBT(buffer, nbt);
        });
    }

    public static void sendItemNBTPacket(Player player, NBTTagCompound nbt) {
        PluginMessageSender.sendPluginMessage(player, 1004, buffer -> writeNBT(buffer, nbt));
    }

    public static void writeNBT(PacketBuffer buffer, NBTTagCompound nbt) {
        if (nbt == null) {
            buffer.writeByte(0);
        } else {
            try {
                NBTCompressedStreamTools.a(nbt, (DataOutput) new ByteBufOutputStream(buffer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static NBTTagCompound readNBT(ByteBuf buf) {
        int index = buf.readerIndex();
        byte isNull = buf.readByte();
        if (isNull == 0) {
            return null;
        }
        buf.readerIndex(index);
        try {
            return NBTCompressedStreamTools.a(new ByteBufInputStream(buf), new NBTReadLimiter(2097152L));
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }
}