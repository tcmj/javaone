package com.tcmj.common.tools.helper.reflection;

/**
 * Helper for reflection operations.
 * @author tdeut - Thomas Deutsch
 * @junit com.tcmj.common.tools.helper.reflection.ReflectionHelperTest
 */
public final class ReflectionHelper {

    /** Singleton. */
    private ReflectionHelper() {
    }

    public static <T> Class<T> loadClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFound: " + className, e);
        }
    }

    /**
     * Instantiates the given class with the given parameters
     * @param className the class to be instantiated
     * @param paramTypes the constructor parameters types
     * @param parameters the constructor parameters
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters) {

        try {
            Class<T> classinstance = loadClass(className);
            return classinstance.getConstructor(paramTypes).newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
