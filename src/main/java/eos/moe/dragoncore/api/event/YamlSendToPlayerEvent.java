package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class YamlSendToPlayerEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public YamlSendToPlayerEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable) this).isCancelled();
        } else {
            return true;
        }
    }
}
