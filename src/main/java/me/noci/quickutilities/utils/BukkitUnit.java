package me.noci.quickutilities.utils;

public enum BukkitUnit {

    TICK {
        long toTick(long d)     { return d; }
        long toSeconds(long d)  { return d/(C1/C0); }
        long toMinutes(long d)  { return d/(C2/C0); }
        long toHours(long d)    { return d/(C3/C0); }
        long convert(long d, BukkitUnit u) { return u.toTick(d); }
    },
    SECONDS {
        long toTick(long d)     { return x(d, C1/C0, MAX/(C1/C0)); }
        long toSeconds(long d)  { return d; }
        long toMinutes(long d)  { return d/(C2/C1); }
        long toHours(long d)    { return d/(C3/C1); }
        long convert(long d, BukkitUnit u) { return u.toSeconds(d); }
    },
    MINUTES {
        long toTick(long d)     { return x(d, C2/C0, MAX/(C2/C0)); }
        long toSeconds(long d)  { return x(d, C2/C1, MAX/(C2/C1)); }
        long toMinutes(long d)  { return d; }
        long toHours(long d)    { return d/(C3/C2); }
        long convert(long d, BukkitUnit u) { return u.toMinutes(d); }
    },
    HOURS {
        long toTick(long d)     { return x(d, C3/C0, MAX/(C3/C0)); }
        long toSeconds(long d)  { return x(d, C3/C1, MAX/(C3/C1)); }
        long toMinutes(long d)  { return x(d, C3/C2, MAX/(C3/C2)); }
        long toHours(long d)    { return d; }
        long convert(long d, BukkitUnit u) { return u.toHours(d); }
    };

    static final long C0 = 1L; //ONE TICK
    static final long C1 = C0 * 20L; //ONE SECOND
    static final long C2 = C1 * 60L; //ONE MINUTE
    static final long C3 = C2 * 60L; //ONE HOUR
    
    static final long MAX = Long.MAX_VALUE;

    static long x(long d, long m, long over) {
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    abstract long convert(long sourceDuration, BukkitUnit sourceUnit);

    abstract long toTick(long duration);
    abstract long toSeconds(long duration);
    abstract long toMinutes(long duration);
    abstract long toHours(long duration);

}
