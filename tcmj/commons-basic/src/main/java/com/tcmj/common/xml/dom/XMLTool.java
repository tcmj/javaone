package com.tcmj.common.xml.dom;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <b>JAXP</b> XML Helper.
 * @author tcmj - Thomas Deutsch
 */
public class XMLTool {

    /** internal: searches a xml node via xpath.
     * @param document jaxp document
     * @param xpathexp XPath Expression
     * @param prefix namespaceprefix used in the xpath expression (or null if none)
     * @param returntype XPathConstants.NODE or XPathConstants.NODESET
     * @return depending on the param returntype: Node or NodeList object
     * @throws XPathExpressionException on xpath compile errors
     */
    private static Object selectNode(Document document, String xpathexp,
            final String prefix, QName returntype) throws XPathExpressionException {

        // 1. Instantiate an XPathFactory.
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();

        // 2. Use the XPathFactory to create a new XPath object
        javax.xml.xpath.XPath xpath = factory.newXPath();

        //Namespace-prefix-workaround
        if (prefix != null) {
            //search uri to the prefix
            final String uri = document.lookupNamespaceURI(prefix);
            NamespaceContext nsc = new NamespaceContext() {

                @Override
                public String getNamespaceURI(String prefix) {
                    return uri;
                }

                @Override
                public String getPrefix(String namespaceURI) {
                    return prefix;
                }

                @Override
                public Iterator<?> getPrefixes(String namespaceURI) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            xpath.setNamespaceContext(nsc);
        }

        // 3. Compile an XPath string into an XPathExpression
        javax.xml.xpath.XPathExpression expression = xpath.compile(xpathexp);

        // 4. Evaluate the XPath expression on an input document
        if (returntype == XPathConstants.NODE) {
            return expression.evaluate(document, XPathConstants.NODE);
        } else if (returntype == XPathConstants.NODESET) {
            return expression.evaluate(document, XPathConstants.NODESET);
        } else {
            throw new UnsupportedOperationException("returntype not supported");
        }

    }

    /** searches some xml nodes via xpath.
     * @param document jaxp document
     * @param xpathexp XPath Expression
     * @param prefix namespaceprefix used in the xpath expression (or null if none)
     * @return NodeList object
     * @throws XPathExpressionException on xpath compile errors
     */
    public static NodeList selectNodes(Document document,
            String xpathexp, final String prefix) throws XPathExpressionException {
        return (NodeList) selectNode(document, xpathexp, prefix, XPathConstants.NODESET);
    }

    /**
     * searches a single xml node via xpath.
     *
     * @param document jaxp document
     * @param xpathexp XPath Expression
     * @param prefix namespaceprefix used in the xpath expression (or null if none)
     * @return Node object
     * @throws XPathExpressionException on xpath compile errors
     */
    public static Node selectNode(Document document,
            String xpathexp, final String prefix) throws XPathExpressionException {
        return (Node) selectNode(document, xpathexp, prefix, XPathConstants.NODE);
    }

    /** Validates a xml file.
     * TODO implement Errorhandler
     * @param document jaxp dom document
     * @param xsdFile file handle to the xsd file
     * @throws SAXException
     * @throws IOException
     */
    public static void validate(Document document, File xsdFile) throws SAXException, IOException {
        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(xsdFile);
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        validator.validate(new DOMSource(document));

    }

    /**
     * Convert a xml document to a string.
     * @param doc xml document
     * @return the string
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static String toXmlString(Document doc) throws TransformerConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();
        return xmlString;

    }
}
