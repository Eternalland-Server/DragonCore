package net.sakuragame.eternal.dragoncore.api;

import eos.moe.armourers.api.DragonAPI;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ArmourAPI {

    public static String getSkinType(String skin) {
        return DragonAPI.getSkinType(skin);
    }

    public static String getItemSkinName(ItemStack item) {
        return DragonAPI.getItemSkinName(item);
    }

    public static void setEntitySkin(UUID uuid, List<String> skins) {
        DragonAPI.setEntitySkin(uuid, skins);
    }

    public static void setEntitySkin(Entity entity, List<String> skins) {
        DragonAPI.setEntitySkin(entity, skins);
    }

}
