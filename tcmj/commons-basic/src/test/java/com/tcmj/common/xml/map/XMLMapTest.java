/*
 * XMLMapTest.java
 * JUnit based test
 *
 * Created on 19. April 2007, 23:27
 */
package com.tcmj.common.xml.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import com.tcmj.common.xml.map.intern.XMLMapException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * JUnit Test for class {@link com.tcmj.common.xml.map.XMLMap}
 * @author tcmj - Thomas Deutsch
 */
public class XMLMapTest {

    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(XMLMap.class);

    /** Path to the xml test files (IN/OUT). */
    private static final String testdatapath = "src\\test\\resources\\com\\tcmj\\common\\xml\\map\\";

    /** delete test files after exiting this test. */
    private static final boolean deleteOnExit = false;

    /** Model. */
    private XMLMap xmlMap;

    /** File handle to an XML file. */
    private static File file;

    @BeforeClass
    public static final void initTestData() throws Exception {
        URL resource = XMLMapTest.class.getResource("XMLMapTest.xml");
        LOG.info("URL resource: {}", resource);
        file = new File(resource.toURI());
        LOG.info("Using file: {}", file);
    }

    @Before
    public void beforeEachTest() {
        LOG.info("-------------------------------------------------------------------------------------------------------");

        //2. New instance of XMLPropertyModels
        xmlMap = new XMLMap(file);
    }

    /**
     * Test of put method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutGet() {
        LOG.info("testPutGet");

        xmlMap.put("one", "1st");
        assertThat("Size has to be 1 after inserting one entry", xmlMap.size(), is(1));
        assertThat("Getting the previous put value has to be the same", xmlMap.get("one"), equalTo("1st"));

        String key = "two.of.a.kind";
        String value = "2nd";
        xmlMap.put(key, value);
        assertEquals(key, 2, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));

        key = "three.level2";
        value = "a b c d e f";
        xmlMap.put(key, value);
        assertEquals(key, 3, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));

        key = "four.big";
        value = "lower case";
        xmlMap.put(key, value);
        assertEquals(key, 4, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));

        key = "four.BIG";
        value = "upper case";
        xmlMap.put(key, value);
        assertEquals(key, 5, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));
        xmlMap.put(key, value);
        assertEquals(key, 5, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));

        key = "x1.x2.x3.x4.x5";
        value = "long";
        xmlMap.put(key, value);
        assertEquals(key, 6, xmlMap.size());
        assertEquals(key, value, xmlMap.get(key));

        key = "x1.x2.x3.x4.a";
        value = "value no ";
        for (int i = 1; i <= 25; i++) {
            xmlMap.put(key + i, value + i);
            assertEquals(key + i, value + i, xmlMap.get(key + i));
        }
        assertEquals(key, 6 + 25, xmlMap.size());
        for (int i = 1; i <= 25; i++) {
            assertEquals(key + i, value + i, xmlMap.get(key + i));
        }

        assertNull(xmlMap.get("Four.BIG"));
        assertNull(xmlMap.get("fOur.BIG"));
        assertNull(xmlMap.get("foUr.BIG"));
        assertNull(xmlMap.get("fouR.BIG"));

//        LOG.info(model.showDataEntries(false, true));
    }

    /**
     * Test of isSameLevel method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testIsSameLevel() {
        LOG.info("testIsSameLevel");
        assertEquals(true, xmlMap.isSameLevel("key1", "key1"));
        assertEquals(true, xmlMap.isSameLevel("key1", "key1"));
        assertEquals(true, xmlMap.isSameLevel("a.key1", "a.key1"));
        assertEquals(true, xmlMap.isSameLevel("a.ab", "a.cd"));
        assertEquals(true, xmlMap.isSameLevel("a.b.c.ab", "a.b.c.cd"));

        assertEquals(false, xmlMap.isSameLevel("a.eins", "b.zwei"));
        assertEquals(false, xmlMap.isSameLevel("a", "b.zwei"));
        assertEquals(false, xmlMap.isSameLevel("a.eins", "b"));
        assertEquals(false, xmlMap.isSameLevel("a.b.c.ab", "v.b.c.ab"));
        assertEquals(false, xmlMap.isSameLevel("a.a.a.a", "a.a.a.a.a"));

    }

    /**
     * Test of putObject method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutObject() {
        LOG.info("testPutObject");

        //--------------------- root key
        String key1 = "one";
        xmlMap.put(key1, "ohnevalue");

        assertNull(xmlMap.getObject(key1));

        xmlMap.putObject(key1, new java.util.Date());

        assertNotNull(xmlMap.getObject(key1));

        xmlMap.putObject(key1, null);

        assertNull(xmlMap.getObject(key1));

        //--------------------- complex key
        String key2 = "a.b.c.d";
        xmlMap.put(key2, "ohnevalue");

        assertNull(xmlMap.getObject(key2));

        java.util.Date specificdate = new java.util.Date();
        xmlMap.putObject(key2, specificdate);

        Object dategot = xmlMap.getObject(key2);
        assertNotNull(dategot);

        assertEquals("not the same object!", specificdate, dategot);

        xmlMap.putObject(key2, null);

        assertNull(xmlMap.getObject(key2));

        //--------------------- behavour of value
        String key3 = "x.aaa.zzz.dee";
        xmlMap.put(key3, "myvalue567");
        xmlMap.putObject(key3, new java.util.Date());

        assertEquals("value lost", "myvalue567", xmlMap.get(key3));

    }

    /**
     * Test of getListProperty method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutListValueGetListValue() {
        LOG.info("Testing putListValue(String, String[]) and getListValue()...");

        String key = "person.address.phone";
        xmlMap.putListValue(key, new String[]{"123", "456", "789"});

        String[] lsv = xmlMap.getListValue(key);

        assertThat("Amount of values", lsv.length, is(3));
        assertThat("Value 123 available", Arrays.asList(lsv).contains("123"), is(true));
        assertThat("Value 456 available", Arrays.asList(lsv).contains("456"), is(true));
        assertThat("Value 789 available", Arrays.asList(lsv).contains("789"), is(true));

        assertThat("Value 555 not available", Arrays.asList(lsv).contains("555"), is(false));
        assertThat("Value not available", xmlMap.getListValue("gibt.es.nicht"), nullValue());

        //case: immutability
        assertThat("immutability", xmlMap.getListValue(key)[0], equalTo("123"));
        xmlMap.getListValue(key)[0] = "change";
        assertThat("immutability", xmlMap.getListValue(key)[0], equalTo("123"));
    }

    /**
     * Test of getObject method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetObject() {
        LOG.info("testGetObject");
        java.util.Date ddd = new java.util.Date();
        xmlMap.put("datum", "xy", ddd);
        assertNotNull(xmlMap.getObject("datum"));
        assertEquals(ddd, xmlMap.getObject("datum"));
    }

    /**
     * Test of getObjectByValue method, of class
     * com.tcmj.tools.xml.model.XMLMap.
     */
    @Test(expected = XMLMapException.class)
    public void testGetObjectByValue() throws Exception {
        LOG.info("testGetObjectByValue");
        java.util.Date ddd1 = new java.util.Date();
        Thread.sleep(1000L);
        java.util.Date ddd2 = new java.util.Date();

        xmlMap.put("a.b.c", "xy", ddd1);
        xmlMap.put("x.y.a", "xy", ddd2);
        xmlMap.put("x.y.z", "123", ddd2);

        assertNotNull(xmlMap.getObjectByValue("123"));
        assertEquals(ddd2, xmlMap.getObjectByValue("123"));

        xmlMap.getObjectByValue("xy");
    }

    /**
     * Test of remove method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testRemove() throws XMLMapException {
        LOG.info("testRemove");
        int size = xmlMap.size();

        xmlMap.put("abcdef", "hello");

        assertEquals(size + 1, xmlMap.size());

        String val = xmlMap.remove("abcdef");
        assertEquals("hello", val);
        assertEquals(size, xmlMap.size());

        //-----------key not avail
        assertNull(xmlMap.remove("key.not.available"));
        assertEquals(size, xmlMap.size());
    }

    /**
     * Test of readXML method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testReadXMLSaveXML() throws FileNotFoundException, TransformerConfigurationException, UnsupportedEncodingException, TransformerException, IOException, XMLMapException, ParserConfigurationException {
        LOG.info("testReadXMLSaveXML");
        int size = xmlMap.size();
        LOG.info("size: " + size);

        xmlMap.saveXML();

        xmlMap.readXML();

        int size2 = xmlMap.size();

        assertEquals(size, size2);

        xmlMap.put("a", "a");
        xmlMap.put("b", "b");
        xmlMap.put("c", "c");
        xmlMap.put("x.y.z", "myvalueXYZ");
        xmlMap.saveXML();
        xmlMap.readXML();

        size2 = xmlMap.size();

        assertEquals(4, size2);
    }

    /**
     * Test of clear method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testClear() {
        LOG.info("clear");

        xmlMap.put("a", "a");
        xmlMap.put("b", "b");
        xmlMap.put("c", "c");

        assertTrue((xmlMap.size() > 0));

        xmlMap.clear();

        assertTrue((xmlMap.size() == 0));
    }

    /**
     * Test of showDataEntries method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testShowDataEntries() {
        LOG.info("showDataEntries");

        //EmptyModel
        xmlMap.clear();
        assertNotNull(xmlMap.showDataEntries(true, true));
        assertNotNull(xmlMap.showDataEntries(true, false));
        assertNotNull(xmlMap.showDataEntries(false, true));
        assertNotNull(xmlMap.showDataEntries(false, false));

        //Model with Data
        xmlMap.put("montag.abend", "Essen ohne Java");
        xmlMap.put("montag.mittag", "Essen mit Java", new java.util.Date());
        xmlMap.setAttribute("montag.abend", "tag", "montag");
        assertNotNull(xmlMap.showDataEntries(true, true));
        assertNotNull(xmlMap.showDataEntries(true, false));
        assertNotNull(xmlMap.showDataEntries(false, true));
        assertNotNull(xmlMap.showDataEntries(false, false));

    }

    /**
     * Test of getXMLRootNodeName method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testGetXMLRootNodeName() {
        LOG.info("testGetXMLRootNodeName");
        assertNotNull(xmlMap.getXMLRootNodeName());
        assertEquals("tcmj", xmlMap.getXMLRootNodeName());

        xmlMap.setXMLRootNodeName("heinz");
        assertNotNull(xmlMap.getXMLRootNodeName());
        assertEquals("heinz", xmlMap.getXMLRootNodeName());

        xmlMap.setXMLRootNodeName("abcdefg");

        assertEquals("abcdefg", xmlMap.getXMLRootNodeName());
    }

    /**
     * Test of getXMLEntryPoint method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testGetChildnodename() {
        LOG.info("testGetChildnodename");
        assertNull(xmlMap.getXMLEntryPoint());

        xmlMap.setXMLEntryPoint("heinz");
        assertNotNull(xmlMap.getXMLEntryPoint());
        assertEquals("heinz", xmlMap.getXMLEntryPoint());
    }

    /**
     * Test of getLevelSeparator method, of class
     * com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetLevelSeparator() {
        LOG.info("getLevelSeparator + setLevelSeparator");
        assertNotNull(xmlMap.getLevelSeparator());
        assertEquals(".", xmlMap.getLevelSeparator());

        xmlMap.setLevelSeparator("-");
        assertNotNull(xmlMap.getLevelSeparator());
        assertEquals("-", xmlMap.getLevelSeparator());

        xmlMap.put("one-oo", "erster eintrag");
        assertEquals("erster eintrag", xmlMap.get("one-oo"));

        xmlMap.put("one-oo", "ersten eintrag ueberschreiben");
        assertEquals("ersten eintrag ueberschreiben", xmlMap.get("one-oo"));

        xmlMap.put("a-b-c-d-e-f-g", "ebene hinzuzufuegen");

        assertEquals("ebene hinzuzufuegen", xmlMap.get("a-b-c-d-e-f-g"));

        assertEquals(2, xmlMap.size());
    }

    /**
     * Test of getXMLFileHandle method, of class
     * com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testGetXMLFileHandle() {
        LOG.info("getXMLFileHandle");
        assertNotNull(xmlMap.getXMLFileHandle());

        File a = new File(testdatapath + "file.xml");
        xmlMap.setXMLFileHandle(a);

        assertEquals(a, xmlMap.getXMLFileHandle());

    }

    /**
     * Test of size method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testSize() {
        LOG.info("size");

        assertEquals(0, xmlMap.size());

        xmlMap.put("a5", "a");
        xmlMap.put("b5", "b");
        xmlMap.put("c5", "c");

        int size = xmlMap.size();

        assertEquals(3, size);

        for (int i = 1; i <= 100; i++) {
            xmlMap.put("key" + i, "value" + i);
        }
        assertEquals(103, xmlMap.size());

        xmlMap.clear();

        //Ausgangsposition (leeres Model)
        assertEquals(0, xmlMap.size());

        xmlMap.put("one.key10", "irgendwas");
        xmlMap.put("one.key1", "irgendwas anderes");
        assertEquals(2, xmlMap.size());

    }

    /**
     * Test of isEmpty method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testIsEmpty() {
        LOG.info("isEmpty");
        assertEquals(true, xmlMap.isEmpty());
        xmlMap.put("c5", "c");
        assertEquals(false, xmlMap.isEmpty());
        xmlMap.clear();
        assertEquals(true, xmlMap.isEmpty());

    }

    /**
     * Test of containsKey method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testContainsKey() throws XMLMapException {
        LOG.info("containsKey");

        xmlMap.put("1.two.three", "100");
        xmlMap.put("2.two.three", "100");
        xmlMap.put("one.two.three.1", "100");
        xmlMap.put("one.two.three.2", "100");

        assertEquals(true, xmlMap.containsKey("one.two.three.1"));
        assertEquals(false, xmlMap.containsKey("6.5.three.1"));
    }

    /**
     * Test of containsValue method, of class com.tcmj.tools.xml.model.XMLMap.
     * <p>
     * // XMLMap.XMLEntry a = null; // a = new
     * XMLMap.XMLEntry("key1","value1"){}; // // XMLMap.XMLEntry b = new
     * XMLEntry(null,"value1"); // // logger.info("equals "+a.equals(b)); // //
     * logger.info("equals "+a.equals("value1"));
     */
    @Test
    public void testContainsValue() {
        LOG.info("containsValue");
        xmlMap.put("1.two.three", "1");
        xmlMap.put("2.two.three", "2");
        xmlMap.put("one.two.three.1", "3");
        xmlMap.put("one.two.three.2", "4");

        assertEquals(true, xmlMap.containsValue("3"));
        assertEquals(false, xmlMap.containsValue("43543"));
        assertEquals(true, xmlMap.containsValue("1"));
        assertEquals(false, xmlMap.containsValue("5"));
        assertEquals(true, xmlMap.containsValue("4"));
        assertEquals(false, xmlMap.containsValue("one.two.three.2"));
    }

    /**
     * Test of keySet method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testKeySet() {
        LOG.info("keySet");
        xmlMap.put("mykey1", "myvalue1");

        Set<String> result = xmlMap.keySet();
        assertNotNull(result);

    }

    /**
     * Test of values method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testValues() {
        LOG.info("values");

        Collection<String> result = xmlMap.values();

        assertNotNull(result);

    }

    /**
     * Test of entrySet method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testEntrySet() {
        LOG.info("entrySet");

        Collection<Entry<String, String>> entries = xmlMap.entrySet();

        assertNotNull(entries);
    }

    /**
     * Test of putAll method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testPutAll() {
        LOG.info("putAll");

        Map<String, String> map = new HashMap<String, String>();

        map.put("mykey1", "myvalue1");
        map.put("mykey2", "myvalue2");
        map.put("a.b.c", "myvalue3");

        xmlMap.putAll(map);
        assertEquals(3, xmlMap.size());
    }

    /**
     * Test of testSetPropertiesToNull class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testSetPropertiesToNull() {
        LOG.info("testSetPropertiesToNull");

        assertEquals(0, xmlMap.size());
        LOG.info("\t1");
        xmlMap.put("ebene.uno", "Hallo");
        xmlMap.put("mwst", "15%");
        xmlMap.put("datum", "xy", new java.util.Date());

        assertEquals(3, xmlMap.size());

        LOG.info("\t2");
        assertEquals("Hallo", xmlMap.get("ebene.uno"));
        assertEquals("15%", xmlMap.get("mwst"));
        assertEquals("xy", xmlMap.get("datum"));
        assertNotNull(xmlMap.getObject("datum"));

        LOG.info("\t3");
        xmlMap.put("ebene.uno", null);
        xmlMap.put("mwst", null);
        xmlMap.put("datum", null, null);

        LOG.info("\t4");
        assertNull("ebene.uno value sollte null liefern und nicht " + xmlMap.get("ebene.uno"), xmlMap.get("ebene.uno"));
        assertNull("mwst value sollte null liefern und nicht " + xmlMap.get("mwst"), xmlMap.get("mwst"));

        assertNull("datum value sollte null liefern und nicht " + xmlMap.get("datum"), xmlMap.get("datum"));
        assertNull("datum object sollte null liefern und nicht " + xmlMap.getObject("datum"), xmlMap.getObject("datum"));

        assertEquals(3, xmlMap.size());

        LOG.info("\t5");
        xmlMap.putObject("mwst", new java.util.Date());

        assertNotNull("mwst soll nicht null sein!", xmlMap.getObject("mwst"));

    }

    /**
     * Test of put method, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test(expected = XMLMapException.class)
    public void testMixingLevels1() {
        LOG.info("testMixingLevels1");
        xmlMap.put("one.two", "yet ok");
        xmlMap.put("one.two.three", "not allowed");
        fail("not allowed to put 'one.two.three' after 'one.two'");
    }

    /**
     * Test of put, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test(expected = XMLMapException.class)
    public void testMixingLevels2() {
        LOG.info("testMixingLevels2");
        xmlMap.put("one.two.three", "yet ok");
        xmlMap.put("one.two", "not allowed");
        fail("not allowed to put 'one.two' after 'one.two.three'");
    }

    /**
     * Test of testSpecial1, of class tcmj.panels.xmlprop.XMLMap.
     */
    @Test
    public void testSpecial1() {
        LOG.info("testSpecial1");

        xmlMap.put("key1", "irgendwas1");
        xmlMap.put("key10", "irgendwas2");
        xmlMap.put("key100", "irgendwas3");

        xmlMap.put("key1", "irgendwas4");
        xmlMap.put("key10", "irgendwas5");
        xmlMap.put("key100", "irgendwas6");

        xmlMap.put("one.key1", "irgendwas7");
        xmlMap.put("one.key10", "irgendwas8");
        xmlMap.put("one.key100", "irgendwas9");

        xmlMap.put("one.key1", "irgendwas10");
        xmlMap.put("one.key10", "irgendwas11");
        xmlMap.put("one.key100", "irgendwas12");

        assertEquals(6, xmlMap.size());
    }

    @Test
    public void testMap() throws Exception {
        LOG.info("testMap");

        Map<String, String> map = new XMLMap();

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

    /**
     * Test of LevelSeparators.
     */
    @Test
    public void testLevelSeparators() throws Exception {
        LOG.info("testLevelSeparators");
        File testfile = new File(testdatapath, "XMLMapTest_testLevelSeparators_TMP.xml");
        initFile(testfile);
        String[] cases = new String[]{
                ".", "-", "\\", ",", "*", "_", "ooo", "@", "/", "|", "=", "?",
                "...", "x", "-|-", "||", "+"};
        LOG.info(" Separators to Test: {}", Arrays.asList(cases));

        for (int i = 0; i < cases.length; i++) {
            if (testfile.exists()) {
                testfile.delete();
            }
            String sep = cases[i];
            String separatormsg = "Separator[" + sep + "]";

            XMLMap xmap = new XMLMap(testfile);
            xmap.setLevelSeparator(sep);

            xmap.put("a" + sep + "b" + sep + "one", "xmap");

            assertEquals(separatormsg, 1, xmap.size());

            xmap.put("a" + sep + "b" + sep + "two", "xmap");

            assertEquals(separatormsg, 2, xmap.size());

            xmap.saveXML();
            xmap.readXML();

            assertEquals(separatormsg, 2, xmap.size());

        }
    }

    /**
     * Test of LevelSeparators.
     */
    @Test
    public void testGetAttribute() throws Exception {
        LOG.info("testGetAttribute");

        File testfile = new File(testdatapath, "XMLMapTest_testGetAttribute.xml");

        XMLMap xmap = new XMLMap(testfile);
        xmap.setXMLEntryPoint("xmlmap");
        xmap.readXML();

//        xmap.put("level1", "level1");
//        xmap.put("level2.level2", "level2");
//        xmap.put("level3.level3.level3A", "level3");
//        xmap.put("level3.level3.level3B", "level3");
//        xmap.put("level3.level3.level3C", "level3");
//        xmap.saveXML();
//        logger.info(xmap.showDataEntries(false, true));
        xmap.getAttribute("level1", "myat");

        assertEquals("value1", xmap.get("level1"));
        assertEquals("12", xmap.getAttribute("level1", "myat"));
        assertEquals("435", xmap.getAttribute("level1", "myotherat"));

        assertEquals("2", xmap.getAttribute("level2.childlevel2", "level"));
        assertEquals("eins", xmap.getAttribute("level3.level3.level3A", "abc"));

        File destfile = new File(testdatapath, "XMLMapTest_testGetAttributeOut_TMP.xml");
        initFile(destfile);
        xmap.setXMLFileHandle(destfile);
        xmap.saveXML();

    }

    /**
     * Test of SetAttribute.
     */
    @Test
    public void testSetAttribute() throws Exception {
        LOG.info("testSetAttribute");

        File testfile = new File(testdatapath, "XMLMapTest_testSetAttribute_TMP.xml");
        initFile(testfile);
        XMLMap xmap = new XMLMap(testfile);

        xmap.put("level1", "valuexy");
        xmap.put("level2.level2", "value999");
//        xmap.put("level3.level3.level3A", "level3");
//        xmap.put("level3.level3.level3B", "level3");
//        xmap.put("level3.level3.level3C", "level3");

        xmap.setAttribute("level1", "myat", "435");
        xmap.setAttribute("level1", "myotherat", "888");

        xmap.setAttribute("level2.level2", "type", "ProjectCode");
        xmap.saveXML();
        xmap.readXML();

        assertEquals("435", xmap.getAttribute("level1", "myat"));
        assertEquals("888", xmap.getAttribute("level1", "myotherat"));
        assertEquals("ProjectCode", xmap.getAttribute("level2.level2", "type"));

    }

    @Test
    public void testConstructor() {
        LOG.info("testConstructor");
        XMLMap xmap = new XMLMap();
        File xfile = xmap.getXMLFileHandle();
        LOG.info(xfile.getName());
        assertEquals("XMLMap.xml", xfile.getName());
    }

    /**
     * helper method to set that the generated xml-output-files should be
     * deleted after jvm exit.
     */
    private static final void initFile(File file) {
        if (deleteOnExit) {
            file.deleteOnExit();
        }
    }

    @Test
    public void testXMLEntrypointReading() throws Exception {
        LOG.info("testXMLEntrypoint");

        //Create Test-XML-File:
        File myfile = new File(testdatapath, "XMLMapTest_testXMLEntrypoint_TMP.xml");
        if (myfile.exists()) {
            myfile.delete();
        }
        initFile(myfile);

        XMLMap xmap = new XMLMap(myfile);
        xmap.put("anderer.pre", "dieser Eintrag darf nicht beruerhrt werden!");
        xmap.put("one.two.three.four.five", "4711");
        xmap.put("irgendwas.post", "dieser Eintrag darf nicht beruerhrt werden!");
        xmap.saveXML();

        //1. No Entrypoint
        xmap.readXML();
        LOG.info(xmap.showDataEntries(false, false));
        assertEquals("1", "4711", xmap.get("one.two.three.four.five"));

        //2. Entrypoint = one
        xmap.setXMLEntryPoint("one");
        xmap.readXML();
        assertEquals("2", "4711", xmap.get("two.three.four.five"));

        //3. Entrypoint = one
        xmap.setXMLEntryPoint("one.two");
        xmap.readXML();
        assertEquals("3", "4711", xmap.get("three.four.five"));

        //4. Entrypoint = one
        xmap.setXMLEntryPoint("one.two.three");
        xmap.readXML();
        assertEquals("4", "4711", xmap.get("four.five"));

        //5. Entrypoint = one
        xmap.setXMLEntryPoint("one.two.three.four");
        xmap.readXML();
        assertEquals("5", "4711", xmap.get("five"));

    }

}
