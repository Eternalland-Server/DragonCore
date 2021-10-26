package eos.moe.dragoncore.config;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.event.YamlSendToPlayerEvent;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class YamlHandler {

    private final static DragonCore plugin = DragonCore.getInstance();

    public static void sendYaml2Player() {
        Bukkit.getOnlinePlayers().forEach(YamlHandler::sendYaml2Player);
    }

    public static void sendYaml2Player(Player player) {
        PacketSender.sendClearCache(player);
        PacketSender.sendZipPassword(player);
        PacketSender.sendKeyRegister(player);
        PacketSender.sendPlayerWorld(player);

        String clientTitle = plugin.getConfig().getString("ClientTitle");
        if (clientTitle != null) {
            PacketSender.setWindowTitle(player, clientTitle);
        }

        plugin.getFileManager().sendYaml2Player(player);

        YamlSendToPlayerEvent event = new YamlSendToPlayerEvent(player);
        event.callEvent();

        PacketSender.sendFinished(player);
    }
}
