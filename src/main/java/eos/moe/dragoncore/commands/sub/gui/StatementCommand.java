package eos.moe.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatementCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "statement";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 4) return;

        String s1 = strings[1];
        String s2 = strings[2];
        String s3 = strings[3];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            commandSender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        PacketSender.sendRunFunction(player, s2, s3, false);

        commandSender.sendMessage(" §7界面执行语句完成");
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
