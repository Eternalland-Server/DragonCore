package net.sakuragame.eternal.dragoncore.config.sub;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.DragonCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigFile {
    private static YamlConfiguration config;

    public static int joinPacketDelay;
    public static String password;
    public static String clientTitle;
    public static boolean replaceChatColor;
    public static boolean dragonArmourers;
    public static boolean slotEnable;

    public static Set<String> registeredKeys = new HashSet<>();

    public static void init() {
        config = DragonCore.getInstance().getFileManager().getConfig();

        joinPacketDelay = config.getInt("JoinPacketDelay", 60);
        password = config.getString("Password", "unknown");
        clientTitle = "亘古大陆";
        replaceChatColor = config.getBoolean("replaceChatColor");
        dragonArmourers = config.getBoolean("DragonArmourers");
        slotEnable = config.getBoolean("SlotEnable", true);
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }
}
