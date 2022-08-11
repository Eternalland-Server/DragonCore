package net.sakuragame.eternal.dragoncore.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonListener implements Listener {

    @EventHandler
    public void onSpawn(ItemSpawnEvent e) {
        this.showName(e.getEntity());
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
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
