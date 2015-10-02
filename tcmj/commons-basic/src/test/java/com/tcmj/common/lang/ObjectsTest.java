package com.tcmj.common.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * com.tcmj.common.lang.Objects
 * @author tcmj
 */
public class ObjectsTest {

    public ObjectsTest() {
    }

    /**
     * Test of notNull method, of class Objects and also the internal formatting method.
     */
    @Test
    public void testNotNull() {
        System.out.println("notNull");
        Object nullInstance = null;
        //We have to test substitution with 1 variable
        try {
            Objects.notNull(nullInstance, "We have null on {}", "any");
        } catch (Exception e) {
            assertThat("NullPointerException expected 1", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 1", e.getMessage(), equalTo("We have null on any"));
        }
        //We have to test substitution with 2 variables
        try {
            Objects.notNull(nullInstance, "We have null on {} and {}", "one", "two");
        } catch (Exception e) {
            assertThat("NullPointerException expected 2", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 2", e.getMessage(), equalTo("We have null on one and two"));
        }
        //We have to test substitution with more than 2 variables (runs through another if case on formatting)
        try {
            Objects.notNull(nullInstance, "We have null on {} and {} and {}", "one", "two", "three");
        } catch (Exception e) {
            assertThat("NullPointerException expected 3", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 3", e.getMessage(), equalTo("We have null on one and two and three"));
        }
    }

    /**
     * Test of notNull method, of class Objects.
     */
    @Test(expected = java.io.IOException.class)
    public void testNotNullWithCustomCheckedException() {
        Date nullInstance = null;
        Objects.notNull(nullInstance, IOException.class, "Here we have a custom i/o exception in {}", this);
    }

    /**
     * Test of notNull method, of class Objects.
     */
    @Test(expected = RuntimeException.class)
    public void testNotNullWithCustomRuntimeException() {
        Date nullInstance = null;
        Objects.notNull(nullInstance, RuntimeException.class, "Here we have a custom RuntimeException in {}", this);
    }


    /**
     * Test of ensure method, of class Objects.
     */
    @Test(expected = java.lang.IllegalStateException.class)
    public void testEnsure() {
        System.out.println("ensure");
        boolean condition = false;
        String msg = "testEnsure";
        Object[] params = null;
        Objects.ensure(condition, msg, params);
    }

    /**
     * Test of notBlank method, of class Objects.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNotBlank() {
        String string = " ";
        String message = "testNotBlank!";
        Object[] params = null;
        Objects.notBlank(string, message, params);
    }

    /**
     * Test of isEmpty method, of class Objects.
     */
    @Test
    public void testIsEmpty() {

        assertThat("Any object", Objects.isEmpty(11), is(false));
        assertThat("Any null object", Objects.isEmpty(null), is(true));

        List<String> list = new ArrayList<>();
        assertThat("Empty List", Objects.isEmpty(list), is(true));
        list.add("iron");
        assertThat("Non-Empty List", Objects.isEmpty(list), is(false));

        Set<String> set = new HashSet<>();
        assertThat("Empty Set", Objects.isEmpty(set), is(true));
        set.add("iron");
        assertThat("Non-Empty Set", Objects.isEmpty(set), is(false));

        Map<String, String> map = new HashMap<>();
        assertThat("Empty Map", Objects.isEmpty(map), is(true));
        map.put("world", "planet");
        assertThat("Non-Empty Map", Objects.isEmpty(map), is(false));

        assertThat("Empty Array", Objects.isEmpty(new Object[]{}), is(true));
        assertThat("Non-Empty Array", Objects.isEmpty(new Object[]{1}), is(false));
        assertThat("Empty String-Array", Objects.isEmpty(new String[]{}), is(true));
        assertThat("Non-Empty String-Array", Objects.isEmpty(new String[]{"1"}), is(false));

        assertThat("Empty String", Objects.isEmpty(""), is(true));
        assertThat("Whitespace String", Objects.isEmpty(" "), is(false));
        assertThat("Non-Empty String", Objects.isEmpty("1"), is(false));

        assertThat("Empty Stream", Objects.isEmpty(Stream.empty()), is(true));

        //@todo add ResultSet test!

    }
}
