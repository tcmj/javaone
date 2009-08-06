/* Copyright(c) 2009 tcmj.de  All Rights Reserved. */
package com.tcmj.common.tools.helper.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper for reflection operations.
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
                throw new RuntimeException("Class not found: " + className, e);
            }
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

    public static final Method getMethod(Class clazz, String methodName) {

        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
        }

        StringBuilder bld = new StringBuilder(""+clazz.hashCode());
        bld.append(methodName.hashCode());
        String key = bld.toString();

        Method method = methodCache.get(key);

        if (method == null) {

            Method[] methods = clazz.getMethods();
            
            for (int i = 0; i < methods.length; i++) {
                method = methods[i];

                if (method.getName().equals(methodName)) {

                    methodCache.put(key, method);
                    System.out.println("put "+methodName);
                    return method;

                }

            }

            throw new RuntimeException("No method: '" + methodName + "' found in class: " + clazz);
            
        }else{
            return method;
        }

    }

}
