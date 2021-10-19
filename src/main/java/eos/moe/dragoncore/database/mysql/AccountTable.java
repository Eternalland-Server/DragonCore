package eos.moe.dragoncore.database.mysql;

public enum AccountTable {

    DRAGON_CORE_ACCOUNT(new DatabaseTable("dragoncore_account",
            new String[] {
                    "`uuid` varchar(36) NOT NULL PRIMARY KEY",
                    "`player` varchar(48) NOT NULL",
                    "`slots` json default NULL"
            }));

    private DatabaseTable table;

    AccountTable(DatabaseTable table) {
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
