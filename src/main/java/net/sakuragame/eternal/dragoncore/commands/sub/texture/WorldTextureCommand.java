package net.sakuragame.eternal.dragoncore.commands.sub.texture;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.CoreAPI;
import net.sakuragame.eternal.dragoncore.api.worldtexture.WorldTexture;
import net.sakuragame.eternal.dragoncore.api.worldtexture.animation.ScaleAnimation;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WorldTextureCommand extends SubCommand {

    private DragonCore plugin;

    public WorldTextureCommand() {
        this.plugin = DragonCore.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "worldTexture";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = getPlayer();
        if (args.length == 0) {
            Location location = player.getLocation();
            WorldTexture wt = new WorldTexture(
                    location.getWorld().getName(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    0, 0, 0,
                    "ui/pack/break_unable_background.png",
                    null,
                    1, 1, 1,
                    true, true,
                    null,
                    false,
                    0, 0,
                    Arrays.asList(
                            new ScaleAnimation(
                                    5, 1f, 0.5f, 0, 10, true, 20
                            )
                    )
            );

            CoreAPI.setPlayerWorldTextureItem(player, "test", wt);
            player.sendMessage("done!");
            return;
        }

        if (args.length < 5) return;

        Location location = player.getEyeLocation();

        String node = args[0];
        String path = args[1];
        float width = Float.parseFloat(args[2]);
        float height = Float.parseFloat(args[3]);
        boolean follow = Boolean.parseBoolean(args[4]);

        float rotateX = player.getLocation().getPitch();
        float rotateY = -player.getLocation().getYaw();

        YamlConfiguration section = new YamlConfiguration();
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("rotateX", follow ? 0 : rotateX);
        section.set("rotateY", follow ? 0 : rotateY);
        section.set("rotateZ", 0);
        section.set("path", path);
        section.set("width", width);
        section.set("height", height);
        section.set("alpha", 1);
        section.set("follow", follow);
        section.set("glow", true);
        plugin.getFileManager().getWorldTexture().set(node, section);
        plugin.getFileManager().saveWorldTexture();

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
