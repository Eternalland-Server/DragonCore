package eos.moe.dragoncore.network;


import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.network.receiver.*;
import eos.moe.dragoncore.util.Scheduler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

public class PluginMessageReceiver implements PluginMessageListener {

    private final DragonCore plugin;
    private final Map<Integer, Class<? extends IMessage>> messageHandlers;

    public PluginMessageReceiver(DragonCore plugin) {
        this.plugin = plugin;
        messageHandlers = new HashMap<>();
        messageHandlers.put(3, MessagePlaceholder.class);
        messageHandlers.put(4, MessageEntityJoinWorld.class);
        messageHandlers.put(5, MessageKeyPress.class);
        messageHandlers.put(6, MessageChatTextbox.class);
        messageHandlers.put(7, MessageSetModel.class);
        messageHandlers.put(9, MessageInteract.class);
        messageHandlers.put(10, MessageArrow.class);
        //messageHandlers.put(11, MessageEntityLeaveWorld.class);
        messageHandlers.put(100, MessageCustomPacket.class);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        try {
            byte b = buf.readByte();
            if (b != 64) return;

            PacketBuffer pb = new PacketBuffer(buf);
            int packetID = pb.readInt();
            Class<? extends IMessage> aClass = messageHandlers.get(packetID);
            if (aClass != null) {
                IMessage iMessage = aClass.newInstance();
                iMessage.read(pb);
                if (iMessage.isAsync()) {
                    Scheduler.runAsync(() -> iMessage.onMessage(player));
                } else {
                    Scheduler.run(() -> iMessage.onMessage(player));
                }
            }
        } catch (Throwable e) {
            plugin.getLogger().info("处理客户端发包过程中出现异常");
            e.printStackTrace();
        } finally {
            buf.release();
        }
    }
}
