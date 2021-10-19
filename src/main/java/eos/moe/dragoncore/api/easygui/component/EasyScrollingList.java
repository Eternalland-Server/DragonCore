package eos.moe.dragoncore.api.easygui.component;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
public class EasyScrollingList extends BasicContainer {
    //private EasyComponent parent;
    private int x;
    private int y;
    private int w;
    private int h;
    private int barW;
    private int barH;
    private int scrollHeight;
    private String url;
    private String barUrl;

    public EasyScrollingList(int x, int y, int w, int h, String url) {
        this.url = url;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setBar(int w, int h, int scrollHeight, String url) {
        this.barW = w;
        this.barH = h;
        this.scrollHeight = scrollHeight;
        this.barUrl = url;
    }


    @Override
    public Map<String, Map<String, Object>> build(Player player, Map<String, String> functions) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        LinkedHashMap<String, String> actions = new LinkedHashMap<>();
        actions.put("wheel", String.format("%s_bar.distanceY=%s_bar.distanceY-func.mouse_get_wheel*%.2f;", getId(), getId(), ((this.h - barH) * 0.1f)));

        Map<String, Object> bgMap = new LinkedHashMap<>();
        map.put(this.getId() + "_bg", bgMap);
        bgMap.put("type", "texture");
        bgMap.put("x", x);
        bgMap.put("y", y);
        bgMap.put("width", w);
        bgMap.put("height", h);
        bgMap.put("texture", url);
        bgMap.put("actions", actions);

        Map<String, Object> extends_ = new LinkedHashMap<>();
        map.put(this.getId() + "_extends", extends_);
        extends_.put("limitX", x);
        extends_.put("limitY", y);
        extends_.put("limitWidth", w - barW);
        extends_.put("limitHeight", h);
        for (EasyComponent component : this.getComponents().values()) {
            Map<String, Map<String, Object>> build = component.build(player, functions);
            for (Map<String, Object> value : build.values()) {
                value.put("x", String.format("%d+%s", x, value.getOrDefault("x", 0)));
                value.put("y", String.format("%d-%s_bar.dy*%d+%s", y, this.getId(),
                        scrollHeight, value.getOrDefault("y", 0)));
                value.put("extends", this.getId() + "_extends");
            }
            map.putAll(build);
        }

        Map<String, Object> barMap = new LinkedHashMap<>();
        map.put(this.getId() + "_bar", barMap);
        barMap.put("type", "texture");
        barMap.put("x", x + w - barW);
        barMap.put("y", y);
        barMap.put("width", barW);
        barMap.put("height", barH);
        barMap.put("texture", barUrl);
        barMap.put("maxDistanceY", this.h - barH);

        return map;
    }
}
