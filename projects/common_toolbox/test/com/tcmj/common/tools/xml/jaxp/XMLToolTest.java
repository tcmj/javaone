/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.common.tools.xml.jaxp;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author tcmj
 */
public class XMLToolTest {

    public XMLToolTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of selectNode method, of class XMLTool.
     */
    @Test
    public void testSearchNode() throws Exception {
        System.out.println("searchNode");


        String xmlstring = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<i:tcmj xmlns:i=\"urn:com.tcmj:xmlmap\"> " +
                "    <darth>anotherdarth</darth> " +
                "<i:darth> " +
                "    <i:one> " +
                "      <i:two>anything</i:two> " +
                "    </i:one> " +
                "  </i:darth> " +
                "</i:tcmj>";


        StringReader sreader = new StringReader(xmlstring);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new InputSource(sreader));
        assertNotNull("xml document is null", document);


        String xpathexp = "/i:tcmj/i:darth/i:one/i:two";
        String prefix = "i";
        String expResult = "anything";
        Node result = XMLTool.selectNode(document, xpathexp, prefix);
        System.out.println("" + result);
        assertNotNull("xpath result is null", result);
        System.out.println("" + result.getTextContent());
        assertEquals(expResult, result.getTextContent());
    }

    @Test
    public void testSearchNode2() throws Exception {
        System.out.println("searchNode");


        String xmlstring = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<catalog xmlns:journal=\"http://www.w3.org/2001/XMLSchema-Instance\" >  " +
                "  <journal:journal title=\"XML\"  publisher=\"IBM developerWorks\">  " +
                "      <article journal:level=\"Intermediate\" " +
                "            date=\"February-2003\"> " +
                "         <title>Design XML Schemas Using UML</title> " +
                "         <author>Ayesha Malik</author> " +
                "      </article> " +
                "  </journal:journal> " +
                "  <journal title=\"Java Technology\"  publisher=\"IBM  developerWorks\"> " +
                "      <article level=\"Advanced\" date=\"January-2004\">  " +
                "          <title>Design service-oriented architecture frameworks with J2EE technology</title>  " +
                "          <author>Naveen Balani </author>   " +
                "      </article> " +
                "      <article level=\"Advanced\" date=\"October-2003\">    " +
                "          <title>Advance DAO Programming</title>  " +
                "          <author>Sean Sullivan </author>   " +
                "      </article> " +
                "  </journal>  " +
                "</catalog>";


        StringReader sreader = new StringReader(xmlstring);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new InputSource(sreader));
        assertNotNull("xml document is null", document);


        /* select a node in the default namespace */
        String xpathexp = "/catalog/journal/article[@date='January-2004']/title";
        String prefix = null;
        String expResult = "Design service-oriented architecture frameworks with J2EE technology";
        Node result = XMLTool.selectNode(document, xpathexp, prefix);
        System.out.println("" + result);
        assertNotNull("xpath result is null", result);
        System.out.println("" + result.getTextContent());
        assertEquals(expResult, result.getTextContent());

        /* select a node in the journal namespace */
        xpathexp = "/catalog/journal:journal/article[@date='February-2003']/title";
        prefix = "journal";
        expResult = "Design XML Schemas Using UML";
        result = XMLTool.selectNode(document, xpathexp, prefix);
        System.out.println("" + result);
        assertNotNull("xpath result 2 is null", result);
        System.out.println("" + result.getTextContent());
        assertEquals("xpath result 2 is not equal", expResult, result.getTextContent());


        /* select several nodes in the default namespace */
        xpathexp = "/catalog/journal/article";
        prefix = null;
        int expResultAmount = 2;
        NodeList results = XMLTool.selectNodes(document, xpathexp, prefix);
        System.out.println("" + results);
        assertNotNull("xpath result 2 is null", results);
        System.out.println("" + results.getLength());
        assertEquals("xpath amount of result 3 is not ok", expResultAmount, results.getLength());

        for (int i = 0; i < results.getLength(); i++) {
            Element article = (Element) results.item(i);
            System.out.println(""+article);
        }


    }

    /**
     * Test of validate method, of class XMLTool.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");
        Document document = null;
        File xsdFile = null;
        XMLTool.validate(document, xsdFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}