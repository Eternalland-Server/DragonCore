package net.sakuragame.eternal.dragoncore.api.event.slot;

import net.sakuragame.eternal.dragoncore.api.slot.ClickType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerSlotClickEvent extends PlayerEvent implements Cancellable {

    private final String identifier;
    private final ClickType clickType;
    private boolean cancel;
    private final static HandlerList handlerList = new HandlerList();

    public PlayerSlotClickEvent(Player who, String identifier, ClickType clickType) {
        super(who);
        this.identifier = identifier;
        this.clickType = clickType;
        this.cancel = false;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        return isCancelled();
    }
}
