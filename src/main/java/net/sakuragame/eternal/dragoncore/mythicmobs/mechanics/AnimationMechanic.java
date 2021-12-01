package net.sakuragame.eternal.dragoncore.mythicmobs.mechanics;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

        if (!this.isRemove) {
            PacketSender.setModelEntityAnimation(bukkitTarget, animation, transitionTime);
            debug(target, "开始播放: " + animation);
            debug(target, "过渡时间: " + transitionTime);

        } else {
            PacketSender.removeModelEntityAnimation(bukkitTarget, animation, transitionTime);
            debug(target, "删除动画");
        }
        return true;
    }

    private void debug(AbstractEntity target, String message) {
        Entity entity = target.getBukkitEntity();
        entity.getWorld().getPlayers().forEach(player -> {
            if (player.isOp()) {
                player.sendMessage(message);
            }
        });
    }
}
