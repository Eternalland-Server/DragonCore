package net.sakuragame.eternal.dragoncore.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SlotSetting {
    private final String identifier;
    private final boolean skin;
    private final boolean attribute;
    private final boolean realmLimit;
    private final int equipType;

    public SlotSetting(ConfigurationSection section) {
        this.identifier = section.getName();
        this.skin = section.getBoolean("skin");
        this.attribute = section.getBoolean("attribute");
        this.realmLimit = section.getBoolean("realm-limit");
        this.equipType = section.getInt("equip-type");
    }
}
