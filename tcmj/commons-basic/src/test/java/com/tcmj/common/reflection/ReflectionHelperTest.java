package com.tcmj.common.reflection;

import com.tcmj.common.reflection.ReflectionHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author tcmj
 */
public class ReflectionHelperTest {

    public ReflectionHelperTest() {
    }


    /**
     * Test of 'public static Field getField(Class<?> clazz, String fieldname, Class<?> fieldtype)' method, of class ReflectionHelper.
     */
    @Test
    public void shouldGetAField() throws Exception{
        System.out.println("public static Field getField(Class<?> clazz, String fieldname, Class<?> fieldtype)");

        File cut = new File("pom.xml");
        Field pathField = ReflectionHelper.getField(File.class, "path", String.class);
        assertThat(pathField, notNullValue());
        assertThat(pathField.getName(), equalTo("path"));

        pathField.setAccessible(true);
        assertThat(pathField.get(cut), equalTo("pom.xml"));
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


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCalledWithNullParameter_loadClass() {
        ReflectionHelper.loadClass(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCalledWithNullParameter_newObject() {
        ReflectionHelper.newObject(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCalledWithNullParameter_newObject_P1() {
        String className = null;
        ReflectionHelper.newObject(className, new Class<?>[]{int.class}, 30);
    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCalledWithNullParameter_newObject_P2() {
        Object newObject = ReflectionHelper.newObject("java.lang.StringBuilder", null, "Hello");
        fail("StringBuilder "+newObject+" created!");
    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCalledWithNullParameter_newObject_P3() {
        Object newObject = ReflectionHelper.newObject("java.lang.Integer", new Class<?>[]{int.class}, (Object[])null);
        fail("Integer "+newObject+" created!");
    }


    /**
     * Test of 'public static <T> T newObject(String className, Class<?>[] paramTypes, Object... parameters)' method, of class ReflectionHelper.
     */
    @Test
    public void shouldCreateNewObjects() {
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

    @Test
    public void shouldCreateANewExceptionUsingAStringParameter() throws Exception {
        System.out.println("public static <T extends Exception> T newException(Class<T> clazz, Object... parameters)");
        IOException exception = ReflectionHelper.newException(IOException.class, "AnyMessageForOurIOException");
        assertThat("IOException is not null", exception, notNullValue());
        assertThat("MessageText", exception.getMessage(), equalTo("AnyMessageForOurIOException"));
        System.out.println(exception);

    }

    @Test
    public void shouldCreateANewExceptionUsingAnEmptyConstructor() throws Exception {
        System.out.println("public static <T extends Exception> T newException(Class<T> clazz, Object... parameters)");
        IOException exception = ReflectionHelper.newException(IOException.class);
        assertThat("IOException is not null", exception, notNullValue());
        assertThat("MessageText is not null", exception.getMessage(), nullValue());
        System.out.println(exception);
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


        String got = ReflectionHelper.getValue(pojo, "getValueA");
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
    public void annotationTest_ReadAnnotation() throws Exception {
        System.out.println("public static <T extends Annotation> T  getMethodAnnotation(Class<?> clz, String methodName, Class<T> annotationClazz)");
        XmlElement xmlElementAnnotation = ReflectionHelper.getMethodAnnotation(SimplePojo.class, "getValueB", XmlElement.class);
        assertNotNull(xmlElementAnnotation);
        System.out.println(" xmlElementAnnotation: " + xmlElementAnnotation);
        Test testAnnotation = ReflectionHelper.getMethodAnnotation(ReflectionHelperTest.class, "annotationTest_ReadAnnotation", Test.class);
        assertNotNull(testAnnotation);
        System.out.println(" testAnnotation: " + testAnnotation);

    }


    @Test
    public void shouldFindAllMethods(){
        assertThat(ReflectionHelper.getMethod(SimplePojo.class, "toString"), notNullValue());
    }

    @Test
    public void shouldExtractAllClasses(){
        //given
        String one = "a string";
        Class two = Double.class;
        SimplePojo three = new SimplePojo();
        Object[] anything = new Object[]{one, two, three};

        //when
        Class[] extracted = ReflectionHelper.extractClasses(anything);

        //then
        assertEquals(extracted[0], String.class);
        assertEquals(extracted[1], Double.class);
        assertEquals(extracted[2], SimplePojo.class);
    }




    static class SimplePojo {

        private String valueA;

        private String valueB;


        @SuppressWarnings("unchecked")
        public String getValueA() {
            return valueA;
        }


        public void setValueA(String valueA) {
            this.valueA = valueA;
        }


        @XmlElement(name = "test55")
        public String getValueB() {
            return valueB;
        }


        public void setValueB(String valueB) {
            this.valueB = valueB;
        }
    }
}
