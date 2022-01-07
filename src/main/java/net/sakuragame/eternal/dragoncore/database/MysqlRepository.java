package net.sakuragame.eternal.dragoncore.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    private final DragonCore plugin;
    private final DataManager dataManager;

    private final Gson gson;

    public MysqlRepository(DragonCore plugin) {
        this.plugin = plugin;
        this.dataManager = ClientManagerAPI.getDataManager();
        this.gson = new Gson();
        this.init();
    }

    public void init() {
        DragonCoreTable.DRAGON_CORE_SLOTS.createTable();
    }

    @Override
    public void getData(Player player, String identifier, Callback<ItemStack> callback) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());

        try (DatabaseQuery query = dataManager.createQuery(DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(), new String[] {"uid", "ident"}, new Object[] {uid, identifier})) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                String data = result.getString("data");
                if (data == null || data.isEmpty()) {
                    Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                    return;
                }

                ItemStack itemStack = deserialize(player, data);
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
                String data = result.getString("data");
                if (data == null || data.isEmpty()) {
                    items.put(ident, new ItemStack(Material.AIR));
                    continue;
                }

                try {
                    ItemStack item = deserialize(player, data);
                    items.put(ident, item);
                }
                catch (IllegalStateException e) {
                    plugin.getLogger().info(String.format("读取 %s 玩家 %s 槽位物品失败", player.getName(), ident));
                }
            }
            Scheduler.run(() -> callback.onResult(items));
        }
        catch (Exception e) {
            e.printStackTrace();
            Scheduler.run(callback::onFail);
        }
    }

    private ItemStack deserialize(Player player, String input) {
        try {
            JsonObject target = gson.fromJson(input, JsonObject.class);
            ItemStream itemStream = ZaphkielAPI.INSTANCE.deserialize(target);
            ItemStack itemStack = itemStream.rebuildToItemStack(player);
            itemStack.setAmount(target.get("amount").getAsInt());

            return itemStack;
        }
        catch (Exception e) {
            return new ItemStack(Material.AIR);
        }
    }
}
