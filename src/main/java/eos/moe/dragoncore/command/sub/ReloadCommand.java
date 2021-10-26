package eos.moe.dragoncore.command.sub;


import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.config.YamlHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * @Author: Baka
 * @Date: 2020/9/15 0:25
 */
public class ReloadCommand extends CommandBase {

    private DragonCore plugin = DragonCore.getInstance();

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        plugin.getFileManager().init();
        YamlHandler.sendYaml2Player();
        sender.sendMessage("§a重载完成");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "core.command.reload";
    }

    @Override
    public String getCommandDesc() {
        return "/core reload";
    }
}
