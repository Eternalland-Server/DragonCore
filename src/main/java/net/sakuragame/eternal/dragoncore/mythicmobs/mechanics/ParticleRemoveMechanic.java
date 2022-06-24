package net.sakuragame.eternal.dragoncore.mythicmobs.mechanics;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ParticleRemoveMechanic extends SkillMechanic implements ITargetedEntitySkill {

    private final String uuid;

    public ParticleRemoveMechanic(CustomMechanic mechanic, MythicLineConfig mlc) {
        super(mechanic.getConfigLine(), mlc);
        this.setAsyncSafe(true);
        this.uuid = UUID.nameUUIDFromBytes(("Bedrock:Particle:" + mlc.getString(new String[]{"id"}, "")).getBytes(StandardCharsets.UTF_8)).toString();
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        for (AbstractPlayer player : abstractEntity.getWorld().getPlayers()) {
            if (!player.isPlayer()) continue;
            PacketSender.removeParticle((Player) player.getBukkitEntity(), this.uuid);
        }
        return true;
    }
}
