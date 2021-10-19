package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class OpenGuiCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

        Player player = args[0].equals("me") ? (Player) sender : Bukkit.getPlayer(args[0]);

        if (!Config.fileMap.containsKey("Gui" + File.separator + args[1] + ".yml"))
            sender.sendMessage("Gui配置内无该文件: " + args[1] + ".yml");
        else if (player != null)
            PacketSender.sendOpenGui(player, args[1]);
        else
            sender.sendMessage("玩家不在线");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "core.gui.open";
    }

    @Override
    public String getCommandDesc() {
        return "/core opengui <玩家> <界面名>";
    }
}
