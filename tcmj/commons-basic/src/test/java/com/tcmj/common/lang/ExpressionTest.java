package com.tcmj.common.lang;

import com.tcmj.common.lang.Expression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tcmj.common.lang.Expression.*;

/**
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class ExpressionTest {

    public ExpressionTest() {
    }


    /**
     * Test of isEmpty and isNotEmpty method, of class Expression.
     */
    @Test
    public void testTheIsEmptyAndIsNotEmptyMethods() {
        System.out.println("isEmpty");
        assertEquals("Null", true, Expression.isEmpty(null));
        assertEquals("Null", false, Expression.isNotEmpty(null));

        //List
        List<String> lstStr = null;
        assertEquals("Null List", true, Expression.isEmpty(lstStr));
        assertEquals("Null List", false, Expression.isNotEmpty(lstStr));
        lstStr = new ArrayList<String>();
        assertEquals("Empty List", true, Expression.isEmpty(lstStr));
        assertEquals("Empty List", false, Expression.isNotEmpty(lstStr));
        lstStr.add("one");
        assertEquals("Filled List", false, Expression.isEmpty(lstStr));
        assertEquals("Filled List", true, Expression.isNotEmpty(lstStr));

        //Set
        Set<String> setStr = null;
        assertEquals("Null Set", true, Expression.isEmpty(setStr));
        assertEquals("Null Set", false, Expression.isNotEmpty(setStr));
        setStr = new HashSet<String>();
        assertEquals("Empty Set", true, Expression.isEmpty(setStr));
        assertEquals("Empty Set", false, Expression.isNotEmpty(setStr));
        setStr.add("one");
        assertEquals("Filled Set", false, Expression.isEmpty(setStr));
        assertEquals("Filled Set", true, Expression.isNotEmpty(setStr));


        //Map
        Map<String, Date> map = null;
        assertEquals("Null Map", true, Expression.isEmpty(map));
        assertEquals("Null Map", false, Expression.isNotEmpty(map));
        map = new HashMap<String, Date>();
        assertEquals("Empty Map", true, Expression.isEmpty(map));
        assertEquals("Empty Map", false, Expression.isNotEmpty(map));
        map.put("one", new Date());
        assertEquals("Filled Map", false, Expression.isEmpty(map));
        assertEquals("Filled Map", true, Expression.isNotEmpty(map));


    }
}
