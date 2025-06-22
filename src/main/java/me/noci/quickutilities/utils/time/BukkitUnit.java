package me.noci.quickutilities.utils.time;

import lombok.Getter;

public enum BukkitUnit {

    NANOSECONDS(BukkitUnit.NANO_SCALE, "ns"),
    MICROSECONDS(BukkitUnit.MICRO_SCALE, "μs"),
    MILLISECONDS(BukkitUnit.MILLI_SCALE, "ms"),
    TICKS(BukkitUnit.TICK_SCALE, "ticks"),
    SECONDS(BukkitUnit.SECOND_SCALE, "sec"),
    MINUTES(BukkitUnit.MINUTE_SCALE, "min"),
    HOURS(BukkitUnit.HOUR_SCALE, "hr"),
    DAYS(BukkitUnit.DAY_SCALE, "days");

    private static final long NANO_SCALE = 1L;
    private static final long MICRO_SCALE = 1000L * NANO_SCALE;
    private static final long MILLI_SCALE = 1000L * MICRO_SCALE;
    private static final long TICK_SCALE = MILLI_SCALE * 50L;
    private static final long SECOND_SCALE = TICK_SCALE * 20L;
    private static final long MINUTE_SCALE = SECOND_SCALE * 60L;
    private static final long HOUR_SCALE = MINUTE_SCALE * 60L;
    private static final long DAY_SCALE = HOUR_SCALE * 24L;

    @Getter private final String abbreviation;

    /*
     * Instances cache conversion ratios and saturation cutoffs for
     * the units up through SECONDS. Other cases compute them, in
     * method cvt.
     */

    private final long scale;
    private final long maxNanos;
    private final long maxMicros;
    private final long maxMillis;
    private final long maxTicks;
    private final long maxSecs;
    private final long microRatio;
    private final int milliRatio;   // fits in 32 bits
    private final int tickRatio;    // fits in 32 bit
    private final int secRatio;     // fits in 32 bits

    BukkitUnit(long scale, String abbreviation) {
        this.abbreviation = abbreviation;
        this.scale = scale;
        this.maxNanos = Long.MAX_VALUE / scale;
        this.microRatio = (scale >= MICRO_SCALE) ? (scale / MICRO_SCALE) : (MICRO_SCALE / scale);
        this.maxMicros = Long.MAX_VALUE / microRatio;
        this.milliRatio = (int) ((scale >= MILLI_SCALE) ? (scale / MILLI_SCALE) : (MILLI_SCALE / scale));
        this.maxMillis = Long.MAX_VALUE / milliRatio;
        this.tickRatio = (int) ((scale >= TICK_SCALE) ? (scale / TICK_SCALE) : (TICK_SCALE / scale));
        this.maxTicks = Long.MAX_VALUE / tickRatio;
        this.secRatio = (int) ((scale >= SECOND_SCALE) ? (scale / SECOND_SCALE) : (SECOND_SCALE / scale));
        this.maxSecs = Long.MAX_VALUE / secRatio;
    }


    private static long cvt(long d, long dst, long src) {
        long r, m;
        if (src == dst) {
            return d;
        }
        if (src < dst) {
            return d / (dst / src);
        }
        if (d > (m = Long.MAX_VALUE / (r = src / dst))) {
            return Long.MAX_VALUE;
        }
        if (d < -m) {
            return Long.MIN_VALUE;
        }
        return d * r;
    }

    private static long cvtCache(long duration, long tickScale, long tickRatio, long maxTicks, long scale) {
        if (scale == tickScale) {
            return duration;
        }
        if (scale < tickScale) {
            return duration / tickRatio;
        }
        if (duration > maxTicks) {
            return Long.MAX_VALUE;
        }
        if (duration < -maxTicks) {
            return Long.MIN_VALUE;
        }
        return duration * tickRatio;
    }


    public long convert(long sourceDuration, BukkitUnit sourceUnit) {
        return switch (this) {
            case NANOSECONDS -> sourceUnit.toNanos(sourceDuration);
            case MICROSECONDS -> sourceUnit.toMicros(sourceDuration);
            case MILLISECONDS -> sourceUnit.toMillis(sourceDuration);
            case TICKS -> sourceUnit.toTicks(sourceDuration);
            case SECONDS -> sourceUnit.toSeconds(sourceDuration);
            default -> cvt(sourceDuration, scale, sourceUnit.scale);
        };
    }

    public long toNanos(long duration) {
        if (scale == NANO_SCALE) {
            return duration;
        }
        if (duration > maxNanos) {
            return Long.MAX_VALUE;
        }
        if (duration < -maxNanos) {
            return Long.MIN_VALUE;
        }
        return duration * scale;
    }

    public long toMicros(long duration) {
        return cvtCache(duration, MICRO_SCALE, microRatio, maxMicros, scale);
    }

    public long toMillis(long duration) {
        return cvtCache(duration, MILLI_SCALE, milliRatio, maxMillis, scale);
    }


    public long toSeconds(long duration) {
        return cvtCache(duration, SECOND_SCALE, secRatio, maxSecs, scale);
    }

    public long toTicks(long duration) {
        return cvtCache(duration, TICK_SCALE, tickRatio, maxTicks, scale);
    }

    public long toMinutes(long duration) {
        return cvt(duration, MINUTE_SCALE, scale);
    }

    public long toHours(long duration) {
        return cvt(duration, HOUR_SCALE, scale);
    }

    public long toDays(long duration) {
        return cvt(duration, DAY_SCALE, scale);
    }

}
