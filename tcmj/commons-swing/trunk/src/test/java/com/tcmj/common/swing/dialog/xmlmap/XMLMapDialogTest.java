

package com.tcmj.common.swing.dialog.xmlmap;

import com.tcmj.common.swing.dialog.xmlmap.XMLMapDialog;
import com.tcmj.common.tools.xml.map.XMLMap;
import java.io.File;

import javax.swing.WindowConstants;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class XMLMapDialogTest {

    private static final String testdatapath = "testdata\\com.tcmj.common.swing.dialog.xmlmap\\";

    public XMLMapDialogTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of simpleExampleWithGui method, of class XMLMapDialog.
     */
    @Test
    public void simpleExampleWithGui() throws Exception {

      
//File file = new File(testdatapath + "simpleExampleWithGui.xml");
File file = new File(testdatapath + "BREBootStrap.xml");



        XMLMap mo = new XMLMap(file);

//        mo.setXMLRootNodeName("BusinessObjects");
        mo.setXMLRootNodeName("BootStrap");
//        mo.setXMLEntryPoint("Project");
        mo.setXMLEntryPoint("Database");

        mo.readXML();
//        mo.put("dates", "lastrun", "1979-11-02");
//        mo.put("dates", "nextrun", "2005-12-24");
//        mo.put("numbers", "one", "1");
//        mo.put("numbers", "two", "2");
//        mo.put("numbers", "three", "3");
//java.util.Date ddd1 = new java.util.Date();
//        Thread.sleep(1000L);
//        java.util.Date ddd2 = new java.util.Date();
//
//        mo.put("a.b.c", "xy", ddd1);
//        mo.put("x.y.a", "xy", ddd2);
//        mo.put("x.y.z", "123", ddd2);
//        mo.put("foo", "1");
//java.util.Date ddd = new java.util.Date();
//        mo.put("datum", "xy", ddd);

        //Der User kann selber seine Properties anpassen mit Hilfe der Klasse
        //    XMLMapDialog:
        XMLMapDialog pan = new XMLMapDialog(null, true);
        pan.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pan.setDataModel(mo);
        pan.show();

        

//        assertEquals("get", "555", mo.get("x.y.z"));


    }



    public void createDummyMapEntries(XMLMap model) {


    }



}