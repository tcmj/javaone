/*
 * XMLMapTestExtended.java
 * JUnit based test
 *
 * Created on 19. April 2007, 23:27
 */
package com.tcmj.common.tools.xml.map;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static junit.framework.Assert.*;

/**
 * XMLMapTestExtended.
 * @author tdeut
 */
public class XMLMapTestExtended {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(XMLMap.class);

//    private static final String testdatapathOut = "testdata\\com.tcmj.common.tools.xml.map\\XMLMapTestExtended_";
    //private static final String testdatapathIn = "testdata\\com.tcmj.common.tools.xml.map\\";

    private static final String testdatapath = "src\\test\\resources\\com\\tcmj\\common\\tools\\xml\\map\\";

    /** delete test files after exiting this test. */
    private static final boolean deleteOnExit = true;

    private static final char[][] RANDOM_STR = {
        {'a', 'e', 'i', 'o', 'u'},
        {'b', 'd', 'f', 'g', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'w'}
    };

    private static final Random RAND = new Random();


    @Before
    public void beforeEachTest() {
        m();

    }


    private void m() {
        logger.info("-------------------------------------------------------------------------------------------------------");

    }


    /** helper method to set that the generated xml-output-files should be deleted after jvm exit. */
    private static final void initFile(File file) {
        if (deleteOnExit) {
            file.deleteOnExit();
        }
    }


    @Test
    public void testSerialisation() throws Exception {

        File file = new File(testdatapath + "XMLMapTestExtended_testSerialisation_TMP.xml");
        initFile(file);
        String serFilename = testdatapath + "XMLMapTestExtended_testSerialisation_TMP.ser";

        XMLMap xmap = new XMLMap(file);
        xmap.put("a", "123");
        xmap.put("b.c", "456");
        xmap.put("d.e.f", "789");
        logger.info(xmap.toString());

        ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(serFilename)));
        objOut.writeObject(xmap);
        objOut.close();

        ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serFilename)));
        XMLMap xmap2 = (XMLMap) objIn.readObject();
        objIn.close();
        logger.info(xmap2.toString());
        assertEquals("123", xmap2.get("a"));
        assertEquals("456", xmap2.get("b.c"));
        assertEquals("789", xmap2.get("d.e.f"));

        assertEquals(3, xmap2.size());

        xmap2.put("ser", "ser");
        assertEquals(4, xmap2.size());

        xmap2.saveXML();
        
        File serFile = new File(serFilename);
        if (serFile!=null && serFile.exists() && deleteOnExit) {
            serFile.deleteOnExit();
        }


    }


    @Test
    public void aBREBootStrapTest() throws Exception {
        logger.info("aBREBootStrapTest");
        File file = new File(testdatapath + "XMLMapTestExtended_BREBootStrap.xml");

        XMLMap xmap = new XMLMap(file);

        xmap.setXMLRootNodeName("BootStrap");
        xmap.setXMLEntryPoint("Database");
        xmap.readXML();
        System.out.println(xmap.showDataEntries(false, true));


        File fileOut = new File(testdatapath + "XMLMapTestExtended_BREBootStrap_TMP.xml");
        initFile(fileOut);
        xmap.setXMLFileHandle(fileOut);
        xmap.saveXML();



    }


    @Test
    public void schemaTest() throws Exception {
        XMLMap xmap = new XMLMap(new File(testdatapath + "XMLMapTestExtended_schemaTest.xml"));
        xmap.readXML();
        System.out.println(xmap.showDataEntries(false, false));

        assertNotNull("i:darth.i:one.i:two not available", xmap.get("i:darth.i:one.i:two"));
        assertNotNull("darth", xmap.get("darth"));

        xmap.setXMLRootNodeName("i:tcmj");
        xmap.setXMLEntryPoint("i:darth");
        xmap.readXML();
        System.out.println(xmap.showDataEntries(false, false));
        assertNotNull("i:one.i:two not available", xmap.get("i:one.i:two"));


        //        xmap.setXMLRootNodeName("tcmj");
//        
//        xmap.put("one.two", testdatapathOut);
        xmap.saveXML();
    }


    /**
     * Test of readXML method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testReadXMLandSaveXMLwithMassData() throws Exception {
        final File file = new File(testdatapath + "XMLMapTestExtended_testReadXMLandSaveXMLwithMassData_TMP.xml");
        initFile(file);
        XMLMap model = new XMLMap(file);
        //leeren (mit 0 entries speichern):
        model.saveXML();

        //kontrolle ob wirklich leer:
        model.readXML();
        assertEquals(0, model.size());

        String sep = model.getLevelSeparator();

        String anykey50 = null;
        String e1 = "Randomized";
        for (int ia = 1; ia <= 100; ia++) {

//            String w0 = randomWord(20);
            String key = e1.concat(sep).concat(randomWord(10)).concat(randomWord(10));
            model.put(key, randomWord(50));

            model.setAttribute(key, "number", String.valueOf(ia));

            if (ia == 50) {
                anykey50 = key;
            }


        }

        model.saveXML();
        model.readXML();
        logger.info("Size of loaded model: " + model.size());
        assertEquals(100, model.size());
        assertEquals("50", model.getAttribute(anykey50, "number"));


        //nochmal lesen 1
        model.readXML();
        logger.info("Size of loaded model: " + model.size());
        assertEquals(100, model.size());
        assertEquals("50", model.getAttribute(anykey50, "number"));

        //2x speichern und nochmal lesen
//        model.saveXML();
//        model.saveXML();
//        model.readXML();

        for (int i = 1; i <= 4; i++) {

            String level1 = "L"+i;
            for (int j = 1; j <= 5; j++) {

                String level2 = "L"+j;

                for (int k = 1; k <= 5; k++) {
                    String level3 = "L"+k;
                    String key = level1 + model.getLevelSeparator()+
                            level2 + model.getLevelSeparator() +
                            level3;
                    model.put(key, randomWord(10));
                }
            }

        }
        
        model.saveXML();
        model.readXML();
        logger.info("Size of loaded model: " + model.size());
        assertEquals(200, model.size());
        assertEquals("50", model.getAttribute(anykey50, "number"));

    }


    private String randomWord(int length) {
        char[] res = new char[length];
        int toggle = 1, max = 0;
        for (int i = 0; i < length; i++) {
            max = RANDOM_STR[toggle].length;
            res[i] = RANDOM_STR[toggle][RAND.nextInt(max)];
            toggle = 1 - toggle;
        }
        return new String(res);
    }


}//eof
