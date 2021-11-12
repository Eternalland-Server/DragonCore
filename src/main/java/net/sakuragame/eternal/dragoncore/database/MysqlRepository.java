package net.sakuragame.eternal.dragoncore.database;

import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.database.mysql.DragonCoreTable;
import net.sakuragame.eternal.dragoncore.util.ItemUtil;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
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

    public MysqlRepository(DragonCore plugin) {
        this.plugin = plugin;
        this.dataManager = ClientManagerAPI.getDataManager();
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

                ItemStack item = ZaphkielAPI.INSTANCE.deserialize(data).rebuildToItemStack(player);
                Scheduler.run(() -> callback.onResult(item));
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
        String data = ZaphkielAPI.INSTANCE.serialize(itemStack).toString();

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
                    ItemStack item = ItemUtil.jsonToItem(data.replace("/|/", "'"));
                    items.put(ident, item);
                }
                catch (MojangsonParseException e) {
                    items.put(ident, new ItemStack(Material.AIR));
                }
            }
            Scheduler.run(() -> callback.onResult(items));
        }
        catch (Exception e) {
            e.printStackTrace();
            Scheduler.run(callback::onFail);
        }
    }
}
