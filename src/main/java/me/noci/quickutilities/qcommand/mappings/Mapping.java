package me.noci.quickutilities.qcommand.mappings;

@FunctionalInterface
public interface Mapping<R, A> {
    R map(A argument);
}
