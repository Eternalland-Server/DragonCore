package eos.moe.dragoncore.api.easygui.component;

import lombok.Getter;
import lombok.Setter;

@GuiField(name = "texture")
@Getter
@Setter
public class EasyImage extends BasicComponent {
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
    @GuiField(name = "text")
    private String text;

    public EasyImage(int x, int y, int w, int h, String url) {
        this(x, y, w, h, url, "");
    }

    public EasyImage(int x, int y, int w, int h, String url, String text) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.url = url;
        this.text = text;
    }
}
