package xdoclet.retest.test;

import junit.framework.TestSuite;
import junit.framework.Test;
import org.w3c.dom.Node;
import xdoclet.retest.util.XMLComparator;
import xdoclet.retest.util.ComparisonResultSet;

import java.io.FileNotFoundException;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.2 $
 */
public class DDEjbJarTestCase
        extends XmlRegressionTestCase
{
    public DDEjbJarTestCase(String name)
    {
        super(name);
    }

    public DDEjbJarTestCase(String name,String cn)
    {
        super(name,cn);
    }

    public Test getSuite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new DDEjbJarTestCase("testEjbJarXml",getClassName()));
        return suite;
    }

    public void testEjbJarXml()
    throws Exception
    {
        Node one = readReferenceNode("ejb-jar.xml");
        Node two = readGeneratedNode("ejb-jar.xml");
        XMLComparator comp = new XMLComparator(one,two);
        comp.setAttributeValidation(false);
        ComparisonResultSet res = comp.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("ejb-jar.xml comparison failed "+res, ! res.error());
    }

}
