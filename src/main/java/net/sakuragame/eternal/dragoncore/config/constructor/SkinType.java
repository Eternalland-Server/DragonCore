package net.sakuragame.eternal.dragoncore.config.constructor;

public enum SkinType {

    None(-1),
    Suit(0, "USER_HIDE_SUIT"),
    Part(1, "USER_HIDE_PART"),
    Wings(2, "USER_HIDE_WINGS");

    private final int ID;
    private final String identifier;

    SkinType(int ID) {
        this.ID = ID;
        this.identifier = null;
    }

    SkinType(int ID, String identifier) {
        this.ID = ID;
        this.identifier = identifier;
    }

    public int getID() {
        return ID;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static SkinType match(int ID) {
        for (SkinType type : values()) {
            if (type.getID() != ID) continue;
            return type;
        }

        return None;
    }
}
