package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NameCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("控制台无法使用该指令");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();
        String text = String.join(" ", args).replace("&", "§");
        if (text.contains("<end>")) {
            try {
                text = ColorUtil.parseColor(text);
            } catch (Exception e) {
                player.sendMessage("错误的格式，添加失败");
                return;
            }
        }

        meta.setDisplayName(text);
        itemStack.setItemMeta(meta);
        System.out.println("注意: 颜色符号&应换为§");
        System.out.println(text.replace("§", "&"));
        player.sendMessage("已设置名称");
    }


    @Override
    public String getPermission() {
        return "core.command.name";
    }

    @Override
    public String getCommandDesc() {
        return "/core name <文字>";
    }
}
