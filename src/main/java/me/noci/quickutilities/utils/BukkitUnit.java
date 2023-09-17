package me.noci.quickutilities.utils;

public enum BukkitUnit {

    MILLISECONDS {
        public long toMilliseconds(long d)     { return d; }
        public long toTicks(long d)            { return convertUp(d, C_MILLISECOND, C_TICK); }
        public long toSeconds(long d)          { return convertUp(d, C_MILLISECOND, C_SECOND); }
        public long toMinutes(long d)          { return convertUp(d, C_MILLISECOND, C_MINUTE); }
        public long toHours(long d)            { return convertUp(d, C_MILLISECOND, C_HOUR); }
        public long toDays(long d)             { return convertUp(d, C_MILLISECOND, C_DAY); }
        public long convert(long d, BukkitUnit u) { return u.toMilliseconds(d); }
    },
    TICKS {
        public long toMilliseconds(long d)     { return convertDown(d, C_TICK, C_MILLISECOND); }
        public long toTicks(long d)            { return d; }
        public long toSeconds(long d)          { return convertUp(d, C_TICK, C_SECOND); }
        public long toMinutes(long d)          { return convertUp(d, C_TICK, C_MINUTE); }
        public long toHours(long d)            { return convertUp(d, C_TICK, C_HOUR); }
        public long toDays(long d)             { return convertUp(d, C_TICK, C_DAY); }
        public long convert(long d, BukkitUnit u) { return u.toTicks(d); }
    },
    SECONDS {
        public long toMilliseconds(long d)     { return convertDown(d, C_SECOND, C_MILLISECOND); }
        public long toTicks(long d)            { return convertDown(d, C_SECOND, C_TICK); }
        public long toSeconds(long d)          { return d; }
        public long toMinutes(long d)          { return convertUp(d, C_SECOND, C_MILLISECOND); }
        public long toHours(long d)            { return convertUp(d, C_SECOND, C_HOUR); }
        public long toDays(long d)             { return convertUp(d, C_SECOND, C_DAY); }
        public long convert(long d, BukkitUnit u) { return u.toSeconds(d); }
    },
    MINUTES {
        public long toMilliseconds(long d)     { return convertDown(d, C_MINUTE, C_MILLISECOND); }
        public long toTicks(long d)            { return convertDown(d, C_MINUTE, C_TICK); }
        public long toSeconds(long d)          { return convertDown(d, C_MINUTE, C_SECOND); }
        public long toMinutes(long d)          { return d; }
        public long toHours(long d)            { return convertUp(d, C_MINUTE, C_HOUR); }
        public long toDays(long d)             { return convertUp(d, C_MINUTE, C_DAY); }
        public long convert(long d, BukkitUnit u) { return u.toMinutes(d); }
    },
    HOURS {
        public long toMilliseconds(long d)     { return convertDown(d, C_HOUR, C_MILLISECOND); }
        public long toTicks(long d)            { return convertDown(d, C_HOUR, C_TICK); }
        public long toSeconds(long d)          { return convertDown(d, C_HOUR, C_SECOND); }
        public long toMinutes(long d)          { return convertDown(d, C_HOUR, C_MINUTE); }
        public long toHours(long d)            { return d; }
        public long toDays(long d)             { return convertUp(d, C_HOUR, C_DAY); }
        public long convert(long d, BukkitUnit u) { return u.toHours(d); }
    },
    DAYS {
        public long toMilliseconds(long d)     { return convertDown(d, C_DAY, C_MILLISECOND); }
        public long toTicks(long d)            { return convertDown(d, C_DAY, C_TICK); }
        public long toSeconds(long d)          { return convertDown(d, C_DAY, C_SECOND); }
        public long toMinutes(long d)          { return convertDown(d, C_DAY, C_MINUTE); }
        public long toHours(long d)            { return convertDown(d, C_DAY, C_HOUR); }
        public long toDays(long d)             { return d; }
        public long convert(long d, BukkitUnit u) { return u.toDays(d); }
    };

    private static final long C_MILLISECOND = 1L; //ONE MILLISECOND
    private static final long C_TICK = C_MILLISECOND * 50L; //ONE TICK
    private static final long C_SECOND = C_TICK * 20L; //ONE SECOND
    private static final long C_MINUTE = C_SECOND * 60L; //ONE MINUTE
    private static final long C_HOUR = C_MINUTE * 60L; //ONE HOUR
    private static final long C_DAY = C_HOUR * 24L; //ONE DAY

    private static final long MAX = Long.MAX_VALUE;

    private static long convertDown(long d, long thisConstant, long otherConstant) {
        long m = thisConstant / otherConstant;
        long over = MAX / m;
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    private static long convertUp(long d, long thisConstant, long otherConstant) {
        return d / (otherConstant / thisConstant);
    }

    public abstract long convert(long sourceDuration, BukkitUnit sourceUnit);

    public abstract long toMilliseconds(long duration);
    public abstract long toTicks(long duration);
    public abstract long toSeconds(long duration);
    public abstract long toMinutes(long duration);
    public abstract long toHours(long duration);
    public abstract long toDays(long duration);

}
