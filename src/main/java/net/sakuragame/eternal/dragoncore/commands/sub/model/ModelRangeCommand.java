package net.sakuragame.eternal.dragoncore.commands.sub.model;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ModelRangeCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "modelRange";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) return;

        String model = args[1];
        String s = args[2];

        if (!MegumiUtil.isNumber(s)) {
            sender.sendMessage(" §7参数有误，请检查输入的数字");
            return;
        }

        Player player = getPlayer();
        int range = Integer.parseInt(s);

        player.getNearbyEntities(range, range, range).forEach(entity -> {
            if (entity instanceof LivingEntity) {
                PacketSender.setEntityModel(player, entity.getUniqueId(), model);
            }
        });

        sender.sendMessage(" §7已设置周围生物模型");
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
