package eos.moe.dragoncore.mythicmobs.mechanics;

import eos.moe.dragoncore.api.ModelAPI;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;
import org.bukkit.entity.LivingEntity;

public class ModelMechanic extends SkillMechanic implements ITargetedEntitySkill {

    private final String model;

    public ModelMechanic(CustomMechanic holder, MythicLineConfig mlc) {
        super(holder.getConfigLine(), mlc);
        this.setAsyncSafe(false);
        this.model = mlc.getString(new String[] {"model", "m"}, "");
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity target) {
        LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
        if (this.model.isEmpty() || bukkitTarget == null)
            return false;

        ModelAPI.setEntityModel(target.getUniqueId(), model);
        return true;
    }
}
