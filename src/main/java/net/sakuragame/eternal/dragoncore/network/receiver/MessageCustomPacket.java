package net.sakuragame.eternal.dragoncore.network.receiver;

import net.sakuragame.eternal.dragoncore.api.gui.event.CustomPacketEvent;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageCustomPacket implements IMessage {

    private String identifier;
    private List<String> data;

    @Override
    public void read(PacketBuffer buffer) {
        identifier = buffer.readString();
        data = buffer.readStringList();
    }

    @Override
    public void onMessage(Player player) {
        new CustomPacketEvent(player, identifier, data).callEvent();
    }
}
