package eos.moe.dragoncore.api.event;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ConfigLoadEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String fileName;
    private YamlConfiguration yaml;


    public ConfigLoadEvent(String fileName, YamlConfiguration yaml) {
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

    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable)this).isCancelled();
        } else {
            return true;
        }
    }
}
