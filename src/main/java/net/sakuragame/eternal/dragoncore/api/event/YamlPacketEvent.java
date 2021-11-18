package net.sakuragame.eternal.dragoncore.api.event;

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

    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }
}