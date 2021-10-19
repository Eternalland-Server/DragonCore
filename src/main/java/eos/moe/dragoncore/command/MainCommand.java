package eos.moe.dragoncore.command;

import com.google.common.collect.Maps;
import eos.moe.dragoncore.command.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Baka
 * @Date: 2020/1/14 20:02
 */
public class MainCommand implements TabExecutor {
    private Map<String, CommandBase> commandMap;

    public MainCommand() {
        commandMap = Maps.newHashMap();
        registerCommand(new ReloadCommand());
        registerCommand(new WorldTexture_CreateCommand());
        registerCommand(new WorldItem_CreateCommand());
        registerCommand(new Model_StartAnimationCommand());
        registerCommand(new Model_StopAnimationCommand());
        registerCommand(new ManagerCommand());
        registerCommand(new ColorCommand());
        registerCommand(new LoreCommand());
        registerCommand(new NameCommand());
        registerCommand(new OpenHudCommand());
        registerCommand(new OpenGuiCommand());
        registerCommand(new SoundCommand());
        registerCommand(new FunctionCommand());
        registerCommand(new Sound1Command());

    }

    private void registerCommand(CommandBase commandBase) {
        commandMap.put(commandBase.getCommand(), commandBase);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            CommandBase commandBase = commandMap.get(args[0].toLowerCase());
            if (commandBase == null) {
                sender.sendMessage("§6[DragonCore] §a该子命令不存在 -> " + args[0]);
            } else if (!sender.hasPermission(commandBase.getPermission())) {
                sender.sendMessage("§6[DragonCore] §a执行该指令需要权限 -> " + commandBase.getPermission());
            } else if (commandBase.getLength() > args.length) {
                sender.sendMessage("§6[DragonCore] §a参数不完整 -> " + commandBase.getCommandDesc());
            } else {
                String[] args1 = Arrays.copyOfRange(args, 1, args.length);
                if (sender instanceof Player) {
                    commandBase.onPlayerCommand((Player) sender, args1);
                } else {
                    commandBase.onConsoleCommand(sender, args1);
                }
            }
        } else {

            if (sender.isOp()) {
                sender.sendMessage("/core model_start <动作名> <过渡时间>  为周围存在该动作的模型启动播放动作");
                sender.sendMessage("/core model_stop <动作名> <过渡时间>  为周围存在该动作的模型停止播放动作");
                sender.sendMessage("/core WorldTexture_create <节点名> <贴图地址> <宽> <高> <是否跟随玩家视角(true/false)>  在面前添加世界贴图");
                sender.sendMessage("/core worlditem_test <节点名> <缩放倍数> <是否跟随玩家视角>   在面前添加手持物品的模型(测试指令,仅自己能看到)");
                sender.sendMessage("/core manager entity 查看怪物模型文件");
                sender.sendMessage("/core manager item   查看物品模型文件");
                sender.sendMessage("/core color <#FFFFFF>   设置物品附魔颜色");
                sender.sendMessage("/core lore <#ff0000-#1800ff-#00ff48-#e4ff00>喵喵喵喵喵喵喵喵<end>   添加渐变Lore");
                sender.sendMessage("/core name <#ff0000-#1800ff-#00ff48-#e4ff00>喵喵喵喵喵喵喵喵<end>   添加渐变名称");
                sender.sendMessage("/core opengui <玩家> <文件名>   令玩家打开某个界面");
                sender.sendMessage("/core openhud <玩家> <文件名>   令玩家打开某个hud");
                sender.sendMessage("/core reload 重载配置文件");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return commandMap.keySet().stream().filter(cmd -> cmd.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length >= 2) {
            return commandMap.containsKey(args[0].toLowerCase()) ? commandMap.get(args[0].toLowerCase()).
                    onTabComplete(sender, command, s, args) : null;
        }
        return null;
    }
}

