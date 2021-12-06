package net.sakuragame.eternal.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSenderWithNBTEdit;
import net.sakuragame.eternal.dragoncore.util.ItemUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NbtEditCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "nbtEdit";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = getPlayer();

        if (args.length == 1 && args[0].equalsIgnoreCase("hand")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (MegumiUtil.isEmpty(item)) {
                sender.sendMessage(" §7你手中没有物品");
                return;
            }
            PacketSenderWithNBTEdit.sendItemNBTPacket(player, ItemUtil.toNBT(item));
            return;
        }

        PacketSenderWithNBTEdit.sendMouseOverPacket(player);
        player.sendMessage(" §7打开成功");
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
