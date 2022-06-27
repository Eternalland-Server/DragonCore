package net.sakuragame.eternal.dragoncore.skript.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.sakuragame.eternal.dragoncore.api.CoreAPI;
import net.sakuragame.eternal.dragoncore.util.ClassUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("DuplicatedCode")
public class EffectPlayAnimation extends Effect {

    private Expression<UUID> entityUUID;
    private Expression<String> animationID;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exp, int argA, @NotNull Kleenean argB, @NotNull SkriptParser.ParseResult argC) {
        entityUUID = (Expression<UUID>) exp[1];
        animationID = (Expression<String>) exp[0];
        return true;
    }

    @NotNull
    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "";
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (entityUUID == null || animationID == null) {
            return;
        }
        UUID entityUUID = this.entityUUID.getSingle(event);
        LivingEntity entity = ClassUtil.safeCast(Bukkit.getEntity(entityUUID), LivingEntity.class);
        if (entity == null) {
            return;
        }
        String animationName = this.animationID.getSingle(event);
        CoreAPI.setEntityAnimation(entity, animationName, 20);
    }
}