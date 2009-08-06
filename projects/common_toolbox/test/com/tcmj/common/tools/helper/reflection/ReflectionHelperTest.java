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

    
    /**
     * Test of 'public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters)' method, of class ReflectionHelper.
     */
    @Test
    public void newClass() {
        System.out.println("public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters)");
        String clazzName = "java.math.BigDecimal";
        Class<?>[] parameterTypes = new Class<?>[]{int.class};
        int expResult = 30;
        Object result = ReflectionHelper.newClass(clazzName, parameterTypes, 30);
        assertEquals(expResult, ((BigDecimal) result).intValue());
        System.out.println(" result = " + result);
    }


    @Test(expected = RuntimeException.class)
    public void newClassWhenClassDoesNotExist() throws Exception {
        ReflectionHelper.newClass("non.existing.Unknown", new Class[]{}, new Object[]{});
    }


    /**
     * Test of 'public static void setValue(Object instance, String setter, Object value)' method, of class ReflectionHelper.
     */
    @Test
    public void setValue() throws Exception{
        System.out.println("public static void setValue(Object instance, String setter, Object value)");

        SimplePojo pojo = new SimplePojo();

        assertNull(pojo.getValueA());
        assertNull(pojo.getValueB());

        ReflectionHelper.setValue(pojo, "setValueA", "Alpha");

        assertEquals("Alpha", pojo.getValueA());

        ReflectionHelper.setValue(pojo, "setValueB", "Beta");

        assertEquals("Alpha", pojo.getValueA());

        System.out.println(" result = setValue works fine! " );


        long start = System.currentTimeMillis();

        for (int i = 1; i <= 1000000; i++) {

            ReflectionHelper.setValue(pojo, "setValueA", "Alpha");
            ReflectionHelper.setValue(pojo, "setValueB", "Beta");

        }

        long end = System.currentTimeMillis();
        System.out.println("time test: "+(end-start)+" ms");




    }




    class SimplePojo{
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
