package net.sakuragame.eternal.dragoncore.database;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlRepository implements IDataBase {
    private File folder;

    public YamlRepository(File folder) {
        this.folder = folder;
        folder.mkdirs();
    }

    @Override
    public void getData(Player player, String identifier, Callback<ItemStack> callback) {
        File file = new File(folder, player.getUniqueId().toString());
        if (!file.exists()) {
            callback.onResult(new ItemStack(Material.AIR));
        } else {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            callback.onResult(yaml.getItemStack("Item." + identifier));
        }
    }

    @Override
    public void getAllData(Player player, Callback<Map<String, ItemStack>> callback) {
        File file = new File(folder, player.getUniqueId().toString());

        if (!file.exists()) {

            callback.onResult(new HashMap<>());
        } else {

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection item = yaml.getConfigurationSection("Item");
            if (item == null) {

                callback.onResult(new HashMap<>());
            } else {

                Map<String, ItemStack> map = new HashMap<>();
                for (String key : item.getKeys(false)) {
                    map.put(key,item.getItemStack(key));
                }

                callback.onResult(map);
            }
        }
    }

    @Override
    public void setData(Player player, String identifier, ItemStack itemStack, Callback<ItemStack> callback) {
        File file = new File(folder, player.getUniqueId().toString());
        YamlConfiguration yaml = new YamlConfiguration();
        if (file.exists()) {
            try {
                yaml.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                callback.onFail();
                e.printStackTrace();
                return;
            }
        } else {
            yaml.set("Name", player.getName());
            yaml.set("UUID", player.getUniqueId().toString());
        }
        yaml.set("Item." + identifier, itemStack);
        try {
            yaml.save(file);
            callback.onResult(itemStack);
        } catch (IOException e) {
            callback.onFail();
            e.printStackTrace();
        }
    }
}
