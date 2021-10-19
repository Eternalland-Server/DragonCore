package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FunctionCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("玩家 " + args[0] + " 不在线，无法执行界面方法");
        } else if (args.length == 2) {
            PacketSender.sendRunFunction(player, "", args[1], false);
        } else if (args.length == 3) {
            PacketSender.sendRunFunction(player, args[1], args[2], false);
        }
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "core.function";
    }

    @Override
    public String getCommandDesc() {
        return "/core function <玩家> <界面名(可忽略)> <执行的界面方法>";
    }

    public int getLength() {
        return 3;
    }
}
