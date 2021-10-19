package eos.moe.dragoncore.listener.misc;

import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.config.SlotSetting;
import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SXAttributeHook implements Listener {

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        Map<String, ItemStack> items = SlotAPI.getCacheAllSlotItem(e.getPlayer());

        SXAttributeData sxData = new SXAttributeData();

        int level = SXAttribute.getApi().getEntityLevel(player);
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            SlotSetting slotSetting = Config.slotSettings.get(entry.getKey());
            if (slotSetting == null || !slotSetting.isAttribute())
                continue;
            if (entry.getValue() == null || entry.getValue().getType() == Material.AIR) {
                continue;
            }
            if (level < SXAttribute.getApi().getItemLevel(entry.getValue())) {
                continue;
            }
            sxData.add(SXAttribute.getApi().getItemData(player, null, entry.getValue()));
        }
        SXAttribute.getApi().setEntityAPIData(SXAttributeHook.class, player.getUniqueId(), sxData);
        SXAttribute.getApi().updateStats(player);
        SXAttribute.getApi().updateHandData(player);
    }
}
