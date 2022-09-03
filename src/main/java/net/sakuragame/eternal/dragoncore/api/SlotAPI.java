package net.sakuragame.eternal.dragoncore.api;

import net.sakuragame.eternal.dragoncore.DragonCore;
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
        Validate.notNull(identifier, "identifier cant be null");

        if (itemStack == null) {
            plugin.getMiscManager().removeItem(player, identifier);
        }
        else {
            plugin.getMiscManager().putItem(player, identifier, itemStack);
        }

        if (syncToClient) {
            PacketSender.putClientSlotItem(player, identifier, itemStack);
        }
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
