package eos.moe.dragoncore.api.easygui.component;

import org.bukkit.entity.Player;

import java.util.Map;

public interface EasyComponent {
     String getId();

     Map<String, Map<String, Object>> build(Player player, Map<String, String> functions);
}
