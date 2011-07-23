/*
 * Copyright (C) 2011 tcmj development
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.lang;

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
 *
 * @author tcmj
 */
public class CheckTest {
    
    public CheckTest() {
    }


    /**
     * Test of notNull method, of class Check.
     */
    @Test(expected=java.lang.NullPointerException.class)
    public void testNotNull() {
        System.out.println("notNull");
        Object instance_2 = null;
        String msg = "testNotNull";
        Object[] params = null;
        Check.notNull(instance_2, msg, params);
        
    }

    /**
     * Test of ensure method, of class Check.
     */
    @Test(expected=java.lang.IllegalArgumentException.class)
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
    @Test(expected=java.lang.IllegalArgumentException.class)
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
        
        Map<String,String> map = new HashMap<String, String>();
        assertThat("Empty Map", Check.isEmpty(map), is(true));
        map.put("world","planet");
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
