package net.sakuragame.eternal.dragoncore.config;

import com.taylorswiftcn.justwei.file.JustConfiguration;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.config.constructor.SlotSetting;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import lombok.Getter;
import net.sakuragame.eternal.dragoncore.util.YamlContainer;
import org.apache.commons.lang3.Validate;
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

public class FileManager extends JustConfiguration {

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
    @Getter private final static List<String> fontReplace = new ArrayList<>();

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
        loadFontReplace();
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
        PacketSender.sendYaml(player, "WorldTexture.yml", worldTexture);
    }

    private void loadAllProfile() {
        armor.clear();
        blood.clear();
        entityModel.clear();
        fontConfig.clear();
        gui.clear();
        itemIcon.clear();
        itemModel.clear();
        itemTip.clear();
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
        slotSettings.clear();
        for (String key : slotConfig.getKeys(false)) {
            ConfigurationSection section = slotConfig.getConfigurationSection(key);
            if (section != null) {
                SlotSetting slotSetting = new SlotSetting(section);
                slotSettings.put(slotSetting.getIdentifier(), slotSetting);
            }
        }
    }

    private void loadFontReplace() {
        fontReplace.clear();
        for (YamlConfiguration yaml : fontConfig.values()) {
            fontReplace.addAll(yaml.getKeys(false));
        }
    }

    private HashMap<String, YamlConfiguration> getProfile(FolderType type) {
        String subFolder = type.getName();

        HashMap<String, YamlConfiguration> profiles = new HashMap<>();
        File file = new File(plugin.getDataFolder(), subFolder);

        if (!file.exists()) {
            file.mkdirs();
        }
        else {
            List<File> files = getYamlFiles(file, new ArrayList<>());
            files.forEach(sub -> {
                YamlConfiguration yaml = loadConfiguration(sub);
                try {
                    yaml.saveToString();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                profiles.put(sub.getName(), yaml);
            });
        }

        Bukkit.getConsoleSender().sendMessage("§a┃ §b" + type.getName() + "§a loaded: " + profiles.size());

        return profiles;
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
        YamlContainer container = new YamlContainer();

        try {
            container.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        }

        return container;
    }

    public void saveWorldTexture() {
        try {
            worldTexture.save(new File(plugin.getDataFolder(), "WorldTexture.yml"));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PacketSender.sendYaml(onlinePlayer, "WorldTexture.yml", worldTexture);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
