package net.sakuragame.eternal.dragoncore.commands.sub.particle;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleRemoveCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "particleRemove";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        String s1 = args[0];

        Player player = Bukkit.getPlayerExact(s1);
        if (player == null) return;

        PacketSender.removeParticle(player, "test");

        sender.sendMessage(" §7已移除暴雪粒子");
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
