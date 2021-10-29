package eos.moe.dragoncore.util;

import com.taylorswiftcn.justwei.nbt.NBTItem;

import org.bukkit.inventory.ItemStack;

public class NBTUtils {

    public static void setNBT(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        nbtItem.applyNBT(item);
    }
}
