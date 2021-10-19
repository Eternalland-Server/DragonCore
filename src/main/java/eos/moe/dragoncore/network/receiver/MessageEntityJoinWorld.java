package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.api.event.EntityJoinWorldEvent;
import eos.moe.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageEntityJoinWorld implements IMessage {
    private UUID uuid;

    @Override
    public void read(PacketBuffer buffer) {
        uuid = buffer.readUniqueId();
    }

    @Override
    public void onMessage(Player player) {
        new EntityJoinWorldEvent(player, uuid).callEvent();
    }
}
