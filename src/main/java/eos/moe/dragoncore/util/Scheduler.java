package eos.moe.dragoncore.util;

import eos.moe.dragoncore.DragonCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
