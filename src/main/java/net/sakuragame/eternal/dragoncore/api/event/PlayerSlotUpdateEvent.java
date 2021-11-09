package net.sakuragame.eternal.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerSlotUpdateEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String identifier;
    private final ItemStack itemStack;


    public PlayerSlotUpdateEvent(Player who, String identifier, ItemStack itemStack) {
        super(who);
        this.identifier = identifier;
        this.itemStack = itemStack;
    }


    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getItemStack() {
        return itemStack;
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
            return !((Cancellable)this).isCancelled();
        } else {
            return true;
        }
    }
}