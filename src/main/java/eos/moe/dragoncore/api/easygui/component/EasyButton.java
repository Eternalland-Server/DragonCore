package eos.moe.dragoncore.api.easygui.component;

import eos.moe.dragoncore.api.easygui.component.listener.ClickListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@GuiField(name = "texture")
@Getter
@Setter
public class EasyButton extends BasicComponent implements ClickListener {
    @GuiField(name = "x")
    private int x;
    @GuiField(name = "y")
    private int y;
    @GuiField(name = "width")
    private int w;
    @GuiField(name = "height")
    private int h;
    @GuiField(name = "texture")
    private String url;
    @GuiField(name = "textureHovered")
    private String urlHov;

    @GuiField(name = "text")
    private String text;

    public EasyButton(int x, int y, int w, int h, String url) {
        this(x, y, w, h, url, "", "");
    }

    public EasyButton(int x, int y, int w, int h, String url, String urlHov) {
        this(x, y, w, h, url, urlHov, "");
    }

    public EasyButton(int x, int y, int w, int h, String url, String urlHov, String text) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.url = url;
        this.urlHov = urlHov;
        this.text = text;
    }

    @Override
    public void onClick(Player player, ClickListener.Type type) {
        player.sendMessage("你点击了按钮" + type.name());
    }
}
