package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.api.event.ChatTextboxEvent;
import eos.moe.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

public class MessageChatTextbox implements IMessage {
    private String lastText;
    private String currentText;

    @Override
    public void read(PacketBuffer buffer) {
        lastText = buffer.readString();
        currentText = buffer.readString();
    }

    @Override
    public void onMessage(Player player) {
        new ChatTextboxEvent(player, lastText, currentText).callEvent();
    }
}
