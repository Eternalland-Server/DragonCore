package net.sakuragame.eternal.dragoncore.listener.misc;


import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
        UUID uuid = player.getUniqueId();
        cacheMap.remove(uuid);

        Scheduler.runLater(() -> {
            Bukkit.getConsoleSender().sendMessage("[DragonCore] 开始载入玩家 " + player.getName() + " 物品");

            long start = System.currentTimeMillis();
            Map<String, ItemStack> slotItems = plugin.getDB().getSlotItems(player);
            long end = System.currentTimeMillis();

            Bukkit.getConsoleSender().sendMessage("[DragonCore] 载入玩家 " + player.getName() + " 物品完成,共 " + slotItems.size() + " 个物品(" + (end - start) + "ms)");

            this.cacheMap.put(uuid, slotItems);
            new PlayerSlotLoadedEvent(player).callEvent();
        }, 20);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        this.moveDir.put(uuid, e.getTo().clone().subtract(e.getFrom()).toVector());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        this.moveDir.remove(uuid);

        Map<String, ItemStack> slotItems = this.cacheMap.remove(uuid);
        if (slotItems == null || slotItems.size() == 0) return;

        Scheduler.runAsync(() -> plugin.getDB().saveSlotItems(player, slotItems));
    }

    public void putItem(Player player, String identifier, ItemStack itemStack) {
        UUID uuid = player.getUniqueId();
        if (cacheMap.containsKey(uuid)) {
            cacheMap.get(uuid).put(identifier, itemStack);
            new PlayerSlotUpdateEvent(player, identifier, itemStack).callEvent();
        }
    }

    public void removeItem(Player player, String identifier) {
        UUID uuid = player.getUniqueId();
        if (cacheMap.containsKey(uuid)) {
            cacheMap.get(uuid).remove(identifier);
            new PlayerSlotUpdateEvent(player, identifier, null).callEvent();
        }
    }

    public Map<UUID, Map<String, ItemStack>> getCacheMap() {
        return cacheMap;
    }

    public Vector getMoveDirection(UUID uuid) {
        return this.moveDir.get(uuid);
    }

}
