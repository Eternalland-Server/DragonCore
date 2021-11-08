package net.sakuragame.eternal.dragoncore.commands.sub.sound;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SoundPlayCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "soundPlay";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 5) return;

        try {
            String s = args[0];

            Player player = Bukkit.getPlayerExact(s);
            if (player == null) {
                sender.sendMessage(" §7玩家 " + s + " 不在线");
                return;
            }

            String sound = args[1];
            float volume = Float.parseFloat(args[2]);
            float pitch = Float.parseFloat(args[3]);
            boolean loop = Boolean.parseBoolean(args[4]);

            PacketSender.sendPlaySound(player, sound, sound, volume, pitch, loop, 0, 0, 0);
            player.sendMessage(" §7已开始播放: " + sound);
        } catch (NumberFormatException e) {
            sender.sendMessage(" §7参数错误，请检查输入的数字");
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
