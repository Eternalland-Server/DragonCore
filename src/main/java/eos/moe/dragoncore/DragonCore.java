package eos.moe.dragoncore;


import eos.moe.dragoncore.commands.MainCommand;
import eos.moe.dragoncore.config.FileManager;
import eos.moe.dragoncore.config.sub.ConfigFile;
import eos.moe.dragoncore.database.IDataBase;
import eos.moe.dragoncore.database.YamlRepository;
import eos.moe.dragoncore.database.MysqlRepository;
import eos.moe.dragoncore.listener.*;
import eos.moe.dragoncore.listener.misc.MiscManager;
import eos.moe.dragoncore.mythicmobs.listener.MythicMobsListener;
import eos.moe.dragoncore.network.PluginMessageReceiver;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class DragonCore extends JavaPlugin {
    private static final Logger log = LogManager.getLogger();
    private static DragonCore instance;

    private MiscManager miscManager;
    private FileManager fileManager;
    private IDataBase DB;

    public static DragonCore getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("§a************************************************************");
        Bukkit.getConsoleSender().sendMessage("§6[DragonCore] §a龙之核心插件原作者: 空白格(QQ: 448780139)");
        Bukkit.getConsoleSender().sendMessage("§6[DragonCore] §a当前版本维护开发者: justwei(QQ: 1731598625)");
        Bukkit.getConsoleSender().sendMessage("§a************************************************************");

        instance = this;

        /*Config.init(this);*/
        fileManager = new FileManager(this);
        fileManager.init();
        if (ConfigFile.sql) {
            Bukkit.getConsoleSender().sendMessage("§6当前数据存储: §cMysql");
            DB = new MysqlRepository(this);
        } else {
            Bukkit.getConsoleSender().sendMessage("§6当前数据存储: §cYaml");
            String playerDataFolder = ConfigFile.playerDataFolder;
            if (playerDataFolder == null || playerDataFolder.isEmpty()) {
                DB = new YamlRepository(new File(getDataFolder(), "PlayerData"));
            } else {
                DB = new YamlRepository(new File(playerDataFolder));
            }
        }

        Bukkit.getPluginCommand("dragoncore").setExecutor(new MainCommand());


        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "dragoncore:main");

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "dragoncore:main", new PluginMessageReceiver(this));

        registerListener(miscManager = new MiscManager(this));
        registerListener(new PlayerJoinListener());
        registerListener(new PlayerKeyListener());
        registerListener(new PlayerChatListener());
        registerListener(new PlaceholderListener());
        registerListener(new SlotListener(this));

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs"))
            registerListener(new MythicMobsListener());

        Bukkit.getConsoleSender().sendMessage("§6DragonCore - 加载完成");


    }

    @Override
    public void onDisable() {
        DB.close();
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }


    public Logger getLog() {
        return log;
    }
}
