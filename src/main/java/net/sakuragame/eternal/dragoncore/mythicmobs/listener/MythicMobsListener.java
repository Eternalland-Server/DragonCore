package net.sakuragame.eternal.dragoncore.mythicmobs.listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.minecraft.server.v1_12_R1.*;
import net.sakuragame.eternal.dragoncore.mythicmobs.mechanics.AnimationMechanic;
import net.sakuragame.eternal.dragoncore.mythicmobs.mechanics.ModelMechanic;
import net.sakuragame.eternal.dragoncore.mythicmobs.mechanics.SoundPlayMechanic;
import net.sakuragame.eternal.dragoncore.mythicmobs.mechanics.SoundStopMechanic;
import net.sakuragame.eternal.dragoncore.util.Scheduler;
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
    public void onMythicSpawn(MythicMobSpawnEvent e) {
        Entity entity = e.getEntity();
        entity.setCustomNameVisible(false);
        double attackDistance = e.getMobType().getConfig().getDouble("AttackDistance");
        if (attackDistance == 0)
            return;

        Scheduler.runLater(() -> {
            net.minecraft.server.v1_12_R1.Entity handle = ((CraftEntity) entity).getHandle();
            if (handle instanceof EntityCreature) {
                EntityCreature entityLiving = (EntityCreature) handle;
                PathfinderGoalSelector goalSelector = entityLiving.goalSelector;
                try {
                    Set o = (Set) field1.get(goalSelector);
                    o.clear();
                    goalSelector.a(0, new DragonCore_PathfinderGoalMeleeAttack(entityLiving, attackDistance));
                    for (Object o1 : o) {
                        Object o2 = field2.get(o1);
                        if (o2 instanceof PathfinderGoalMeleeAttack) {
                            field2.set(o1, new DragonCore_PathfinderGoalMeleeAttack(entityLiving, attackDistance));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 2);
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
        }
    }

    public static class DragonCore_PathfinderGoalMeleeAttack extends PathfinderGoalMeleeAttack {
        World world;
        protected EntityCreature attacker;
        protected int attackTick;
        double speedTowardsTarget;
        boolean longMemory;
        PathEntity path;
        private int delayCounter;
        private double targetX;
        private double targetY;
        private double targetZ;
        protected final int attackInterval = 20;
        private double distance;

        public DragonCore_PathfinderGoalMeleeAttack(EntityCreature entityCreature, double distance) {
            super(entityCreature, 1.0, true);

            this.attacker = entityCreature;
            this.world = entityCreature.world;
            this.speedTowardsTarget = 1;
            this.longMemory = true;
            this.distance = distance;
            this.a(3);
        }

        public boolean a() {
            EntityLiving entitylivingbase = this.attacker.getGoalTarget();
            if (entitylivingbase == null) {
                return false;
            } else if (!entitylivingbase.isAlive()) {
                return false;
            } else {
                this.path = this.attacker.getNavigation().a(entitylivingbase);
                if (this.path != null) {
                    return true;
                } else {
                    return this.a(entitylivingbase) >= this.attacker.d(entitylivingbase.locX, entitylivingbase.getBoundingBox().b, entitylivingbase.locZ);
                }
            }
        }

        public boolean b() {
            EntityLiving entitylivingbase = this.attacker.getGoalTarget();
            if (entitylivingbase == null) {
                return false;
            } else if (!entitylivingbase.isAlive()) {
                return false;
            } else if (!this.longMemory) {
                return !this.attacker.getNavigation().o();
            } else if (!this.attacker.f(new BlockPosition(entitylivingbase))) {
                return false;
            } else {
                return !(entitylivingbase instanceof EntityHuman) || !((EntityHuman) entitylivingbase).isSpectator() && !((EntityHuman) entitylivingbase).z();
            }
        }

        public void c() {
            this.attacker.getNavigation().a(this.path, this.speedTowardsTarget);
            this.delayCounter = 0;
        }

        public void d() {
            EntityLiving entitylivingbase = this.attacker.getGoalTarget();
            if (entitylivingbase instanceof EntityHuman && (((EntityHuman) entitylivingbase).isSpectator() || ((EntityHuman) entitylivingbase).z())) {
                this.attacker.setGoalTarget(null);
            }

            this.attacker.getNavigation().p();
        }

        public void e() {
            EntityLiving entitylivingbase = this.attacker.getGoalTarget();
            if (entitylivingbase == null) return;

            this.attacker.getControllerLook().a(entitylivingbase, 30.0F, 30.0F);
            double var2 = this.attacker.d(entitylivingbase.locX, entitylivingbase.getBoundingBox().b, entitylivingbase.locZ);
            --this.delayCounter;
            if ((this.longMemory || this.attacker.getEntitySenses().a(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.d(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = entitylivingbase.locX;
                this.targetY = entitylivingbase.getBoundingBox().b;
                this.targetZ = entitylivingbase.locZ;
                this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                if (var2 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (var2 > 256.0D) {
                    this.delayCounter += 5;
                }

                if (!this.attacker.getNavigation().a(entitylivingbase, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }
            this.attackTick = Math.max(this.attackTick - 1, 0);
            this.a(entitylivingbase, var2);
        }

        protected void a(EntityLiving var1, double var2) {
            double var4 = this.a(var1);
            if (var2 <= var4 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.attacker.a(EnumHand.MAIN_HAND);
                this.attacker.B(var1);
            }
        }

        protected double a(EntityLiving var1) {
            return distance;
        }
    }
}