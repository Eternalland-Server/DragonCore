package eos.moe.dragoncore.commands.sub.texture;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldItemCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "worldItem";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 4) return;

        Player player = getPlayer();

        String node = strings[1];
        float scale = Float.parseFloat(strings[2]);
        boolean follow = Boolean.parseBoolean(strings[3]);

        float rotateX = player.getLocation().getPitch();
        float rotateY = -player.getLocation().getYaw();

        PacketSender.setPlayerWorldTexture(
                player,
                node,
                player.getEyeLocation(),
                follow ? 0 : rotateX,
                follow ? 0 : rotateY,
                0,
                player.getInventory().getItemInMainHand(),
                scale,
                follow,
                true,
                null,
                true,
                0, 0, 0
        );

        player.sendMessage(" §7创建完成");
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
