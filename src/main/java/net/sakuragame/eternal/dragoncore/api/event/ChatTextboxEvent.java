package net.sakuragame.eternal.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class ChatTextboxEvent extends PlayerEvent  {
    private static final HandlerList HANDLERS = new HandlerList();
    private String lastText;
    private String currentText;


    public ChatTextboxEvent(Player who, String lastText, String currentText) {
        super(who);
        this.lastText = lastText;
        this.currentText = currentText;
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }

    public String getLastText() {
        return lastText;
    }

    public String getCurrentText() {
        return currentText;
    }


    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable)this).isCancelled();
        } else {
            return true;
        }
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public void desc() {
        System.out.println("该事件为 玩家在聊天栏输入文字 的触发事件");
        System.out.println("每输入一个文字都会触发一次事件");
        System.out.println("参数:");
        System.out.println("  lastText    上次的文本");
        System.out.println("  currentText 当前的文本");
    }
}
