package eos.moe.dragoncore.commands.sub.item;

import com.taylorswiftcn.justwei.commands.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.util.ColorUtil;
import eos.moe.dragoncore.util.ItemUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LoreCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "lore";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 2) return;

        Player player = getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (MegumiUtil.isEmpty(item)) {
            player.sendMessage(" §7请手持物品");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = ItemUtil.getLore(item);
        String text = MegumiUtil.onReplace(strings[1]);

        if (text.contains("<end>")) {
            try {
                text = ColorUtil.parseColor(text);
            } catch (Exception e) {
                player.sendMessage(" §7错误的格式，添加失败");
                return;
            }
            lore.add(text);
        } else {
            lore.add(text);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendMessage(" §7已添加Lore");
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
