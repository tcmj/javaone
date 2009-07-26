package com.tcmj.common.tools.helper.reflection;

import java.math.BigDecimal;
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
        System.out.println(" result = "+result);
    }

    /**
     * Test of 'public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters)' method, of class ReflectionHelper.
     */
    @Test
    public void testNewClass() {
        System.out.println("public static <T> T newClass(String className, Class<?>[] paramTypes, Object... parameters)");
        String clazzName = "java.math.BigDecimal";
        Class<?>[] parameterTypes = new Class<?>[]{int.class};
        Object[] parameters = new Object[]{30};
        int expResult = 30;
        Object result = ReflectionHelper.newClass(clazzName, parameterTypes, parameters);
        assertEquals(expResult, ((BigDecimal) result).intValue());
        System.out.println(" result = "+result);
    }

}
