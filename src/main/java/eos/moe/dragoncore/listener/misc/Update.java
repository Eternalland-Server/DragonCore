package eos.moe.dragoncore.listener.misc;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Deprecated
public interface Update {
    void update(Player player, Map<String, ItemStack> items);
}
