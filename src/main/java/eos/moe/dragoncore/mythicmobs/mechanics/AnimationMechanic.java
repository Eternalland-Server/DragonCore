package eos.moe.dragoncore.mythicmobs.mechanics;

import eos.moe.dragoncore.network.PacketSender;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;
import org.bukkit.entity.LivingEntity;

public class AnimationMechanic extends SkillMechanic implements ITargetedEntitySkill {

    private String animation;
    private boolean isRemove;
    private int transitionTime;

    public AnimationMechanic(CustomMechanic holder, MythicLineConfig mlc) {
        super(holder.getConfigLine(), mlc);
        this.setAsyncSafe(false);
        this.animation = mlc.getString(new String[]{"n", "name"}, null);
        this.isRemove = mlc.getBoolean(new String[]{"r", "remove"}, false);
        this.transitionTime = mlc.getInteger(new String[]{"t", "time"}, 200);
    }

    public boolean castAtEntity(final SkillMetadata data, final AbstractEntity target) {
        LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
        if (this.animation == null || bukkitTarget == null)
            return false;
        //String animation = this.animation.get(data, target);
        if (!this.isRemove) {
            PacketSender.setModelEntityAnimation(bukkitTarget, animation, transitionTime);
        } else {
            PacketSender.removeModelEntityAnimation(bukkitTarget, animation, transitionTime);
        }
        return true;
    }
}
