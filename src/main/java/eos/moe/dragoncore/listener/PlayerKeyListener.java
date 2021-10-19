package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.api.KeyPressEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.util.CommandUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerKeyListener implements Listener {
    //private Map<UUID, Map<String, Long>> cooldown = new HashMap<>();


    @EventHandler(priority = EventPriority.HIGH)
    public void onKeyPressEvent(KeyPressEvent e) {
        if (e.isCancelled()) return;

        List<String> stringList = Config.fileMap.get("KeyConfig.yml").getStringList(e.getKey());
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
