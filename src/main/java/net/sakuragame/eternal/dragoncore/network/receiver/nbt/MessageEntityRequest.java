package net.sakuragame.eternal.dragoncore.network.receiver.nbt;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldServer;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import net.sakuragame.eternal.dragoncore.network.PacketBuffer;
import net.sakuragame.eternal.dragoncore.network.PacketSenderWithNBTEdit;
import net.sakuragame.eternal.dragoncore.network.receiver.IMessage;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

public class MessageEntityRequest implements IMessage {

    private int entityID;

    @Override
    public void read(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
    }

    @Override
    public void onMessage(Player player) {
        if (!player.hasPermission(CommandPerms.ADMIN.getNode())) return;
        World world = player.getWorld();
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer handle = craftWorld.getHandle();
        Entity entity = handle.getEntity(this.entityID);
        if (entity != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            entity.save(tagCompound);
            PacketSenderWithNBTEdit.sendEntityNBTPacket(player, this.entityID, tagCompound);
        }
    }
}