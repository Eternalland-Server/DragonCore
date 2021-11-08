package net.sakuragame.eternal.dragoncore.api.slot;

import lombok.Getter;

public enum ClickType {

    LEFT_CLICK(0),
    RIGHT_CLICK(1),
    MIDDLE_CLICK(2);

    @Getter private int param;

    ClickType(int param) {
        this.param = param;
    }

    public static ClickType getType(int param) {
        for (ClickType type : values()) {
            if (type.getParam() != param) continue;
            return type;
        }

        return null;
    }
}
