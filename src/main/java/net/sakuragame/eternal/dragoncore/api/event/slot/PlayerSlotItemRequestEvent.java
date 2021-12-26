package net.sakuragame.eternal.dragoncore.api.event.slot;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class PlayerSlotItemRequestEvent extends PlayerEvent {

    private final String identifier;
    private ItemStack item;

    private final static HandlerList handlerList = new HandlerList();

    public PlayerSlotItemRequestEvent(Player who, String identifier) {
        super(who);
        this.identifier = identifier;
        this.item = new ItemStack(Material.AIR);
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
