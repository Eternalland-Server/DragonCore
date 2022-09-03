package net.sakuragame.eternal.dragoncore;


import lombok.Getter;
import net.sakuragame.eternal.dragoncore.commands.MainCommand;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.database.IDataBase;
import net.sakuragame.eternal.dragoncore.database.MysqlRepository;
import net.sakuragame.eternal.dragoncore.listener.*;
import net.sakuragame.eternal.dragoncore.listener.misc.MiscManager;
import net.sakuragame.eternal.dragoncore.mythicmobs.listener.MythicMobsListener;
import net.sakuragame.eternal.dragoncore.network.PluginMessageReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        Bukkit.getConsoleSender().sendMessage("§6[DragonCore] §a龙之核心插件原作者: 白龙(QQ: 448780139)");
        Bukkit.getConsoleSender().sendMessage("§6[DragonCore] §a当前版本维护开发者: justwei(QQ: 1731598625)");
        Bukkit.getConsoleSender().sendMessage("§a************************************************************");

        instance = this;

        fileManager = new FileManager(this);
        fileManager.init();
        DB = new MysqlRepository();

        Bukkit.getPluginCommand("dragoncore").setExecutor(new MainCommand());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "dragoncore:main");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "dragoncore:main", new PluginMessageReceiver(this));

        registerListener(miscManager = new MiscManager(this));
        registerListener(new PlayerListener());
        registerListener(new KeyListener());
        registerListener(new ChatListener());
        registerListener(new PlaceholderListener());
        registerListener(new SlotListener());
        registerListener(new CommonListener());

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs"))
            registerListener(new MythicMobsListener());

        Bukkit.getConsoleSender().sendMessage("§6DragonCore - 加载完成");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§6DragonCore - 已卸载");
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public Logger getLog() {
        return log;
    }
}
