package eos.moe.dragoncore.commands.sub.sound;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SoundPlayCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "soundPlay";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 6) return;

        try {
            String s = strings[1];

            Player player = Bukkit.getPlayerExact(s);
            if (player == null) {
                commandSender.sendMessage(" §7玩家 " + s + " 不在线");
                return;
            }

            String sound = strings[2];
            float volume = Float.parseFloat(strings[3]);
            float pitch = Float.parseFloat(strings[4]);
            boolean loop = Boolean.parseBoolean(strings[5]);

            PacketSender.sendPlaySound(player, sound, sound, volume, pitch, loop, 0, 0, 0);
            player.sendMessage(" §7已开始播放: " + sound);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(" §7参数错误，请检查输入的数字");
        }

    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
