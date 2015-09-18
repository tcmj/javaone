package com.tcmj.common.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.tcmj.common.lang.Objects;

/**
 * Helper for reflection operations. Uses a cache for classes and methods internally.
 * <img alt="tcmj" src="doc-files/tcmjbg.gif" align="right">
 * @test com.tcmj.common.reflection.ReflectionHelperTest
 * @author tdeut - Thomas Deutsch
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
        Objects.notBlank(className, "Name of the class cannot be blank!");
        if (classCache == null) {
            classCache = new HashMap<>();
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
        Objects.notBlank(className, "Name of the class cannot be blank!");
        Class<T> classInstance = loadClass(className);
        return newObject(classInstance, paramTypes, parameters);
    }

    /**
     * Instantiates the given class with the given parameters
     * @param <T> type safety
     * @param clazz the class to be instantiated
     * @param paramTypes the constructor parameters types
     * @param parameters the constructor parameters
     * @return <T> instance of given type
     */
    public static <T> T newObject(Class<T> clazz, Class<?>[] paramTypes, Object... parameters) {
        Objects.notNull(clazz, "Class object cannot be null!");
        try {
            Constructor<T> constructor = clazz.getConstructor(paramTypes);
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException("Exception: " + ex.getMessage(), ex);
        }
    }

    public static <T extends Exception> T newException(Class<T> clazz, Object... parameters) {
        Objects.notNull(clazz, "Exception class object cannot be null!");
        try {
            Constructor<T> constructor;

            if(parameters.length == 0){
                constructor = clazz.getConstructor(new Class[]{});
            }else if(parameters.length == 1){
                constructor = clazz.getConstructor(new Class[]{String.class});
            }else  if(parameters.length == 2){
                constructor = clazz.getConstructor(new Class[]{String.class, Exception.class});
            }else{
                throw new IllegalArgumentException("Cannot guess constructor! Try a String as parameter!");
            }

            return constructor.newInstance(parameters);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException("Exception: " + ex.getMessage(), ex);
        }
    }


    /**
     * Instantiates the given class with a no-arg constructor.
     * @param className the class to be instantiated
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T newObject(String className) {
        Objects.notBlank(className, "Name of the class cannot be blank!");
        Class<T> classinstance = loadClass(className);

        try {

            Object[] parameters = null;
            Class<?>[] paramTypes = null;

            Constructor<T> constructor = classinstance.getConstructor(paramTypes);

            return constructor.newInstance(parameters);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException("Exception: " + ex.getMessage(), ex);
        }
    }

    /** Executes a method on the given instance object using exactly one parameter.
     * This function is designed to call set methods.<br>
     * Warning!: Do not use this function if you have more than one set methods
     * with same name (but different paramters) !!!
     * @param instance the object on which the method should be invoked
     * @param setter name of the (set)method to invoke
     * @param value parameter of the (set)method
     */
    public static void setValue(Object instance, String setter, Object value) {
        Objects.notNull(instance, "Parameter Object must not be null! (1)");
        Objects.notBlank(setter, "Name of the setter cannot be blank (Parameter 2)");

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
        Objects.notNull(instance, "Parameter Object must not be null! (1)");
        Objects.notBlank(getter, "Name of the getter cannot be blank (Parameter 2)");

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
     * @return Method object if found (or null if not)
     */
    public static Method getMethod(Class<?> clazz, String methodName) {
        Objects.notNull(clazz, "Parameter Class<?> must not be null! (1)");
        Objects.notBlank(methodName, "Name of the method cannot be blank!");
        if (methodCache == null) {
            methodCache = new HashMap<>();
        }

        String key = String.valueOf(clazz.hashCode()) + methodName;

        Method method = methodCache.get(key);

        if (method == null) {

            Method[] methods = clazz.getMethods();
//TODO exception wenn mehr als 1x gefunden!
            for (int i = 0, max = methods.length; i < max; i++) {
                method = methods[i];

                if (method.getName().equals(methodName)) {

                    methodCache.put(key, method);
                    return method;

                }

            }
//            throw new RuntimeException("No method: '" + methodName + "' found in class: " + clazz);
            return null;
        } else {
            return method;
        }

    }

    /**
     * Tries to find a {@link Field field} on the supplied {@link Class} with the
     * supplied <code>name</code> and/or {@link Class type}. Searches all superclasses
     * up to {@link Object}.
     * @param clazz the class to introspect
     * @param fieldname the name of the field (may be <code>null</code> if type is specified)
     * @param fieldtype the type of the field (may be <code>null</code> if name is specified)
     * @return the corresponding Field object, or <code>null</code> if not found
     */
    public static Field getField(Class<?> clazz, String fieldname, Class<?> fieldtype) {
        Objects.notNull(clazz, "Parameter Class<?> must not be null! (1)");
        Objects.ensure(fieldname != null || fieldtype != null, "Either name or type of the field must be specified");
        Class<?> currentClass = clazz;
        while (!Object.class.equals(currentClass) && currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if ((fieldname == null || fieldname.equals(field.getName()))
                        && (fieldtype == null || fieldtype.equals(field.getType()))) {
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
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
        return method.getAnnotation(annotationClazz);
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

    /**
     * Extracts all classes of the given objects using the getClass() method.
     * <p>
     * @param parameter any java objects
     * @return array containing each return value of the getClass method
     */
    public static Class[] extractClasses(Object[] parameter) {
        Class[] classes = new Class[parameter.length];
        for (int i = 0; i < parameter.length; i++) {
            if (parameter[i] instanceof Class) {
                classes[i] = (Class) parameter[i];
            } else {
                classes[i] = parameter[i].getClass();
            }
        }
        return classes;
    }

}
