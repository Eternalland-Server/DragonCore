package net.sakuragame.eternal.dragoncore.util;

import net.sakuragame.eternal.dragoncore.DragonCore;
import org.bukkit.Bukkit;

public class Scheduler {
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(DragonCore.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DragonCore.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, int tick) {
        Bukkit.getScheduler().runTaskLater(DragonCore.getInstance(), runnable, tick);
    }

    public static void runLaterAsync(Runnable runnable, int tick) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(DragonCore.getInstance(), runnable, tick);
    }
}
