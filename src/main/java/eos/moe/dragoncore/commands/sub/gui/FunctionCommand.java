package eos.moe.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunctionCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "function";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 5) return;

        String s1 = strings[1];
        String s2 = strings[2];
        String s3 = strings[3];
        String s4 = strings[4];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            commandSender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        boolean async = Boolean.parseBoolean(s4);

        if (async) {
            PacketSender.sendRunFunction(player, s2, "func.Function_Async_Execute('" + s3 + "');", false);
        }
        else {
            PacketSender.sendRunFunction(player, s2, "func.Function_Execute('" + s3 + "');", false);
        }

        commandSender.sendMessage(" §7界面执行方法完成");
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
