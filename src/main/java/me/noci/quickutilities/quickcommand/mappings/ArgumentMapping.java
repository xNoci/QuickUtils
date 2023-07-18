package me.noci.quickutilities.quickcommand.mappings;

@FunctionalInterface
public interface ArgumentMapping<T> extends Mapping<T, String> {
    @Override
    T map(String argument);
}
