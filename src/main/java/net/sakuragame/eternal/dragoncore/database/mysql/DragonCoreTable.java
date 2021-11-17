package net.sakuragame.eternal.dragoncore.database.mysql;

public enum DragonCoreTable {

    DRAGON_CORE_SLOTS(new DatabaseTable("dragoncore_slots",
            new String[] {
                    "`uid` int NOT NULL",
                    "`ident` varchar(32) NOT NULL",
                    "`data` text default NULL",
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
