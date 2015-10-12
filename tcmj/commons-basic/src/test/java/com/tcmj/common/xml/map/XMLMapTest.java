/*
 * XMLMapTest.java
 * JUnit based test
 *
 * Created on 19. April 2007, 23:27
 */
package com.tcmj.common.xml.map;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.tcmj.common.xml.map.intern.XMLMapException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * JUnit Test for class {@link com.tcmj.common.xml.map.XMLMap}
 * <b>In-Memory test cases</b>
 * @author tcmj - Thomas Deutsch
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class XMLMapTest {

    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(XMLMapTest.class);

    /** Model. */
    private XMLMap xmlMap;

    @Before
    public void beforeEachTest() {
        LOG.info(StringUtils.repeat('-', 130));
        xmlMap = new XMLMap();
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
        assertThat("Expect default file handle", xmlMap.getXMLFileHandle(), notNullValue());

        File anyFile = new File("memoryfile.xml");
        xmlMap.setXMLFileHandle(anyFile);
        assertThat("Expect the handle set", xmlMap.getXMLFileHandle(), equalTo(anyFile));
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


    @Test
    public void testConstructorEmpty() {
        LOG.info("testConstructorEmpty");
        XMLMap xmap = new XMLMap();
        assertThat("FileHandle may not be null", xmap.getXMLFileHandle(), notNullValue());
        assertThat("FileHandle Name", xmap.getXMLFileHandle().getName(), equalTo("XMLMap.xml"));
    }

    @Test
    public void testConstructorString() {
        LOG.info("testConstructorString");
        XMLMap xmap = new XMLMap("FooBar.xml");
        assertThat("FileHandle may not be null", xmap.getXMLFileHandle(), notNullValue());
        assertThat("FileHandle Name", xmap.getXMLFileHandle().getName(), equalTo("FooBar.xml"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorStringNull() {
        LOG.info("testConstructorStringNull");
        String fileName = null;
        new XMLMap(fileName);
    }

    @Test
    public void testConstructorFile() {
        LOG.info("testConstructorFile");
        File file = new File("FooBar.xml");
        XMLMap xmap = new XMLMap(file);
        assertThat("FileHandle may not be null", xmap.getXMLFileHandle(), notNullValue());
        assertThat("FileHandle Name", xmap.getXMLFileHandle().getName(), equalTo("FooBar.xml"));
        assertThat("FileHandle instance", xmap.getXMLFileHandle(), is(file));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorFileNull() {
        LOG.info("testConstructorFileNull");
        File file = null;
        new XMLMap(file);
    }

    @Test
    public void testComments() throws Exception {
        LOG.info("testComments");
        //given
        XMLMap map = new XMLMap();
        String key = "one.two.three";
        String comment = "This is the comment for one.two.three: ";
        map.put(key, "theValue!");
        //initially no comment was set so we want a null value
        assertThat("No comment returns null", map.getComment(key), nullValue());
        //when we set a comment
        map.setComment(key, comment);
        //we expect the same comment in return
        assertThat("getComment", map.getComment(key), equalTo(comment));
        //deletion of the comment by setting null
        map.setComment(key, null);
        assertThat("No comment returns null", map.getComment(key), nullValue());

        //finally we want a exception if we work on a non existant key
        try {
            map.setComment("no.existant.key", null);
            fail("No exception thrown! XMLMapException expected!");
        }catch (XMLMapException mex){
            assertThat("Exception", mex.getMessage(), equalTo("Entry not found for key: no.existant.key"));
        }


    }

    @Test
    public void testAttributes() throws Exception {
        LOG.info("testAttributes");
        //given ..single type entry
        XMLMap map = new XMLMap();
        String key = "cars.mitsubishi";
        String attributeKey = "color";
        String attributeValue = "black";
        map.put(key, "SpaceStar");

        assertThat("No attribute returns null", map.getAttribute(key, attributeKey), nullValue());

        //when we set the attribute
        map.setAttribute(key, attributeKey, attributeValue);
        //we expect to get the same
        assertThat("getAttribute", map.getAttribute(key, attributeKey), equalTo(attributeValue));
        //deletion of the attribute by setting null
        map.setAttribute(key, attributeKey, null);
        assertThat("No attribute returns null", map.getAttribute(key, attributeKey), nullValue());

        //finally we want a exception if we work on a non existant key
        try {
            map.setAttribute("no.existant.key", "bla", "blub");
            fail("No exception thrown! XMLMapException expected!");
        }catch (XMLMapException mex){
            assertThat("Exception", mex.getMessage(), equalTo("Entry not found for key: no.existant.key"));
        }
    }

    @Test
    public void testAttributesOnListValues() throws Exception {
        LOG.info("testAttributesOnListValues");
        //given ..multi type / list entry
        XMLMap map = new XMLMap();
        String listkey = "year.week.day";
        map.putListValue(listkey, new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        LOG.info("{}", map.showDataEntries(false, true));

        String attributeKey = "no";

        //initially we expect both methods to return null because of non existing attribute
        assertThat("getAttribute 1", map.getAttribute(listkey, attributeKey), nullValue());
        assertThat("getListAttribute 1", map.getListAttribute(listkey, attributeKey), nullValue());

        //when we set attributes on the list values we have to use same amount and order
        //todo implement setAttribute with index option
        //todo change this to putListAttributes (same like putListValues) !!!!!!!!!!!
        map.setAttribute(listkey, attributeKey, "1"); //Monday
        map.setAttribute(listkey, attributeKey, "2");
        map.setAttribute(listkey, attributeKey, "3");
        map.setAttribute(listkey, attributeKey, "4");
        map.setAttribute(listkey, attributeKey, "5"); //Friday

        LOG.info("{}", map.showDataEntries(false, true));

        //then...getAttribute returns null even on existing key and attributeName
        assertThat("getAttribute 2", map.getAttribute(listkey, attributeKey), nullValue());

        //then...getListAttribute returns the values we want to get
        String[] listAttributes = map.getListAttribute(listkey, attributeKey);

        assertThat("getAttribute", listAttributes[0], equalTo("1"));
        assertThat("getAttribute", listAttributes[1], equalTo("2"));
        assertThat("getAttribute", listAttributes[2], equalTo("3"));
        assertThat("getAttribute", listAttributes[3], equalTo("4"));
        assertThat("getAttribute", listAttributes[4], equalTo("5"));

        //we want a exception if we try to delete a attribute of a list value
        try {
            map.setAttribute(listkey, attributeKey, null);
            fail("No exception thrown! XMLMapException expected!");
        }catch (UnsupportedOperationException mex){
            assertThat("Exception", mex.getMessage(), equalTo("deletion on multi types not supported!"));
        }
    }
}
