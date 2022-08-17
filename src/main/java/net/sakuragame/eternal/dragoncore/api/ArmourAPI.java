package net.sakuragame.eternal.dragoncore.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eos.moe.armourers.api.DragonAPI;
import net.sakuragame.eternal.dragoncore.database.mysql.DragonCoreTable;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.ArrayList;
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

    public static List<String> getSkinsFormDB(int uid) {
        JsonParser parser = new JsonParser();
        List<String> skins = new ArrayList<>();

        try (DatabaseQuery query = ClientManagerAPI.getDataManager().createQuery(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(),
                "uid", uid
        )) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                String ident = result.getString("ident");
                if (!ident.endsWith("_skin")) continue;

                String data = result.getString("item_data");
                if (data == null) continue;

                JsonObject json = parser.parse(data).getAsJsonObject();
                String skin = json.get("justattribute").getAsJsonObject().get("skin").getAsString();
                skin = skin.substring(0, skin.length() - 1);
                skins.add(skin);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return skins;
    }

}
