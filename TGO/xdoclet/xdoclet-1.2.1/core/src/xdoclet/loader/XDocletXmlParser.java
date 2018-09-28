/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses xdoclet.xml deployment descriptor
 *
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created   7. april 2002
 * @version   $Revision: 1.6 $
 */
class XDocletXmlParser extends DefaultHandler
{
    private final SAXParserFactory _factory;
    private XDocletModule module;

    public XDocletXmlParser()
    {
        _factory = SAXParserFactory.newInstance();
        _factory.setValidating(false);
    }

    /**
     * @param in
     * @return
     * @todo      log some sensible error messages
     */
    public XDocletModule parse(InputSource in)
    {
        try {
            SAXParser parser = _factory.newSAXParser();

            parser.parse(in, this);
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
        catch (SAXParseException e) {
            module = null;
            e.printStackTrace();
            System.out.println("location:" + in.getSystemId() + ":" + e.getLineNumber() + "," + e.getColumnNumber());
        }
        catch (SAXException e) {
            module = null;
            e.printStackTrace();
            System.out.println("location:" + in.getSystemId());
        }
        return module;
    }

    public void startDocument()
    {
        module = new XDocletModule();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes)
    {

        if (qName.equals("taghandler")) {
            module.addTagHandler(
                attributes.getValue("namespace"),
                attributes.getValue("class")
                );
        }
        else
            if (qName.equals("subtask")) {
            module.addSubTask(
                attributes.getValue("name"),
                attributes.getValue("implementation-class"),
                attributes.getValue("parent-task-class")
                );
        }
    }
}
