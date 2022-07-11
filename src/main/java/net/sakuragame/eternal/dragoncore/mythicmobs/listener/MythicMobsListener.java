package net.sakuragame.eternal.dragoncore.mythicmobs.listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnedEvent;
import net.minecraft.server.v1_12_R1.*;
import net.sakuragame.eternal.dragoncore.mythicmobs.goals.MyPathfinderGoalMeleeAttack;
import net.sakuragame.eternal.dragoncore.mythicmobs.mechanics.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class MythicMobsListener implements Listener {
    private Field field1;
    private Field field2;


    public MythicMobsListener() {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            field1 = PathfinderGoalSelector.class.getDeclaredField("b");
            field1.setAccessible(true);
            Class<?> aClass = Class.forName("net.minecraft.server.v1_12_R1.PathfinderGoalSelector$PathfinderGoalSelectorItem");

            field2 = aClass.getDeclaredField("a");
            field2.setAccessible(true);
            // 取消final
            modifiersField.setInt(field2, field2.getModifiers() & ~Modifier.FINAL);
        } catch (Exception e) {
            System.out.println("无法启用MythicMobs攻击距离修改");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMythicSpawn(MythicMobSpawnedEvent e) {
        Entity entity = e.getEntity();
        double attackDistance = e.getMobType().getConfig().getDouble("AttackDistance");
        if (attackDistance == 0)
            return;

        net.minecraft.server.v1_12_R1.Entity handle = ((CraftEntity) entity).getHandle();
        if (handle instanceof EntityCreature) {
            EntityCreature entityLiving = (EntityCreature) handle;
            PathfinderGoalSelector goalSelector = entityLiving.goalSelector;
            try {
                Set o = (Set) field1.get(goalSelector);
                o.clear();
                goalSelector.a(0, new MyPathfinderGoalMeleeAttack(entityLiving, 1, true, attackDistance));
                for (Object o1 : o) {
                    Object o2 = field2.get(o1);
                    if (o2 instanceof PathfinderGoalMeleeAttack) {
                        field2.set(o1, new MyPathfinderGoalMeleeAttack(entityLiving, 1, true, attackDistance));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent e) {
        if (e.getMechanicName().equalsIgnoreCase("Animation")) {
            e.register(new AnimationMechanic(e.getContainer(), e.getConfig()));
            return;
        }

        if (e.getMechanicName().equalsIgnoreCase("SoundPlay")) {
            e.register(new SoundPlayMechanic(e.getContainer(), e.getConfig()));
            return;
        }

        if (e.getMechanicName().equalsIgnoreCase("SoundStop")) {
            e.register(new SoundStopMechanic(e.getContainer(), e.getConfig()));
            return;
        }

        if (e.getMechanicName().equalsIgnoreCase("Model")) {
            e.register(new ModelMechanic(e.getContainer(), e.getConfig()));
            return;
        }

        if (e.getMechanicName().equalsIgnoreCase("ParticleApply")) {
            e.register(new ParticleApplyMechanic(e.getContainer(), e.getConfig()));
            return;
        }

        if (e.getMechanicName().equalsIgnoreCase("ParticleRemove")) {
            e.register(new ParticleRemoveMechanic(e.getContainer(), e.getConfig()));
        }
    }
}