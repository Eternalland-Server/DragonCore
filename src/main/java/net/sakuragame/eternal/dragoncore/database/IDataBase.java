package net.sakuragame.eternal.dragoncore.database;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface IDataBase {

    Map<String, ItemStack> getSlotItems(Player player);

    void saveSlotItems(Player player, Map<String, ItemStack> items);
}
