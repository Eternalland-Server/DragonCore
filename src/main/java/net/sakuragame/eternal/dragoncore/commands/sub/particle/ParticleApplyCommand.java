package net.sakuragame.eternal.dragoncore.commands.sub.particle;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleApplyCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "particleApply";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 3) return;

        String s1 = args[0];
        String s2 = args[1];
        String s3 = args[2];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) return;

        if (!MegumiUtil.isInteger(s3)) return;

        PacketSender.addParticle(
                player,
                s2,
                "test",
                player.getUniqueId().toString(),
                "look",
                Integer.parseInt(s3)
        );

        sender.sendMessage(" §7已应用暴雪粒子");
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
