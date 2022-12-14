package net.sakuragame.eternal.dragoncore.api.event;

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

    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
