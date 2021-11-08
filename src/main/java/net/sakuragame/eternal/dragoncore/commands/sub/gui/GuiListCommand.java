package net.sakuragame.eternal.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import org.bukkit.command.CommandSender;

public class GuiListCommand extends SubCommand {

    private DragonCore plugin;

    public GuiListCommand() {
        this.plugin = DragonCore.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "guiList";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(" §6§lGui列表: ");
        for (String s : plugin.getFileManager().getGui().keySet()) {
            sender.sendMessage("  §7- " + s);
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
