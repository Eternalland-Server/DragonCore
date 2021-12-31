package net.sakuragame.eternal.dragoncore.listener.misc;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import eos.moe.armourers.api.DragonAPI;
import eos.moe.armourers.api.PlayerSkinUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.config.SlotSetting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
        Player player = e.getPlayer();

        Map<String, ItemStack> map = manager.getCacheMap().get(player.getUniqueId());
        if (map == null) {
            return;
        }

        for (String key : map.keySet()) {
            ItemStack item = map.get(key);

            SlotSetting slotSetting = FileManager.getSlotSettings().get(key);
            if (slotSetting == null) continue;
            if (!slotSetting.isSkin()) continue;

            if (!MegumiUtil.isEmpty(item)) {
                e.getSkinList().add(DragonAPI.getItemSkinName(item));
            }
            else {
                if (slotSetting.getIdentifier().equals("offhand_skin")) {
                    e.getSkinList().add("eternal-shield");
                }
            }
        }
    }
}
