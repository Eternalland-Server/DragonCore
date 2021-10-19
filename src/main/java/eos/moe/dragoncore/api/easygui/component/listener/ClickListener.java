package eos.moe.dragoncore.api.easygui.component.listener;

import org.bukkit.entity.Player;

public interface ClickListener {
    enum Type {
        LEFT,
        RIGHT,
        MIDDLE;
    }

    void onClick(Player player, Type button);
}
