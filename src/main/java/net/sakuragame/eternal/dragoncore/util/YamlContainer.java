package net.sakuragame.eternal.dragoncore.util;

import org.bukkit.configuration.file.YamlConfiguration;

public class YamlContainer extends YamlConfiguration {
    private String cache;

    @Override
    public String saveToString() {
        if (cache == null) {
            cache = super.saveToString();
            return cache;
        }
        return cache;
    }

    @Override
    public void set(String path, Object value) {
        super.set(path, value);
        cache = null;
    }

    @Override
    public Object get(String path) {
        cache = null;
        return super.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        cache = null;
        return super.get(path, def);
    }
}
