package eos.moe.dragoncore.commands.sub.sound;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundStopCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "soundStop";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) return;

        String s = strings[1];

        Player player = Bukkit.getPlayerExact(s);
        if (player == null) {
            commandSender.sendMessage(" §7玩家 " + s + " 不在线");
            return;
        }

        String sound = strings[2];

        PacketSender.sendStopSound(player, sound);
        player.sendMessage(" §7已停止播放: " + sound);
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
