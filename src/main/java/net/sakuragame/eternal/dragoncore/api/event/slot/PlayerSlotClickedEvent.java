package net.sakuragame.eternal.dragoncore.api.event.slot;

import lombok.Getter;
import net.sakuragame.eternal.dragoncore.api.slot.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerSlotClickedEvent extends PlayerEvent {

    private final String identifier;
    private final ClickType clickType;
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

    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
