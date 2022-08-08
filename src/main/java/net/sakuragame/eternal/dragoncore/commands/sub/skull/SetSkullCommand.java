package net.sakuragame.eternal.dragoncore.commands.sub.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.TileEntitySkull;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetSkullCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "setSkull";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        String id = args[0];
        Player player = this.getPlayer();

        Block lookAt = player.getTargetBlock(null, 2);
        if (lookAt == null) return;
        if (lookAt.getType() != Material.SKULL) return;

        TileEntity tileEntity = ((CraftWorld) lookAt.getWorld()).getTileEntityAt(lookAt.getX(), lookAt.getY(), lookAt.getZ());
        if (tileEntity instanceof TileEntitySkull) {
            TileEntitySkull entity = (TileEntitySkull) tileEntity;

            GameProfile rua = new GameProfile(UUID.randomUUID(), id);
            rua.getProperties().put("textures", new Property("", ""));
            rua.getProperties().put("model", new Property("model", "model: " + id));

            entity.setGameProfile(rua);
            entity.update();
            lookAt.getState().update();
        }

        player.sendMessage(" §7设置成功");
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
