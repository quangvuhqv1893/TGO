package xdoclet.retest.test;

import xdoclet.retest.util.ClassComparator;
import xdoclet.retest.util.ComparisonResultSet;
import xdoclet.retest.util.InterfaceComparator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.2 $
 */
public class EnterpriseJavaBeanRegressionTestCase
extends XDocletRegressionTestCase
{

    public EnterpriseJavaBeanRegressionTestCase(String name)
    {
        super(name);
    }

    public EnterpriseJavaBeanRegressionTestCase(String name,String cn)
    {
        super(name,cn);
    }

    public Test getSuite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new EnterpriseJavaBeanRegressionTestCase("testHome",getClassName()));
        suite.addTest(new EnterpriseJavaBeanRegressionTestCase("testRemote",getClassName()));
        suite.addTest(new EnterpriseJavaBeanRegressionTestCase("testLocalHome",getClassName()));
        suite.addTest(new EnterpriseJavaBeanRegressionTestCase("testLocal",getClassName()));
        return suite;
    }

    public void testHome()
    throws Exception
    {
        InterfaceComparator comparator;
        ComparisonResultSet res;
        Class gen = Thread.currentThread().getContextClassLoader().loadClass(genBase+ ".interfaces." + getClassName() + "Home");
        Class ref = Thread.currentThread().getContextClassLoader().loadClass(refBase+ ".interfaces." + getClassName() + "Home");
        comparator = new InterfaceComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Home Interface comparison failed "+res, ! res.error());
    }

    public void testLocalHome()
    throws Exception
    {
        InterfaceComparator comparator;
        ComparisonResultSet res;
        Class ref = Thread.currentThread().getContextClassLoader().loadClass(refBase+ ".interfaces." + getClassName() + "LocalHome");
        Class gen = Thread.currentThread().getContextClassLoader().loadClass(genBase+ ".interfaces." + getClassName() + "LocalHome");
        comparator = new InterfaceComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Home Interface comparison failed "+res, ! res.error());
    }

    public void testRemote()
    throws Exception
    {
        InterfaceComparator comparator;
        ComparisonResultSet res;
        Class ref = Class.forName(refBase+ ".interfaces." + getClassName() );
        Class gen = Class.forName(genBase+ ".interfaces." + getClassName());
        comparator = new InterfaceComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Remote Interface comparison failed "+res, ! res.error());
    }

    public void testLocal()
    throws Exception
    {
        InterfaceComparator comparator;
        ComparisonResultSet res;
        Class ref = Class.forName(refBase+ ".interfaces." + getClassName() + "Local");
        Class gen = Class.forName(genBase+ ".interfaces." + getClassName() + "Local");
        comparator = new InterfaceComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Home Interface comparison failed "+res, ! res.error());
    }

}
