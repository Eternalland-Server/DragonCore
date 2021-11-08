package net.sakuragame.eternal.dragoncore.network.receiver;

import net.sakuragame.eternal.dragoncore.api.event.SyncPlaceholderEvent;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessagePlaceholder implements IMessage {

    private Map<String, String> placeholder;

    @Override
    public void read(PacketBuffer buffer) {
        placeholder = new HashMap<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            placeholder.put(buffer.readString(), buffer.readString());
        }
    }

    @Override
    public void onMessage(Player player) {
        SyncPlaceholderEvent syncPlaceholderEvent = new SyncPlaceholderEvent(player, new HashMap<>(placeholder));
        syncPlaceholderEvent.callEvent();

        Map<String, String> placeholders = syncPlaceholderEvent.getPlaceholders();
        placeholders.entrySet().removeIf(entry -> {
            if (entry.getValue() == null)
                return true;
            if (entry.getValue().equals(placeholder.get(entry.getKey())))
                return true;
            return false;
        });

        if (placeholders.size() > 0) {
            PacketSender.sendSyncPlaceholder(player, placeholders);
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
