package eos.moe.dragoncore.commands;

public enum CommandPerms {

    USER("dragoncore.user"),
    ADMIN("dragoncore.admin");

    private final String node;

    CommandPerms(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
