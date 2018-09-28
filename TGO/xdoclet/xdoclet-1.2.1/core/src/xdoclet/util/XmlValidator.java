/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;

import org.apache.tools.ant.AntClassLoader;

import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;

/**
 * This handler implementation is capable of providing dtds from a local storage instead of accessing them over the net.
 * Further, it will throw an exception if the parsed xml is not acording to the DTD it specifies.
 *
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Helles�y</a>
 * @created   September 18, 2001
 * @version   $Revision: 1.16 $
 * @todo      Deal with Translator.getString()'s exception better in resolveEntity(String, String)
 */
public class XmlValidator extends DefaultHandler
{

    /**
     * The crimson implementation is shipped with ant.
     */
    public static String DEFAULT_XML_READER_CLASSNAME = "org.apache.crimson.parser.XMLReaderImpl";

    private static XmlValidator instance = new XmlValidator(null);

    protected ClassLoader classLoader;

    /**
     * XMLReader used for validation
     */
    protected XMLReader xmlReader = null;

    protected String readerClassName = DEFAULT_XML_READER_CLASSNAME;

    private final HashMap _dtds = new HashMap();

    /**
     * Describe what the XmlValidator constructor does
     *
     * @param classLoader  Describe what the parameter does
     */
    public XmlValidator(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    /**
     * Gets the Instance attribute of the XmlValidator class
     *
     * @return   The Instance value
     */
    public static XmlValidator getInstance()
    {
        return instance;
    }

    /**
     * Sets the Instance attribute of the XmlValidator class
     *
     * @param instance  The new Instance value
     */
    public static void setInstance(XmlValidator instance)
    {
        XmlValidator.instance = instance;
    }

    /**
     * Registers a local DTD document by its public id. This is necessary to avoid DTD loading over the net.
     *
     * @param publicId  the publicId of the DTD
     * @param dtdURL    the URL of the local DTD, which must be loadable by the class passed in the constructor. This
     *      URL typically points inside a local jar file
     */
    public void registerDTD(String publicId, URL dtdURL)
    {
        Log log = LogUtil.getLog(XmlValidator.class, "registerDTD");

        if (log.isDebugEnabled()) {
            log.debug("DTD '" + dtdURL + "' registered for public Id '" + publicId + "'.");
        }

        _dtds.put(publicId, dtdURL);
    }

    /**
     * Called by parser when a DTD declaration is encountered in the parsed XML document
     *
     * @param publicId  the public id of the DTD
     * @param systemId  the system id of the DTD
     * @return          an InputSource from containing the DTD document, provided it has been previously registered via
     *      the {@link #registerDTD} method. If not, null will be returned, and the parser will atempt to load the DTD
     *      from the systemId value, Usually an Internet http URL.
     */
    public InputSource resolveEntity(String publicId, String systemId)
    {
        Log log = LogUtil.getLog(XmlValidator.class, "resolveEntity");

        if (log.isDebugEnabled()) {
            log.debug("publicId=" + publicId);
            log.debug("systemId=" + systemId);
        }

        URL dtdURL = (URL) _dtds.get(publicId);

        if (dtdURL != null) {
            String dtd = FileManager.getURLContent(dtdURL);

            if (log.isDebugEnabled()) {
                log.debug("dtdURL != null, dtdURL=" + dtdURL);
                log.debug("dtd.length()=" + dtd.length());
            }

            return new InputSource(new StringReader(dtd));
        }
        else {
            log.debug("dtdURL == null");
            log.error(Translator.getString(XDocletMessages.class, XDocletMessages.COULDNT_LOAD_LOCAL_DTD, new String[]{publicId}));
            return null;
        }
    }

    /**
     * Called by parser if a error occurs
     *
     * @param e                   an exception describing the error
     * @throws SAXParseException  every time this method is called by the parser
     */
    public void error(SAXParseException e)
         throws SAXParseException
    {
        throw e;
    }

    /**
     * Called by parser if a warning occurs
     *
     * @param e                   an exception describing the warning
     * @throws SAXParseException  every time this method is called by the parser
     */
    public void warning(SAXParseException e)
         throws SAXParseException
    {
//		throw e;
    }

    /**
     * Validates an XML file for conformance to a declared DTD or XMLSchema. This method is useful for subclasses that
     * wish to verify that a generated XML file is ok. Please note that the callers should make sure to register any
     * DTDs required for validation on the handler object.
     *
     * @param xmlFile               Description of Parameter
     * @exception XDocletException  Description of Exception
     */
    public void validate(File xmlFile) throws XDocletException
    {
        if (classLoader == null) {
            // we're running in forked mode. no need to use ant classloader hack
            // Get strange  org.xml.sax.SAXParseException: Declared encoding "UTF-8" does not match actual one "Cp1252"; this might not be an error.
            initValidator();
        }
        else {
            initValidatorHack();
        }
        doValidate(xmlFile);
    }

    /*
     * parse the file
     */
    /**
     * Describe what the method does
     *
     * @param xml_file              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    private void doValidate(File xml_file) throws XDocletException
    {
        Log log = LogUtil.getLog(XmlValidator.class, "doValidate");

        try {
            if (log.isDebugEnabled()) {
                log.debug("Validating " + xml_file.getName() + "... ");
            }

            //errorHandler.init( afile );
            InputSource is = new InputSource(new FileReader(xml_file));
            String uri = "file:" + xml_file.getAbsolutePath().replace('\\', '/');

            for (int index = uri.indexOf('#'); index != -1; index = uri.indexOf('#')) {
                uri = uri.substring(0, index) + "%23" + uri.substring(index + 1);
            }

            is.setSystemId(uri);

            xmlReader.parse(is);
        }
        catch (SAXException e) {
            e.printStackTrace();
            throw new XDocletException("Couldn't validate document " + xml_file);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new XDocletException(e, "Couldn't validate document " + xml_file);
        }

//      if( errorHandler.getFailure() )
//      {
//         if( failOnError )
//            throw new BuildException( xml_file + " is not a valid XML document." );
//         else
//            log( xml_file + " is not a valid XML document", Project.MSG_ERR );
//      }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    private void initValidatorHack() throws XDocletException
    {
        Log log = LogUtil.getLog(XmlValidator.class, "initValidator");

        try {
            // load the parser class
            // with JAXP, we would use a SAXParser factory
            Class readerClass = null;

            if (classLoader != null) {
                readerClass = classLoader.loadClass(readerClassName);
                AntClassLoader.initializeClass(readerClass);
            }
            else {
                readerClass = Class.forName(readerClassName);
            }

            // then check it implements XMLReader
            if (XMLReader.class.isAssignableFrom(readerClass)) {
                xmlReader = (XMLReader) readerClass.newInstance();

                if (log.isDebugEnabled()) {
                    log.debug("Using SAX2 reader " + readerClassName);
                }
            }
            else {
                // see if it is a SAX1 Parser
                if (Parser.class.isAssignableFrom(readerClass)) {
                    Parser parser = (Parser) readerClass.newInstance();

                    xmlReader = new ParserAdapter(parser);

                    if (log.isDebugEnabled()) {
                        log.debug("Using SAX1 parser " + readerClassName);
                    }
                }
                else {
                    String message = Translator.getString(XDocletUtilMessages.class, XDocletUtilMessages.INIT_FAILED, new String[]{readerClassName});

                    System.out.println("init_failed");

                    throw new XDocletException(message);
                }
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();

            String message = Translator.getString(XDocletUtilMessages.class, XDocletUtilMessages.INIT_FAILED, new String[]{readerClassName});

            throw new XDocletException(e, message);
        }
        catch (InstantiationException e) {
            e.printStackTrace();

            String message = Translator.getString(XDocletUtilMessages.class, XDocletUtilMessages.INIT_FAILED, new String[]{readerClassName});

            throw new XDocletException(e, message);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();

            String message = Translator.getString(XDocletUtilMessages.class, XDocletUtilMessages.INIT_FAILED, new String[]{readerClassName});

            throw new XDocletException(e, message);
        }

        //\\xmlReader.setErrorHandler( errorHandler );
        xmlReader.setEntityResolver(this);

        if (!(xmlReader instanceof ParserAdapter)) {
            // turn validation on
            try {
                xmlReader.setFeature("http://xml.org/sax/features/validation", true);
            }
            catch (SAXNotRecognizedException e) {
                e.printStackTrace();
            }
            catch (SAXNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initValidator() throws XDocletException
    {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(true);
            xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setEntityResolver(this);
            xmlReader.setErrorHandler(this);
        }
        catch (SAXParseException e) {
            String message = Translator.getString(XDocletUtilMessages.class, XDocletUtilMessages.INIT_FAILED,
                new String[]{e.getSystemId(), Integer.toString(e.getLineNumber()), e.getMessage()});

            throw new XDocletException(e, message);
        }
        catch (SAXException e) {
            throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.COULDNT_INIT_XML_PARSER));
        }
        catch (ParserConfigurationException e) {
            throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.COULDNT_CONF_XML_PARSER));
        }
        catch (NullPointerException e) {
            throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.COULDNT_LOAD_DTD));
        }
    }
}
