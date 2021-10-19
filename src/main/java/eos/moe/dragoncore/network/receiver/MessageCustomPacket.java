package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
