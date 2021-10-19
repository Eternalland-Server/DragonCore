package eos.moe.dragoncore.util;

import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    public static String getName(ItemStack itemStack) {
        return itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null ? "" : itemStack.getItemMeta().getDisplayName();
    }
    public static List<String> getLore(ItemStack itemStack) {
        return itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null ? new ArrayList<>() : itemStack.getItemMeta().getLore();
    }

    public static NBTTagCompound toNBT(ItemStack itemStack) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        if (itemStack != null) {
            CraftItemStack.asNMSCopy(itemStack).save(nbtTagCompound);
        } else {
            CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)).save(nbtTagCompound);
        }
        return nbtTagCompound;
    }

    public static String itemToJson(ItemStack itemStack) {
        return toNBT(itemStack).toString();
    }

    public static ItemStack jsonToItem(String json) throws MojangsonParseException {
        NBTTagCompound parse = MojangsonParser.parse(json);
        net.minecraft.server.v1_12_R1.ItemStack itemStack = new net.minecraft.server.v1_12_R1.ItemStack(parse);
        return CraftItemStack.asBukkitCopy(itemStack);
    }
}
