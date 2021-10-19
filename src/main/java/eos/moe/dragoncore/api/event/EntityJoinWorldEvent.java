package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public class EntityJoinWorldEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private UUID uuid;

    public EntityJoinWorldEvent(Player who, UUID uuid) {
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


    public void desc() {
        System.out.println("该事件为 客户端某实体加入世界 的触发事件");
        System.out.println("掉落物不会触发事件");
        System.out.println("PS: 当客户端离开实体范围，又再次进入实体范围，也会触发该事件");
        System.out.println("参数:");
        System.out.println("  Player  玩家");
        System.out.println("  UUID    加入世界的实体的UUID");
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
