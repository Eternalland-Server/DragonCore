package net.sakuragame.eternal.dragoncore.config.constructor;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SlotSetting {
    private final String identifier;
    private final boolean skin;
    private final SkinType type;

    public SlotSetting(ConfigurationSection section) {
        this.identifier = section.getName();
        this.skin = section.getBoolean("skin");
        this.type = SkinType.match(section.getInt("skin-type", -1));
    }
}
