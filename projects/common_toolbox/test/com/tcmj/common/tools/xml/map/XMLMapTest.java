/*
 * XMLMapTest.java
 * JUnit based test
 *
 * Created on 19. April 2007, 23:27
 */
package com.tcmj.common.tools.xml.map;

import com.tcmj.common.tools.xml.map.XMLMap.XMLMapException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tdeut
 */
public class XMLMapTest {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(XMLMap.class);
    private static final String testdatapath = "testdata\\com.tcmj.common.tools.xml.map\\";
    /** Model. */
    private XMLMap model;
    private static final File file = new File(testdatapath + "XMLMapTest.xml");

    @Before
    public void beforeEachTest() {
        m();


        //2. Neue Instanz des XMLPropertyModels erzeugen:
        model = new XMLMap(file);
    }

    private void m() {
        logger.info("-------------------------------------------------------------------------------------------------------");


    }

    /**
     * Test of put method, of class com.tcmj.tools.xml.model.XMLMap.
     *
     */
    @Test
    public void testPutGet() {

        logger.info("testPutGet");

        String key = "one", value = "1st";
        model.put(key, value);
        assertEquals(key, 1, model.size());
        assertEquals(key, value, model.get(key));

        key = "two.of.a.kind";
        value = "2nd";
        model.put(key, value);
        assertEquals(key, 2, model.size());
        assertEquals(key, value, model.get(key));

        key = "three.level2";
        value = "a b c d e f";
        model.put(key, value);
        assertEquals(key, 3, model.size());
        assertEquals(key, value, model.get(key));

        key = "four.big";
        value = "lower case";
        model.put(key, value);
        assertEquals(key, 4, model.size());
        assertEquals(key, value, model.get(key));

        key = "four.BIG";
        value = "upper case";
        model.put(key, value);
        assertEquals(key, 5, model.size());
        assertEquals(key, value, model.get(key));
        model.put(key, value);
        assertEquals(key, 5, model.size());
        assertEquals(key, value, model.get(key));

        key = "x1.x2.x3.x4.x5";
        value = "long";
        model.put(key, value);
        assertEquals(key, 6, model.size());
        assertEquals(key, value, model.get(key));


        key = "x1.x2.x3.x4.a";
        value = "value no ";
        for (int i = 1; i <= 25; i++) {
            model.put(key + i, value + i);
            assertEquals(key + i, value + i, model.get(key + i));
        }
        assertEquals(key, 6 + 25, model.size());
        for (int i = 1; i <= 25; i++) {
            assertEquals(key + i, value + i, model.get(key + i));
        }



        assertNull(model.get("Four.BIG"));
        assertNull(model.get("fOur.BIG"));
        assertNull(model.get("foUr.BIG"));
        assertNull(model.get("fouR.BIG"));


        logger.info(model.showDataEntries(false, true));

    }

    /**
     * Test of isSameLevel method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testIsSameLevel() {

        assertEquals(true, model.isSameLevel("key1", "key1"));
        assertEquals(true, model.isSameLevel("key1", "key1"));
        assertEquals(true, model.isSameLevel("a.key1", "a.key1"));
        assertEquals(true, model.isSameLevel("a.ab", "a.cd"));
        assertEquals(true, model.isSameLevel("a.b.c.ab", "a.b.c.cd"));

        assertEquals(false, model.isSameLevel("a.eins", "b.zwei"));
        assertEquals(false, model.isSameLevel("a", "b.zwei"));
        assertEquals(false, model.isSameLevel("a.eins", "b"));
        assertEquals(false, model.isSameLevel("a.b.c.ab", "v.b.c.ab"));
        assertEquals(false, model.isSameLevel("a.a.a.a", "a.a.a.a.a"));

    }

    /**
     * Test of putObject method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutObject() {


        //--------------------- root key
        String key1 = "one";
        model.put(key1, "ohnevalue");

        assertNull(model.getObject(key1));

        model.putObject(key1, new java.util.Date());

        assertNotNull(model.getObject(key1));

        model.putObject(key1, null);

        assertNull(model.getObject(key1));

        //--------------------- complex key
        String key2 = "a.b.c.d";
        model.put(key2, "ohnevalue");

        assertNull(model.getObject(key2));

        java.util.Date specificdate = new java.util.Date();
        model.putObject(key2, specificdate);

        Object dategot = model.getObject(key2);
        assertNotNull(dategot);

        assertEquals("not the same object!", specificdate, dategot);

        model.putObject(key2, null);

        assertNull(model.getObject(key2));

        //--------------------- behavour of value
        String key3 = "x.aaa.zzz.dee";
        model.put(key3, "myvalue567");
        model.putObject(key3, new java.util.Date());

        assertEquals("value lost", "myvalue567", model.get(key3));


    }

    /**
     * Test of getListProperty method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutListValueGetListValue() {
        logger.info("putListValue + getListValue");

        String[] v = new String[]{"aaa", "bbb", "ccc"};

        model.putListValue("o.boo.koo", v);


        String[] lsv = model.getListValue("o.boo.koo");

        assertEquals(3, lsv.length);
        assertEquals("aaa", lsv[0]);
        assertEquals("bbb", lsv[1]);
        assertEquals("ccc", lsv[2]);

        //case
        assertNull(model.getListValue("gibt.es.nicht"));

        //case: immutability
        String[] list = model.getListValue("o.boo.koo");
        assertEquals("aaa", list[0]);
        list[0] = "change";
        String[] anotherlist = model.getListValue("o.boo.koo");
        assertEquals("aaa", anotherlist[0]);
    }

    /**
     * Test of getObject method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetObject() {
        logger.info("getObject");
        java.util.Date ddd = new java.util.Date();
        model.put("datum", "xy", ddd);
        assertNotNull(model.getObject("datum"));
        assertEquals(ddd, model.getObject("datum"));
    }

    /**
     * Test of getObjectByValue method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetObjectByValue() throws Exception {
        java.util.Date ddd1 = new java.util.Date();
        Thread.sleep(1000L);
        java.util.Date ddd2 = new java.util.Date();

        model.put("a.b.c", "xy", ddd1);
        model.put("x.y.a", "xy", ddd2);
        model.put("x.y.z", "123", ddd2);
        try {
            model.getObjectByValue("xy");
            fail("illegal search");
        } catch (Exception ex) {
            logger.info("Expected: " + ex.getMessage());
        }

        assertNotNull(model.getObjectByValue("123"));
        assertEquals(ddd2, model.getObjectByValue("123"));
    }

    /**
     * Test of remove method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testRemove() throws XMLMapException {

        int size = model.size();

        model.put("abcdef", "hello");


        assertEquals(size + 1, model.size());

        String val = model.remove("abcdef");
        assertEquals("hello", val);
        assertEquals(size, model.size());

        //-----------key not avail
        assertNull(model.remove("key.not.available"));
        assertEquals(size, model.size());
    }

    /**
     * Test of readXML method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testReadXMLSaveXML() throws FileNotFoundException, TransformerConfigurationException, UnsupportedEncodingException, TransformerException, IOException, XMLMapException, ParserConfigurationException {
        int size = model.size();
        logger.info("size: " + size);

        model.saveXML();

        model.readXML();

        int size2 = model.size();

        assertEquals(size, size2);

        model.put("a", "a");
        model.put("b", "b");
        model.put("c", "c");
        model.put("x.y.z", "myvalueXYZ");
        model.saveXML();
        model.readXML();

        size2 = model.size();

        assertEquals(4, size2);
    }

    /**
     * Test of clear method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testClear() {
        logger.info("clear");


        model.put("a", "a");
        model.put("b", "b");
        model.put("c", "c");


        assertTrue((model.size() > 0));

        model.clear();

        assertTrue((model.size() == 0));
    }

    /**
     * Test of showDataEntries method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testShowDataEntries() {
        logger.info("showDataEntries");
        String sdet = model.showDataEntries(true, true);
        String sdef = model.showDataEntries(false, true);
        assertNotNull(sdet);
        assertNotNull(sdef);
        logger.info(sdet);
        model.put("montag.mittag", "Essen mit Java", new java.util.Date());
        model.put("montag.abend", "Essen ohne Java");
        model.setAttribute("montag.abend", "tag", "montag");
        sdet = model.showDataEntries(true, true);
        sdef = model.showDataEntries(false, true);
        assertNotNull(sdet);
        assertNotNull(sdef);
        logger.info(sdet);
    }

    /**
     * Test of getXMLRootNodeName method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testGetXMLRootNodeName() {
        assertNotNull(model.getXMLRootNodeName());
        assertEquals("tcmj", model.getXMLRootNodeName());

        model.setXMLRootNodeName("heinz");
        assertNotNull(model.getXMLRootNodeName());
        assertEquals("heinz", model.getXMLRootNodeName());


        model.setXMLRootNodeName("abcdefg");

        assertEquals("abcdefg", model.getXMLRootNodeName());
    }

    /**
     * Test of getXMLEntryPoint method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testGetChildnodename() {
        assertNotNull(model.getXMLEntryPoint());
        assertEquals("xmlprop", model.getXMLEntryPoint());

        model.setXMLEntryPoint("heinz");
        assertNotNull(model.getXMLEntryPoint());
        assertEquals("heinz", model.getXMLEntryPoint());
    }

    /**
     * Test of getLevelSeparator method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetLevelSeparator() {
        logger.info("getLevelSeparator + setLevelSeparator");
        assertNotNull(model.getLevelSeparator());
        assertEquals(".", model.getLevelSeparator());

        model.setLevelSeparator("-");
        assertNotNull(model.getXMLEntryPoint());
        assertEquals("-", model.getLevelSeparator());

        model.put("one-oo", "erster eintrag");
        assertEquals("erster eintrag", model.get("one-oo"));

        model.put("one-oo", "ersten eintrag überschreiben");
        assertEquals("ersten eintrag überschreiben", model.get("one-oo"));

        model.put("a-b-c-d-e-f-g", "ebene hinzuzufügen");

        assertEquals("ebene hinzuzufügen", model.get("a-b-c-d-e-f-g"));

        assertEquals(2, model.size());
    }

    /**
     * Test of getXMLFileHandle method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetXMLFileHandle() {
        logger.info("getXMLFileHandle");
        assertNotNull(model.getXMLFileHandle());

        File a = new File(testdatapath + "file.xml");
        model.setXMLFileHandle(a);

        assertEquals(a, model.getXMLFileHandle());

    }

    /**
     * Test of size method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testSize() {
        logger.info("size");

        assertEquals(0, model.size());

        model.put("a5", "a");
        model.put("b5", "b");
        model.put("c5", "c");

        int size = model.size();

        assertEquals(3, size);

        for (int i = 1; i <= 100; i++) {
            model.put("key" + i, "value" + i);
        }
        assertEquals(103, model.size());

        model.clear();

        //Ausgangsposition (leeres Model)
        assertEquals(0, model.size());


        model.put("one.key10", "irgendwas");
        model.put("one.key1", "irgendwas anderes");
        assertEquals(2, model.size());

    }

    /**
     * Test of isEmpty method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testIsEmpty() {
        logger.info("isEmpty");
        assertEquals(true, model.isEmpty());
        model.put("c5", "c");
        assertEquals(false, model.isEmpty());
        model.clear();
        assertEquals(true, model.isEmpty());

    }

    /**
     * Test of containsKey method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testContainsKey() throws XMLMapException {
        logger.info("containsKey");

        model.put("1.two.three", "100");
        model.put("2.two.three", "100");
        model.put("one.two.three.1", "100");
        model.put("one.two.three.2", "100");


        assertEquals(true, model.containsKey("one.two.three.1"));
        assertEquals(false, model.containsKey("6.5.three.1"));
    }

    /**
     * Test of containsValue method, of class com.tcmj.tools.xml.model.XMLMap.
     *
    //        XMLMap.XMLEntry a  = null;
    //                a = new XMLMap.XMLEntry("key1","value1"){};
    //
    //        XMLMap.XMLEntry b = new XMLEntry(null,"value1");
    //
    //        logger.info("equals "+a.equals(b));
    //
    //        logger.info("equals "+a.equals("value1"));
     */
    @Test
    public void testContainsValue() {
        logger.info("containsValue");
        model.put("1.two.three", "1");
        model.put("2.two.three", "2");
        model.put("one.two.three.1", "3");
        model.put("one.two.three.2", "4");


        assertEquals(true, model.containsValue("3"));
        assertEquals(false, model.containsValue("43543"));
        assertEquals(true, model.containsValue("1"));
        assertEquals(false, model.containsValue("5"));
        assertEquals(true, model.containsValue("4"));
        assertEquals(false, model.containsValue("one.two.three.2"));
    }

    /**
     * Test of keySet method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testKeySet() {
        logger.info("keySet");
        model.put("mykey1", "myvalue1");

        Set<String> result = model.keySet();
        assertNotNull(result);

    }

    /**
     * Test of values method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testValues() {
        logger.info("values");

        Collection<String> result = model.values();

        assertNotNull(result);

    }

    /**
     * Test of entrySet method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testEntrySet() {
        logger.info("entrySet");

        Collection entries = model.entrySet();

        assertNotNull(entries);
    }

    /**
     * Test of putAll method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutAll() {
        logger.info("putAll");

        Map map = new HashMap();

        map.put("mykey1", "myvalue1");
        map.put("mykey2", "myvalue2");
        map.put("a.b.c", "myvalue3");

        model.putAll(map);
        assertEquals(3, model.size());
    }

    /**
     * Test of testSetPropertiesToNull class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testSetPropertiesToNull() {

        assertEquals(0, model.size());
        logger.info("\t1");
        model.put("ebene.uno", "Hallo");
        model.put("mwst", "15%");
        model.put("datum", "xy", new java.util.Date());

        logger.info(model.showDataEntries(true, true));
        assertEquals(3, model.size());

        logger.info("\t2");
        assertEquals("Hallo", model.get("ebene.uno"));
        assertEquals("15%", model.get("mwst"));
        assertEquals("xy", model.get("datum"));
        assertNotNull(model.getObject("datum"));
        logger.info(model.showDataEntries(true, true));
        logger.info("\t3");
        model.put("ebene.uno", null);
        model.put("mwst", null);
        model.put("datum", null, null);

        logger.info("\t4");
        assertNull("ebene.uno value sollte null liefern und nicht " + model.get("ebene.uno"), model.get("ebene.uno"));
        assertNull("mwst value sollte null liefern und nicht " + model.get("mwst"), model.get("mwst"));

        assertNull("datum value sollte null liefern und nicht " + model.get("datum"), model.get("datum"));
        assertNull("datum object sollte null liefern und nicht " + model.getObject("datum"), model.getObject("datum"));

        assertEquals(3, model.size());

        logger.info("\t5");
        model.putObject("mwst", new java.util.HashSet());

        assertNotNull("mwst soll nicht null sein!", model.getObject("mwst"));

        logger.info(model.showDataEntries(true, true));

    }

    /**
     * Test of put method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test(expected = XMLMap.XMLMapException.class)
    public void testSonderfall1() {
        model.put("one.two", "yet ok");
        model.put("one.two.three", "not allowed");
        fail("not allowed to put 'one.two.three' after 'one.two'");
    }

    /**
     * Test of put, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test(expected = XMLMap.XMLMapException.class)
    public void testSonderfall2() {
        model.put("one.two.three", "yet ok");
        model.put("one.two", "not allowed");
        fail("not allowed to put 'one.two' after 'one.two.three'");
    }

    /**
     * Test of testSonderfall2Ausnahmen, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testSonderfall2Ausnahmen() {


        model.put("key1", "irgendwas1");
        model.put("key10", "irgendwas2");
        model.put("key100", "irgendwas3");

        model.put("key1", "irgendwas4");
        model.put("key10", "irgendwas5");
        model.put("key100", "irgendwas6");

        model.put("one.key1", "irgendwas7");
        model.put("one.key10", "irgendwas8");
        model.put("one.key100", "irgendwas9");

        model.put("one.key1", "irgendwas10");
        model.put("one.key10", "irgendwas11");
        model.put("one.key100", "irgendwas12");

        assertEquals(6, model.size());
    }

    @Test
    public void testMap() throws Exception {
        Map map = new XMLMap();

        map.put("key1", "irgendwas1");
        map.put("key10", "irgendwas2");
        map.put("key100", "irgendwas3");

        map.put("key1", "irgendwas4");
        map.put("key10", "irgendwas5");
        map.put("key100", "irgendwas6");

        map.put("one.key1", "irgendwas7");
        map.put("one.key10", "irgendwas8");
        map.put("one.key100", "irgendwas9");

        map.put("one.key1", "irgendwas10");
        map.put("one.key10", "irgendwas11");
        map.put("one.key100", "irgendwas12");

        assertEquals(6, map.size());


        assertEquals("irgendwas12", map.get("one.key100"));






    }

    @Test
    public void testMultiNodes() throws Exception {

        XMLMap map = new XMLMap(new File(testdatapath + "filename.xml"));

        map.readXML();

        logger.info(map.showDataEntries(false, true));

        String[] lst = map.getListValue("o.boo.koo");

        logger.info("lst.length = " + lst.length);
        logger.info("lst.0 = " + lst[0]);


        map.saveXML();



        assertEquals(3, lst.length);
    }

    /**
     * Test of LevelSeparators.
     */
    @Test
    public void testLevelSeparators() throws Exception {

        File file = new File(testdatapath + "ram.xml");

        String[] cases = new String[]{
            ".", "-", "\\", ",", "*", "_", ":", "@", "/", "|", "=", "?",
            "...", "x", "-|-", "||", "+"};

        for (int i = 0; i < cases.length; i++) {
            if (file.exists()) {
                file.delete();
            }
            String sep = cases[i];
            logger.info("test separator: " + sep);


            XMLMap xmap = new XMLMap(file);

            xmap.setLevelSeparator(sep);

            xmap.put("a" + sep + "b" + sep + "one", "xmap");

            assertEquals(1, xmap.size());

            xmap.put("a" + sep + "b" + sep + "two", "xmap");

            assertEquals(2, xmap.size());

            xmap.saveXML();


            xmap.readXML();

            logger.info(xmap.showDataEntries(false, true));


            assertEquals(2, xmap.size());


        }
    }

    /**
     * Test of LevelSeparators.
     */
    @Test
    public void testGetAttribute() throws Exception {

        File file = new File(testdatapath + "testGetAttribute.xml");

        XMLMap xmap = new XMLMap(file);
        xmap.readXML();

//        xmap.put("level1", "level1");
//        xmap.put("level2.level2", "level2");
//        xmap.put("level3.level3.level3A", "level3");
//        xmap.put("level3.level3.level3B", "level3");
//        xmap.put("level3.level3.level3C", "level3");
//        xmap.saveXML();

        logger.info(xmap.showDataEntries(false, true));


        xmap.getAttribute("level1", "myat");

        assertEquals("value1", xmap.get("level1"));
        assertEquals("12", xmap.getAttribute("level1", "myat"));
        assertEquals("435", xmap.getAttribute("level1", "myotherat"));


        assertEquals("2", xmap.getAttribute("level2.childlevel2", "level"));
        assertEquals("eins", xmap.getAttribute("level3.level3.level3A", "abc"));

        File destfile = new File(testdatapath + "testGetAttributeOut.xml");

        xmap.setXMLFileHandle(destfile);
        xmap.saveXML();


    }

    /**
     * Test of SetAttribute.
     */
    @Test
    public void testSetAttribute() throws Exception {

        File file = new File(testdatapath + "testSetAttribute.xml");

        XMLMap xmap = new XMLMap(file);


        xmap.put("level1", "valuexy");
        xmap.put("level2.level2", "value999");
//        xmap.put("level3.level3.level3A", "level3");
//        xmap.put("level3.level3.level3B", "level3");
//        xmap.put("level3.level3.level3C", "level3");

        xmap.setAttribute("level1", "myat", "435");
        xmap.setAttribute("level1", "myotherat", "888");

        xmap.setAttribute("level2.level2", "type", "ProjectCode");
        logger.info(xmap.showDataEntries(false, true));
        xmap.saveXML();
        xmap.readXML();
        logger.info(xmap.showDataEntries(false, true));


        assertEquals("435", xmap.getAttribute("level1", "myat"));
        assertEquals("888", xmap.getAttribute("level1", "myotherat"));
        assertEquals("ProjectCode", xmap.getAttribute("level2.level2", "type"));




    }

    @Test
    public void testConstructor() {

        XMLMap xmap = new XMLMap();

        File xfile = xmap.getXMLFileHandle();

        logger.info(xfile.getName());

        assertEquals("XMLMap.xml", xfile.getName());

    }
}//eof