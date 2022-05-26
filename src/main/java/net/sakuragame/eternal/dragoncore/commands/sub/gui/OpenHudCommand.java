package net.sakuragame.eternal.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenHudCommand extends SubCommand {

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
