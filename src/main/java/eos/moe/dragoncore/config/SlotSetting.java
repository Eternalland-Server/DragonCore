package eos.moe.dragoncore.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SlotSetting {
    private String identifier;
    private boolean skin;
    private boolean attribute;
    private ConfigurationSection section;

    public SlotSetting(ConfigurationSection section) {
        this.section = section;
        this.identifier = section.getName();
        this.skin = section.getBoolean("skin");
        this.attribute = section.getBoolean("attribute");
    }
}
