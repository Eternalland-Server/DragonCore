package net.sakuragame.eternal.dragoncore.commands.sub.sound;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundStopCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "soundStop";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) return;

        String s = args[0];

        Player player = Bukkit.getPlayerExact(s);
        if (player == null) {
            sender.sendMessage(" §7玩家 " + s + " 不在线");
            return;
        }

        String sound = args[1];

        PacketSender.sendStopSound(player, sound);
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
