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

@SuppressWarnings("unchecked")
public class EffectSoundStop extends Effect {

    private Expression<Player> player;
    private Expression<String> soundName;

    public boolean init(Expression<?>[] exp, int argA, @NotNull Kleenean argB, @NotNull SkriptParser.ParseResult argC) {
        player = (Expression<Player>) exp[0];
        soundName = (Expression<String>) exp[1];
        return true;
    }

    @NotNull
    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "";
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (player == null || soundName == null) {
            return;
        }
        Player[] players = this.player.getAll(event);
        String soundName = this.soundName.getSingle(event);
        Arrays.stream(players).forEach(player -> PacketSender.sendStopSound(player, soundName));
    }
}
