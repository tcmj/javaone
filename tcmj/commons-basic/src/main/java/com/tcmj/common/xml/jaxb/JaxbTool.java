package com.tcmj.common.xml.jaxb;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.tcmj.common.lang.Objects;

/**
 * JaxbTool.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 16.10.2009
 */
public class JaxbTool {

    /** private no-arg-constructor. */
    private JaxbTool() {
    }

    public static <T> void createXml(T instance) throws JAXBException {
        Objects.notNull(instance, "Parameter 'instance' may not be null calling method createXml(T instance)!");
        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(instance, System.out);
    }

    public static <T> T xmlString2Class(String xmlString, Class<T> clazz) throws JAXBException {
        Objects.notNull(xmlString, "Parameter 1 (xmlString) may not be null!");
        Objects.notNull(clazz, "Parameter 2 (clazz) may not be null!");
        StringReader reader = new StringReader(xmlString);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(reader);
        return individual;
    }

    public static <T> T xmlStream2Class(InputStream xmlStream, Class<T> clazz) throws JAXBException {
        Objects.notNull(xmlStream, "Parameter 1 (xmlStream) may not be null!");
        Objects.notNull(clazz, "Parameter 2 (clazz) may not be null!");
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(xmlStream);
        return individual;
    }

    public static <T> T xmlReader2Class(Reader xmlReader, Class<T> clazz) throws JAXBException {
        Objects.notNull(xmlReader, "Parameter 1 (xmlReader) may not be null!");
        Objects.notNull(clazz, "Parameter 2 (clazz) may not be null!");
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(xmlReader);
        return individual;
    }

}
