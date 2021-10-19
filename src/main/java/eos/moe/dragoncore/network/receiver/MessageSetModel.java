package eos.moe.dragoncore.network.receiver;

import eos.moe.dragoncore.network.PacketBuffer;
import eos.moe.dragoncore.util.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageSetModel implements IMessage {
    private int slot;
    private String name;

    @Override
    public void read(PacketBuffer buffer) {
        slot = buffer.readInt();
        name = buffer.readString();
    }

    @Override
    public void onMessage(Player player) {
        if (player.isOp()) {
            ItemStack itemStack = player.getInventory().getItem(slot);
            NBTUtils.setNBT(itemStack, "model", name);
            player.sendMessage("已为物品添加NBT: model: \"" + name + "\"");
        }
    }
}
