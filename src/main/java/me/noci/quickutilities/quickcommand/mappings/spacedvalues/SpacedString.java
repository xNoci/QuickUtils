package me.noci.quickutilities.quickcommand.mappings.spacedvalues;

public class SpacedString implements SpacedValue<String> {

    private final String value;

    public SpacedString(String[] args) {
        value = String.join(" ", args);
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
