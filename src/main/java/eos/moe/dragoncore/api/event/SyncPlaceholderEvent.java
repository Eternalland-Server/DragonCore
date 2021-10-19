package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class SyncPlaceholderEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Map<String, String> placeholders;
    private final Player player;

    public SyncPlaceholderEvent(Player player, Map<String, String> placeholders) {
        super(true);
        this.player = player;
        this.placeholders = placeholders;
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public Map<String, String> getPlaceholders() {
        return placeholders;
    }


    public Player getPlayer() {
        return player;
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
