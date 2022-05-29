package net.sakuragame.eternal.dragoncore.listener;

import net.sakuragame.eternal.dragoncore.config.ClientHandler;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Scheduler.runLaterAsync(() -> {
            if (player == null) return;
            ClientHandler.sendYaml2Player(e.getPlayer());

            int uid = ClientManagerAPI.getUserID(player.getUniqueId());
            Map<String, String> map = new HashMap<>();
            map.put("eternal_user_uid", uid + "");
            PacketSender.sendSyncPlaceholder(player, map);
        }, ConfigFile.joinPacketDelay);
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        Scheduler.runLaterAsync(() -> PacketSender.sendPlayerWorld(e.getPlayer()), 20);
    }

}
