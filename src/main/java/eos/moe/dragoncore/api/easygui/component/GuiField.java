package eos.moe.dragoncore.api.easygui.component;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiField {
    String name();
}
