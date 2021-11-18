package net.sakuragame.eternal.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class YamlSendFinishedEvent extends PlayerEvent {

    private final static HandlerList handlerList = new HandlerList();

    public YamlSendFinishedEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
