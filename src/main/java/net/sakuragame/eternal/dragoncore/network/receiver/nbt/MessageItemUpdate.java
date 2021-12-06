package net.sakuragame.eternal.dragoncore.network.receiver.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.sakuragame.eternal.dragoncore.network.PacketSenderWithNBTEdit;
import net.sakuragame.eternal.dragoncore.network.receiver.IMessage;
import net.sakuragame.eternal.dragoncore.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageItemUpdate implements IMessage {
    private NBTTagCompound nbt;

    @Override
    public void read(PacketBuffer buffer) {
        nbt = PacketSenderWithNBTEdit.readNBT(buffer);
    }

    @Override
    public void onMessage(Player player) {
        if (!player.hasPermission(CommandPerms.ADMIN.getNode())) return;

        ItemStack itemStack = ItemUtil.nbtToItem(nbt);
        player.getInventory().setItemInMainHand(itemStack);
        player.updateInventory();
    }
}
