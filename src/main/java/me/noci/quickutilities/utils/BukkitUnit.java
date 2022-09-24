package me.noci.quickutilities.utils;

public enum BukkitUnit {

    MILLISECONDS {
        public long toMilliseconds(long d)     { return d; }
        public long toTicks(long d)            { return d/(C1 / C0); }
        public long toSeconds(long d)          { return d/(C2 / C0); }
        public long toMinutes(long d)          { return d/(C3 / C0); }
        public long toHours(long d)            { return d/(C4 / C0); }
        public long convert(long d, BukkitUnit u) { return u.toMilliseconds(d); }
    },
    TICKS {
        public long toMilliseconds(long d)     { return x(d, C1 / C0, MAX/(C1 / C0)); }
        public long toTicks(long d)            { return d; }
        public long toSeconds(long d)          { return d/(C2 / C1); }
        public long toMinutes(long d)          { return d/(C3 / C1); }
        public long toHours(long d)            { return d/(C4 / C1); }
        public long convert(long d, BukkitUnit u) { return u.toTicks(d); }
    },
    SECONDS {
        public long toMilliseconds(long d)     { return x(d, C2 / C0, MAX/(C2 / C0)); }
        public long toTicks(long d)            { return x(d, C2 / C1, MAX/(C2 / C1)); }
        public long toSeconds(long d)          { return d; }
        public long toMinutes(long d)          { return d/(C3 / C2); }
        public long toHours(long d)            { return d/(C4 / C2); }
        public long convert(long d, BukkitUnit u) { return u.toSeconds(d); }
    },
    MINUTES {
        public long toMilliseconds(long d)     { return x(d, C3 / C0, MAX/(C3 / C0)); }
        public long toTicks(long d)            { return x(d, C3 / C1, MAX/(C3 / C1)); }
        public long toSeconds(long d)          { return x(d, C3 / C2, MAX/(C3 / C2)); }
        public long toMinutes(long d)          { return d; }
        public long toHours(long d)            { return d/(C4 / C3); }
        public long convert(long d, BukkitUnit u) { return u.toMinutes(d); }
    },
    HOURS {
        public long toMilliseconds(long d)     { return x(d, C4 / C1, MAX/(C4 / C0)); }
        public long toTicks(long d)            { return x(d, C4 / C1, MAX/(C4 / C1)); }
        public long toSeconds(long d)          { return x(d, C4 / C2, MAX/(C4 / C2)); }
        public long toMinutes(long d)          { return x(d, C4 / C3, MAX/(C4 / C3)); }
        public long toHours(long d)            { return d; }
        public long convert(long d, BukkitUnit u) { return u.toHours(d); }
    };

    private static final long C0 = 1L; //ONE MILLISECOND
    private static final long C1 = C0 * 50L; //ONE TICK
    private static final long C2 = C1 * 20L; //ONE SECOND
    private static final long C3 = C2 * 60L; //ONE MINUTE
    private static final long C4 = C3 * 60L; //ONE HOUR

    private static final long MAX = Long.MAX_VALUE;

    private static long x(long d, long m, long over) {
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    public abstract long convert(long sourceDuration, BukkitUnit sourceUnit);

    public abstract long toMilliseconds(long duration);
    public abstract long toTicks(long duration);
    public abstract long toSeconds(long duration);
    public abstract long toMinutes(long duration);
    public abstract long toHours(long duration);

}
