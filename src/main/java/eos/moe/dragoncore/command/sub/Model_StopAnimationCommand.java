package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Model_StopAnimationCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        player.getNearbyEntities(10, 10, 10).forEach(e -> {
            if (e instanceof LivingEntity)
                PacketSender.removeModelEntityAnimation((LivingEntity) e, args[0], Integer.parseInt(args[1]));
        });
        player.sendMessage("已为半径10米内怪物取消动作" + args[0] + "(仅适用于存在该动作的模型)");
    }

    @Override
    public String getPermission() {
        return "core.command.animation";
    }

    @Override
    public String getCommandDesc() {
        return "/core model_stop <动作名> <过渡时间>";
    }
}
