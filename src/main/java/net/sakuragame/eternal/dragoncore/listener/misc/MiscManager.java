package net.sakuragame.eternal.dragoncore.listener.misc;


import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.database.IDataBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiscManager implements Listener {
    private Map<UUID, Map<String, ItemStack>> cacheMap;
    private DragonCore plugin;

    public MiscManager(DragonCore plugin) {
        this.plugin = plugin;
        this.cacheMap = new ConcurrentHashMap<>();

        PluginManager pm = Bukkit.getPluginManager();
        YamlConfiguration config = plugin.getFileManager().getConfig();
        if (pm.isPluginEnabled("DragonArmourers") && config.getBoolean("DragonArmourers", false)) {
            plugin.registerListener(new DragonArmourersHook(this));
            Bukkit.getConsoleSender().sendMessage(" - 已兼容龙之时装付费版");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
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
    public void onQuit(PlayerQuitEvent e) {
        cacheMap.remove(e.getPlayer().getUniqueId());
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

}
