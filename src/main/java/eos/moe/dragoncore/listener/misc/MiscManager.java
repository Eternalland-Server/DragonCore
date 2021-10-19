package eos.moe.dragoncore.listener.misc;


import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.database.IDataBase;
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
    private Set<Update> updateSet;

    public MiscManager(DragonCore plugin) {
        this.plugin = plugin;
        this.cacheMap = new ConcurrentHashMap<>();
        this.updateSet = new HashSet<>();

        PluginManager pm = Bukkit.getPluginManager();
        YamlConfiguration config = Config.getConfig();
        if (pm.isPluginEnabled("DragonArmourers") && config.getBoolean("DragonArmourers", false)) {
            plugin.registerListener(new DragonArmourersHook(this));
            Bukkit.getConsoleSender().sendMessage(" - 已兼容龙之时装付费版");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        cacheMap.remove(e.getPlayer().getUniqueId());
        System.out.println("[DragonCore] 开始载入玩家 " + player.getName() + " 物品");
        plugin.getDB().getAllData(player, new IDataBase.Callback<Map<String, ItemStack>>() {
            @Override
            public void onResult(Map<String, ItemStack> p0) {
                MiscManager.this.cacheMap.put(player.getUniqueId(), p0);
                update1(player);
                System.out.println("[DragonCore] 载入玩家 " + player.getName() + " 物品完成,共 " + p0.size() + " 个物品");
            }

            @Override
            public void onFail() {
                MiscManager.this.cacheMap.put(player.getUniqueId(), new HashMap<>());
                update1(player);
                System.out.println("[DragonCore] 载入玩家 " + player.getName() + " 物品失败");
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
            this.update(player, false);
            new PlayerSlotUpdateEvent(player, identifier, itemStack).callEvent();

        }
    }

    private void update1(Player player) {
        update(player, true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline())
                update(player, true);
        }, 40);
    }

    public void update(Player player, boolean callEvent) {
        Map<String, ItemStack> map = this.cacheMap.get(player.getUniqueId());
        if (map != null) {
            for (Update update : updateSet) {
                update.update(player, map);
            }
            if (callEvent) {
                new PlayerSlotUpdateEvent(player, null, null).callEvent();
            }
        }
    }

    public Map<UUID, Map<String, ItemStack>> getCacheMap() {
        return cacheMap;
    }

    @Deprecated
    public Set<Update> getUpdateSet() {
        return updateSet;
    }
}
