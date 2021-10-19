package eos.moe.dragoncore.api.easygui.component;

import eos.moe.dragoncore.api.easygui.component.listener.TextListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GuiField(name = "textbox")
public class EasyTextField extends BasicComponent implements TextListener {
    @GuiField(name = "x")
    private int x;
    @GuiField(name = "y")
    private int y;
    @GuiField(name = "width")
    private int w;
    @GuiField(name = "height")
    private int h;
    @GuiField(name = "text")
    private String text;

    public EasyTextField(int x, int y, int w) {
        this(x, y, w, "");
    }

    public EasyTextField(int x, int y, int w, String text) {
        this(x, y, w, 14, text);
    }

    public EasyTextField(int x, int y, int w, int h, String text) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
    }

    public void onTextChange(String text) {
        this.text = text;
    }
}
