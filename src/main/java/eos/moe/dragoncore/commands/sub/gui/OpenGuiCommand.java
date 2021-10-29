package eos.moe.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenGuiCommand extends SubCommand {

    private DragonCore plugin;

    public OpenGuiCommand() {
        this.plugin = DragonCore.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "openGui";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) return;

        String s1 = strings[1];
        String s2 = strings[2];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            commandSender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        if (!plugin.getFileManager().getGui().containsKey(s2 + ".yml")) {
            commandSender.sendMessage(" §7Gui文件夹内无该文件: " + s2 + ".yml");
            return;
        }

        PacketSender.sendOpenGui(player, s2);
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
