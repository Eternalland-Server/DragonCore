package eos.moe.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.SubCommand;
import eos.moe.dragoncore.commands.CommandPerms;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand extends SubCommand {

    private List<String> help;

    public HelpCommand() {
        this.loadHelp();
    }

    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        this.help.forEach(commandSender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }

    private void loadHelp() {
        this.help = new ArrayList<>(
                Arrays.asList(
                        " §7/core modelPlay <animation> <transient time> - 使周围模型开始播放动作",
                        " §7/core modelStop <animation> <transient time> - 使周围模型停止播放动作",
                        " §7/core modelRange <model> <range> - 设置周围生物模型",
                        " §7/core modelSet <player> <model> - 设置玩家模型",
                        " §7/core soundPlay <player> <ogg> <volume> <pitch> <loop> - 播放OGG音乐",
                        " §7/core soundStop <player> <ogg> - 暂停播放OGG音乐",
                        " §7/core worldTexture <node> <path> <width> <height> <follow> - 在准星处设置世界贴图",
                        " §7/core worldItem <node> <scale> <follow> - 在准星处设置手持物品模型(本人看见)",
                        " §7/core color <#FFFFFF> - 设置物品附魔颜色",
                        " §7/core name <#ff0000-#1800ff-#00ff48-#e4ff00>Test<end> - 物品设置渐变Name",
                        " §7/core lore <#ff0000-#1800ff-#00ff48-#e4ff00>Test<end> - 物品添加渐变Lore",
                        " §7/core manager entity - 查看怪物模型",
                        " §7/core manager item - 查看物品模型",
                        " §7/core guiList - 查看界面列表",
                        " §7/core openGui <player> <file name> - 打开界面",
                        " §7/core openHud <player> <file name> - 打开Hud",
                        " §7/core reload - 重载配置文件"
                )
        );
    }
}
