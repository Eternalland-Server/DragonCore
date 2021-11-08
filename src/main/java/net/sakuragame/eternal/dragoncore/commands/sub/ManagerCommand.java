package net.sakuragame.eternal.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "manager";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) return;

        Player player = getPlayer();
        String s = args[0];

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
