package eos.moe.dragoncore.config.sub;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;


public class WorldTextureConfig {

    private static DragonCore plugin = DragonCore.getInstance();

    public static YamlConfiguration getConfig() {
        return plugin.getFileManager().getWorldTexture();
    }

    public static void saveConfig() {
        try {
            YamlConfiguration config = plugin.getFileManager().getWorldTexture();
            config.save(new File(plugin.getDataFolder(), "WorldTexture.yml"));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PacketSender.sendYaml(onlinePlayer, "WorldTexture.yml", config);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
