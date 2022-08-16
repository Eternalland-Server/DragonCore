package net.sakuragame.eternal.dragoncore.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(ItemSpawnEvent e) {
        if (e.isCancelled()) return;
        this.showName(e.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMerge(ItemMergeEvent e) {
        if (e.isCancelled()) return;
        this.showName(e.getEntity());
    }

    private void showName(Entity entity) {
        Item item = (Item) entity;
        ItemStack itemStack = item.getItemStack();

        int amount = itemStack.getAmount();

        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.hasDisplayName()) return;

        entity.setCustomName(meta.getDisplayName() + (amount > 1 ? "§3(x" + amount + ")" : ""));
        entity.setCustomNameVisible(true);
    }
}
