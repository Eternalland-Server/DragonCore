package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.KeyPressEvent;
import eos.moe.dragoncore.util.CommandUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerKeyListener implements Listener {

    private DragonCore plugin = DragonCore.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onKeyPressEvent(KeyPressEvent e) {
        if (e.isCancelled()) return;

        List<String> stringList = plugin.getFileManager().getKeyConfig().getStringList(e.getKey());
        for (String s : stringList) {
            CommandUtil.executeCommand(e.getPlayer(), s);
        }
    }

    /*@EventHandler
    public void test(ChatTextboxEvent e) {
        // 把聊天栏的  /马儿 变成 韫
        if (e.getCurrentText().contains("/马儿")) {
            PacketSender.setChatBoxText(e.getPlayer(),
                    e.getCurrentText().replace("/马儿", "韫"));
        }
    }*/
}
