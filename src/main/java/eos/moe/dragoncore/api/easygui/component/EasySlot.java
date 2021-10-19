package eos.moe.dragoncore.api.easygui.component;

import eos.moe.dragoncore.api.easygui.component.listener.ClickListener;
import eos.moe.dragoncore.network.PacketSender;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Setter
@Getter
@GuiField(name = "slot")
public class EasySlot extends BasicComponent implements ClickListener {
    @GuiField(name = "x")
    private int x;
    @GuiField(name = "y")
    private int y;
    private ItemStack itemStack;
    @GuiField(name = "identifier")
    private String identifier;

    public EasySlot(int x, int y, String identifier) {
        this.x = x;
        this.y = y;
        this.identifier = identifier;
        this.itemStack = new ItemStack(Material.AIR);
    }

    public EasySlot(int x, int y, ItemStack itemStack) {
        this.x = x;
        this.y = y;
        this.itemStack = itemStack;
        this.identifier = this.getId();
    }

    @Override
    public void onClick(Player player, ClickListener.Type type) {
        player.sendMessage("你点击了物品槽" + type.name());
    }

    public void updateItemStack(Player player) {
        if (!identifier.startsWith("container_")) {
            PacketSender.putClientSlotItem(player, identifier, itemStack);
        }
    }

    @Override
    public Map<String, Map<String, Object>> build(Player player, Map<String, String> functions) {
        if (!identifier.startsWith("container_") && player != null) {
            PacketSender.putClientSlotItem(player, identifier, itemStack);
        }
        return super.build(player, functions);
    }
}
