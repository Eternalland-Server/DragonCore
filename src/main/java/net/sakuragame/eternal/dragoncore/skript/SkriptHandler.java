package net.sakuragame.eternal.dragoncore.skript;

import ch.njol.skript.Skript;
import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.skript.effects.EffectParticleApply;
import net.sakuragame.eternal.dragoncore.skript.effects.EffectPlayAnimation;
import net.sakuragame.eternal.dragoncore.skript.effects.EffectSetModel;
import org.bukkit.Bukkit;

@SuppressWarnings("SpellCheckingInspection")
public class SkriptHandler {

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("Skript") == null) {
            return;
        }
        DragonCore.getInstance().getLog().info("与 Skript 进行挂钩.");
    }

    private static void registerEffects() {
        Skript.registerEffect(EffectParticleApply.class, "play snowstorm particle %string% to %player% for %number% seconds");
        Skript.registerEffect(EffectParticleApply.class, "stop snowstorm particle %string% to %player%");
        Skript.registerEffect(EffectPlayAnimation.class, "play animation %string% of uuid %uuid%");
        Skript.registerEffect(EffectSetModel.class, "set model of uuid %uuid% to %string%");
        Skript.registerEffect(EffectSetModel.class, "play ogg sound %string% at volume %number% with pitch %number% and loop %boolean% to %player%");
        Skript.registerEffect(EffectSetModel.class, "stop ogg sound %string% to %player%");
    }
}
