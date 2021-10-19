package eos.moe.dragoncore.api;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.database.IDataBase;
import eos.moe.dragoncore.network.PacketSender;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SlotAPI {

    private static DragonCore plugin = DragonCore.getInstance();

    public static void setSlotItem(Player player, String identifier, ItemStack itemStack, boolean syncToClient) {
        setSlotItem(player, identifier, itemStack, syncToClient, null);
    }

    public static void setSlotItem(Player player, String identifier, ItemStack itemStack, boolean syncToClient, IDataBase.Callback<ItemStack> callback) {
        Validate.notNull(identifier, "identifier cant be null");
        Validate.notNull(itemStack, "itemStack cant be null");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDB().setData(player, identifier, itemStack, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack p0) {
                DragonCore.getInstance().getMiscManager().putItem(player, identifier, itemStack);
                if (syncToClient) {
                    PacketSender.putClientSlotItem(player, identifier, itemStack);
                }
                if (callback != null) {
                    callback.onResult(p0);
                }
            }

            @Override
            public void onFail() {
                if (callback != null) {
                    callback.onFail();
                }
            }
        }));
    }

    public static void getSlotItem(Player player, String identifier, IDataBase.Callback<ItemStack> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDB().getData(player, identifier, callback));
    }

    public static void getAllSlotItem(Player player, IDataBase.Callback<Map<String, ItemStack>> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDB().getAllData(player, callback));
    }

    public static ItemStack getCacheSlotItem(Player player, String identifier) {
        Map<String, ItemStack> map = plugin.getMiscManager().getCacheMap().get(player.getUniqueId());
        return map != null ? map.get(identifier) : null;
    }

    public static Map<String, ItemStack> getCacheAllSlotItem(Player player) {
        return plugin.getMiscManager().getCacheMap().get(player.getUniqueId());
    }
}
