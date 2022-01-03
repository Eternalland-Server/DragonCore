package net.sakuragame.eternal.dragoncore.api;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.database.IDataBase;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlotAPI {

    private final static DragonCore plugin = DragonCore.getInstance();

    public static void setSlotItem(Player player, String identifier, ItemStack itemStack, boolean syncToClient) {
        setSlotItem(player, identifier, itemStack, syncToClient, null);
    }

    public static void setSlotItem(Player player, String identifier, ItemStack itemStack, boolean syncToClient, IDataBase.Callback<ItemStack> callback) {
        Validate.notNull(identifier, "identifier cant be null");
        Validate.notNull(itemStack, "itemStack cant be null");

        plugin.getDB().setData(player, identifier, itemStack, new IDataBase.Callback<ItemStack>() {
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
        });
    }

    public static void getSlotItem(Player player, String identifier, IDataBase.Callback<ItemStack> callback) {
        plugin.getDB().getData(player, identifier, callback);
    }

    public static void getAllSlotItem(Player player, IDataBase.Callback<Map<String, ItemStack>> callback) {
        plugin.getDB().getAllData(player, callback);
    }

    public static ItemStack getCacheSlotItem(Player player, String identifier) {
        Map<String, ItemStack> map = plugin.getMiscManager().getCacheMap().get(player.getUniqueId());
        if (map == null) {
            return null;
        }
        ItemStack item = map.get(identifier);

        return item == null ? null : item.clone();
    }

    public static Map<String, ItemStack> getCacheAllSlotItem(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, ItemStack> map = plugin.getMiscManager().getCacheMap().get(uuid);
        if (map == null) return new HashMap<>();

        return new HashMap<>(map);
    }
}
