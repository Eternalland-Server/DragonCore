package eos.moe.dragoncore.listener;

import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Deprecated
public class EntityAnimationHandler implements Listener {
    private Map<UUID, EntityData> map = new HashMap<>();

    public EntityAnimationHandler() {
        new BukkitRunnable() {
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entitiesByClass : world.getEntitiesByClasses(LivingEntity.class)) {
                        if (entitiesByClass.getCustomName() == null) continue;
                        if (!map.containsKey(entitiesByClass.getUniqueId())) {
                            map.put(entitiesByClass.getUniqueId(), new EntityData());
                        }
                        map.get(entitiesByClass.getUniqueId()).eventTrigger(entitiesByClass);
                    }
                }
            }
        }.runTaskTimer(DragonCore.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void onEntityRemove(EntityDeathEvent e) {
        map.remove(e.getEntity().getUniqueId());
    }


    public static class EntityData {
        private boolean stopEventFired;
        private boolean moveEventFired;

        private void eventTrigger(Entity entity) {
            if (entity.getVelocity().getX() == 0.0 && entity.getVelocity().getZ() == 0.0) {
                if (!this.stopEventFired) {
                    this.moveEventFired = false;
                    this.stopEventFired = true;
                    PacketSender.removeModelEntityAnimation((LivingEntity) entity, "walk", 200);
                    PacketSender.setModelEntityAnimation((LivingEntity) entity, "idle", 0);
                }
            } else if (!this.moveEventFired) {
                this.stopEventFired = false;
                this.moveEventFired = true;
                PacketSender.removeModelEntityAnimation((LivingEntity) entity, "idle", 0);
                PacketSender.setModelEntityAnimation((LivingEntity) entity, "walk", 0);
            }
        }
    }
}
