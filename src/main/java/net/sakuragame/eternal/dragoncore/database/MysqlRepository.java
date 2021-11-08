package net.sakuragame.eternal.dragoncore.database;

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

    private DragonCore plugin;
    private DataManager dataManager;

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
        try (DatabaseQuery query = dataManager.createQuery(DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(), new String[] {"uuid", "slot"}, new String[] {player.getUniqueId().toString(), identifier})) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                String data = result.getString("data");
                if (data == null || data.isEmpty()) {
                    Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                    return;
                }

                try {
                    ItemStack item = ItemUtil.jsonToItem(data.replace("/|/", "'"));
                    Scheduler.run(() -> callback.onResult(item));
                }
                catch (MojangsonParseException e) {
                    Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                }
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
        String data = ItemUtil.itemToJson(itemStack).replace("'", "/|/");

        dataManager.executeReplace(
                DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(),
                new String[] {"uuid", "slot", "data"},
                new Object[] {player.getUniqueId().toString(), identifier, data}
        );
        Scheduler.run(() -> callback.onResult(itemStack));
    }

    @Override
    public void getAllData(Player player, Callback<Map<String, ItemStack>> callback) {
        Map<String, ItemStack> items = new HashMap<>();

        try (DatabaseQuery query = dataManager.createQuery(DragonCoreTable.DRAGON_CORE_SLOTS.getTableName(), "uuid", player.getUniqueId().toString())) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                String slot = result.getString("slot");
                String data = result.getString("data");
                if (data == null || data.isEmpty()) {
                    items.put(slot, new ItemStack(Material.AIR));
                    continue;
                }
                try {
                    ItemStack item = ItemUtil.jsonToItem(data.replace("/|/", "'"));
                    items.put(slot, item);
                }
                catch (MojangsonParseException e) {
                    items.put(slot, new ItemStack(Material.AIR));
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
