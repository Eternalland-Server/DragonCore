package net.sakuragame.eternal.dragoncore.skript.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;


@SuppressWarnings("ConstantConditions")
public class EffectSoundPlay extends Effect {

    private Expression<Player> player;
    private Expression<String> soundName;
    private Expression<Float> volume;
    private Expression<Float> pitch;
    private Expression<Boolean> loop;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exp, int argA, @NotNull Kleenean argB, @NotNull SkriptParser.ParseResult argC) {
        soundName = (Expression<String>) exp[0];
        volume = (Expression<Float>) exp[1];
        pitch = (Expression<Float>) exp[2];
        loop = (Expression<Boolean>) exp[3];
        player = (Expression<Player>) exp[4];
        return true;
    }

    @NotNull
    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "";
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (player == null || soundName == null || volume == null || pitch == null || loop == null) {
            return;
        }
        Player[] players = this.player.getAll(event);
        String soundName = this.soundName.getSingle(event);
        float volume = this.volume.getSingle(event);
        float pitch = this.pitch.getSingle(event);
        boolean loop = Boolean.TRUE.equals(this.loop.getSingle(event));
        Arrays.stream(players).forEach(player -> PacketSender.sendPlaySound(player, soundName, soundName, volume, pitch, loop, 0, 0, 0));
    }
}
