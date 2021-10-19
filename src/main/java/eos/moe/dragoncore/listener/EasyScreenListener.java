package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.api.easygui.EasyScreen;
import eos.moe.dragoncore.api.easygui.component.EasyComponent;
import eos.moe.dragoncore.api.easygui.component.listener.ClickListener;
import eos.moe.dragoncore.api.easygui.component.listener.TextListener;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EasyScreenListener implements Listener {



    @EventHandler
    public void onEasyScreenEvent(CustomPacketEvent e) {
        if (!"EasyScreenEvent".equals(e.getIdentifier())) return;
        if (e.getData().size() < 2) return;
        String componentName = e.getData().get(0);
        String actionType = e.getData().get(1);
        EasyScreen openedScreen = EasyScreen.getOpenedScreen(e.getPlayer());
        if (openedScreen == null) return;
        EasyComponent component = openedScreen.getComponentById(componentName);

        if (component == null) return;
        switch (actionType) {
            case "click":
                if (e.getData().size() == 3 && component instanceof ClickListener) {
                    ClickListener.Type type;
                    try {
                        type = ClickListener.Type.valueOf(e.getData().get(2));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                        break;
                    }
                    ((ClickListener) component).onClick(e.getPlayer(), type);
                }
                break;
            case "textbox":
                if (e.getData().size() == 3 && component instanceof TextListener) {
                    String text = e.getData().get(2);
                    ((TextListener) component).onTextChange(text);
                }
                break;
        }
    }

    @EventHandler
    public void onEasyScreenCloseEvent(CustomPacketEvent e) {
        if (!"EasyScreenCloseEvent".equals(e.getIdentifier())) return;
        if (e.getData().size() != 1) return;
        EasyScreen.closeScreen(e.getPlayer(), e.getData().get(0));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        EasyScreen.closeScreen(e.getPlayer(), false);
    }

  /*  @EventHandler
    public void onquit(InventoryCloseEvent e) {
        new Error("测试输出").printStackTrace();
        System.out.println("关闭了背包" + e.getInventory().getTitle());
    }*/
}

