package net.sakuragame.eternal.dragoncore.api.event;

import net.sakuragame.eternal.dragoncore.api.slot.ClickType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSlotClickedEvent extends PlayerEvent {

    @Getter private final String identifier;
    @Getter private final ClickType clickType;
    private final static HandlerList handlerList = new HandlerList();

    public PlayerSlotClickedEvent(Player who, String identifier, ClickType clickType) {
        super(who);
        this.identifier = identifier;
        this.clickType = clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
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
