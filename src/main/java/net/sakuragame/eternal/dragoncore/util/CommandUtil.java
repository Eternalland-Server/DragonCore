package net.sakuragame.eternal.dragoncore.util;

import net.sakuragame.eternal.dragoncore.DragonCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static void executeCommand(Player player, String cmd) {
        cmd = cmd.replace("%p%", player.getName());
        cmd = cmd.replace("%player%", player.getName());
        cmd = cmd.replace("<player>", player.getName());
        cmd = PlaceholderAPI.setPlaceholders(player, cmd);

        String type = cmd.toLowerCase();

        if (type.startsWith("[op]")) {
            boolean isOp = player.isOp();
            try {
                player.setOp(true);
                player.chat("/" + cmd.substring(4).trim());
            } catch (Throwable e) {
                DragonCore.getInstance().getLogger().info("执行OP命令出现了异常: " + cmd);
                e.printStackTrace();
            } finally {
                player.setOp(isOp);
            }
        } else if (type.startsWith("[console]")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(9).trim());
        } else {
            player.chat("/" + cmd.trim());
        }
    }
}
