package net.sakuragame.eternal.dragoncore.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class KeyPressEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private String key;
    private List<String> keys;
    private boolean cancelled;

    public KeyPressEvent(Player who, String key, List<String> keys) {
        super(who);
        this.key = key;
        this.keys = keys;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getKey() {
        return key;
    }

    public List<String> getKeys() {
        return keys;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public void desc() {
        System.out.println("该事件为 玩家按下某按键 的触发事件");
        System.out.println("未CoreAPI.registerKey的按键不会出现事件");
        System.out.println("参数:");
        System.out.println("  key  当前按下的按键");
        System.out.println("  keys 当前所有按下的按键");
    }

    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        return !((Cancellable)this).isCancelled();
    }
}
