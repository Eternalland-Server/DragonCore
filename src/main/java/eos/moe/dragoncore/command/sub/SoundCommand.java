package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoundCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        try {
            Player player = Bukkit.getPlayer(args[0]);
            float volume = Float.parseFloat(args[2]);
            float pitch = Float.parseFloat(args[3]);
            boolean loop = Boolean.parseBoolean(args[4]);
            if (player == null) {
                sender.sendMessage("该玩家" + args[0] + "不在线");
            } else if (args.length >= 8) {
                float x = Float.parseFloat(args[4]);
                float y = Float.parseFloat(args[5]);
                float z = Float.parseFloat(args[6]);
                PacketSender.sendPlaySound(player, args[1], volume, pitch, loop, x, y, z);
            } else {
                PacketSender.sendPlaySound(player, args[1], volume, pitch, loop, 0, 0, 0);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("参数错误，参数无法被转换为数字" + Arrays.toString(args));
        }
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "core.sound";
    }

    @Override
    public String getCommandDesc() {
        return "/core sound <玩家> <声音(.ogg)> <volume> <pitch> <循环(true/false)> <x(可)> <y(忽)> <z(略)>";
    }

    @Override
    public int getLength() {
        return 6;
    }
}
