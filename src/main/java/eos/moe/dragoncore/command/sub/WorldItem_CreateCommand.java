package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldItem_CreateCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("控制台无法使用该指令");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        float rotateX = player.getLocation().getPitch();
        float rotateY = -player.getLocation().getYaw();
        boolean followPlayer = args[2].equals("true");
        PacketSender.setPlayerWorldTexture(player, args[0],
                player.getEyeLocation(),
                followPlayer ? 0: rotateX , followPlayer ? 0 : rotateY,
                0, player.getInventory().getItemInMainHand(),
                Float.parseFloat(args[1]), args[2].equals("true"), true,  null,
                true, 0, 0, 0
        );
        player.sendMessage("创建完成");
    }

    @Override
    public String getPermission() {
        return "core.command.wi.create";
    }

    @Override
    public String getCommandDesc() {
        return "/core worlditem_test <节点名> <缩放倍数> <是否跟随玩家视角>";
    }
}
