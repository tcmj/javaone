/*
 *  Copyright (C) 2011 Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.tcmj.common.tools.lang;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class ApplicationTest {

    public ApplicationTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of getApplicationTitle method, of class Application.
     */
    @Test
    public void testGetApplicationTitle() {
        System.out.println("getApplicationTitle");
        Class context = getClass();
        String result = Application.getApplicationTitle(context);
        assertNotNull(result);
    }


    /**
     * Test of getApplicationVersion method, of class Application.
     */
    @Test
    public void testGetApplicationVersion() {
        System.out.println("getApplicationVersion");
        Class context = getClass();
        String result = Application.getApplicationVersion(context);
        assertNotNull(result);
    }


    /**
     * Test of getApplicationVendor method, of class Application.
     */
    @Test
    public void testGetApplicationVendor() {
        System.out.println("getApplicationVendor");
        Class context = getClass();
        String result = Application.getApplicationVendor(context);
        assertNotNull(result);
    }

}