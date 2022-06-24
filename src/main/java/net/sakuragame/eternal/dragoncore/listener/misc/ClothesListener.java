package net.sakuragame.eternal.dragoncore.listener.misc;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.generate.function.Statements;
import eos.moe.armourers.api.DragonAPI;
import eos.moe.armourers.api.PlayerSkinUpdateEvent;
import net.sakuragame.eternal.cargo.CargoAPI;
import net.sakuragame.eternal.cargo.user.CargoAccount;
import net.sakuragame.eternal.cargo.value.ValueType;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.YamlSendFinishedEvent;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.config.constructor.SkinType;
import net.sakuragame.eternal.dragoncore.config.constructor.SlotSetting;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClothesListener implements Listener {
    private final MiscManager manager;

    public ClothesListener(MiscManager miscManager) {
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

        CargoAccount account = CargoAPI.getAccountsManager().getAccount(player);

        for (String key : FileManager.getSlotSettings().keySet()) {
            ItemStack item = map.get(key);

            SlotSetting slot = FileManager.getSlotSettings().get(key);
            if (!slot.isSkin()) continue;

            if (slot.getType() != SkinType.None) {
                boolean off = Boolean.parseBoolean(account.get(ValueType.STORAGE, slot.getType().getIdentifier()));
                if (off) continue;
            }

            if (!MegumiUtil.isEmpty(item)) {
                skins.add(DragonAPI.getItemSkinName(item));
            }
            else {
                if (slot.getIdentifier().equals("offhand_skin")) {
                    skins.add("eternal-shield");
                }
            }
        }

        return skins;
    }

    @EventHandler
    public void onSuit(YamlSendFinishedEvent e) {
        Player player = e.getPlayer();

        CargoAccount account = CargoAPI.getAccountsManager().getAccount(player);
        Statements statements = new Statements();

        for (SkinType type : SkinType.values()) {
            boolean off = Boolean.parseBoolean(account.get(ValueType.STORAGE, type.getIdentifier()));
            statements.add("global." + type.getIdentifier() + " = " + (off ? 1 : 0) + ";");
        }

        PacketSender.sendRunFunction(player, "default", statements.build(), false);
    }

    @EventHandler
    public void onSwitch(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals("user_clothes")) return;
        if (!e.getCompID().equals("hide")) return;

        CargoAccount account = CargoAPI.getAccountsManager().getAccount(player);

        int i = e.getParams().getParamI(0);
        SkinType type = SkinType.match(i);
        if (type == SkinType.None) return;

        boolean off = !Boolean.parseBoolean(account.get(ValueType.STORAGE, type.getIdentifier()));
        Scheduler.runAsync(() -> {
            account.set(ValueType.STORAGE, type.getIdentifier(), off + "");
            PacketSender.sendRunFunction(player, "default", "global." + type.getIdentifier() + " = " + (off ? 1 : 0) + ";", false);
            DragonAPI.updatePlayerSkin(player);
        });
    }
}
