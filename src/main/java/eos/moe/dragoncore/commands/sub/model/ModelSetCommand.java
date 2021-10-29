package eos.moe.dragoncore.commands.sub.model;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModelSetCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "modelSet";
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

        PacketSender.setEntityModel(player, player.getUniqueId(), s2);

        commandSender.sendMessage(" §7已设置玩家模型");
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
