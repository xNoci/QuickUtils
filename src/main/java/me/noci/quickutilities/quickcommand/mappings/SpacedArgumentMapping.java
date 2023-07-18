package me.noci.quickutilities.quickcommand.mappings;

@FunctionalInterface
public interface SpacedArgumentMapping<T> extends Mapping<T, String[]> {
    @Override
    T map(String[] arguments);
}
