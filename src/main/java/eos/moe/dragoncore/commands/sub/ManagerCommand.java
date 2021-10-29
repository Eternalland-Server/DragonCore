package eos.moe.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "manager";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 2) return;

        Player player = getPlayer();
        String s = strings[1];

        switch (s.toLowerCase()) {
            case "item": {
                player.openInventory(Bukkit.createInventory(null, 0, "Dragon_Core_ItemManager"));
                break;

            }
            case "entity":{
                player.openInventory(Bukkit.createInventory(null, 0, "Dragon_Core_EntityManager"));
                break;
            }
        }
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
