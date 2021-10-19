package eos.moe.dragoncore.listener.misc;

import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.config.SlotSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.serverct.ersha.jd.AttributeAPI;

import java.util.*;

public class AttributePlusHook implements Listener {

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Map<String, ItemStack> attributeItems = new HashMap<>();
        if (e.getIdentifier() != null) {
            attributeItems.put(e.getIdentifier(), e.getItemStack());
        } else {
            attributeItems.putAll(SlotAPI.getCacheAllSlotItem(e.getPlayer()));
        }
        attributeItems.entrySet().removeIf(entry ->
                !Config.slotSettings.containsKey(entry.getKey()) || !Config.slotSettings.get(entry.getKey()).isAttribute()
        );
        if (Config.debug)
            System.out.println("开始更新玩家槽位属性" + e.getPlayer().getName());
        for (Map.Entry<String, ItemStack> entry : attributeItems.entrySet()) {
            SlotSetting slotSetting = Config.slotSettings.get(entry.getKey());
            if (slotSetting != null && slotSetting.isAttribute()) {
                List<String> itemLore = getItemLore(entry.getValue());
                AttributeAPI.addAttribute(e.getPlayer(), "dragoncore_" + entry.getKey(), itemLore);
                if (Config.debug)
                    System.out.println(entry.getKey() + "  " + Arrays.toString(itemLore.toArray()));
            }
        }
    }

    protected static List<String> getItemLore(ItemStack itemStack) {
        return isEmptyStack(itemStack) || itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null ? new ArrayList<>() :
                itemStack.getItemMeta().getLore();
    }

    public static boolean isEmptyStack(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }
}
