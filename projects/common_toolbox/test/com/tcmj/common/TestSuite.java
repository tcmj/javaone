/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcmj.common;

import com.tcmj.common.jdbc.connect.DBQuickConnectTest;
import com.tcmj.common.jdbc.connect.DBQuickConnectTestOnline;
import com.tcmj.common.tools.helper.date.DateHelperTest;
import com.tcmj.common.tools.text.RandomStringsTest;
import com.tcmj.common.tools.xml.map.XMLMapTest;
import com.tcmj.common.tools.xml.map.XMLMapTestExtended;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author Administrator
 */
@RunWith(Suite.class)
@SuiteClasses({DBQuickConnectTest.class, DBQuickConnectTestOnline.class, XMLMapTest.class, XMLMapTestExtended.class, RandomStringsTest.class, DateHelperTest.class})
public class TestSuite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
