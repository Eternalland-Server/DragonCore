package net.sakuragame.eternal.dragoncore.network.receiver;

import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

public interface IMessage {
    void read(PacketBuffer buffer);

    void onMessage(Player player);

    default boolean isAsync(){
        return false;
    }
}
