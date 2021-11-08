package net.sakuragame.eternal.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatementCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "statement";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) return;

        String s1 = args[0];
        String s2 = args[1];
        String s3 = args[2];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            sender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        PacketSender.sendRunFunction(player, s2, s3, false);

        sender.sendMessage(" §7界面执行语句完成");
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
