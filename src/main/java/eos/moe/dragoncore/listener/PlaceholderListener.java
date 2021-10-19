package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.event.SyncPlaceholderEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlaceholder(SyncPlaceholderEvent e) {
        if (e.getPlayer() == null)
            return;
        if (!e.getPlayer().isOnline())
            return;
        Map<String, String> replace = new HashMap<>();
        Map<String, String> placeholders = e.getPlaceholders();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            try {
                String s = PlaceholderAPI.setPlaceholders(e.getPlayer(), entry.getKey());
                replace.put(entry.getKey(), s);
            } catch (Exception ex) {
                DragonCore.getInstance().getLog().warn("获取变量值时,对应插件处理出现异常->" + entry.getKey(), ex);
            }
        }
        placeholders.clear();
        placeholders.putAll(replace);
    }
}
