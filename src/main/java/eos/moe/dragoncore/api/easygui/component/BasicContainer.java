package eos.moe.dragoncore.api.easygui.component;

import eos.moe.dragoncore.util.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BasicContainer implements EasyContainer {

    private Map<String, EasyComponent> components;
    private String id;

    public BasicContainer() {
        components = new LinkedHashMap<>();
        id = Utils.generateComponentId();
    }


    @Override
    public void addComponent(String name, EasyComponent component) {
        components.put(name, component);
    }

    @Override
    public void addComponent(EasyComponent component) {
        components.put(component.getId(), component);
    }

    @Override
    public Map<String, EasyComponent> getComponents() {
        return components;
    }

    @Override
    public EasyComponent getComponent(String name) {
        return components.get(name);
    }

    @Override
    public EasyComponent getComponentById(String id) {
        for (EasyComponent component : components.values()) {
            if (component instanceof EasyContainer) {
                EasyComponent componentByKey = ((EasyContainer) component).getComponentById(id);
                if (componentByKey != null) {
                    return componentByKey;
                }
            }
            if (component.getId().equals(id))
                return component;
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }
}
