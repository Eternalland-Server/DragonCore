package eos.moe.dragoncore.commands;

import com.taylorswiftcn.justwei.commands.ICommand;
import eos.moe.dragoncore.commands.sub.HelpCommand;
import eos.moe.dragoncore.commands.sub.ManagerCommand;
import eos.moe.dragoncore.commands.sub.ReloadCommand;
import eos.moe.dragoncore.commands.sub.animation.ModelPlayCommand;
import eos.moe.dragoncore.commands.sub.animation.ModelStopCommand;
import eos.moe.dragoncore.commands.sub.gui.*;
import eos.moe.dragoncore.commands.sub.item.ColorCommand;
import eos.moe.dragoncore.commands.sub.item.LoreCommand;
import eos.moe.dragoncore.commands.sub.item.NameCommand;
import eos.moe.dragoncore.commands.sub.sound.SoundPlayCommand;
import eos.moe.dragoncore.commands.sub.sound.SoundStopCommand;
import eos.moe.dragoncore.commands.sub.texture.WorldItemCommand;
import eos.moe.dragoncore.commands.sub.texture.WorldTextureCommand;

public class MainCommand extends ICommand {

    public MainCommand() {
        this.setHelpCommand(new HelpCommand());
        this.register(new ManagerCommand());
        this.register(new ModelPlayCommand());
        this.register(new ModelStopCommand());
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
        this.register(new ReloadCommand());
    }
}
