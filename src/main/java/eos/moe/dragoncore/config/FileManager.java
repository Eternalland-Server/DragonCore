package eos.moe.dragoncore.config;

import com.taylorswiftcn.justwei.file.IConfiguration;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.config.sub.ConfigFile;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.DYaml;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class FileManager extends IConfiguration {

    private final DragonCore plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration keyConfig;
    @Getter private YamlConfiguration slotConfig;
    @Getter private YamlConfiguration worldTexture;

    @Getter private final HashMap<String, YamlConfiguration> armor;
    @Getter private final HashMap<String, YamlConfiguration> blood;
    @Getter private final HashMap<String, YamlConfiguration> entityModel;
    @Getter private final HashMap<String, YamlConfiguration> fontConfig;
    @Getter private final HashMap<String, YamlConfiguration> gui;
    @Getter private final HashMap<String, YamlConfiguration> itemIcon;
    @Getter private final HashMap<String, YamlConfiguration> itemModel;
    @Getter private final HashMap<String, YamlConfiguration> itemTip;

    @Getter private final static Map<String, SlotSetting> slotSettings = new HashMap<>();

    public FileManager(DragonCore plugin) {
        super(plugin);
        this.plugin = plugin;
        this.armor = new HashMap<>();
        this.blood = new HashMap<>();
        this.entityModel = new HashMap<>();
        this.fontConfig = new HashMap<>();
        this.gui = new HashMap<>();
        this.itemIcon = new HashMap<>();
        this.itemModel = new HashMap<>();
        this.itemTip = new HashMap<>();
    }

    @Override
    public void init() {
        config = initFile("config.yml");
        keyConfig = initFile("KeyConfig.yml");
        slotConfig = initFile("SlotConfig.yml");
        worldTexture = initFile("WorldTexture.yml");

        loadAllProfile();

        ConfigFile.init();
        loadSlotSetting();
    }

    public void sendYaml2Player(Player player) {
        for (String s : armor.keySet()) {
            PacketSender.sendYaml(player, FolderType.Armor.format(s), armor.get(s));
        }
        for (String s : blood.keySet()) {
            PacketSender.sendYaml(player, FolderType.Blood.format(s), blood.get(s));
        }
        for (String s : entityModel.keySet()) {
            PacketSender.sendYaml(player, FolderType.EntityModel.format(s), entityModel.get(s));
        }
        for (String s : fontConfig.keySet()) {
            PacketSender.sendYaml(player, FolderType.FontConfig.format(s), fontConfig.get(s));
        }
        for (String s : gui.keySet()) {
            PacketSender.sendYaml(player, FolderType.Gui.format(s), gui.get(s));
        }
        for (String s : itemIcon.keySet()) {
            PacketSender.sendYaml(player, FolderType.ItemIcon.format(s), itemIcon.get(s));
        }
        for (String s : itemModel.keySet()) {
            PacketSender.sendYaml(player, FolderType.ItemModel.format(s), itemModel.get(s));
        }
        for (String s : itemTip.keySet()) {
            PacketSender.sendYaml(player, FolderType.ItemTip.format(s), itemTip.get(s));
        }
    }

    private void loadAllProfile() {
        Bukkit.getConsoleSender().sendMessage("§6DragonCore - 开始载入文件");
        Bukkit.getConsoleSender().sendMessage("§a┏━━━━━━━━━ 开始载入Yml文件 ━━━━━━━━━");
        armor.putAll(getProfile(FolderType.Armor));
        blood.putAll(getProfile(FolderType.Blood));
        entityModel.putAll(getProfile(FolderType.EntityModel));
        fontConfig.putAll(getProfile(FolderType.FontConfig));
        gui.putAll(getProfile(FolderType.Gui));
        itemIcon.putAll(getProfile(FolderType.ItemIcon));
        itemModel.putAll(getProfile(FolderType.ItemModel));
        itemTip.putAll(getProfile(FolderType.ItemTip));
        Bukkit.getConsoleSender().sendMessage("§a┖━━━━━━━━━━ 文件载入完成 ━━━━━━━━━━━");
    }

    private void loadSlotSetting() {
        for (String key : slotConfig.getKeys(false)) {
            if ("Script".equals(key)) continue;
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section != null) {
                SlotSetting slotSetting = new SlotSetting(section);
                slotSettings.put(slotSetting.getIdentifier(), slotSetting);
            }
        }
    }

    private HashMap<String, YamlConfiguration> getProfile(FolderType type) {
        String subFolder = type.getName();

        HashMap<String, YamlConfiguration> yaml = new HashMap<>();
        File file = new File(plugin.getDataFolder(), subFolder);

        if (!file.exists()) {
            file.mkdirs();
        }
        else {
            List<File> files = getYamlFiles(file, new ArrayList<>());
            files.forEach(sub -> {
                yaml.put(sub.getName(), loadConfiguration(sub));
            });
        }

        Bukkit.getConsoleSender().sendMessage("§a┃ §6Loading " + subFolder + ":");
        for (String s : yaml.keySet()) {
            Bukkit.getConsoleSender().sendMessage("§a┃   - " + s);
        }
        Bukkit.getConsoleSender().sendMessage("§a┃");

        return yaml;
    }

    public static List<File> getYamlFiles(File folder, List<File> list) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    getYamlFiles(file, list);
                }
                else if (file.isFile() && file.getName().endsWith(".yml")) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        DYaml config = new DYaml();

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        }

        return config;
    }
}
