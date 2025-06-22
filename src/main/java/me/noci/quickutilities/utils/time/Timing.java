package me.noci.quickutilities.utils.time;

import org.jetbrains.annotations.NotNull;

public class Timing {

    private static boolean LOG = true;

    @NotNull private final String profile;
    @NotNull private final BukkitUnit measuringUnit;
    private final long startTime;

    private Timing(@NotNull String profile, @NotNull BukkitUnit measuringUnit) {
        this.profile = profile;
        this.measuringUnit = measuringUnit;
        this.startTime = System.nanoTime();

        if (LOG) {
            System.out.println("Starting measuring: " + this.profile);
        }
    }

    public static boolean isLogging() {
        return LOG;
    }

    public static void enableLogging() {
        LOG = true;
    }

    public static void disableLogging() {
        LOG = false;
    }

    public static <T> T track(@NotNull String profile, ThrowableSupplier<T> task) {
        return track(profile, BukkitUnit.MILLISECONDS, task);
    }

    public static <T> T track(@NotNull String profile, @NotNull BukkitUnit measuringUnit, ThrowableSupplier<T> task) {
        try {
            return trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }

    }

    public static void track(@NotNull String profile, ThrowableRunnable task) {
        track(profile, BukkitUnit.MILLISECONDS, task);
    }

    public static void track(@NotNull String profile, @NotNull BukkitUnit measuringUnit, ThrowableRunnable task) {
        try {
            trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }
    }

    public static <T> T trackThrows(@NotNull String profile, ThrowableSupplier<T> task) throws Exception {
        return trackThrows(profile, BukkitUnit.MILLISECONDS, task);
    }

    public static <T> T trackThrows(@NotNull String profile, @NotNull BukkitUnit measuringUnit, ThrowableSupplier<T> task) throws Exception {
        return of(profile, measuringUnit).trackThrows(task);
    }

    public static void trackThrows(@NotNull String profile, ThrowableRunnable task) throws Exception {
        trackThrows(profile, BukkitUnit.MILLISECONDS, task);
    }

    public static void trackThrows(@NotNull String profile, @NotNull BukkitUnit measuringUnit, ThrowableRunnable task) throws Exception {
        of(profile, measuringUnit).trackThrows(task);
    }

    public static Timing of(@NotNull String profile) {
        return of(profile, BukkitUnit.MILLISECONDS);
    }

    public static Timing of(@NotNull String profile, @NotNull BukkitUnit measuringUnit) {
        return new Timing(profile, measuringUnit);
    }

    public <T> T track(ThrowableSupplier<T> task) {
        try {
            return trackThrows(task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }
    }

    public <T> T trackThrows(ThrowableSupplier<T> task) throws Exception {
        try {
            return task.get();
        } finally {
            close();
        }
    }

    public void track(ThrowableRunnable task) {
        try {
            trackThrows(task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }
    }

    public void trackThrows(ThrowableRunnable task) throws Exception {
        try {
            task.run();
        } finally {
            close();
        }
    }

    private void close() {
        if (LOG) {
            long endTime = measuringUnit.convert(System.nanoTime() - startTime, BukkitUnit.NANOSECONDS);
            System.out.printf("Stopping measuring profiler '%s' - Took: %s %s. %n", this.profile, endTime, measuringUnit.getAbbreviation());
        }
    }


    @FunctionalInterface
    public interface ThrowableSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Exception;
    }

}
