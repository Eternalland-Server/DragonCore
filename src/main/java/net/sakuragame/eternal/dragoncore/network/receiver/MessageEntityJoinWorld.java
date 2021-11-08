package net.sakuragame.eternal.dragoncore.network.receiver;

import net.sakuragame.eternal.dragoncore.api.event.EntityJoinWorldEvent;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
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
