package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class YamlSendFinishedEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public YamlSendFinishedEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return getHANDLERS();
    }

    public static HandlerList getHANDLERS() {
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
