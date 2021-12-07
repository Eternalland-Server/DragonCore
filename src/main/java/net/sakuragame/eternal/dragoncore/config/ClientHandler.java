package net.sakuragame.eternal.dragoncore.config;

import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.event.YamlSendToPlayerEvent;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClientHandler {

    private final static DragonCore plugin = DragonCore.getInstance();

    public static void sendYaml2Player() {
        Bukkit.getOnlinePlayers().forEach(ClientHandler::sendYaml2Player);
    }

    public static void sendYaml2Player(Player player) {
        PacketSender.sendClearCache(player);
        PacketSender.sendZipPassword(player);
        PacketSender.sendKeyRegister(player);
        PacketSender.sendPlayerWorld(player);
        PacketSender.setWindowTitle(player, ConfigFile.clientTitle);

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
