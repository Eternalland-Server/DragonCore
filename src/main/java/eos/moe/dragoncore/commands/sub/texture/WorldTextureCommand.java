package eos.moe.dragoncore.commands.sub.texture;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.commands.CommandPerms;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 6) return;

        Player player = getPlayer();
        Location location = player.getEyeLocation();

        String node = strings[1];
        String path = strings[2];
        float width = Float.parseFloat(strings[3]);
        float height = Float.parseFloat(strings[4]);
        boolean follow = Boolean.parseBoolean(strings[5]);

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
