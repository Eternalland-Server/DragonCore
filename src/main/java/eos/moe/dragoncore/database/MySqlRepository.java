package eos.moe.dragoncore.database;

import com.zaxxer.hikari.HikariDataSource;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.config.SlotSetting;
import eos.moe.dragoncore.util.ItemUtil;
import eos.moe.dragoncore.util.Scheduler;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class MySqlRepository implements IDataBase {

    private HikariDataSource dataSource;

    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;
    private String table;
    private int maxConnections;
    private static ExecutorService EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
        private final ThreadGroup group = new ThreadGroup("DragonCore MySql Threads");
        private int index = 1;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "DragonCore Thread #" + this.index++);
        }
    });

    public MySqlRepository() {
        YamlConfiguration config = Config.getConfig();
        hostname = config.getString("SQL.hostname");
        port = config.getString("SQL.port");
        database = config.getString("SQL.database");
        username = config.getString("SQL.username");
        password = config.getString("SQL.password");
        table = config.getString("SQL.table");
        maxConnections = config.getInt("SQL.maxconnections");
        if (maxConnections <= 0) {
            maxConnections = Runtime.getRuntime().availableProcessors() * 2;
        }


        List<String> list = config.getStringList("SQL.properties");
        if (!config.contains("SQL.properties")) {
            list = new ArrayList<>();
            list.add("useUnicode=true");
            list.add("characterEncoding=utf8");
            list.add("useSSL=false");
        }
        StringBuilder str = new StringBuilder();
        for (String s : list) {
            str.append("&").append(s);
        }
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database +
                "?autoReconnect=true" + str);
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
        this.dataSource.setMaximumPoolSize(maxConnections);
        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.setLeakDetectionThreshold(10000L);
        this.dataSource.setPoolName("DragonCore-Connection-Pool");

        try (final Connection connection = this.dataSource.getConnection();
             Statement create = connection.createStatement()) {
            create.executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (`name` varchar(100) NOT NULL);");
            try {
                create.executeUpdate("ALTER TABLE " + table + " ADD CONSTRAINT name UNIQUE (name);");
            } catch (Exception e) {

            }
        } catch (SQLException e2) {
            Bukkit.getConsoleSender().sendMessage("无法连接到数据库！！");
            e2.printStackTrace();
        }
    }


    @Override
    public void getData(Player player, String identifier, Callback<ItemStack> callback) {

        EXECUTOR.execute(() -> {
            try (Connection connection = MySqlRepository.this.dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?;")) {
                statement.setString(1, player.getName());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String string = null;
                    try {
                        string = resultSet.getString(identifier);
                    } catch (SQLException ignored) {

                    }
                    if (string == null || string.isEmpty()) {
                        Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                    } else {
                        try {
                            ItemStack itemStack = ItemUtil.jsonToItem(string.replace("/|/", "'"));
                            Scheduler.run(() -> callback.onResult(itemStack));
                        } catch (MojangsonParseException e) {
                            Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                        }
                    }
                } else {
                    Scheduler.run(() -> callback.onResult(new ItemStack(Material.AIR)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Scheduler.run(callback::onFail);
            }
        });
    }

    @Override
    public void setData(Player player, String identifier, ItemStack itemStack, Callback<ItemStack> callback) {

        EXECUTOR.execute(() -> {
            try (final Connection connection = MySqlRepository.this.dataSource.getConnection();
                 final PreparedStatement statementAlter = connection.prepareStatement("ALTER TABLE " + table + " ADD COLUMN " + identifier + " TEXT");
                 final PreparedStatement statementSave = connection.prepareStatement("INSERT INTO " + table + " (name," + identifier + ") VALUES(?,?) ON DUPLICATE KEY UPDATE " + identifier + "=?;");
            ) {
                try {
                    statementAlter.executeUpdate();
                } catch (Exception ignored) {

                }
                String save = ItemUtil.itemToJson(itemStack).replace("'", "/|/");

                statementSave.setString(1, player.getName());
                statementSave.setString(2, save);
                statementSave.setString(3, save);
                statementSave.executeUpdate();
                Scheduler.run(() -> callback.onResult(itemStack));
            } catch (SQLException e) {
                e.printStackTrace();
                Scheduler.run(callback::onFail);
            }
        });
    }

    @Override
    public void getAllData(Player player, Callback<Map<String, ItemStack>> callback) {
        EXECUTOR.execute(() -> {
            try (Connection connection = MySqlRepository.this.dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?;")) {

                statement.setString(1, player.getName());
                ResultSet resultSet = statement.executeQuery();
                Map<String, ItemStack> items = new HashMap<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    items.put(columnName, null);
                }
                if (resultSet.next()) {
                    Map<String, SlotSetting> slotSettings = Config.slotSettings;
                    items.entrySet().removeIf(entry -> !slotSettings.containsKey(entry.getKey()));
                    for (String identifier : items.keySet()) {
                        String string = resultSet.getString(identifier);

                        if (string == null || string.isEmpty()) {
                            items.put(identifier, new ItemStack(Material.AIR));
                        } else {
                            try {
                                ItemStack itemStack = ItemUtil.jsonToItem(string.replace("/|/", "'"));
                                items.put(identifier, itemStack);
                            } catch (MojangsonParseException e) {
                                items.put(identifier, new ItemStack(Material.AIR));
                            }
                        }

                    }
                }
                Scheduler.run(() -> callback.onResult(items));
            } catch (SQLException e) {
                e.printStackTrace();
                Scheduler.run(callback::onFail);
            }
        });
    }

    @Override
    public void close() {
        dataSource.close();
        EXECUTOR.shutdown();
    }
}
