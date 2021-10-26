package eos.moe.dragoncore.config;

public enum FolderType {
    
    Armor("ArmorLayer"),
    Blood("Blood"),
    EntityModel("EntityModel"),
    FontConfig("FontConfig"),
    Gui("Gui"),
    ItemIcon("ItemIcon"),
    ItemModel("ItemModel"),
    ItemTip("ItemTip");

    private final String name;

    FolderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String format(String file) {
        return name + "/" + file;
    }
}
