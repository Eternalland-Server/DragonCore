package eos.moe.dragoncore.api.easygui.component;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@GuiField(name = "entity")
public class EasyEntityView extends BasicComponent {
    @GuiField(name = "x")
    private int x;
    @GuiField(name = "y")
    private int y;
    @GuiField(name = "scale")
    private int scale;
    @GuiField(name = "entity")
    private String entity;

    public EasyEntityView(int x, int y, int scale, UUID entity) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.entity = entity.toString();
    }

    public EasyEntityView(int x, int y, UUID entity) {
        this(x, y, 1, entity);
    }
}
