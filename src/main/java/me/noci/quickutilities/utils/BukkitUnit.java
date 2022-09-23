package me.noci.quickutilities.utils;

public enum BukkitUnit {

    MILLISECONDS {
        long toMilliseconds(long d)     { return d; }
        long toTicks(long d)            { return d/(C1 / C0); }
        long toSeconds(long d)          { return d/(C2 / C0); }
        long toMinutes(long d)          { return d/(C3 / C0); }
        long toHours(long d)            { return d/(C4 / C0); }
        long convert(long d, BukkitUnit u) { return u.toMilliseconds(d); }
    },
    TICKS {
        long toMilliseconds(long d)     { return x(d, C1 / C0, MAX/(C1 / C0)); }
        long toTicks(long d)            { return d; }
        long toSeconds(long d)          { return d/(C2 / C1); }
        long toMinutes(long d)          { return d/(C3 / C1); }
        long toHours(long d)            { return d/(C4 / C1); }
        long convert(long d, BukkitUnit u) { return u.toTicks(d); }
    },
    SECONDS {
        long toMilliseconds(long d)     { return x(d, C2 / C0, MAX/(C2 / C0)); }
        long toTicks(long d)            { return x(d, C2 / C1, MAX/(C2 / C1)); }
        long toSeconds(long d)          { return d; }
        long toMinutes(long d)          { return d/(C3 / C2); }
        long toHours(long d)            { return d/(C4 / C2); }
        long convert(long d, BukkitUnit u) { return u.toSeconds(d); }
    },
    MINUTES {
        long toMilliseconds(long d)     { return x(d, C3 / C0, MAX/(C3 / C0)); }
        long toTicks(long d)            { return x(d, C3 / C1, MAX/(C3 / C1)); }
        long toSeconds(long d)          { return x(d, C3 / C2, MAX/(C3 / C2)); }
        long toMinutes(long d)          { return d; }
        long toHours(long d)            { return d/(C4 / C3); }
        long convert(long d, BukkitUnit u) { return u.toMinutes(d); }
    },
    HOURS {
        long toMilliseconds(long d)     { return x(d, C4 / C1, MAX/(C4 / C0)); }
        long toTicks(long d)            { return x(d, C4 / C1, MAX/(C4 / C1)); }
        long toSeconds(long d)          { return x(d, C4 / C2, MAX/(C4 / C2)); }
        long toMinutes(long d)          { return x(d, C4 / C3, MAX/(C4 / C3)); }
        long toHours(long d)            { return d; }
        long convert(long d, BukkitUnit u) { return u.toHours(d); }
    };

    static final long C0 = 1L; //ONE MILLISECOND
    static final long C1 = C0 * 50L; //ONE TICK
    static final long C2 = C1 * 20L; //ONE SECOND
    static final long C3 = C2 * 60L; //ONE MINUTE
    static final long C4 = C3 * 60L; //ONE HOUR

    static final long MAX = Long.MAX_VALUE;

    static long x(long d, long m, long over) {
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    abstract long convert(long sourceDuration, BukkitUnit sourceUnit);

    abstract long toMilliseconds(long duration);
    abstract long toTicks(long duration);
    abstract long toSeconds(long duration);
    abstract long toMinutes(long duration);
    abstract long toHours(long duration);

}
