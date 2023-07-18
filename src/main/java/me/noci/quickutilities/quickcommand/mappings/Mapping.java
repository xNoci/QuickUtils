package me.noci.quickutilities.quickcommand.mappings;

@FunctionalInterface
public interface Mapping<R, A> {
    R map(A argument);
}
