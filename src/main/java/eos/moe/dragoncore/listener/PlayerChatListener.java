package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.util.ColorUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        if (Config.replaceChatColor && e.getMessage().contains("<end>")) {
            try {
                e.setMessage(ColorUtil.parseColor(e.getMessage()));
            } catch (Exception ex) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§c[错误] §a该文本格式错误，无法应用渐变颜色");
            }
        }
    }
}
