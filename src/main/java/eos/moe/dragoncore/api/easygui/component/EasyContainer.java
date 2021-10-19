package eos.moe.dragoncore.api.easygui.component;

import java.util.Map;

public interface EasyContainer extends EasyComponent {
    Map<String, EasyComponent> getComponents();

    EasyComponent getComponent(String name);

    EasyComponent getComponentById(String key);

    void addComponent(String name, EasyComponent component);

    void addComponent(EasyComponent component);
}
