package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ManagerCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

        sender.sendMessage("控制台无法使用该指令");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {

        switch (args[0].toLowerCase()) {
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
    public String getPermission() {
        return "core.command.manager";
    }

    @Override
    public String getCommandDesc() {
        return "/core manager <类型(item/entity)>";
    }
}
