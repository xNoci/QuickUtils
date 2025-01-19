package me.noci.quickutilities.utils.time;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Timing {

    private static boolean LOG = true;

    @NotNull private final String profile;
    @NotNull private final TimeUnit timeUnit;
    private final long startTime;

    private Timing(@NotNull String profile, @NotNull TimeUnit timeUnit) {
        this.profile = profile;
        this.timeUnit = timeUnit;
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
        return track(profile, TimeUnit.MILLISECONDS, task);
    }

    public static <T> T track(@NotNull String profile, @NotNull TimeUnit measuringUnit, ThrowableSupplier<T> task) {
        try {
            return trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }

    }

    public static void track(@NotNull String profile, ThrowableRunnable task) {
        track(profile, TimeUnit.MILLISECONDS, task);
    }

    public static void track(@NotNull String profile, @NotNull TimeUnit measuringUnit, ThrowableRunnable task) {
        try {
            trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }
    }

    public static <T> T trackThrows(@NotNull String profile, ThrowableSupplier<T> task) throws Exception {
        return trackThrows(profile, TimeUnit.MILLISECONDS, task);
    }

    public static <T> T trackThrows(@NotNull String profile, @NotNull TimeUnit measuringUnit, ThrowableSupplier<T> task) throws Exception {
        return of(profile, measuringUnit).trackThrows(task);
    }

    public static void trackThrows(@NotNull String profile, ThrowableRunnable task) throws Exception {
        trackThrows(profile, TimeUnit.MILLISECONDS, task);
    }

    public static void trackThrows(@NotNull String profile, @NotNull TimeUnit measuringUnit, ThrowableRunnable task) throws Exception {
        of(profile, measuringUnit).trackThrows(task);
    }

    public static Timing of(@NotNull String profile) {
        return of(profile, TimeUnit.MILLISECONDS);
    }

    public static Timing of(@NotNull String profile, @NotNull TimeUnit measuringUnit) {
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
            long endTime = timeUnit.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            String suffix = switch (timeUnit) {
                case NANOSECONDS -> "ns";
                case MICROSECONDS -> "μs";
                case MILLISECONDS -> "ms";
                case SECONDS -> "sec";
                case MINUTES -> "min";
                case HOURS -> "hr";
                case DAYS -> "days";
                default -> timeUnit.name();
            };

            System.out.printf("Stopping measuring profiler '%s' - Took: %s %s. %n", this.profile, endTime, suffix);
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
