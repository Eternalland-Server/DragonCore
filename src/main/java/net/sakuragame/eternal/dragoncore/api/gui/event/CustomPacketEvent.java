package net.sakuragame.eternal.dragoncore.api.gui.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class CustomPacketEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String identifier;
    private final List<String> data;
    private boolean cancelled;

    public CustomPacketEvent(Player who, String identifier, List<String> data) {
        super(who);

        this.identifier = identifier;
        this.data = data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public void desc() {

    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        return !((Cancellable)this).isCancelled();
    }
}
