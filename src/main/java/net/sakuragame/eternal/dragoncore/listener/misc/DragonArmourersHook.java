package net.sakuragame.eternal.dragoncore.listener.misc;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import eos.moe.armourers.api.DragonAPI;
import eos.moe.armourers.api.PlayerSkinUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.config.constructor.SlotSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DragonArmourersHook implements Listener {
    private final MiscManager manager;

    public DragonArmourersHook(MiscManager miscManager) {
        this.manager = miscManager;
    }

    @EventHandler
    public void onSlotUpdate(PlayerSlotUpdateEvent e) {
        DragonAPI.updatePlayerSkin(e.getPlayer());
    }

    @EventHandler
    public void onSkinUpdate(PlayerSkinUpdateEvent e) {
        Player player = e.getPlayer();
        e.setSkinList(getSkins(player));
    }

    private List<String> getSkins(Player player) {
        List<String> skins = new ArrayList<>();

        Map<String, ItemStack> map = manager.getCacheMap().get(player.getUniqueId());
        if (map == null) {
            return skins;
        }

        for (String key : FileManager.getSlotSettings().keySet()) {
            ItemStack item = map.get(key);

            SlotSetting slotSetting = FileManager.getSlotSettings().get(key);
            if (!slotSetting.isSkin()) continue;

            if (!MegumiUtil.isEmpty(item)) {
                skins.add(DragonAPI.getItemSkinName(item));
            }
            else {
                if (slotSetting.getIdentifier().equals("offhand_skin")) {
                    skins.add("eternal-shield");
                }
            }
        }

        return skins;
    }
}
