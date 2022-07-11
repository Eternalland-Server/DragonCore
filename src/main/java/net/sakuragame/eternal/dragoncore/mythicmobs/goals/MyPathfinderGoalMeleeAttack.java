package net.sakuragame.eternal.dragoncore.mythicmobs.goals;

import net.minecraft.server.v1_12_R1.*;

public class MyPathfinderGoalMeleeAttack extends PathfinderGoalMeleeAttack {

    private final double distance;
    private final boolean longMemory;

    public MyPathfinderGoalMeleeAttack(EntityCreature entityCreature, double v, boolean b, double distance) {
        super(entityCreature, v, b);
        this.distance = distance;
        this.longMemory = b;
    }

    @Override
    public boolean b() {
        EntityLiving target = this.b.getGoalTarget();
        if (target == null) return false;
        if (!target.isAlive()) return false;
        if (this.b.d(target.locX, target.getBoundingBox().b, target.locZ) <= distance) {
            e();
            return false;
        }
        if (!this.longMemory) return !this.b.getNavigation().o();
        if (!this.b.f(new BlockPosition(target))) return false;

        return !(target instanceof EntityHuman) || !((EntityHuman) target).isSpectator() && !((EntityHuman) target).z();
    }

    @Override
    protected double a(EntityLiving entityLiving) {
        return this.distance;
    }
}
