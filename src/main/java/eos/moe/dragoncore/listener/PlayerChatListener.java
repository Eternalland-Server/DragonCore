package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.config.FileManager;
import eos.moe.dragoncore.config.sub.ConfigFile;
import eos.moe.dragoncore.util.ColorUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onRGB(AsyncPlayerChatEvent e) {
        if (ConfigFile.replaceChatColor && e.getMessage().contains("<end>")) {
            try {
                e.setMessage(ColorUtil.parseColor(e.getMessage()));
            } catch (Exception ex) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§c[错误] §a该文本格式错误，无法应用渐变颜色");
            }
        }
    }

    @EventHandler
    public void onReplace(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        for (String s : FileManager.getFontReplace()) {
            msg = msg.replace(s, "");
        }

        if (msg.isEmpty()) {
            e.setCancelled(true);
            return;
        }

        e.setMessage(msg);
    }
}
