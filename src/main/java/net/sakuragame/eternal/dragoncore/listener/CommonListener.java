package net.sakuragame.eternal.dragoncore.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CommonListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getItemDrop();
        entity.setCustomName(e.getItemDrop().getCustomName());
        entity.setCustomNameVisible(true);
    }
}
