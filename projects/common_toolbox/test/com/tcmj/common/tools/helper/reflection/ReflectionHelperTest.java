package com.tcmj.common.tools.helper.reflection;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tcmj
 */
public class ReflectionHelperTest {

    public ReflectionHelperTest() {
    }

    /**
     * Test of 'public static <T> Class<T> loadClass(String className)' method, of class ReflectionHelper.
     */
    @Test
    public void loadClass() {
        System.out.println("public static <T> Class<T> loadClass(String className)");
        String classname = "java.util.Date";
        Class expResult = Date.class;
        Class result = ReflectionHelper.loadClass(classname);
        assertEquals(expResult, result);
        System.out.println(" result = " + result);
    }

    @Test(expected = RuntimeException.class)
    public void loadClassWhenClassDoesNotExist() {
        ReflectionHelper.loadClass("jafa.nutil.Dates");
    }

    /**
     * Test of 'public static <T> T newObject(String className, Class<?>[] paramTypes, Object... parameters)' method, of class ReflectionHelper.
     */
    @Test
    public void newObject() {
        System.out.println("public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters)");
        String clazzName = "java.math.BigDecimal";
        Class<?>[] parameterTypes = new Class<?>[]{int.class};
        int expResult = 30;
        Object result = ReflectionHelper.newObject(clazzName, parameterTypes, 30);
        assertEquals(expResult, ((BigDecimal) result).intValue());
        System.out.println(" result = " + result);

        System.out.println("public static <T> T newClass(String className)");
        Object result2 = ReflectionHelper.newObject("java.util.Date");
        assertNotNull(result2);
        System.out.println(" result = " + result2);

    }

    @Test(expected = RuntimeException.class)
    public void newClassWhenClassDoesNotExist() throws Exception {
        ReflectionHelper.newObject("non.existing.Unknown", new Class[]{}, new Object[]{});
    }

    /**
     * Test of 'public static void setValue(Object instance, String setter, Object value)' method, of class ReflectionHelper.
     */
    @Test
    public void setValueAndGetValue() throws Exception {
        System.out.println("public static void setValue(Object instance, String setter, Object value)");

        SimplePojo pojo = new SimplePojo();

        assertNull(pojo.getValueA());
        assertNull(pojo.getValueB());

        ReflectionHelper.setValue(pojo, "setValueA", "Alpha");

        assertEquals("Alpha", pojo.getValueA());

        ReflectionHelper.setValue(pojo, "setValueB", "Beta");

        assertEquals("Alpha", pojo.getValueA());

        System.out.println(" result = setValue works fine! ");


        Object got = ReflectionHelper.getValue(pojo, "getValueA");
        assertEquals(got, pojo.getValueA());
        System.out.println(" result = getValue works also fine! ");

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 100000; i++) {

            ReflectionHelper.setValue(pojo, "setValueA", "Alpha");
            ReflectionHelper.setValue(pojo, "setValueB", "Beta");
            ReflectionHelper.getValue(pojo, "getValueA");
            ReflectionHelper.getValue(pojo, "getValueB");

        }

        long end = System.currentTimeMillis();
        System.out.println(" time: " + (end - start) + " ms ");


        System.out.println(" " + ReflectionHelper.getCacheInfo());
    }

    @Test(expected = RuntimeException.class)
    public void setValueWhenMethodDoesNotExist() {
        SimplePojo pojo = new SimplePojo();
        ReflectionHelper.setValue(pojo, "unExistingMethodName", "Value2Set");
    }

    @Test
    public void simpleTest() throws Exception {
//        Class c = new Class();
    }

    static class SimplePojo {

        private String valueA;

        private String valueB;

        public String getValueA() {
            return valueA;
        }

        public void setValueA(String valueA) {
            this.valueA = valueA;
        }

        public String getValueB() {
            return valueB;
        }

        public void setValueB(String valueB) {
            this.valueB = valueB;
        }

    }
}
