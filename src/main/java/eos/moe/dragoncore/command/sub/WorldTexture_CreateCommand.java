package eos.moe.dragoncore.command.sub;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.command.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WorldTexture_CreateCommand extends CommandBase {

    private DragonCore plugin = DragonCore.getInstance();

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("控制台无法使用该指令");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        float rotateX = player.getLocation().getPitch();
        float rotateY = -player.getLocation().getYaw();
        boolean followPlayer = args[4].equals("true");

        YamlConfiguration section = new YamlConfiguration();
        section.set("world", player.getWorld().getName());
        section.set("x", player.getEyeLocation().getX());
        section.set("y", player.getEyeLocation().getY());
        section.set("z", player.getEyeLocation().getZ());
        section.set("rotateX", followPlayer ? 0 : rotateX);
        section.set("rotateY", followPlayer ? 0 : rotateY);
        section.set("rotateZ", 0);
        section.set("path", args[1]);
        section.set("width", Float.parseFloat(args[2]));
        section.set("height", Float.parseFloat(args[3]));
        section.set("alpha", 1);
        section.set("follow", args[4].equals("true"));
        section.set("glow", true);
        plugin.getFileManager().getWorldTexture().set(args[0], section);
        plugin.getFileManager().saveWorldTexture();

        player.sendMessage("创建完成");

    }

    @Override
    public String getPermission() {
        return "core.command.wtcreate";
    }

    @Override
    public String getCommandDesc() {
        return "/core WorldTexture_create <节点名> <贴图地址> <宽> <高> <是否跟随玩家视角(true/false)>";
    }
}
