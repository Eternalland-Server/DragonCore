package eos.moe.dragoncore.database.mysql;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.database.IDataBase;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        AccountTable.DRAGON_CORE_ACCOUNT.createTable();
    }

    @Override
    public void getData(Player player, String identifier, Callback<ItemStack> callback) {

    }

    @Override
    public void setData(Player player, String identifier, ItemStack itemStack, Callback<ItemStack> callback) {

    }

    @Override
    public void getAllData(Player player, Callback<Map<String, ItemStack>> callback) {

    }
}
