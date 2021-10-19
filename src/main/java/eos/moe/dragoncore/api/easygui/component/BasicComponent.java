package eos.moe.dragoncore.api.easygui.component;

import eos.moe.dragoncore.api.easygui.component.listener.ClickListener;
import eos.moe.dragoncore.api.easygui.component.listener.TextListener;
import eos.moe.dragoncore.util.Utils;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Getter
public abstract class BasicComponent implements EasyComponent {
    private String id;

    public BasicComponent() {
        id = Utils.generateComponentId();
    }

    public Map<String, Map<String, Object>> build(Player player, Map<String, String> functions) {
        LinkedHashMap<String, Map<String, Object>> map = new LinkedHashMap<>();
        Map<String, Object> componentMap = new LinkedHashMap<>();
        map.put(this.getId(), componentMap);
        try {
            GuiField annotation1 = this.getClass().getAnnotation(GuiField.class);
            if (annotation1 == null) throw new RuntimeException(this.getClass() + " 未定义GuiField");

            componentMap.put("type", annotation1.name());

            List<Field> declaredFields = Utils.getDeclaredFields(this.getClass());
            for (Field declaredField : declaredFields) {
                GuiField annotation = declaredField.getAnnotation(GuiField.class);
                if (annotation != null) {
                    declaredField.setAccessible(true);
                    componentMap.put(annotation.name(), declaredField.get(this));
                }
            }
            Map<String, Object> componentActions = new LinkedHashMap<>();
            componentMap.put("actions", componentActions);
            if (this instanceof TextListener) {
                componentActions.put("textChange", String.format("func.Packet_Send('EasyScreenEvent','%s','textbox',%s.text);", this.getId(), this.getId()));
            }
            if (this instanceof ClickListener) {
                componentActions.put("click_left", String.format("func.Packet_Send('EasyScreenEvent','%s','click','LEFT');", this.getId()));
                componentActions.put("click_right", String.format("func.Packet_Send('EasyScreenEvent','%s','click','RIGHT');", this.getId()));
                componentActions.put("click_middle", String.format("func.Packet_Send('EasyScreenEvent','%s','click','MIDDLE');", this.getId()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}
