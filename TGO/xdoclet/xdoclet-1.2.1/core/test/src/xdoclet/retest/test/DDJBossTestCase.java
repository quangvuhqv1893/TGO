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
 * @version   $Revision: 1.3 $
 */
public class DDJBossTestCase
        extends XmlRegressionTestCase
{
    public DDJBossTestCase(String name)
    {
        super(name);
    }

    public DDJBossTestCase(String name,String cn)
    {
        super(name,cn);
    }

    public Test getSuite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new DDJBossTestCase("testJbossXml",getClassName()));
        suite.addTest(new DDJBossTestCase("testJawsXml",getClassName()));
        suite.addTest(new DDJBossTestCase("testJBossCmpXml",getClassName()));
        return suite;
    }

    public void testJbossXml()
    throws Exception
    {
        Node one = readReferenceNode("jboss.xml");
        Node two = readGeneratedNode("jboss.xml");
        XMLComparator comp = new XMLComparator(one,two);
        comp.setAttributeValidation(false);
        ComparisonResultSet res = comp.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("ejb-jar.xml comparison failed "+res, ! res.error());
    }

    public void testJawsXml()
    throws Exception
    {
        // For session beans we bypass this test
        Node one = null;
        try{
            one = readReferenceNode("jaws.xml");
        }catch (FileNotFoundException e){
            return;
        }
        Node two = readGeneratedNode("jaws.xml");
        XMLComparator comp = new XMLComparator(one,two);
        comp.setAttributeValidation(false);
        ComparisonResultSet res = comp.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("ejb-jar.xml comparison failed "+res, ! res.error());
    }

    public void testJBossCmpXml()
    throws Exception
    {
        // For session beans we bypass this test
        Node one = null;
        try{
            one = readReferenceNode("jboss-cmp.xml");
        }catch (FileNotFoundException e){
            return;
        }
        Node two = readGeneratedNode("jboss-cmp.xml");
        XMLComparator comp = new XMLComparator(one,two);
        comp.setAttributeValidation(false);
        ComparisonResultSet res = comp.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("ejb-jar.xml comparison failed "+res, ! res.error());
    }


}
