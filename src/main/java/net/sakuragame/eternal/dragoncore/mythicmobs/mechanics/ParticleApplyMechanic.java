package net.sakuragame.eternal.dragoncore.mythicmobs.mechanics;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ParticleApplyMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

    private final String key;
    private final String uuid;
    private final String rotation;
    private final boolean pos;
    private final int durations;

    public ParticleApplyMechanic(CustomMechanic mechanic, MythicLineConfig mlc) {
        super(mechanic.getConfigLine(), mlc);
        this.setAsyncSafe(true);
        this.key = mlc.getString(new String[]{"key", "k"}, null);
        this.uuid = UUID.nameUUIDFromBytes(("Bedrock:Particle:" + mlc.getString(new String[]{"id"}, "")).getBytes(StandardCharsets.UTF_8)).toString();
        this.rotation = mlc.getString(new String[]{"rotation", "r"}, "0,0,0");
        this.pos = mlc.getBoolean(new String[]{"pos", "p"}, false);
        this.durations = mlc.getInteger(new String[]{"durations", "d"}, 100);
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity target = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (this.key == null || target == null) return false;

        for (AbstractPlayer player : skillMetadata.getCaster().getEntity().getWorld().getPlayers()) {
            if (!player.isPlayer()) continue;
            PacketSender.addParticle(
                    (Player) player.getBukkitEntity(),
                    this.key,
                    this.uuid,
                    this.pos ?
                            target.getLocation().getX() + "," + target.getLocation().getY() + "," + target.getLocation().getZ() :
                            target.getUniqueId().toString(),
                    this.rotation,
                    this.durations
            );
        }
        return true;
    }

    @Override
    public boolean castAtLocation(SkillMetadata skillMetadata, AbstractLocation loc) {
        if (this.key == null) return false;
        for (AbstractPlayer player : loc.getWorld().getPlayers()) {
            if (!player.isPlayer()) continue;
            String pos = loc.getX() + "," + loc.getY() + "," + loc.getZ();
            PacketSender.addParticle((Player) player.getBukkitEntity(), this.key, this.uuid, pos, this.rotation, this.durations);
        }
        return true;
    }
}
