package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Sound1Command extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

        try {
            String sound = args[0];
            World world = Bukkit.getWorld(args[1]);
            float x = Float.parseFloat(args[2]);
            float y = Float.parseFloat(args[3]);
            float z = Float.parseFloat(args[4]);
            float range = Float.parseFloat(args[5]);
            boolean loc = Boolean.parseBoolean(args[6]);

            if (world != null) {
                Location location = new Location(world, x, y, z);
                List<Player> collect = world.getPlayers().stream().filter(p -> p.getLocation().distance(location) <= range).collect(Collectors.toList());
                for (Player player : collect) {
                    if (loc)
                        PacketSender.sendPlaySound(player, sound, 1, 1, false, x, y, z);
                    else
                        PacketSender.sendPlaySound(player, sound, 1, 1, false, 0, 0, 0);
                }
            } else {
                sender.sendMessage(args[1] + "世界不存在");
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
        return "/core sound1 <声音(.ogg)> <世界> <x> <y> <z> <半径> <定点(true/false)>";
    }

    @Override
    public int getLength() {
        return 7;
    }
}
