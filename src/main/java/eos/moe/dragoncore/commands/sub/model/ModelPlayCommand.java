package eos.moe.dragoncore.commands.sub.model;

import com.taylorswiftcn.justwei.commands.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ModelPlayCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "modelPlay";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) return;

        Player player = getPlayer();
        String animation = strings[1];
        String duration = strings[2];

        if (!MegumiUtil.isNumber(duration)) return;

        int time = Integer.parseInt(duration);

        player.getNearbyEntities(10, 10, 10).forEach(entity -> {
            if (entity instanceof LivingEntity) {
                PacketSender.setModelEntityAnimation((LivingEntity) entity, animation, time);
            }
        });

        PacketSender.setModelEntityAnimation(player, animation, time);

        player.sendMessage(" §7已为半径10米内怪物设置动作" + animation + "(仅适用于存在该动作的模型)");
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
