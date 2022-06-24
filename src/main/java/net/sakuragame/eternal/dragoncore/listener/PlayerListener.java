package net.sakuragame.eternal.dragoncore.listener;

import net.sakuragame.eternal.dragoncore.config.ClientHandler;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Scheduler.runLaterAsync(() -> {
            if (player == null) return;

            ClientHandler.sendYaml2Player(e.getPlayer());
            this.sendUID(player);

            if (ConfigFile.bgm == null) {
                PacketSender.sendStopSound(player, "eternal_bgm");
            }
            else {
                PacketSender.sendPlaySound(
                        player, "eternal_bgm", "bgms/" + ConfigFile.bgm,
                        0.3f, 1,
                        true,
                        0, 0, 0
                );
            }
        }, ConfigFile.joinPacketDelay);
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        Scheduler.runLaterAsync(() -> PacketSender.sendPlayerWorld(e.getPlayer()), 20);
    }

    private void sendUID(Player player) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());
        String str = new DecimalFormat("0000000").format(uid);

        Map<String, String> map = new HashMap<>();
        map.put("eternal_user_uid", str);
        PacketSender.sendSyncPlaceholder(player, map);
    }

    @EventHandler
    public void onInteractAnimation(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        switch (e.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                PacketSender.setEntityModelItemAnimation(e.getPlayer(), "leftclick");
                return;
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                PacketSender.setEntityModelItemAnimation(e.getPlayer(), "rightclick");
        }
    }

}
