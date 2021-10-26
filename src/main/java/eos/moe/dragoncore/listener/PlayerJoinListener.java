package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.config.YamlHandler;
import eos.moe.dragoncore.config.sub.ConfigFile;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.Scheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Scheduler.runLaterAsync(() -> YamlHandler.sendYaml2Player(e.getPlayer()), ConfigFile.joinPacketDelay);

    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        Scheduler.runLaterAsync(() -> PacketSender.sendPlayerWorld(e.getPlayer()), 20);
    }

}
