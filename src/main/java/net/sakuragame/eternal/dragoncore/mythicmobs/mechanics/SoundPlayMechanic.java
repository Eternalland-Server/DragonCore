package net.sakuragame.eternal.dragoncore.mythicmobs.mechanics;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
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

public class SoundPlayMechanic extends SkillMechanic implements ITargetedEntitySkill {
    private final PlaceholderString sound;
    private final boolean loop;
    private final float v;
    private final float p;
    private final float x;
    private final float y;
    private final float z;

    public SoundPlayMechanic(CustomMechanic holder, MythicLineConfig mlc) {
        super(holder.getConfigLine(), mlc);
        this.setAsyncSafe(false);

        this.sound = mlc.getPlaceholderString(new String[]{"sound"}, "");
        this.loop = mlc.getBoolean(new String[]{"loop", "l"}, false);
        this.v = mlc.getFloat(new String[]{"v", "volume"}, 1);
        this.p = mlc.getFloat(new String[]{"p", "pitch"}, 1);
        this.x = mlc.getFloat(new String[]{"x"}, 0);
        this.y = mlc.getFloat(new String[]{"y"}, 0);
        this.z = mlc.getFloat(new String[]{"z"}, 0);

    }

    @Override
    public boolean castAtEntity(final SkillMetadata data, final AbstractEntity target) {
        LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
        if (bukkitTarget instanceof Player) {
            PacketSender.sendPlaySound(((Player) bukkitTarget), sound.get(), sound.get(), v, p, loop, x, y, z);
        }
        return true;
    }
}
