package eos.moe.dragoncore.api;

import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.ItemUtil;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CooldownAPI {
    private static final Map<Cooldown, Long> map = new HashMap<>();

    public static void setPlayerCooldown(Player player, ItemStack itemStack, int milliseconds) {
        Validate.notNull(itemStack);
        setPlayerCooldown(player, itemStack.getType().name(), ItemUtil.getName(itemStack), ItemUtil.getLore(itemStack), Collections.EMPTY_MAP, milliseconds);
    }

    public static void setPlayerCooldown(Player player, String material, String name, List<String> lore, Map<String, String> nbts, int milliseconds) {
        removeOutdated();

        Cooldown cooldown = new Cooldown(player.getUniqueId(), material, name, String.join("|~|", lore), nbts);
        map.put(cooldown, System.currentTimeMillis() + milliseconds);
        // 发包
        PacketSender.sendCooldown(player, System.currentTimeMillis(), System.currentTimeMillis() + milliseconds,
                cooldown.material, cooldown.name, cooldown.lore, cooldown.nbts);
    }

    public static int getPlayerCooldown(Player player, ItemStack itemStack) {
        return getPlayerCooldown(player, itemStack.getType().name(), ItemUtil.getName(itemStack), ItemUtil.getLore(itemStack), Collections.EMPTY_MAP);
    }

    public static int getPlayerCooldown(Player player, String material, String name, List<String> lore, Map<String, String> nbts) {
        removeOutdated();
        Cooldown cooldown = new Cooldown(player.getUniqueId(), material, name, String.join("|~|", lore), nbts);
        if (map.containsKey(cooldown)) {
            Long aLong = map.get(cooldown);
            return aLong < System.currentTimeMillis() ? 0 : (int) (aLong - System.currentTimeMillis());
        } else {
            return 0;
        }
    }

    private static void removeOutdated() {
        map.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
    }

    private static class Cooldown {
        private final UUID player;
        private final String material;
        private final String name;
        private final String lore;
        private final Map<String, String> nbts;

        public Cooldown(UUID uuid, String material, String name, String lore, Map<String, String> nbts) {
            this.player = uuid;
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.nbts = nbts;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cooldown cooldwon = (Cooldown) o;
            return Objects.equals(player, cooldwon.player) &&
                    Objects.equals(material, cooldwon.material) &&
                    Objects.equals(name, cooldwon.name) &&
                    Objects.equals(lore, cooldwon.lore) &&
                    Objects.equals(nbts, cooldwon.nbts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, material, name, lore, nbts);
        }
    }
}
