package net.sakuragame.eternal.dragoncore.listener;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickedEvent;
import net.sakuragame.eternal.dragoncore.api.gui.event.CustomPacketEvent;
import net.sakuragame.eternal.dragoncore.api.slot.ClickType;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    private final DragonCore plugin;

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
                player.sendMessage(" ??c??l????????????????????????");
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

        // ????????????
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
                plugin.getLogger().info("??????????????? " + player.getName() + " ?????????????????????????????????");
                return;
            }
        }


        //??????????????????
        if (clickType == ClickType.LEFT_CLICK) {
            //????????????????????????????????????
            if (slotItem.getType() != Material.AIR && handItem.getType() != Material.AIR) {
                if (slotItem.isSimilar(handItem)) {
                    int amount = slotItem.getMaxStackSize() - slotItem.getAmount();//????????????????????????
                    int addAmount = Math.min(amount, handItem.getAmount());//?????????????????????
                    if (addAmount > 0) {
                        setItemStack(player, slotIdentity, setAmount(slotItem, slotItem.getAmount() + addAmount));
                        setCursorItem(player, setAmount(handItem, handItem.getAmount() - addAmount));
                    }
                } else {
                    setItemStack(player, slotIdentity, handItem);
                    setCursorItem(player, slotItem);
                }
            } else if (handItem.getType() != Material.AIR || slotItem.getType() != Material.AIR) {
                //??????????????????
                setItemStack(player, slotIdentity, handItem);
                setCursorItem(player, slotItem);
            }


        }
        //??????????????????
        if (clickType == ClickType.RIGHT_CLICK) {
            //??????????????????????????????????????????  ->  ??????????????????
            if (slotItem.getType() == Material.AIR && handItem.getType() != Material.AIR) {
                setItemStack(player, slotIdentity, setAmount(handItem.clone(), 1));
                setCursorItem(player, setAmount(handItem, handItem.getAmount() - 1));
            }
            //??????????????????????????????????????????  ->  ????????????????????????????????????
            if (slotItem.getType() != Material.AIR && handItem.getType() == Material.AIR) {
                if (slotItem.getAmount() == 1) {
                    //?????????????????????????????????????????????????????????
                    setItemStack(player, slotIdentity, handItem);
                    setCursorItem(player, slotItem);
                } else {
                    //?????????????????????????????????
                    int amount = slotItem.getAmount() / 2; //amount ??????????????????
                    setItemStack(player, slotIdentity, setAmount(slotItem, slotItem.getAmount() - amount));
                    setCursorItem(player, setAmount(slotItem.clone(), amount));
                }
            }
            //????????????????????????????????????????????????  ->  ????????????????????????????????????????????????????????????  ??????????????????????????????????????????
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

        if (!FileManager.getSlotSettings().containsKey(slotIdentity)) return;

        ItemStack itemStack = SlotAPI.getCacheSlotItem(e.getPlayer(), slotIdentity);
        if (itemStack == null) {
            itemStack = new ItemStack(Material.AIR);
        }
        PacketSender.putClientSlotItem(e.getPlayer(), slotIdentity, itemStack);
    }

    public void setItemStack(Player player, String slotIdentity, ItemStack itemStack) {
        if (!FileManager.getSlotSettings().containsKey(slotIdentity)) {
            PacketSender.putClientSlotItem(player, slotIdentity, itemStack);

            PlayerSlotUpdateEvent event = new PlayerSlotUpdateEvent(player, slotIdentity, itemStack);
            event.callEvent();
            return;
        }

        SlotAPI.setSlotItem(player, slotIdentity, itemStack, true);
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
