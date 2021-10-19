package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.util.NBTUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

public class ColorCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("控制台无法使用该指令");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage("请手持物品");
        } else {

            Color colora = Color.decode("0x" + args[0].substring(1));

            int r = colora.getRed();
            int g = colora.getGreen();
            int b = colora.getBlue();


            NBTUtils.setNBT(itemStack, "color", r + "," + g + "," + b);
            player.sendMessage("已为物品添加nbt: color: \"" + r + "," + g + "," + b + "\"");
            player.sendMessage("PS: 需要存在附魔的物品才会显示为该颜色");
            player.sendMessage("PS: 黑色无法显示");
        }
    }

    @Override
    public String getPermission() {
        return "core.command.color";
    }

    @Override
    public String getCommandDesc() {
        return "/core color <#FFFFFF>";
    }
}
