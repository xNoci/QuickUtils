package me.noci.quickutilities.utils;

import java.util.Optional;

public class Require {

    /**
     * Check if a string is not null, empty or blank
     *
     * @param sequence to test
     * @return Optional with the given string, when it's not null, empty or blank. Otherwise, returns an empty optional
     */
    public static Optional<String> nonBlank(CharSequence sequence) {
        return !isBlank(sequence) ? Optional.of(sequence.toString()) : Optional.empty();
    }

    /**
     * Check if a string is not null, empty or blank
     *
     * @param sequence to test
     * @return false - when string is null, empty or blank
     */
    public static boolean notBlank(CharSequence sequence) {
        return !isBlank(sequence);
    }

    /**
     * Check if a string is null, empty or blank
     *
     * @param sequence to test
     * @return true - when string is null, empty or blank
     */
    public static boolean isBlank(CharSequence sequence) {
        if (sequence == null || sequence.length() == 0) return true;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!Character.isWhitespace(c)) return false;
        }
        return true;
    }

    /**
     * Check if object is not null.
     *
     * @param object to check
     * @param <T>
     * @return The given object
     * @throws NullPointerException Throws if object is null
     */
    public static <T> T nonNull(T object) throws NullPointerException {
        check(object != null, new NullPointerException());
        return object;
    }

    /**
     * Check if object is not null.
     *
     * @param object  to check
     * @param message to send when object is null
     * @param <T>
     * @return The given object
     * @throws NullPointerException Throws if object is null
     */
    public static <T> T nonNull(T object, String message) throws NullPointerException {
        check(object != null, new NullPointerException(message));
        return object;
    }

    /**
     * Check if a requirement is fulfilled.
     *
     * @param requirement which has to be fulfilled
     * @param message     to send when the {@code requirement} is false
     * @throws IllegalStateException Throws if {@param requirement} is false
     */
    public static void checkState(boolean requirement, String message) throws IllegalStateException {
        check(requirement, new IllegalStateException(message));
    }

    /**
     * Check if a requirement is fulfilled.
     *
     * @param requirement to test
     * @param message     to send when the {@code requirement} is false
     * @throws IllegalStateException Throws if {@link Requirement#test()} returns false
     */
    @Deprecated
    public static void checkState(Requirement requirement, String message) throws IllegalStateException {
        check(requirement, new IllegalStateException(message));
    }

    /**
     * Check if a requirement is fulfilled.
     *
     * @param requirement which has to be fulfilled
     * @param message     to send when the {@code requirement} is false
     * @throws IllegalArgumentException Throws if {@param requirement} is false
     */
    public static void checkArgument(boolean requirement, String message) throws IllegalArgumentException {
        check(requirement, new IllegalArgumentException(message));
    }

    /**
     * Check if a requirement is fulfilled.
     *
     * @param requirement to test
     * @param message     to send when the {@code requirement} is false
     * @throws IllegalArgumentException Throws if {@link Requirement#test()} returns false
     */
    @Deprecated
    public static void checkArgument(Requirement requirement, String message) throws IllegalArgumentException {
        check(requirement, new IllegalArgumentException(message));
    }


    /**
     * Test if a requirement is fulfilled.
     *
     * @param requirement which has to be fulfilled
     * @param exception   which will be thrown if requirement is false
     * @param <T>         type of the RuntimeException
     * @throws T exception which will be thrown if {@param requirement} is false
     */
    public static <T extends RuntimeException> void check(boolean requirement, T exception) throws T {
        if (requirement) return;
        throw exception;
    }

    /**
     * Test if a requirement is fulfilled.
     *
     * @param requirement to test
     * @param exception   which will be thrown if requirement is false
     * @param <T>         type of the RuntimeException
     * @throws T exception which will be thrown if {@link Requirement#test()} returns false
     */
    @Deprecated
    public static <T extends RuntimeException> void check(Requirement requirement, T exception) throws T {
        if (requirement.test()) return;
        throw exception;
    }

    @FunctionalInterface
    @Deprecated
    public interface Requirement {
        boolean test();
    }

}
