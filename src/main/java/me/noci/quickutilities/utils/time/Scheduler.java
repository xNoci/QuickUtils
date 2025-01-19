package me.noci.quickutilities.utils.time;

import me.noci.quickutilities.QuickUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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

    public static <T> T execute(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (isMainThread()) {
            return task.get();
        }

        execute((Task) () -> future.complete(task.get()));
        return future.join();
    }

    public static void execute(Task task) {
        if (isMainThread()) {
            task.run();
            return;
        }

        bukkitScheduler().runTask(QuickUtils.instance(), task::run);
    }

    public static BukkitTask executeAsync(Task task) {
        return bukkitScheduler().runTaskAsynchronously(QuickUtils.instance(), task::run);
    }

    public static BukkitTask delay(long time, BukkitUnit timeUnit, Task task) {
        return delay(timeUnit.toTicks(time), task);
    }

    public static BukkitTask delay(long ticks, Task task) {
        return bukkitScheduler().runTaskLater(QuickUtils.instance(), task::run, ticks);
    }

    public static BukkitTask delayAsync(long time, BukkitUnit timeUnit, Task task) {
        return delayAsync(timeUnit.toTicks(time), task);
    }

    public static BukkitTask delayAsync(long ticks, Task task) {
        return bukkitScheduler().runTaskLaterAsynchronously(QuickUtils.instance(), task::run, ticks);
    }

    public static void repeat(long time, BukkitUnit timeUnit, CancelableTask task) {
        repeat(0, timeUnit.toTicks(time), task);
    }

    public static void repeat(long repeatTicks, CancelableTask task) {
        repeat(0, repeatTicks, task);
    }

    public static void repeat(long delayTicks, long repeatTicks, CancelableTask task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run(this);
            }
        }.runTaskTimer(QuickUtils.instance(), delayTicks, repeatTicks);
    }

    public static BukkitTask repeat(long time, BukkitUnit timeUnit, Task task) {
        return repeat(0, timeUnit.toTicks(time), task);
    }

    public static BukkitTask repeat(long repeatTicks, Task task) {
        return repeat(0, repeatTicks, task);
    }

    public static BukkitTask repeat(long delayTicks, long repeatTicks, Task task) {
        return bukkitScheduler().runTaskTimer(QuickUtils.instance(), task::run, delayTicks, repeatTicks);
    }

    public static void repeatAsync(long time, BukkitUnit timeUnit, CancelableTask task) {
        repeatAsync(0, timeUnit.toTicks(time), task);
    }

    public static void repeatAsync(long repeatTicks, CancelableTask task) {
        repeatAsync(0, repeatTicks, task);
    }

    public static void repeatAsync(long delayTicks, long repeatTicks, CancelableTask task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run(this);
            }
        }.runTaskTimerAsynchronously(QuickUtils.instance(), delayTicks, repeatTicks);
    }

    @FunctionalInterface
    public interface Task {
        void run();
    }

    @FunctionalInterface
    public interface CancelableTask {
        void run(BukkitRunnable runnable);
    }

}
