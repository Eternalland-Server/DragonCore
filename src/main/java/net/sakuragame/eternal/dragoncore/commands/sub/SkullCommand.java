package net.sakuragame.eternal.dragoncore.commands.sub;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.commands.CommandPerms;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "skull";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 2) return;

        String id = args[0];
        String s = args[1];

        if (!MegumiUtil.isInteger(s)) return;
        int amount = Integer.parseInt(s);

        Player player = this.getPlayer();

        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            GameProfile profile = new GameProfile(UUID.randomUUID(), id);
            profile.getProperties().put("textures", new Property("", ""));
            profile.getProperties().put("model", new Property("model", "model: " + id));
            profileField.set(meta, profile);

            skull.setItemMeta(meta);
            skull.setAmount(amount);

            player.getInventory().addItem(skull);
            sender.sendMessage(" §7头颅方块获取成功!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
