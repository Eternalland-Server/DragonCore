package eos.moe.dragoncore.network;


import eos.moe.dragoncore.api.KeyPressEvent;
import eos.moe.dragoncore.api.event.ChatTextboxEvent;
import eos.moe.dragoncore.api.event.EntityJoinWorldEvent;
import eos.moe.dragoncore.api.event.SyncPlaceholderEvent;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.util.NBTUtils;
import eos.moe.dragoncore.util.Scheduler;
import eos.moe.dragoncore.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PluginMessageReceiver_bak implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        //System.out.println("收到客户端发包");
        try {
            ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            byte b = buf.readByte();
            if (b == 64) {
                PacketBuffer pb = new PacketBuffer(buf);
                int packetID = pb.readInt();
                //System.out.println("  - " + packetID);
                switch (packetID) {
                    case 3: {
                        //SyncPlaceholders
                        int size = pb.readInt();
                        Map<String, String> map = new HashMap<>();
                        for (int i = 0; i < size; i++) {
                            map.put(pb.readString(), pb.readString());
                        }
                        Scheduler.runAsync(() -> {
                            if (!player.isOnline()) return;
                            SyncPlaceholderEvent syncPlaceholderEvent = new SyncPlaceholderEvent(player, new HashMap<>(map));
                            Bukkit.getPluginManager().callEvent(syncPlaceholderEvent);

                            Map<String, String> placeholders = syncPlaceholderEvent.getPlaceholders();
                            placeholders.entrySet().removeIf(entry -> {
                                if (entry.getValue() == null)
                                    return true;
                                if (entry.getValue().equals(map.get(entry.getKey())))
                                    return true;
                                return false;
                            });
                            if (placeholders.size() > 0)
                                PacketSender.sendSyncPlaceholder(player, placeholders);
                        });

                        break;
                    }
                    case 4: {
                        UUID uuid = pb.readUniqueId();
                        Scheduler.runAsync(() -> {
                            Bukkit.getPluginManager().callEvent(
                                    new EntityJoinWorldEvent(player, uuid)
                            );
                        });
                        break;
                    }
                    case 5: {
                        String key = pb.readString();
                        List<String> keys = pb.readStringList();
                        Scheduler.runAsync(() -> {
                            Bukkit.getPluginManager().callEvent(
                                    new KeyPressEvent(player, key, keys)
                            );
                        });
                        break;
                    }
                    case 6: {
                        String lastText = pb.readString();
                        String currentText = pb.readString();
                        Scheduler.runAsync(() -> {
                            Bukkit.getPluginManager().callEvent(
                                    new ChatTextboxEvent(player, lastText, currentText)
                            );
                        });
                        break;
                    }
                    case 7: {

                        int slot = pb.readInt();
                        String lore = pb.readString();
                        if (player.isOp()) {
                            Scheduler.runAsync(() -> {
                                ItemStack itemStack = player.getInventory().getItem(slot);
                                NBTUtils.setNBT(itemStack, "model", lore);
                                player.sendMessage("已为物品添加NBT: model: \"" + lore + "\"");
                            });
                        }

                        break;
                    }
                    case 8: {
                        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(pb);
                        PacketPlayInUseEntity packetPlayInUseEntity = new PacketPlayInUseEntity();
                        packetPlayInUseEntity.a(packetDataSerializer);

                        //Scheduler.run(() -> handlePlayerUseEntityPacket(player, packetPlayInUseEntity));
                        break;
                    }
                    case 9: {
                        int id = pb.readInt();
                        int hand = pb.readInt();
                        //System.out.println("收到包" + hand);
                        if (hand == -1)
                            // 手攻击
                            Scheduler.run(() -> attack(player, id));
                        else {
                            // 手交互
                            Vec3D vec3D = new Vec3D(pb.readFloat(), pb.readFloat(), pb.readFloat());
                            if (hand == 0)
                                Scheduler.run(() -> interactAt(player, id, EnumHand.MAIN_HAND, vec3D));
                            else if (hand == 1)
                                Scheduler.run(() -> interactAt(player, id, EnumHand.OFF_HAND, vec3D));
                        }
                        break;
                    }
                    case 10: {
                        int arrow = pb.readInt();
                        int target = pb.readInt();
                        Scheduler.run(() -> arrow(player, arrow, target));
                        break;
                    }
                    case 100: {
                        String identifier = pb.readString();
                        List<String> data = new ArrayList<>();
                        int size = pb.readInt();
                        for (int i = 0; i < size; i++) {
                            data.add(pb.readString());
                        }
                        Scheduler.run(() -> Bukkit.getPluginManager().callEvent(new CustomPacketEvent(player, identifier, data)));
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void attack(Player p, int entityId) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity entity = worldserver.getEntity(entityId);
        if (entity != null) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityExperienceOrb) && !(entity instanceof EntityArrow) && (entity != player || player.isSpectator())) {

                player.attack(entity);
            }
        }
    }

    public void interactAt(Player p, int entityId, EnumHand hand, Vec3D vector) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity entity = worldserver.getEntity(entityId);
        if (entity != null) {

            PlayerInteractEntityEvent event = new PlayerInteractAtEntityEvent(p, entity.getBukkitEntity(), new Vector(vector.x, vector.y, vector.z),
                    hand == EnumHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);

            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }


    public void arrow(Player p, int arrowEntityId, int targetId) {
        EntityPlayer player = ((CraftPlayer) p).getHandle();
        WorldServer worldserver = player.server.getWorldServer(player.dimension);
        Entity arrow = worldserver.getEntity(arrowEntityId);
        Entity target = worldserver.getEntity(targetId);

        if (arrow == null || target == null || !(target.getBukkitEntity() instanceof LivingEntity)) {
            return;
        }
        CraftEntity bukkitEntity = arrow.getBukkitEntity();
        if (!(bukkitEntity instanceof Projectile)) {
            return;
        }
        if (!p.equals(((Projectile) bukkitEntity).getShooter())) {
            return;
        }
        Method a1 = Utils.getDeclaredMethod(arrow.getClass(), "a", MovingObjectPosition.class);
        if (a1 != null) {
            a1.setAccessible(true);
            try {
                a1.invoke(arrow, new MovingObjectPosition(target));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }



}
