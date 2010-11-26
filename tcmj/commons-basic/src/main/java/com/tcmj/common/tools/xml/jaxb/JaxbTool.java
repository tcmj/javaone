/* Copyright 2009 */
package com.tcmj.common.tools.xml.jaxb;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * JaxbTool.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 16.10.2009
 */
public class JaxbTool {

    /**
     * default no-arg-constructor.
     */
    public JaxbTool() {
    }


    public static final <T> void createXml(T instance) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(instance, System.out);

    }


    public static final <T> T xmlString2Class(String xmlString, Class<T> clazz) throws JAXBException {
        StringReader reader = new StringReader(xmlString);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(reader);
        return individual;
    }


    public static final <T> T xmlStream2Class(InputStream xmlStream, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(xmlStream);
        return individual;
    }
    public static final <T> T xmlReader2Class(Reader xmlReader, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();
        T individual = (T) u.unmarshal(xmlReader);
        return individual;
    }




}