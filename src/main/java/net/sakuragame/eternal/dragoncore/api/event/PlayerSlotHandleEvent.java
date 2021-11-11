package net.sakuragame.eternal.dragoncore.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class PlayerSlotHandleEvent extends PlayerEvent implements Cancellable {

    private final String identifier;
    private final ItemStack item;
    private boolean cancel;

    private final static HandlerList handlerList = new HandlerList();

    public PlayerSlotHandleEvent(Player who, String identifier, ItemStack item) {
        super(who);
        this.identifier = identifier;
        this.item = item;
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
