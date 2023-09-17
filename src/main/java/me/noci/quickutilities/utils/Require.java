package me.noci.quickutilities.utils;

import java.util.Optional;

public class Require {

    /**
     * Check if a string is not null, empty or blank
     *
     * @param sequence to test
     * @return Optional with the given string, when it's not null, empty or blank. Otherwise, returns an empty optional
     */
    public static <T extends CharSequence> Optional<T> nonBlank(T sequence) {
        return !isBlank(sequence) ? Optional.of(sequence) : Optional.empty();
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
        if (sequence == null || sequence.isEmpty()) return true;
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
     * @param args    arguments to format the message
     * @param <T>
     * @return The given object
     * @throws NullPointerException Throws if object is null
     */
    public static <T> T nonNull(T object, String message, Object... args) throws NullPointerException {
        return nonNull(object, message.formatted(args));
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
     * @param args        arguments to format the message
     * @throws IllegalStateException Throws if {@param requirement} is false
     */
    public static void checkState(boolean requirement, String message, Object... args) throws IllegalStateException {
        checkState(requirement, message.formatted(args));
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
     * @param requirement which has to be fulfilled
     * @param message     to send when the {@code requirement} is false
     * @param args        arguments to format the message
     * @throws IllegalArgumentException Throws if {@param requirement} is false
     */
    public static void checkArgument(boolean requirement, String message, Object... args) throws IllegalStateException {
        checkArgument(requirement, message.formatted(args));
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

}
