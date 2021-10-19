package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.Scheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {
    //private final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Scheduler.runLaterAsync(() -> {
            Config.sendYamlToClient(e.getPlayer());
        }, Config.joinPacketDelay);

    }

    @EventHandler
    public void join(PlayerInteractEvent e) {
        // PacketSender.removeModelEntityAnimation(e.getPlayer(), "KnifeMan01", 0);
        //  PacketSender.setModelEntityAnimation(e.getPlayer(), "KnifeMan01", 0);


    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        Scheduler.runLaterAsync(() -> {
            PacketSender.sendPlayerWorld(e.getPlayer());
        }, 20);
    }

  /*  @EventHandler
    public void at(PlayerInteractAtEntityEvent e) {
        System.out.println(e.getClickedPosition() + " " + e.getRightClicked().getName());
    }

    @EventHandler
    public void at(EntityInteractEvent e) {
        System.out.println(e.getEntity().getName());
    }*/
}
