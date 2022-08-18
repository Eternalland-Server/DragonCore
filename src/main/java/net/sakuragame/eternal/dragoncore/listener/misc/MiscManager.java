package net.sakuragame.eternal.dragoncore.listener.misc;


import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.database.IDataBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiscManager implements Listener {

    private final DragonCore plugin;
    private final Map<UUID, Map<String, ItemStack>> cacheMap;
    private final Map<UUID, Vector> moveDir;

    public MiscManager(DragonCore plugin) {
        this.plugin = plugin;
        this.cacheMap = new ConcurrentHashMap<>();
        this.moveDir = new HashMap<>();

        PluginManager pm = Bukkit.getPluginManager();
        YamlConfiguration config = plugin.getFileManager().getConfig();
        if (pm.isPluginEnabled("DragonArmourers") && config.getBoolean("DragonArmourers", false)) {
            plugin.registerListener(new ClothesListener(this));
            Bukkit.getConsoleSender().sendMessage(" - 已兼容龙之时装付费版");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!ConfigFile.slotEnable) return;

        Player player = e.getPlayer();
        cacheMap.remove(e.getPlayer().getUniqueId());
        Bukkit.getConsoleSender().sendMessage("[DragonCore] 开始载入玩家 " + player.getName() + " 物品");
        long start = System.currentTimeMillis();
        plugin.getDB().getAllData(player, new IDataBase.Callback<Map<String, ItemStack>>() {
            @Override
            public void onResult(Map<String, ItemStack> p0) {
                long end = System.currentTimeMillis();
                MiscManager.this.cacheMap.put(player.getUniqueId(), p0);
                new PlayerSlotLoadedEvent(player).callEvent();
                Bukkit.getConsoleSender().sendMessage("[DragonCore] 载入玩家 " + player.getName() + " 物品完成,共 " + p0.size() + " 个物品(" + (end - start) + "ms)");
            }

            @Override
            public void onFail() {
                MiscManager.this.cacheMap.put(player.getUniqueId(), new HashMap<>());
                Bukkit.getConsoleSender().sendMessage("[DragonCore] 载入玩家 " + player.getName() + " 物品失败");
            }
        });
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        this.moveDir.put(uuid, e.getTo().clone().subtract(e.getFrom()).toVector());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        this.cacheMap.remove(uuid);
        this.moveDir.remove(uuid);
    }

    public void putItem(Player player, String identifier, ItemStack itemStack) {
        if (cacheMap.containsKey(player.getUniqueId())) {
            cacheMap.get(player.getUniqueId()).put(identifier, itemStack);
            new PlayerSlotUpdateEvent(player, identifier, itemStack).callEvent();
        }
    }

    public Map<UUID, Map<String, ItemStack>> getCacheMap() {
        return cacheMap;
    }

    public Vector getMoveDirection(UUID uuid) {
        return this.moveDir.get(uuid);
    }

}
