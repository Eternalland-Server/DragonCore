package net.sakuragame.eternal.dragoncore.commands.sub.texture;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldItemCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "worldItem";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) return;

        Player player = getPlayer();

        String node = args[0];
        float scale = Float.parseFloat(args[1]);
        boolean follow = Boolean.parseBoolean(args[2]);

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
