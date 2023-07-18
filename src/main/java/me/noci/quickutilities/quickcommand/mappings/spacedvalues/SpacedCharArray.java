package me.noci.quickutilities.quickcommand.mappings.spacedvalues;

import java.util.Arrays;

public class SpacedCharArray implements SpacedValue<char[]> {

    private final char[] value;

    public SpacedCharArray(String[] args) {
        String combinedArgs = String.join(" ", args);
        this.value = combinedArgs.toCharArray();
    }

    @Override
    public char[] value() {
        return value;
    }

    @Override
    public String toString() {
        return "SpacedCharArray{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}