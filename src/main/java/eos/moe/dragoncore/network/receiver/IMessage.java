package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.network.PacketBuffer;
import org.bukkit.entity.Player;

public interface IMessage {
    void read(PacketBuffer buffer);

    void onMessage(Player player);

    default boolean isAsync(){
        return false;
    }
}
