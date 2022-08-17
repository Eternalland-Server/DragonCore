package net.sakuragame.eternal.dragoncore.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.dragoncore.api.ArmourAPI;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import org.bukkit.command.CommandSender;

public class GetSkinsCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "getSkins";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        int uid = Integer.parseInt(args[0]);
        Scheduler.runAsync(() -> {
            sender.sendMessage(ArmourAPI.getSkinsFormDB(uid).toString());
        });
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
