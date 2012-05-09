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
package com.tcmj.common.jdbc.connect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * NewMain.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 09.02.2011
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { DBQConnection.class })

public class NewMain {

    /**
     * default no-arg-constructor.
     */
    public NewMain() {
    }


    /**
     * Test of setDriver method, of class DBQConnection.
     */
    @Test
    public void testGetSetDriver() throws Exception {
        System.out.println("setDriverClass");
        DBQConnection instance = new DBQConnection();
        instance.setDriver(Driver.CUSTOM);
//        assertEquals(Driver.ORACLE, instance.getDriver());
    }


    /** runit. */
    public void runit() throws Exception {
        testGetSetDriver();

    }


    /**
     * Start entry point.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.out.println("Starting NewMain");
            NewMain app = new NewMain();
            long lnstart = System.currentTimeMillis();
            app.runit();
            long lnend = System.currentTimeMillis();
            System.out.println("Finished NewMain in " + (lnend - lnstart) + " ms.");
        } catch (Exception exc) {
            System.out.println("Error: " + exc.getMessage());
            exc.printStackTrace();
        }

    }
}
