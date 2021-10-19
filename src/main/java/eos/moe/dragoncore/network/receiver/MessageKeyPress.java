package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.api.KeyPressEvent;
import eos.moe.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageKeyPress implements IMessage {
    private String key;
    private List<String> keys;

    @Override
    public void read(PacketBuffer buffer) {
        key = buffer.readString();
        keys = buffer.readStringList();
    }

    @Override
    public void onMessage(Player player) {
        new KeyPressEvent(player, key, keys).callEvent();
    }
}
