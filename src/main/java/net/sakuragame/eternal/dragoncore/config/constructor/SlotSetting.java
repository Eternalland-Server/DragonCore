package net.sakuragame.eternal.dragoncore.config.constructor;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SlotSetting {
    private final String identifier;
    private final boolean skin;
    private final boolean ignoreOnlySuit;

    public SlotSetting(ConfigurationSection section) {
        this.identifier = section.getName();
        this.skin = section.getBoolean("skin");
        this.ignoreOnlySuit = section.getBoolean("ignore-only-suit", false);
    }
}
