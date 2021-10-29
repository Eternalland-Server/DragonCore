package eos.moe.dragoncore.commands.sub.model;

import com.taylorswiftcn.justwei.commands.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ModelRangeCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "modelRange";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) return;

        String model = strings[1];
        String s = strings[2];

        if (!MegumiUtil.isNumber(s)) {
            commandSender.sendMessage(" §7参数有误，请检查输入的数字");
            return;
        }

        Player player = getPlayer();
        int range = Integer.parseInt(s);

        player.getNearbyEntities(range, range, range).forEach(entity -> {
            if (entity instanceof LivingEntity) {
                PacketSender.setEntityModel(player, entity.getUniqueId(), model);
            }
        });

        commandSender.sendMessage(" §7已设置周围生物模型");
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
