package net.sakuragame.eternal.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public class EntityLeaveWorldEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private UUID uuid;

    public EntityLeaveWorldEvent(Player who, UUID uuid) {
        super(who);
        this.uuid = uuid;
    }

    public UUID getEntityUUID() {
        return uuid;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
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
