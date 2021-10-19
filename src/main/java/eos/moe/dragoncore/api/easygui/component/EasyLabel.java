package eos.moe.dragoncore.api.easygui.component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@GuiField(name = "label")
public class EasyLabel extends BasicComponent {
    @GuiField(name = "x")
    private int x;
    @GuiField(name = "y")
    private int y;
    @GuiField(name = "scale")
    private int scale;
    @GuiField(name = "texts")
    private List<String> texts;
    @GuiField(name = "center")
    private boolean center;
    @GuiField(name = "font")
    private String font;

    public EasyLabel(int x, int y, int scale, List<String> texts, String font, boolean center) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.texts = texts;
        this.font = font;
        this.center = center;
    }

    public EasyLabel(int x, int y, int scale, List<String> texts, boolean center) {
        this(x, y, scale, texts, "", center);
    }

    public EasyLabel(int x, int y, int scale, List<String> texts) {
        this(x, y, scale, texts, false);
    }

    public EasyLabel(int x, int y, List<String> texts, boolean center) {
        this(x, y, 1, texts, center);
    }

    public EasyLabel(int x, int y, List<String> texts) {
        this(x, y, 1, texts, false);
    }
}
