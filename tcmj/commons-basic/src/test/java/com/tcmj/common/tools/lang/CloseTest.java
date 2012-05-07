/*
 * Copyright (C) 2012 tcmj development
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

import java.io.Closeable;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * CloseTest.
 * 
 * TODO think about how to test them!
 * 
 * @author tcmj - Thomas Deutsch
 */
public class CloseTest {
    
    public CloseTest() {
    }

    /**
     * Test of quiet method, of class Close.
     */
    @Test
    public void testQuiet() {
        System.out.println("quiet");
        StringReader reader = new StringReader("abc");
        Close.quiet(reader);
        // TODO review the generated test code and remove the default call to fail.
         
    }

    /**
     * Test of unchecked method, of class Close.
     */
    @Test
    public void testUnchecked() {
        System.out.println("unchecked");
        StringReader reader = new StringReader("abc");
        Close.unchecked(reader);
        // TODO review the generated test code and remove the default call to fail.
    }
}
