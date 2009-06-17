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
    private static final String testdatapathOut = "testdata\\com.tcmj.common.tools.xml.map\\XMLMapTestExtended_";
    private static final String testdatapathIn = "testdata\\com.tcmj.common.tools.xml.map\\";
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

    @Test
    public void testSerialisation() throws Exception {

        File file = new File(testdatapathOut + "testSerialisation.xml");
        String serFilename = testdatapathOut + "testSerialisation.ser";

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
    }

    @Test
    public void aBREBootStrapTest() throws Exception {
        logger.info("aBREBootStrapTest");
        File file = new File(testdatapathIn + "BREBootStrap.xml");
        XMLMap xmap = new XMLMap(file);

        xmap.setXMLRootNodeName("BootStrap");
        xmap.setXMLEntryPoint("Database");
        xmap.readXML();
        System.out.println(xmap.showDataEntries(false, true));


        File fileOut = new File(testdatapathOut + "BREBootStrapOut.xml");
        xmap.setXMLFileHandle(fileOut);
        xmap.saveXML();



    }

    @Test
    public void schemaTest() throws Exception {
        XMLMap xmap = new XMLMap(new File(testdatapathOut + "schemaTest.xml"));
        xmap.setXMLRootNodeName("i:tcmj");
        xmap.setXMLEntryPoint("i:darth");
        xmap.put("i:one.i:two", testdatapathOut);
        xmap.saveXML();
    }

    /**
     * Test of readXML method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testReadXMLandSaveXMLwithMassData() throws Exception {

        XMLMap model = new XMLMap(new File(testdatapathOut + "testReadXMLandSaveXMLwithMassData.xml"));
        //leeren (mit 0 entries speichern):
        model.saveXML();

        //kontrolle ob wirklich leer:
        model.readXML();
        assertEquals(0, model.size());

        String sep = model.getLevelSeparator();



        String anykey50 = null;
        String e1 = "Randomized";
        for (int ia = 1; ia <= 100; ia++) {

            String w0 = randomWord(20);
            String key = e1.concat(sep).concat(w0);
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
        model.saveXML();
        model.saveXML();
        model.readXML();
        logger.info("Size of loaded model: " + model.size());
        assertEquals(100, model.size());
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