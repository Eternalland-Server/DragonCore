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
import org.bukkit.entity.Player;

public class StopSoundMechanic extends SkillMechanic implements ITargetedEntitySkill {
    private final PlaceholderString sound;

    public StopSoundMechanic(CustomMechanic holder, MythicLineConfig mlc) {
        super(holder.getConfigLine(), mlc);
        this.setAsyncSafe(false);

        this.sound = mlc.getPlaceholderString(new String[]{"sound"}, "");
    }

    @Override
    public boolean castAtEntity(final SkillMetadata data, final AbstractEntity target) {
        LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
        if (bukkitTarget instanceof Player) {
            PacketSender.sendStopSound(((Player) bukkitTarget), sound.get());
        }
        return true;
    }
}
