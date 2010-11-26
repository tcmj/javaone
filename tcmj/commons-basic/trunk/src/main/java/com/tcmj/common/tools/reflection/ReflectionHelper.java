/* Copyright(c) 2009 tcmj  All Rights Reserved. */
package com.tcmj.common.tools.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for reflection operations (Cached!).
 * @author tdeut - Thomas Deutsch
 * @junit com.tcmj.common.tools.helper.reflection.ReflectionHelperTest
 */
public final class ReflectionHelper {

    /** Cache for Class objects. */
    private static Map<String, Class> classCache;

    /** Cache for Method objects. */
    private static Map<String, Method> methodCache;


    /** Singleton. */
    private ReflectionHelper() {
    }


    /** Loads a class with the Class.forName(String) method.
     * Catches the ClassNotFoundException and throws a RunTimeException
     * instead.
     * @param className class to load (must be in classpath)
     * @return Class object on success
     */
    public static <T> Class<T> loadClass(String className) {

        if (classCache == null) {
            classCache = new HashMap<String, Class>();
        }

        Class<T> cclass = classCache.get(className);

        if (cclass != null) {
            return cclass;
        } else {
            try {
                cclass = (Class<T>) Class.forName(className);
                classCache.put(className, cclass);
                return cclass;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("ClassNotFound: " + className, e);
            }
        }

    }


    /**
     * Instantiates the given class with the given parameters
     * @param <T> type
     * @param className the class to be instantiated
     * @param paramTypes the constructor parameters types
     * @param parameters the constructor parameters
     * @return <T> instance of given type
     */
    @SuppressWarnings("unchecked")
    public static <T> T newObject(String className, Class<?>[] paramTypes, Object... parameters) {

        Class<T> classinstance = loadClass(className);

        try {

            Constructor<T> constructor = classinstance.getConstructor(paramTypes);

            return constructor.newInstance(parameters);

        } catch (InstantiationException iex) {
            throw new RuntimeException("InstantiationException: " + iex.getMessage(), iex);
        } catch (IllegalAccessException iaccex) {
            throw new RuntimeException("IllegalAccessException: " + iaccex.getMessage(), iaccex);
        } catch (InvocationTargetException invex) {
            throw new RuntimeException("InvocationTargetException: " + invex.getMessage(), invex);
        } catch (NoSuchMethodException nsmex) {
            throw new RuntimeException("NoSuchMethodException: " + nsmex.getMessage(), nsmex);
        }

    }


    /**
     * Instantiates the given class with a no-arg constructor.
     * @param className the class to be instantiated
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T newObject(String className) {

        Class<T> classinstance = loadClass(className);

        try {

            Object[] parameters = null;
            Class<?>[] paramTypes = null;

            Constructor<T> constructor = classinstance.getConstructor(paramTypes);

            return constructor.newInstance(parameters);

        } catch (InstantiationException iex) {
            throw new RuntimeException("InstantiationException: " + iex.getMessage(), iex);
        } catch (IllegalAccessException iaccex) {
            throw new RuntimeException("IllegalAccessException: " + iaccex.getMessage(), iaccex);
        } catch (InvocationTargetException invex) {
            throw new RuntimeException("InvocationTargetException: " + invex.getMessage(), invex);
        } catch (NoSuchMethodException nsmex) {
            throw new RuntimeException("NoSuchMethodException: " + nsmex.getMessage(), nsmex);
        }

    }


    /** Executes a method on the given instance object using exactly one parameter.
     * This function is designed to call set methods.<br>
     * Warning!: Do not use this function if you have more than one set methods
     *           with same name (but different paramters) !!!
     * @param instance the object on which the method should be invoked
     * @param setter name of the (set)method to invoke
     * @param value parameter of the (set)method
     */
    public static void setValue(Object instance, String setter, Object value) {

        Class classObj = instance.getClass();

        Method method = getMethod(classObj, setter);
        try {
            method.invoke(instance, value);
        } catch (IllegalAccessException iaccex) {
            throw new RuntimeException("IllegalAccessException: " + iaccex.getMessage(), iaccex);
        } catch (InvocationTargetException invex) {
            throw new RuntimeException("InvocationTargetException: " + invex.getMessage(), invex);
        }


    }


    /** Executes a method on the given instance object using NO parameters.
     * This function is designed to call get methods.
     * @param instance the object on which the method should be invoked
     * @param getter name of the (get)method to invoke
     * @return return value of the (get)method
     */
    public static <T> T getValue(Object instance, String getter) {

        Class classObj = instance.getClass();

        Method method = getMethod(classObj, getter);
        try {
            Object[] noparams = null;
            return (T) method.invoke(instance, noparams);
        } catch (IllegalAccessException iaccex) {
            throw new RuntimeException("IllegalAccessException: " + iaccex.getMessage(), iaccex);
        } catch (InvocationTargetException invex) {
            throw new RuntimeException("InvocationTargetException: " + invex.getMessage(), invex);
        }


    }


    /** extracts a specific Method object from the given Class object.
     * @param clazz Class object
     * @param methodName name of the method to search for
     * @return Method object if found (or RuntimeException if not)
     */
    public static Method getMethod(Class<?> clazz, String methodName) {

        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
        }

        StringBuilder bld = new StringBuilder(String.valueOf(clazz.hashCode()));
        bld.append(methodName);
        String key = bld.toString();

        Method method = methodCache.get(key);

        if (method == null) {

            Method[] methods = clazz.getMethods();

            for (int i = 0, max = methods.length; i < max; i++) {
                method = methods[i];

                if (method.getName().equals(methodName)) {

                    methodCache.put(key, method);
                    return method;

                }

            }

            throw new RuntimeException("No method: '" + methodName + "' found in class: " + clazz);

        } else {
            return method;
        }

    }


    /**
     * reads a java annotation from a method.
     * @param <T> class object
     * @param clz class to read
     * @param methodName name of the method to search for
     * @param annotationClazz class of the annotation to search for
     * @return the whole annotation class (same type as the third parameter)
     */
    public static <T extends Annotation> T getMethodAnnotation(Class<?> clz, String methodName, Class<T> annotationClazz) {
        Method method = getMethod(clz, methodName);
        Annotation annotation = method.getAnnotation(annotationClazz);
        return (T) annotation;
    }


    /** Prints size of the class/method cache. */
    public static String getCacheInfo() {
        StringBuilder bld = new StringBuilder("ReflectionHelper: ");
        if (classCache != null) {
            bld.append(classCache.size());
            bld.append(" classes cached! ");
        }
        if (methodCache != null) {
            bld.append(methodCache.size());
            bld.append(" methods cached!");
        }
        return bld.toString();
    }
}
