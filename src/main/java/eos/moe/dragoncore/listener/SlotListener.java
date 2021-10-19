package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.database.IDataBase;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.ItemUtil;
import eos.moe.dragoncore.util.ScriptUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SlotListener implements Listener {

    private DragonCore plugin;

    public SlotListener(DragonCore plugin) {
        this.plugin = plugin;
    }




    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlotClickEvent(CustomPacketEvent e) {
        Player player = e.getPlayer();
        if (!e.getIdentifier().equals("DragonCore_ClickSlot"))
            return;
        if (e.isCancelled())
            return;
        if (e.getData().size() != 2)
            return;
        if (saving.contains(player.getUniqueId())) {
            return;
        }
        String slotIdentity = e.getData().get(0);
        int mouseButton = 0;
        try {
            mouseButton = Integer.parseInt(e.getData().get(1));
        } catch (NumberFormatException ignored) {

        }
        if (!Config.getSlotConfig().contains(slotIdentity + ".limit")) {
            return;
        }

        saving.add(player.getUniqueId());

        int finalButton = mouseButton;
        SlotAPI.getSlotItem(player, slotIdentity, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                saving.remove(player.getUniqueId());
                handleSlotClick(player, slotIdentity, finalButton, itemStack);
            }

            @Override
            public void onFail() {
                player.sendMessage("§c[错误] §6无法读取槽位数据");
                saving.remove(player.getUniqueId());
            }
        });

    }

    public void handleSlotClick(Player player, String slotIdentity, int mouseButton, ItemStack slotItem) {
        ItemStack handItem = player.getItemOnCursor();
        if (slotItem == null) {
            slotItem = new ItemStack(Material.AIR);
        }
        if (handItem == null) {
            handItem = new ItemStack(Material.AIR);
        }

        // 鼠标中键
        if (mouseButton == 2 && player.hasPermission("core.slot.clone")) {
            if (slotItem.getType() != Material.AIR && handItem.getType() == Material.AIR) {
                player.setItemOnCursor(slotItem.clone());
                return;
            }
        }

        if (handItem.getType() != Material.AIR && !checkItemStackCanPutInSlot(player, slotIdentity, handItem)) {
            return;
        }
        // 判断Limit


        //左键点击槽位
        if (mouseButton == 0) {
            //相同物品则把手上物品放入
            if (slotItem.getType() != Material.AIR && handItem.getType() != Material.AIR) {
                if (slotItem.isSimilar(handItem)) {
                    int amount = slotItem.getMaxStackSize() - slotItem.getAmount();//最大可放入的数量
                    int addAmount = Math.min(amount, handItem.getAmount());//将要放入的数量
                    if (addAmount > 0) {
                        setItemStack(player, slotIdentity, setAmount(slotItem, slotItem.getAmount() + addAmount));
                        setCursorItem(player, setAmount(handItem, handItem.getAmount() - addAmount));
                    }
                } else {
                    setItemStack(player, slotIdentity, handItem);
                    setCursorItem(player, slotItem);
                }
            } else if (handItem.getType() != Material.AIR || slotItem.getType() != Material.AIR) {
                //否则交换物品
                setItemStack(player, slotIdentity, handItem);
                setCursorItem(player, slotItem);
            }


        }
        //右键点击槽位
        if (mouseButton == 1) {
            //如果格子是空的，且手上有物品  ->  放置一个进去
            if (slotItem.getType() == Material.AIR && handItem.getType() != Material.AIR) {
                setItemStack(player, slotIdentity, setAmount(handItem.clone(), 1));
                setCursorItem(player, setAmount(handItem, handItem.getAmount() - 1));
            }
            //如果格子不是空的，手上是空的  ->  取出格子的一半物品到手上
            if (slotItem.getType() != Material.AIR && handItem.getType() == Material.AIR) {
                if (slotItem.getAmount() == 1) {
                    //如果格子只有一个物品，直接交换物品即可
                    setItemStack(player, slotIdentity, handItem);
                    setCursorItem(player, slotItem);
                } else {
                    //否则取出一半物品到手上
                    int amount = slotItem.getAmount() / 2; //amount 为取出的数量
                    setItemStack(player, slotIdentity, setAmount(slotItem, slotItem.getAmount() - amount));
                    setCursorItem(player, setAmount(slotItem.clone(), amount));
                }
            }
            //如果格子不是空的，手上也不是空的  ->  判断格子与手上物品是否相同，且数量还未满  若满足则放置一个物品到格子内
            if (slotItem.getType() != Material.AIR && handItem.getType() != Material.AIR) {
                if (slotItem.getAmount() < slotItem.getMaxStackSize()) {
                    if (slotItem.isSimilar(handItem)) {
                        setItemStack(player, slotIdentity, setAmount(slotItem, slotItem.getAmount() + 1));
                        setCursorItem(player, setAmount(handItem, handItem.getAmount() - 1));
                    } else {
                        setItemStack(player, slotIdentity, handItem);
                        setCursorItem(player, slotItem);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlotRequireEvent(CustomPacketEvent e) {

        if (!e.getIdentifier().equals("DragonCore_RetrieveSlot"))
            return;
        if (e.isCancelled())
            return;
        if (e.getData().size() != 1)
            return;
        String slotIdentity = e.getData().get(0);
        if (!Config.getSlotConfig().contains(slotIdentity + ".limit")) {
            return;
        }
        ItemStack itemStack = SlotAPI.getCacheSlotItem(e.getPlayer(), slotIdentity);
        if (itemStack == null) {
            itemStack = new ItemStack(Material.AIR);
        }
        PacketSender.putClientSlotItem(e.getPlayer(), slotIdentity, itemStack);
    }

    private final Set<UUID> saving = new HashSet<>();

    public void setItemStack(Player player, String slotIdentity, ItemStack itemStack) {
        saving.add(player.getUniqueId());

        SlotAPI.setSlotItem(player, slotIdentity, itemStack, true, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack p0) {
                saving.remove(player.getUniqueId());
            }

            @Override
            public void onFail() {
                player.sendMessage("§a物品储存失败，已将物品返回至背包中");
                player.getInventory().addItem(itemStack);
                saving.remove(player.getUniqueId());
            }
        });
    }

    public void setCursorItem(Player player, ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) {
            player.setItemOnCursor(new ItemStack(Material.AIR));
        } else {
            player.setItemOnCursor(itemStack);
        }
    }

    public static ItemStack setAmount(ItemStack itemStack, int amount) {
        itemStack.setAmount(amount);
        return itemStack;

    }

    public static boolean checkItemStackCanPutInSlot(Player player, String slotIdentity, ItemStack itemStack) {
        YamlConfiguration yaml = Config.getSlotConfig();
        if (!yaml.contains(slotIdentity + ".limit")) {
            return false;
        }
        List<String> list = yaml.getStringList(slotIdentity + ".limit");
        for (String s : list) {
            if (s.split("\\|").length > 1) {
                try {
                    String[] split = s.split("\\|", 2);
                    String script = yaml.getString("Script." + split[0]);
                    if (script == null) {
                        player.sendMessage("§c[错误] 背包配置Limit存在未知判断类型" + split[0] + "，你无法将物品放入此槽内");
                        return false;
                    }
                    boolean result = ScriptUtil.execute(script, player, itemStack, slotIdentity, split[1]);
                    if (!result) {
                        return false;
                    }
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§c[错误] 处理限制行[" + s + "]出现了错误");
                    player.sendMessage("§c[错误] 背包配置Limit处理出现异常报错,你无法将物品放入此槽内");
                    e.printStackTrace();
                    return false;
                }
            } else {
                List<String> lore = ItemUtil.getLore(itemStack);
                boolean result = lore.contains(s.replace("&", "§"));
                if (!result) {
                    player.sendMessage("§c§l该槽位要求物品存在词条: " + Arrays.toString(list.toArray()));
                    return false;
                }
            }
        }
        return true;
    }
}
