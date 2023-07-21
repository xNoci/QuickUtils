package me.noci.quickutilities.utils;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class Fraction {

    public static @Nullable Fraction of(String value) {
        String[] values = value.split("/");
        if (values.length != 2) return null;
        try {
            long numerator = Long.parseLong(values[0]);
            long denominator = Long.parseLong(values[1]);
            return Fraction.of(numerator, denominator);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Fraction of(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }

    @Getter private final long numerator;
    @Getter private final long denominator;
    @Getter private final double result;

    private Fraction(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;

        double result = Double.NaN;
        if (denominator != 0) {
            result = (double) numerator / (double) denominator;
        }

        this.result = result;
    }

}
