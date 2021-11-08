package net.sakuragame.eternal.dragoncore.listener.misc;

import eos.moe.armourers.api.DragonAPI;
import eos.moe.armourers.api.PlayerSkinUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.config.SlotSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DragonArmourersHook implements Listener {
    private final MiscManager manager;

    public DragonArmourersHook(MiscManager miscManager) {
        this.manager = miscManager;
    }

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        DragonAPI.updatePlayerSkin(e.getPlayer());
    }

    @EventHandler
    public void updateSkin(PlayerSkinUpdateEvent e) {
       // MiscManager miscManager = DragonCore.getInstance().getMiscManager();

        Map<String, ItemStack> map = manager.getCacheMap().get(e.getPlayer().getUniqueId());
        if (map == null) {
            return;
        }
        for (Map.Entry<String, ItemStack> entry : map.entrySet()) {
            SlotSetting slotSetting = FileManager.getSlotSettings().get(entry.getKey());
            if (slotSetting != null && slotSetting.isSkin() && entry.getValue() != null
                    && entry.getValue().getType() != Material.AIR) {
                // entry.key 就是槽位名  entry.value就是物品
                e.getSkinList().add(DragonAPI.getItemSkinName(entry.getValue()));
            }
        }
    }
}
