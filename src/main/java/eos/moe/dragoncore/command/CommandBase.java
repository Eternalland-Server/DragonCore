package eos.moe.dragoncore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: Baka
 * @Date: 2020/9/14 20:46
 */
public abstract class CommandBase {


    public abstract void onConsoleCommand(CommandSender sender, String[] args);

    public abstract void onPlayerCommand(Player player, String[] args);

    public abstract String getPermission();

    public abstract String getCommandDesc();


    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }

    public int getLength() {
        return getCommandDesc().split(" ").length - 1;
    }

    public String getCommand() {
        return getCommandDesc().split(" ")[1].toLowerCase();
    }
}
