package net.sakuragame.eternal.dragoncore.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.HelpCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.ManagerCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.NbtEditCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.ReloadCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.gui.*;
import net.sakuragame.eternal.dragoncore.commands.sub.model.ModelPlayCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.model.ModelRangeCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.model.ModelSetCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.model.ModelStopCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.item.ColorCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.item.LoreCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.item.NameCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.particle.ParticleApplyCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.particle.ParticleRemoveCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.sound.SoundPlayCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.sound.SoundStopCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.texture.WorldItemCommand;
import net.sakuragame.eternal.dragoncore.commands.sub.texture.WorldTextureCommand;

public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        this.register(new ManagerCommand());
        this.register(new ModelPlayCommand());
        this.register(new ModelStopCommand());
        this.register(new ModelRangeCommand());
        this.register(new ModelSetCommand());
        this.register(new FunctionCommand());
        this.register(new StatementCommand());
        this.register(new GuiListCommand());
        this.register(new OpenGuiCommand());
        this.register(new OpenHudCommand());
        this.register(new ColorCommand());
        this.register(new NameCommand());
        this.register(new LoreCommand());
        this.register(new SoundPlayCommand());
        this.register(new SoundStopCommand());
        this.register(new WorldItemCommand());
        this.register(new WorldTextureCommand());
        this.register(new NbtEditCommand());
        this.register(new ParticleApplyCommand());
        this.register(new ParticleRemoveCommand());
        this.register(new ReloadCommand());
    }
}
