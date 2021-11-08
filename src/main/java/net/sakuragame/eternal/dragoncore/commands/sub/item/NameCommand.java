package net.sakuragame.eternal.dragoncore.commands.sub.item;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class NameCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "name";
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

        ItemMeta meta = item.getItemMeta();
        String text = MegumiUtil.onReplace(args[0]);

        if (text.contains("<end>")) {
            try {
                text = ColorUtil.parseColor(text);
            } catch (Exception e) {
                player.sendMessage(" §7错误的格式，添加失败");
                return;
            }
        }

        meta.setDisplayName(text);
        item.setItemMeta(meta);

        player.sendMessage(" §7已设置名称");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
