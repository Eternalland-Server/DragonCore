package net.sakuragame.eternal.dragoncore.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.database.mysql.DragonCoreTable;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlRepository implements IDataBase {

    private final DataManager dataManager;

    private final JsonParser parse = new JsonParser();

    public MysqlRepository() {
        this.dataManager = ClientManagerAPI.getDataManager();
        this.init();
    }

    public void init() {
        DragonCoreTable.DRAGON_CORE_SLOTS.createTable();
    }

    @Override
    public Map<String, ItemStack> getSlotItems(Player player) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());
        if (uid == -1) return null;

        Map<String, ItemStack> items = new HashMap<>();

        try (DatabaseQuery query = dataManager.createQuery(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(),
                "uid",
                uid)
        ) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                String ident = result.getString("ident");
                String id = result.getString("item_id");
                int amount = result.getInt("item_amount");
                String data = result.getString("item_data");
                String unique = result.getString("item_unique");

                if (amount == 0) continue;

                ItemStack itemStack = deserialize(player, id, amount, data, unique);
                items.put(ident, itemStack);
            }
            return items;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveSlotItems(Player player, Map<String, ItemStack> items) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());
        if (uid == -1) return;

        List<Object[]> data = new ArrayList<>();
        for (String ident : FileManager.getSlotSettings().keySet()) {
            ItemStack item = items.get(ident);
            if (MegumiUtil.isEmpty(item)) {
                data.add(new Object[] {uid, ident, null, 0, null, null});
                continue;
            }

            ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
            String id = itemStream.getZaphkielName();
            int amount = item.getAmount();
            String itemData = itemStream.getZaphkielData().toJson();
            ItemTag uniqueTag = itemStream.getZaphkielUniqueData();
            String unique = uniqueTag == null ? null : uniqueTag.toJson();

            data.add(new Object[] {uid, ident, id, amount, itemData, unique});
        }

        dataManager.executeReplace(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(),
                new String[] {"uid", "ident", "item_id", "item_amount", "item_data", "item_unique"},
                data
        );
    }

    private ItemStack deserialize(Player player, String id, int amount, String data, String unique) {
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        if (data != null && !data.isEmpty()) object.add("data", parse.parse(data));
        if (unique != null && !unique.isEmpty()) object.add("unique", parse.parse(unique));

        ItemStream itemStream = ZaphkielAPI.INSTANCE.deserialize(object);
        ItemStack result = itemStream.rebuildToItemStack(player);
        result.setAmount(amount);

        return result;
    }
}
