package net.sakuragame.eternal.dragoncore.api;

import net.sakuragame.eternal.dragoncore.DragonCore;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PlayerAPI {

    public static Vector getMoveDirection(Player player) {
        return getMoveDirection(player.getUniqueId());
    }

    public static Vector getMoveDirection(UUID uuid) {
        return DragonCore.getInstance().getMiscManager().getMoveDirection(uuid);
    }
}
