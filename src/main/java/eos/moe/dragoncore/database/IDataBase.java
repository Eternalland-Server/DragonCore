package eos.moe.dragoncore.database;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface IDataBase {

    void getData(Player player, String identifier, Callback<ItemStack> callback);

    void setData(Player player, String identifier, ItemStack itemStack, Callback<ItemStack> callback);

    void getAllData(Player player, Callback<Map<String, ItemStack>> callback);

    default void close() {
    }

    public interface Callback<T> {
        void onResult(T p0);

        void onFail();
    }
}
