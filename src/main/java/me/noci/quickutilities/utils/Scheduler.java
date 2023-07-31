package me.noci.quickutilities.utils;

import me.noci.quickutilities.QuickUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    public static BukkitScheduler bukkitScheduler() {
        return Bukkit.getScheduler();
    }

    public static boolean isMainThread() {
        return Bukkit.isPrimaryThread();
    }

    public static void cancelTask(int taskId) {
        bukkitScheduler().cancelTask(taskId);
    }

    public static void cancelTask(BukkitTask task) {
        task.cancel();
    }

    public BukkitTask execute(Runnable runnable) {
        return bukkitScheduler().runTask(QuickUtils.instance(), runnable);
    }

    public BukkitTask executeAsync(Runnable runnable) {
        return bukkitScheduler().runTaskAsynchronously(QuickUtils.instance(), runnable);
    }

    public static BukkitTask delay(long time, BukkitUnit timeUnit, Runnable runnable) {
        return delay(timeUnit.toTicks(time), runnable);
    }

    public static BukkitTask delay(long ticks, Runnable runnable) {
        return bukkitScheduler().runTaskLater(QuickUtils.instance(), runnable, ticks);
    }

    public static BukkitTask delayAsync(long time, BukkitUnit timeUnit, Runnable runnable) {
        return delayAsync(timeUnit.toTicks(time), runnable);
    }

    public static BukkitTask delayAsync(long ticks, Runnable runnable) {
        return bukkitScheduler().runTaskLaterAsynchronously(QuickUtils.instance(), runnable, ticks);
    }

    public static void repeat(long time, BukkitUnit timeUnit, BukkitRunnable runnable) {
        repeat(0, timeUnit.toTicks(time), runnable);
    }

    public static void repeat(long repeatTicks, BukkitRunnable runnable) {
        repeat(0, repeatTicks, runnable);
    }

    public static void repeat(long delayTicks, long repeatTicks, BukkitRunnable runnable) {
        runnable.runTaskTimer(QuickUtils.instance(), delayTicks, repeatTicks);
    }

    public static BukkitTask repeat(long time, BukkitUnit timeUnit, Runnable runnable) {
        return repeat(0, timeUnit.toTicks(time), runnable);
    }

    public static BukkitTask repeat(long repeatTicks, Runnable runnable) {
        return repeat(0, repeatTicks, runnable);
    }

    public static BukkitTask repeat(long delayTicks, long repeatTicks, Runnable runnable) {
        return bukkitScheduler().runTaskTimer(QuickUtils.instance(), runnable, delayTicks, repeatTicks);
    }

    public static void repeatAsync(long time, BukkitUnit timeUnit, BukkitRunnable runnable) {
        repeatAsync(0, timeUnit.toTicks(time), runnable);
    }

    public static void repeatAsync(long repeatTicks, BukkitRunnable runnable) {
        repeatAsync(0, repeatTicks, runnable);
    }

    public static void repeatAsync(long delayTicks, long repeatTicks, BukkitRunnable runnable) {
        runnable.runTaskTimerAsynchronously(QuickUtils.instance(), delayTicks, repeatTicks);
    }

}
