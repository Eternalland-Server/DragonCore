package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


public class YamlPacketEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private String fileName;
    private YamlConfiguration yaml;


    public YamlPacketEvent(Player player, String fileName, YamlConfiguration yaml) {
        super(player);
        this.fileName = fileName;
        this.yaml = yaml;
        //throw new RuntimeException("芜湖，YamlPacketEvent已被弃用")
    }

    public static HandlerList getHANDLERS() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getFileName() {
        return fileName;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public void setYaml(YamlConfiguration yaml) {
        this.yaml = yaml;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
