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

public class SoundMechanic extends SkillMechanic implements ITargetedEntitySkill {
    private PlaceholderString sound;
    private float v;
    private float p;
    private float x;
    private float y;
    private float z;

    public SoundMechanic(CustomMechanic holder, MythicLineConfig mlc) {
        super(holder.getConfigLine(), mlc);
        this.setAsyncSafe(false);

        this.sound = mlc.getPlaceholderString(new String[]{ "coresound"}, "");
        this.v = mlc.getFloat(new String[]{"v", "volume"}, 1);
        this.p = mlc.getFloat(new String[]{"p", "pitch"}, 1);
        this.x = mlc.getFloat(new String[]{"x"}, 0);
        this.y = mlc.getFloat(new String[]{"y"}, 0);
        this.z = mlc.getFloat(new String[]{"z"}, 0);

    }

    public boolean castAtEntity(final SkillMetadata data, final AbstractEntity target) {
        LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);
        if (bukkitTarget instanceof Player) {
            PacketSender.sendPlaySound(((Player) bukkitTarget), sound.get(), v, p, false, x, y, z);
        }
        return true;
    }
}
