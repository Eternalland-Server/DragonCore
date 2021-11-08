package net.sakuragame.eternal.dragoncore.commands.sub.item;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.util.NBTUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

public class ColorCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "color";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) return;

        Player player = getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (MegumiUtil.isEmpty(item)) {
            player.sendMessage(" §7请手持物品");
            return;
        }

        String s = args[0];

        Color color = Color.decode("0x" + s.substring(1));
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        String rgb = r + "," + g + "," + b;

        NBTUtils.setNBT(item, "color", rgb);

        player.sendMessage(" §7已设置RGB色: " + color);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
