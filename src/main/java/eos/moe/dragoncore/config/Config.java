package eos.moe.dragoncore.config;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.event.ConfigLoadEvent;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.DYaml;
import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

public class Config {
    public static boolean debug;
    public static int joinPacketDelay;
    public static String password;


    public static Map<String, SlotSetting> slotSettings;
    public static HashMap<String, YamlConfiguration> fileMap = new HashMap<>();
    public static Set<String> registeredKeys = new HashSet<>();


    public static void init(DragonCore plugin) {
        if (!plugin.getDataFolder().exists()) {
            saveResources();
        }
        
        plugin.reloadConfig();
        Bukkit.getConsoleSender().sendMessage("§6DragonCore - 开始载入文件");
        Bukkit.getConsoleSender().sendMessage("§a┏━━━━━━━━━ 开始载入Yml文件 ━━━━━━━━━");
        String folder = plugin.getDataFolder().getAbsolutePath();
        List<File> files = getYamlFiles(plugin.getDataFolder(), new ArrayList<>());
        fileMap.clear();
        for (File file : files) {
            String fileName = file.getAbsolutePath().substring(folder.length() + 1);
            Bukkit.getConsoleSender().sendMessage("§a┃ §6载入: " + fileName);
            YamlConfiguration yamlConfiguration = loadConfiguration(file);
            ConfigLoadEvent event = new ConfigLoadEvent(fileName, yamlConfiguration);
            try {
                yamlConfiguration.saveToString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bukkit.getPluginManager().callEvent(event);
            yamlConfiguration = event.getYaml();
            fileMap.put(fileName, yamlConfiguration);
        }

        loadSettings();
        Bukkit.getConsoleSender().sendMessage("§a┖━━━━━━━━━━ 文件载入完成 ━━━━━━━━━━━");
    }

    public static List<File> getYamlFiles(File folder, List<File> list) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getYamlFiles(file, list);
                } else if (file.isFile() && file.getName().endsWith(".yml")) {
                    list.add(file);
                }
            }
        }
        return list;
    }


    public static void sendYamlToClient() {
        Bukkit.getOnlinePlayers().forEach(Config::sendYamlToClient);
    }


    public static void sendYamlToClient(Player player) {
        PacketSender.sendClearCache(player);
        PacketSender.sendZipPassword(player);
        PacketSender.sendKeyRegister(player);
        PacketSender.sendPlayerWorld(player);
        String clientTitle = getConfig().getString("ClientTitle");
        if (clientTitle != null) {
            PacketSender.setWindowTitle(player, clientTitle);
        }
        for (Map.Entry<String, YamlConfiguration> entry : fileMap.entrySet()) {
            if (entry.getKey().equals("config.yml")) continue;
            if (entry.getKey().equals("KeyConfig.yml")) continue;
            if (entry.getKey().equals("SlotConfig.yml")) continue;
            PacketSender.sendYaml(player, entry.getKey(), entry.getValue());
        }
        PacketSender.sendFinished(player);
    }

    public static boolean replaceChatColor;

    public static void loadSettings() {
        YamlConfiguration config = getConfig();
        replaceChatColor = config.getBoolean("replaceChatColor");
        debug = config.getBoolean("debug");
        joinPacketDelay = config.getInt("JoinPacketDelay", 60);
        password = config.getString("Password", "unknown");

        slotSettings = new HashMap<>();
        config = getSlotConfig();
        for (String key : config.getKeys(false)) {
            if ("Script".equals(key)) continue;
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section != null) {
                SlotSetting slotSetting = new SlotSetting(section);
                slotSettings.put(slotSetting.getIdentifier(), slotSetting);
            }
        }
    }

    public static YamlConfiguration getConfig() {
        return fileMap.get("config.yml");
    }

    public static YamlConfiguration getSlotConfig() {
        return fileMap.get("SlotConfig.yml");
    }

    @SneakyThrows
    private static void saveResources() {
        URI uri = Config.class.getResource("/resources").toURI();
        Path myPath;

        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            myPath = fileSystem.getPath("/resources");
        } else {
            myPath = Paths.get(uri);
        }
        DragonCore plugin = DragonCore.getInstance();
        Stream<Path> walk = Files.walk(myPath, 10);
        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            String name = it.next().toString();
            if (name.endsWith(".yml")) {
                File file = new File(plugin.getDataFolder(), name.substring(11));
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    try (InputStream resource = plugin.getResource(name.substring(1))) {
                        Files.copy(resource, file.toPath());
                    }
                }
            }
        }
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        DYaml config = new DYaml();

        try {
            config.load(file);
        } catch (FileNotFoundException var3) {
        } catch (IOException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        } catch (InvalidConfigurationException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var5);
        }

        return config;
    }

}
