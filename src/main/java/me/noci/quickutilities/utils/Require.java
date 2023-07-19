package me.noci.quickutilities.utils;

public class Require {

    /**
     * Check if object is not null.
     *
     * @param object to check
     * @throws NullPointerException Throws if object is null
     */
    public static void nonNull(Object object) throws NullPointerException {
        check(object != null, new NullPointerException());
    }

    /**
     * Check if object is not null.
     *
     * @param object  to check
     * @param message to send when object is null
     * @throws NullPointerException Throws if object is null
     */
    public static void nonNull(Object object, String message) throws NullPointerException {
        check(object != null, new NullPointerException(message));
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
