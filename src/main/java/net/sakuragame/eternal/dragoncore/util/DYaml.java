package net.sakuragame.eternal.dragoncore.util;

import org.bukkit.configuration.file.YamlConfiguration;

public class DYaml extends YamlConfiguration {
    private String toStringCache;

    @Override
    public String saveToString() {
        if (toStringCache == null) {
            toStringCache = super.saveToString();
        }
        return toStringCache;
    }

    @Override
    public void set(String path, Object value) {
        super.set(path, value);
        toStringCache = null;
    }

    @Override
    public Object get(String path) {
        toStringCache = null;
        return super.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        toStringCache = null;
        return super.get(path, def);
    }
}
