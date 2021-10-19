package eos.moe.dragoncore.config;

import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;


public class WorldTextureConfig {
    public static YamlConfiguration getConfig() {
        return Config.fileMap.get("WorldTexture.yml");
    }

    public static void saveConfig() {
        try {
            YamlConfiguration config = Config.fileMap.get("WorldTexture.yml");
            config.save(new File(
                    "plugins" + File.separator + "DragonCore" + File.separator + "WorldTexture.yml")
            );
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PacketSender.sendYaml(onlinePlayer, "WorldTexture.yml", config);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
