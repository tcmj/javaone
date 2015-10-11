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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import com.tcmj.common.xml.map.intern.XMLMapException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * JUnit Test for class {@link XMLMap}
 * There are test xml files in the folder: 'com.tcmj.common.xml.map.in' which will be read.
 * The output files will be stored in: 'com.tcmj.common.xml.map.out' which can/will be deleted before testing
 * @author tcmj - Thomas Deutsch
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class XMLMapReadSaveTest {

    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(XMLMapReadSaveTest.class);

    /** Path to the input xml test files. */
    private static File inputFilePath;

    /** Path to the output xml test files. */
    private static File outputFilePath;

    /** delete test files after exiting this test. */
    private static final boolean deleteOnExit = false;

    /** Model. */
    private XMLMap xmlMap;


    @BeforeClass
    public static final void initTestDataOnceForAllTests() throws Exception {
        URL resource = XMLMapReadSaveTest.class.getResource("in/xmlmaptest01.xml");

        inputFilePath = new File(resource.toURI()).getParentFile();
        LOG.info("inputFilePath = '{}'", inputFilePath);
        inputFilePath.setReadOnly();

        outputFilePath = new File(inputFilePath.getParentFile(), "out");
        LOG.info("outputFilePath = '{}'", outputFilePath);

        if (outputFilePath.listFiles() != null) {
            for (File file : outputFilePath.listFiles()) {
                try {
                    LOG.info("...deleting file = '{}'", file);
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Before
    public void beforeEachTest() {
        LOG.info(StringUtils.repeat('-', 130));
        xmlMap = new XMLMap();
    }

    /**
     * Test of readXML method, of class com.tcmj.tools.xml.model.XMLMap.
     */
    @Test
    public void testReadXMLSaveXML() throws FileNotFoundException, TransformerConfigurationException, UnsupportedEncodingException, TransformerException, IOException, XMLMapException, ParserConfigurationException {
        LOG.info("testReadXMLSaveXML");
        int size = xmlMap.size();
        LOG.info("size: " + size);

        File outputFile = new File(outputFilePath, "out_xmlmapreadsavetest_testreadxmlsavexml.xml");
        xmlMap.setXMLFileHandle(outputFile);

        xmlMap.saveXML();

        xmlMap.readXML();

        int size2 = xmlMap.size();

        assertEquals(size, size2);

        xmlMap.put("a", "a");
        xmlMap.put("b", "b");
        xmlMap.put("c", "John");
        xmlMap.put("x.y.z", "myvalueXYZ");
        xmlMap.saveXML();
        xmlMap.readXML();

        size2 = xmlMap.size();

        assertEquals(4, size2);

        XMLMap map2 = new XMLMap(xmlMap.getXMLFileHandle());
        map2.readXML();
        assertThat("Looking for John", map2.get("c"), equalTo("John"));


    }

    /**
     * Test of LevelSeparators.
     */
    @Test
    public void testLevelSeparators() throws Exception {
        LOG.info("testLevelSeparators");

        String[] cases = new String[]{
                ".", "-", "\\", ",", "*", "_", "ooo", "@", "/", "|", "=", "?",
                "...", "x", "-|-", "||", "+"};
        LOG.info(" Separators to Test: {}", Arrays.asList(cases));

        for (int i = 0; i < cases.length; i++) {

            File outFile = new File(outputFilePath, "out_xmlmapreadsavetest_testlevelseparators_" + i + ".xml");

            String sep = cases[i];
            String separatormsg = "Separator[" + sep + "]";

            XMLMap xmap = new XMLMap(outFile);
            xmap.setLevelSeparator(sep);

            xmap.put("a" + sep + "b" + sep + "one", "xmap");

            assertEquals(separatormsg, 1, xmap.size());

            xmap.put("a" + sep + "b" + sep + "two", "xmap");

            assertEquals(separatormsg, 2, xmap.size());

            xmap.saveXML();
            xmap.readXML();

            assertEquals(separatormsg, 2, xmap.size());
            LOG.info(" Separator: {}", xmap.showDataEntries(true,true));

        }
    }

    @Test
    public void testGetAttribute() throws Exception {
        LOG.info("testGetAttribute");

        File inputFile = new File(inputFilePath, "xmlmapreadsavetest_testgetattribute.xml");

        XMLMap xmap = new XMLMap(inputFile);
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

        //todo create own comment test
        assertThat("Comment available", xmap.getComment("level2.childlevel2"), equalTo("Level 2.2 with attributes and value "));


        //save...
        File destfile = new File(outputFilePath, "out_xmlmapreadsavetest_testgetattribute.xml");
        xmap.setXMLFileHandle(destfile);
        xmap.saveXML();

    }

    @Test
    public void testSetAttribute() throws Exception {
        LOG.info("testSetAttribute");


        File destfile = new File(outputFilePath, "out_xmlmapreadsavetest_testsetattribute.xml");
        XMLMap xmap = new XMLMap(destfile);

        xmap.put("level1", "valuexy");
        xmap.put("level2.level2", "value999");
//        xmap.put("level3.level3.level3A", "level3");
//        xmap.put("level3.level3.level3B", "level3");
//        xmap.put("level3.level3.level3C", "level3");

        xmap.setAttribute("level1", "myat", "435");
        xmap.setAttribute("level1", "myotherat", "888");

        xmap.setAttribute("level2.level2", "type", "ProjectCode");
        xmap.saveXML();

        XMLMap xmapRead = new XMLMap(xmap.getXMLFileHandle());
        xmapRead.readXML();

        assertEquals("435", xmapRead.getAttribute("level1", "myat"));
        assertEquals("888", xmapRead.getAttribute("level1", "myotherat"));
        assertEquals("ProjectCode", xmapRead.getAttribute("level2.level2", "type"));

    }

    @Test
    public void testXMLEntrypointReading() throws Exception {
        LOG.info("testXMLEntrypoint");

        //Create Test-XML-File:
        File destfile = new File(outputFilePath, "out_xmlmapreadsavetest_testxmlentrypointreading.xml");


        XMLMap xmap = new XMLMap(destfile);
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
