package xdoclet.retest.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import xdoclet.XDocletException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.4 $
 */
public class XmlRegressionTestCase
        extends XDocletRegressionTestCase
{

    public XmlRegressionTestCase(String name)
    {
        super(name);
    }

    public XmlRegressionTestCase(String name, String cn)
    {
        super(name,cn);
    }

    public Test getSuite()
    {
        TestSuite suite= new TestSuite();
        return suite;
    }

    protected static Document readDocument(String uri)
    throws XDocletException,FileNotFoundException
    {
        Document doc;
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            LocalResolver resolver = new LocalResolver();
            LocalErrorHandler error = new LocalErrorHandler("",resolver);
            db.setEntityResolver(resolver);
            db.setErrorHandler(error);
            doc = db.parse(uri);
            return doc;
        }catch (ParserConfigurationException e){
            e.printStackTrace();
            throw new XDocletException("Parser Config Error");
        }catch (SAXParseException e){
            e.printStackTrace();
            throw new XDocletException("Parsing Error in "+uri+" at line "+e.getLineNumber());
        }catch (SAXException e){
            e.printStackTrace();
            throw new XDocletException("Parsing Error in "+uri);
        }catch (IOException e){
            if (e instanceof FileNotFoundException)
                throw (FileNotFoundException)e;
            e.printStackTrace();
            throw new XDocletException("IO Error in "+uri);
        }
    }

    protected Node readReferenceNode(String file)
    throws XDocletException,FileNotFoundException
    {
        return readDocument(refXmlBase + File.separator + getClassName() + File.separator + file);
    }

    protected Node readGeneratedNode(String file)
    throws XDocletException,FileNotFoundException
    {
        return readDocument(genXmlBase + File.separator + getClassName() + File.separator + file);
    }


    private static class LocalResolver implements EntityResolver
    {
        private Hashtable dtds = new Hashtable();
        private boolean hasDTD = false;

        public LocalResolver()
        {
            registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "../../dtds/ejb11-jar.dtd");
            registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "../../dtds/ejb20-jar.dtd");
            registerDTD("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "../../dtds/application_1_2.dtd");
            registerDTD("-//Sun Microsystems, Inc.//DTD Connector 1.0//EN", "../../dtds/connector_1_0.dtd");
            registerDTD("-//JBoss//DTD JAWS 2.4//EN", "../../dtds/jaws_2_4.dtd");
            registerDTD("-//JBoss//DTD JAWS 3.0//EN", "../../dtds/jaws_3_0.dtd");
            registerDTD("-//JBoss//DTD JBOSS 2.4//EN","../../dtds/jboss_2_4.dtd");
            registerDTD("-//JBoss//DTD JBOSS 3.0//EN","../../dtds/jboss_3_0.dtd");
        }

        /**
         * Registers available DTDs
         * @param String publicId    - Public ID of DTD
         * @param String dtdFileName - the file name of DTD
         */
        public void registerDTD(String publicId, String dtdFileName)
        {
            dtds.put(publicId, dtdFileName);
        }

        /**
         * Returns DTD inputSource. Is DTD was found in the hashtable and inputSource was created
         * flad hasDTD is ser to true.
         * @param String publicId    - Public ID of DTD
         * @param String dtdFileName - the file name of DTD
         * @return InputSource of DTD
         */
        public InputSource resolveEntity(String publicId, String systemId)
        {
            hasDTD = false;
            String dtd = (String)dtds.get(publicId);

            if (dtd != null)
            {
                hasDTD = true;
                try
                {
                    InputSource aInputSource = new InputSource(dtd);
                    return aInputSource;
                } catch( Exception ex )
                {
                    // ignore
                }
            }
            return null;
        }

        /**
         * Returns the boolean value to inform id DTD was found in the XML file or not
         * @return boolean - true if DTD was found in XML
         */
        public boolean hasDTD()
        {
            return hasDTD;
        }

    }

    private static class LocalErrorHandler implements ErrorHandler
    {
        private String theFileName;
        private LocalResolver localResolver;

        public LocalErrorHandler( String inFileName, LocalResolver localResolver )
        {
            this.theFileName = inFileName;
            this.localResolver = localResolver;
        }

        public void error(SAXParseException exception)
        {
            if ( localResolver.hasDTD() )
            {
                System.err.println("File "
                        + theFileName
                        + " process error. Line: "
                        + String.valueOf(exception.getLineNumber())
                        + ". Error message: "
                        + exception.getMessage()
                );
            }
        }

        public void fatalError(SAXParseException exception)
        {
            if ( localResolver.hasDTD() )
            {
                System.err.println("File "
                        + theFileName
                        + " process fatal error. Line: "
                        + String.valueOf(exception.getLineNumber())
                        + ". Error message: "
                        + exception.getMessage()
                );
            }
        }

        public void warning(SAXParseException exception)
        {
            if ( localResolver.hasDTD() )
            {
                System.err.println("File "
                        + theFileName
                        + " process warning. Line: "
                        + String.valueOf(exception.getLineNumber())
                        + ". Error message: "
                        + exception.getMessage()
                );
            }
        }
    }

}
