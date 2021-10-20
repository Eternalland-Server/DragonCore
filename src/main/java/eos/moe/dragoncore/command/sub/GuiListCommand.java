package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiListCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Gui列表: ");
        for (String s : Config.fileMap.keySet()) {
            sender.sendMessage(" §7" + s);
        }
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "core.gui.list";
    }

    @Override
    public String getCommandDesc() {
        return "/core guilist";
    }
}
