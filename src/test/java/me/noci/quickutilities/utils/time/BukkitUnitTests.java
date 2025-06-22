package me.noci.quickutilities.utils.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BukkitUnitTests {

    @Test
    public void testNanoseconds() {
        var nano = BukkitUnit.NANOSECONDS;

        assertEquals(1, nano.toNanos(1));
        assertEquals(1, nano.toMicros(1000));
        assertEquals(1, nano.toMillis(1000000));
        assertEquals(1, nano.toTicks(50000000));
        assertEquals(1, nano.toSeconds(1000000000));
        assertEquals(1, nano.toMinutes(60000000000L));
        assertEquals(1, nano.toHours(3600000000000L));
        assertEquals(1, nano.toDays(86400000000000L));
    }

    @Test
    public void testMicroseconds() {
        var mirco = BukkitUnit.MICROSECONDS;

        assertEquals(1000, mirco.toNanos(1));
        assertEquals(1, mirco.toMicros(1));
        assertEquals(1, mirco.toMillis(1000));
        assertEquals(1, mirco.toTicks(50000));
        assertEquals(1, mirco.toSeconds(1000000));
        assertEquals(1, mirco.toMinutes(60000000));
        assertEquals(1, mirco.toHours(3600000000L));
        assertEquals(1, mirco.toDays(86400000000L));
    }

    @Test
    public void testMilliseconds() {
        var milli = BukkitUnit.MILLISECONDS;

        assertEquals(1000000, milli.toNanos(1));
        assertEquals(1000, milli.toMicros(1));
        assertEquals(1, milli.toMillis(1));
        assertEquals(1, milli.toTicks(50));
        assertEquals(1, milli.toSeconds(1000));
        assertEquals(1, milli.toMinutes(60000));
        assertEquals(1, milli.toHours(3600000));
        assertEquals(1, milli.toDays(86400000));
    }

    @Test
    public void testTicks() {
        var ticks = BukkitUnit.TICKS;

        assertEquals(50000000, ticks.toNanos(1));
        assertEquals(50000, ticks.toMicros(1));
        assertEquals(50, ticks.toMillis(1));
        assertEquals(1, ticks.toTicks(1));
        assertEquals(1, ticks.toSeconds(20));
        assertEquals(1, ticks.toMinutes(1200));
        assertEquals(1, ticks.toHours(72000));
        assertEquals(1, ticks.toDays(1728000));
    }

    @Test
    public void testSeconds() {
        var seconds = BukkitUnit.SECONDS;

        assertEquals(1000000000, seconds.toNanos(1));
        assertEquals(1000000, seconds.toMicros(1));
        assertEquals(1000, seconds.toMillis(1));
        assertEquals(20, seconds.toTicks(1));
        assertEquals(1, seconds.toSeconds(1));
        assertEquals(1, seconds.toMinutes(60));
        assertEquals(1, seconds.toHours(3600));
        assertEquals(1, seconds.toDays(86400));
    }

    @Test
    public void testMinutes() {
        var minutes = BukkitUnit.MINUTES;

        assertEquals(60000000000L, minutes.toNanos(1));
        assertEquals(60000000, minutes.toMicros(1));
        assertEquals(60000, minutes.toMillis(1));
        assertEquals(1200, minutes.toTicks(1));
        assertEquals(60, minutes.toSeconds(1));
        assertEquals(1, minutes.toMinutes(1));
        assertEquals(1, minutes.toHours(60));
        assertEquals(1, minutes.toDays(1440));
    }

    @Test
    public void testHours() {
        var hours = BukkitUnit.HOURS;

        assertEquals(3600000000000L, hours.toNanos(1));
        assertEquals(3600000000L, hours.toMicros(1));
        assertEquals(3600000, hours.toMillis(1));
        assertEquals(72000, hours.toTicks(1));
        assertEquals(3600, hours.toSeconds(1));
        assertEquals(60, hours.toMinutes(1));
        assertEquals(1, hours.toHours(1));
        assertEquals(1, hours.toDays(24));
    }

    @Test
    public void testDays() {
        var days = BukkitUnit.DAYS;

        assertEquals(86400000000000L, days.toNanos(1));
        assertEquals(86400000000L, days.toMicros(1));
        assertEquals(86400000, days.toMillis(1));
        assertEquals(1728000, days.toTicks(1));
        assertEquals(86400, days.toSeconds(1));
        assertEquals(1440, days.toMinutes(1));
        assertEquals(24, days.toHours(1));
        assertEquals(1, days.toDays(1));
    }

}
