/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.ant.modulesbuilder;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses module.xml module descriptor file.
 *
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   Jun 10, 2002
 * @version   $Revision: 1.2 $
 */
class ModuleXmlParser extends DefaultHandler
{
    private final SAXParserFactory _factory;
    private Module  module;
    private EntityResolver entityResolver = null;

    public ModuleXmlParser()
    {
        _factory = SAXParserFactory.newInstance();
        _factory.setValidating(false);
    }

    public void setEntityResolver(EntityResolver entityResolver)
    {
        this.entityResolver = entityResolver;
    }

    public Module parse(InputStream in)
    {
        try {
            SAXParser parser = _factory.newSAXParser();

            // NB this has to use the reader directly; when I tried to use
            // parser.getXMLReader().setEntityResolver(entityResolver);
            // it seemed to ignore the resolver in the subsequent
            // parser.parse(in, this);
            XMLReader reader = parser.getXMLReader();

            if (entityResolver != null)
                reader.setEntityResolver(entityResolver);
            reader.setContentHandler(this);
            reader.parse(new org.xml.sax.InputSource(in));
        }
        catch (IOException e) {
            module = null;
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            module = null;
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            module = null;
            e.printStackTrace();
        }
        catch (SAXException e) {
            module = null;
            e.printStackTrace();
        }
        return module;
    }

    public void startDocument()
    {
        module = new Module();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes)
    {
        if (qName.equals("module-dependency")) {
            module.addDependency(attributes.getValue("module-name"));
        }
    }
}
