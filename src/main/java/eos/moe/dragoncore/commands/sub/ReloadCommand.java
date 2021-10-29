package eos.moe.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.config.ClientHandler;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    private DragonCore plugin;

    public ReloadCommand() {
        this.plugin = DragonCore.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "reload";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length == 1) {
            plugin.getFileManager().init();
            ClientHandler.sendYaml2Player();
            commandSender.sendMessage(" §7重载完成");
        }
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
