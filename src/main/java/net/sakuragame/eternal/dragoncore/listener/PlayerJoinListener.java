package net.sakuragame.eternal.dragoncore.listener;

import net.sakuragame.eternal.dragoncore.config.ClientHandler;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Scheduler.runLaterAsync(() -> ClientHandler.sendYaml2Player(e.getPlayer()), ConfigFile.joinPacketDelay);
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        Scheduler.runLaterAsync(() -> PacketSender.sendPlayerWorld(e.getPlayer()), 20);
    }

}
