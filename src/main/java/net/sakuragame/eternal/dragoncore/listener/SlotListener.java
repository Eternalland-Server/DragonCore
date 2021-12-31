package net.sakuragame.eternal.dragoncore.listener;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickedEvent;
import net.sakuragame.eternal.dragoncore.api.gui.event.CustomPacketEvent;
import net.sakuragame.eternal.dragoncore.api.slot.ClickType;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.database.IDataBase;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SlotListener implements Listener {

    private final DragonCore plugin;
    private final Set<UUID> saving = new HashSet<>();

    public SlotListener() {
        this.plugin = DragonCore.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemRequest(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();

        if (!FileManager.getSlotSettings().containsKey(ident)) return;
        ItemStack item = SlotAPI.getCacheSlotItem(player, ident);
        if (MegumiUtil.isEmpty(item)) return;

        e.setSlotItem(item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlotClickEvent(CustomPacketEvent e) {
        Player player = e.getPlayer();

        if (!e.getIdentifier().equals("DragonCore_ClickSlot")) return;
        if (e.isCancelled()) return;
        if (e.getData().size() != 2) return;
        if (saving.contains(player.getUniqueId())) return;

        String identifier = e.getData().get(0);
        String clickParam = e.getData().get(1);
        if (identifier.startsWith("container_")) return;
        if (!MegumiUtil.isInteger(clickParam)) return;

        ClickType clickType = ClickType.getType(Integer.parseInt(clickParam));
        if (clickType == null) return;

        PlayerSlotClickEvent clickEvent = new PlayerSlotClickEvent(player, identifier, clickType);
        if (clickEvent.callEvent()) return;

        ItemStack item = clickEvent.getSlotItem();

        handleSlotClick(player, identifier, clickType, item);

        PlayerSlotClickedEvent clickedEvent = new PlayerSlotClickedEvent(player, identifier, clickType);
        clickedEvent.callEvent();

        /*SlotAPI.getSlotItem(player, identifier, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                saving.remove(player.getUniqueId());
                handleSlotClick(player, identifier, clickType, itemStack);

                PlayerSlotClickedEvent clickedEvent = new PlayerSlotClickedEvent(player, identifier, clickType);
                clickedEvent.callEvent();
            }

            @Override
            public void onFail() {
                player.sendMessage(" §c§l无法读取槽位数据");
                saving.remove(player.getUniqueId());
            }
        });*/

    }

    public void handleSlotClick(Player player, String slotIdentity, ClickType clickType, ItemStack slotItem) {
        ItemStack handItem = player.getItemOnCursor();
        if (slotItem == null) {
            slotItem = new ItemStack(Material.AIR);
        }
        if (handItem == null) {
            handItem = new ItemStack(Material.AIR);
        }

        // 鼠标中键
        if (clickType == ClickType.MIDDLE_CLICK && player.isOp()) {
            if (slotItem.getType() != Material.AIR && handItem.getType() == Material.AIR) {
                player.setItemOnCursor(slotItem.clone());
                return;
            }
        }

        if (handItem.getType() != Material.AIR) {
            PlayerSlotHandleEvent event = new PlayerSlotHandleEvent(player, slotIdentity, handItem.clone());
            if (event.callEvent()) return;

            handItem = event.getHandItem();
        }

        if (!MegumiUtil.isEmpty(handItem)) {
            if (ZaphkielAPI.INSTANCE.read(handItem).isVanilla()) {
                plugin.getLogger().info("已阻止玩家 " + player.getName() + " 尝试将原版物品放入槽位");
                return;
            }
        }


        //左键点击槽位
        if (clickType == ClickType.LEFT_CLICK) {
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
        if (clickType == ClickType.RIGHT_CLICK) {
            System.out.println("right click");
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

        ItemStack itemStack = SlotAPI.getCacheSlotItem(e.getPlayer(), slotIdentity);
        if (itemStack == null) {
            itemStack = new ItemStack(Material.AIR);
        }
        PacketSender.putClientSlotItem(e.getPlayer(), slotIdentity, itemStack);
    }

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
}
