package net.sakuragame.eternal.dragoncore.database.mysql;

public enum DragonCoreTable {

    DRAGON_CORE_SLOTS(new DatabaseTable("dragoncore_slots",
            new String[] {
                    "`uid` int NOT NULL",
                    "`ident` varchar(32) NOT NULL",
                    "`item_id` varchar(64)",
                    "`item_amount` int default 0",
                    "`item_data` varchar(512)",
                    "`item_unique` varchar(128)",
                    "UNIQUE KEY `account` (`uid`,`ident`)",
            }));

    private final DatabaseTable table;

    DragonCoreTable(DatabaseTable table) {
        this.table = table;
    }

    public String getTableName() {
        return table.getTableName();
    }

    public String[] getColumns() {
        return table.getTableColumns();
    }

    public DatabaseTable getTable() {
        return table;
    }

    public void createTable() {
        table.createTable();
    }
}
