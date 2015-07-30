package com.tcmj.common.lang;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * com.tcmj.common.lang.Check
 * @author tcmj
 */
public class CheckTest {

    public CheckTest() {
    }

    /**
     * Test of notNull method, of class Check and also the internal formatting method.
     */
    @Test
    public void testNotNull() {
        System.out.println("notNull");
        Object nullInstance = null;
        //We have to test substitution with 1 variable
        try {
            Check.notNull(nullInstance, "We have null on {}", "any");
        } catch (Exception e) {
            assertThat("NullPointerException expected 1", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 1", e.getMessage(), equalTo("We have null on any"));
        }
        //We have to test substitution with 2 variables
        try {
            Check.notNull(nullInstance, "We have null on {} and {}", "one", "two");
        } catch (Exception e) {
            assertThat("NullPointerException expected 2", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 2", e.getMessage(), equalTo("We have null on one and two"));
        }
        //We have to test substitution with more than 2 variables (runs through another if case on formatting)
        try {
            Check.notNull(nullInstance, "We have null on {} and {} and {}", "one", "two", "three");
        } catch (Exception e) {
            assertThat("NullPointerException expected 3", e, instanceOf(NullPointerException.class));
            assertThat("Formatted message expected 3", e.getMessage(), equalTo("We have null on one and two and three"));
        }
    }

    /**
     * Test of ensure method, of class Check.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testEnsure() {
        System.out.println("ensure");
        boolean condition = false;
        String msg = "testEnsure";
        Object[] params = null;
        Check.ensure(condition, msg, params);
    }

    /**
     * Test of notBlank method, of class Check.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNotBlank() {
        String string = " ";
        String message = "testNotBlank!";
        Object[] params = null;
        Check.notBlank(string, message, params);
    }

    /**
     * Test of isEmpty method, of class Check.
     */
    @Test
    public void testIsEmpty() {
        //when
        Integer five = null;
        assertThat("Any object", Check.isEmpty(11), is(false));
        assertThat("Any null object", Check.isEmpty(five), is(true));

        List<String> list = new ArrayList<String>();
        assertThat("Empty List", Check.isEmpty(list), is(true));
        list.add("iron");
        assertThat("Non-Empty List", Check.isEmpty(list), is(false));

        Set<String> set = new HashSet<String>();
        assertThat("Empty Set", Check.isEmpty(set), is(true));
        set.add("iron");
        assertThat("Non-Empty Set", Check.isEmpty(set), is(false));

        Map<String, String> map = new HashMap<String, String>();
        assertThat("Empty Map", Check.isEmpty(map), is(true));
        map.put("world", "planet");
        assertThat("Non-Empty Map", Check.isEmpty(map), is(false));

        assertThat("Empty Array", Check.isEmpty(new Object[]{}), is(true));
        assertThat("Non-Empty Array", Check.isEmpty(new Object[]{1}), is(false));
        assertThat("Empty String-Array", Check.isEmpty(new String[]{}), is(true));
        assertThat("Non-Empty String-Array", Check.isEmpty(new String[]{"1"}), is(false));

        assertThat("Empty String", Check.isEmpty(""), is(true));
        assertThat("Whitespace String", Check.isEmpty(" "), is(false));
        assertThat("Non-Empty String", Check.isEmpty("1"), is(false));

    }
}
