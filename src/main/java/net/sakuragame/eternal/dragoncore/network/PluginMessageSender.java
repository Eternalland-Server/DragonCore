package net.sakuragame.eternal.dragoncore.network;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import net.sakuragame.eternal.dragoncore.DragonCore;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class PluginMessageSender {
    /**
     * 发包给所有玩家
     *
     * @param iPacket
     */
    protected static void sendPluginMessage(int packetID, IPacket iPacket) {
        List<byte[]> b = generatePackets(packetID, iPacket);
        for (byte[] bytes : b) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendPluginMessage(player, bytes);
            }
        }
    }

    /**
     * 发包给部分玩家
     *
     * @param players
     * @param iPacket
     */
    protected static void sendPluginMessage(List<Player> players, int packetID, IPacket iPacket) {
        List<byte[]> b = generatePackets(packetID, iPacket);
        for (byte[] bytes : b) {
            for (Player player : players) {
                sendPluginMessage(player, bytes);
            }
        }
    }

    /**
     * 发包给一个玩家
     *
     * @param player
     * @param iPacket
     */
    protected static void sendPluginMessage(Player player, int packetID, IPacket iPacket) {
        List<byte[]> b = generatePackets(packetID, iPacket);
        for (byte[] bytes : b) {
            sendPluginMessage(player, bytes);
        }
    }

    protected static List<byte[]> generatePackets(int packetID, IPacket iPacket) {
        PacketBuffer buffer = new PacketBuffer();
        if (iPacket != null) {
            iPacket.write(buffer);
        }
        if (buffer.readableBytes() > 30000) {
            List<byte[]> list = new ArrayList<>();
            byte[] bytes;
            while ((bytes = readBytes(buffer, 30000)) != null) {
                bytes = Bytes.concat(intToBytes(packetID),intToBytes(0), bytes);
                list.add(bytes);
            }
            bytes = Bytes.concat(intToBytes(packetID), intToBytes(1));
            list.add(bytes);
            buffer.release();
            return list;
        } else {
            byte[] bytes = Bytes.concat(intToBytes(packetID),intToBytes(1), readBytes(buffer));
            buffer.release();
            return ImmutableList.of(bytes);
        }
    }

    /**
     * 发包给所有玩家
     *
     * @param bytes
     */
    protected static void sendPluginMessage(byte[] bytes) {
        byte[] b = Bytes.concat(new byte[]{64}, compressedByteArray(bytes));
        Bukkit.getOnlinePlayers().stream().filter(p -> p != null && p.isOnline()).forEach(p -> p.sendPluginMessage(DragonCore.getInstance(), "dragoncore:main", b));
    }

    /**
     * 发包给部分玩家
     *
     * @param players
     * @param bytes
     */
    protected static void sendPluginMessage(List<Player> players, byte[] bytes) {
        byte[] b = Bytes.concat(new byte[]{64}, compressedByteArray(bytes));
        players.stream().filter(p -> p != null && p.isOnline()).forEach(p -> p.sendPluginMessage(DragonCore.getInstance(), "dragoncore:main", b));
    }

    /**
     * 发包给一个玩家
     *
     * @param player
     * @param bytes
     */
    protected static void sendPluginMessage(Player player, byte[] bytes) {
        if (player != null && player.isOnline()) {
            byte[] b = Bytes.concat(new byte[]{64}, compressedByteArray(bytes));
            player.sendPluginMessage(DragonCore.getInstance(), "dragoncore:main", b);
        }
    }

    /**
     * 对数据进行压缩
     *
     * @param data
     * @return
     */
    protected static byte[] compressedByteArray(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;
        try {
            gzos = new GZIPOutputStream(baos);
            gzos.write(data);
            gzos.close();
        } catch (IOException e) {
            e.printStackTrace();
            closeQuietly(baos);
            closeQuietly(gzos);
            return null;
        } finally {
            closeQuietly(baos);
            closeQuietly(gzos);
        }
        return baos.toByteArray();
    }

    protected static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * 读取ByteBuf内所有字节
     *
     * @param buf
     * @return
     */
    protected static byte[] readBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        int readerIndex = buf.readerIndex();
        buf.getBytes(readerIndex, bytes);
        return bytes;
    }

    protected static byte[] readBytes(ByteBuf buf, int length) {
        byte[] bytes = new byte[Math.min(buf.readableBytes(), length)];
        if (buf.readableBytes() > 0) {
            buf.readBytes(bytes);
        } else {
            return null;
        }
        return bytes;
    }

    protected static byte[] intToBytes(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    protected interface IPacket {
        void write(PacketBuffer buffer);
    }
}
