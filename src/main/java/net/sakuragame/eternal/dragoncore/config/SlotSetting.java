package net.sakuragame.eternal.dragoncore.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SlotSetting {
    private final String identifier;
    private final boolean skin;

    public SlotSetting(ConfigurationSection section) {
        this.identifier = section.getName();
        this.skin = section.getBoolean("skin");
    }
}
