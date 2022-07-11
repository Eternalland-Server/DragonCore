package net.sakuragame.eternal.dragoncore.listener;

import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.KeyPressEvent;
import net.sakuragame.eternal.dragoncore.util.CommandUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class KeyListener implements Listener {

    private DragonCore plugin = DragonCore.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onKeyPressEvent(KeyPressEvent e) {
        if (e.isCancelled()) return;

        List<String> stringList = plugin.getFileManager().getKeyConfig().getStringList(e.getKey());
        for (String s : stringList) {
            CommandUtil.executeCommand(e.getPlayer(), s);
        }
    }
}