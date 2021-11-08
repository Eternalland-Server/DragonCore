package net.sakuragame.eternal.dragoncore.commands.sub.gui;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunctionCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "function";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 4) return;

        String s1 = args[0];
        String s2 = args[1];
        String s3 = args[2];
        String s4 = args[3];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) {
            sender.sendMessage(" §7玩家 " + s1 + " 不在线");
            return;
        }

        boolean async = Boolean.parseBoolean(s4);

        if (async) {
            PacketSender.sendRunFunction(player, s2, "func.Function_Async_Execute('" + s3 + "');", false);
        }
        else {
            PacketSender.sendRunFunction(player, s2, "func.Function_Execute('" + s3 + "');", false);
        }

        sender.sendMessage(" §7界面执行方法完成");
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
