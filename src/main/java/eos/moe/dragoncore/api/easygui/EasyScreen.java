package eos.moe.dragoncore.api.easygui;

import eos.moe.dragoncore.api.easygui.component.BasicContainer;
import eos.moe.dragoncore.api.easygui.component.EasyComponent;
import eos.moe.dragoncore.network.PacketSender;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class EasyScreen extends BasicContainer {
    private static final Map<UUID, EasyScreen> playerScreens = new ConcurrentHashMap<>();

    public static void setOpenedScreen(Player player, EasyScreen screen) {
        closeScreen(player, false);
        playerScreens.put(player.getUniqueId(), screen);

        YamlConfiguration yaml = screen.build(player);
        PacketSender.sendYaml(player, "Gui/easy_guiscreen.yml", yaml);
        PacketSender.sendOpenGui(player, "easy_guiscreen");
    }

    public static EasyScreen getOpenedScreen(Player player) {
        return playerScreens.get(player.getUniqueId());
    }

    public static EasyScreen closeScreen(Player player, String id) {
        EasyScreen remove = playerScreens.get(player.getUniqueId());
        if (remove != null && remove.getId().equals(id)) {
            playerScreens.remove(player.getUniqueId());
            remove.onClose(player);
            player.closeInventory();
        }

        return remove;
    }

    public static EasyScreen closeScreen(Player player, boolean force) {
        EasyScreen remove = playerScreens.remove(player.getUniqueId());
        if (remove != null) {
            remove.onClose(player);
        }
        if (force) {
            player.closeInventory();
        }
        return remove;
    }

    public static EasyScreen closeScreen(Player player) {
        return closeScreen(player, true);
    }

    private String url;
    private int w;
    private int h;


    public EasyScreen(String url, int w, int h) {
        this.url = url;
        this.w = w;
        this.h = h;
    }

    public void onClose(Player player) {

    }

    public boolean updateGui(Player player) {
        if (getOpenedScreen(player) != this) {
            return false;
        } else {
            YamlConfiguration yaml = this.build(player);
            PacketSender.sendUpdateGui(player, yaml);
            return true;
        }
    }

    public void openGui(Player player) {
        setOpenedScreen(player, this);
    }

    public void closeGui(Player player) {
        closeScreen(player, this.getId());
    }

    public void buildThenPrint() {
        YamlConfiguration yaml = this.build(null);
        // TODO: 2021/4/4 调试输出
        System.out.println("\n" + yaml.saveToString());
    }

    public YamlConfiguration build(Player player) {
        Map<String, String> functions = new LinkedHashMap<>();
        Map<String, Map<String, Object>> guiScreenMap = build(player, functions);
        // 把Map转为Yaml
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("Functions", functions);
        for (Map.Entry<String, Map<String, Object>> entry : guiScreenMap.entrySet()) {
            yaml.set(entry.getKey(), entry.getValue());
        }

        return yaml;
    }

    @Override
    public Map<String, Map<String, Object>> build(Player player, Map<String, String> functions) {
        Map<String, Map<String, Object>> guiScreenMap = new LinkedHashMap<>();
        functions.put("close", String.format("方法.发包('EasyScreenCloseEvent','%s');", this.getId()));
        Map<String, Object> background = new LinkedHashMap<>();
        guiScreenMap.put("background", background);
        background.put("type", "texture");
        background.put("x", String.format("(w-%d)/2", w));
        background.put("y", String.format("(h-%d)/2", h));
        background.put("height", h);
        background.put("width", w);
        background.put("texture", url);

        for (EasyComponent component : this.getComponents().values()) {
            Map<String, Map<String, Object>> build = component.build(player, functions);
            for (Map<String, Object> value : build.values()) {
                if (!value.containsKey("position")) {
                    value.put("position", "1_background");
                }
            }
            guiScreenMap.putAll(build);
        }
        return guiScreenMap;
    }
}
