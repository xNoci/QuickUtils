package me.noci.quickutilities.utils.time;

import java.util.concurrent.TimeUnit;

public class Timing {

    private final String profile;
    private final TimeUnit timeUnit;
    private final boolean log;
    private final long startTime;

    private Timing(String profile, TimeUnit timeUnit, boolean log) {
        this.profile = profile;
        this.timeUnit = timeUnit;
        this.log = log;
        this.startTime = System.nanoTime();

        if (this.log) {
            System.out.println("Starting measuring: " + this.profile);
        }
    }

    public static <T> T track(String profile, ThrowableSupplier<T> task) {
        return track(profile, TimeUnit.MILLISECONDS, task);
    }

    public static <T> T track(String profile, TimeUnit measuringUnit, ThrowableSupplier<T> task) {
        try {
            return trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }

    }

    public static void track(String profile, ThrowableRunnable task) {
        track(profile, TimeUnit.MILLISECONDS, task);
    }

    public static void track(String profile, TimeUnit measuringUnit, ThrowableRunnable task) {
        try {
            trackThrows(profile, measuringUnit, task);
        } catch (Exception e) {
            throw new RuntimeException("Caught exception while executing profiler '%s': ".formatted(profile), e);
        }
    }

    public static <T> T trackThrows(String profile, ThrowableSupplier<T> task) throws Exception {
        return trackThrows(profile, TimeUnit.MILLISECONDS, task);
    }

    public static <T> T trackThrows(String profile, TimeUnit measuringUnit, ThrowableSupplier<T> task) throws Exception {
        return of(profile, measuringUnit).trackThrows(task);
    }

    public static void trackThrows(String profile, ThrowableRunnable task) throws Exception {
        trackThrows(profile, TimeUnit.MILLISECONDS, task);
    }

    public static void trackThrows(String profile, TimeUnit measuringUnit, ThrowableRunnable task) throws Exception {
        of(profile, measuringUnit).trackThrows(task);
    }

    public static Timing of(String profile) {
        return of(profile, TimeUnit.MILLISECONDS);
    }

    public static Timing of(String profile, TimeUnit measuringUnit) {
        return new Timing(profile, measuringUnit, true);
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
        if (this.log) {
            long endTime = timeUnit.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            String suffix = switch (timeUnit) {
                case NANOSECONDS -> "ns";
                case MICROSECONDS -> "μs";
                case MILLISECONDS -> "ms";
                case SECONDS -> "sec";
                case MINUTES -> "min";
                case HOURS -> "hr";
                case DAYS -> "days";
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
