package net.sakuragame.eternal.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.config.ClientHandler;
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
    public void perform(CommandSender sender, String[] args) {
        plugin.getFileManager().init();
        ClientHandler.sendYaml2Player();
        sender.sendMessage(" §7重载完成");
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
