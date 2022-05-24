package net.sakuragame.eternal.dragoncore.database;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.database.mysql.DragonCoreTable;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
    public void getData(Player player, String identifier, Callback<ItemStack> callback) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());

        try (DatabaseQuery query = dataManager.createQuery(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(), new String[] {"uid", "ident"},
                new Object[] {uid, identifier}
        )) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                String id = result.getString("item_id");
                if (id == null || id.equals("")) {
                    Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                    return;
                }

                int amount = result.getInt("item_amount");
                String data = result.getString("item_data");
                String unique = result.getString("item_unique");

                ItemStack itemStack = deserialize(player, id, amount, data, unique);
                Scheduler.run(() -> callback.onResult(itemStack));
            } else {
                Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Scheduler.run(callback::onFail);
        }
    }

    @Override
    public void setData(Player player, String identifier, ItemStack itemStack, Callback<ItemStack> callback) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());

        JsonObject json = MegumiUtil.isEmpty(itemStack) ? null : ZaphkielAPI.INSTANCE.serialize(itemStack);
        if (json != null) {
            json.addProperty("amount", itemStack.getAmount());
        }

        String data = json == null ? "" : json.toString();

        dataManager.executeReplace(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(),
                new String[] {"uid", "ident", "data"},
                new Object[] {uid, identifier, data}
        );
        Scheduler.run(() -> callback.onResult(itemStack));
    }

    @Override
    public void getAllData(Player player, Callback<Map<String, ItemStack>> callback) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());
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
                if (id == null || id.equals("")) {
                    items.put(ident, new ItemStack(Material.AIR));
                    continue;
                }

                int amount = result.getInt("item_amount");
                String data = result.getString("item_data");
                String unique = result.getString("item_unique");

                ItemStack itemStack = deserialize(player, id, amount, data, unique);
                items.put(ident, itemStack);
            }
            Scheduler.run(() -> callback.onResult(items));
        }
        catch (Exception e) {
            e.printStackTrace();
            Scheduler.run(callback::onFail);
        }
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
