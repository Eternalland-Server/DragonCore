package net.sakuragame.eternal.dragoncore.util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {

    public static void setNBT(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        nbtItem.applyNBT(item);
    }
}
