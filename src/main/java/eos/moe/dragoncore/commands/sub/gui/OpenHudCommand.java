package eos.moe.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenHudCommand extends SubCommand {

    private DragonCore plugin;

    public OpenHudCommand() {
        this.plugin = DragonCore.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "openHud";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) return;

        String s1 = args[0];
        String s2 = args[1];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            sender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        if (!plugin.getFileManager().getGui().containsKey(s2 + ".yml")) {
            sender.sendMessage(" §7Gui文件夹内无该文件: " + s2 + ".yml");
            return;
        }

        PacketSender.sendOpenHud(player, s2);
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
