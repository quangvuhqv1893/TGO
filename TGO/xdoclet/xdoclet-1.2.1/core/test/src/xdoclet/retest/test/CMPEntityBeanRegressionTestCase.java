package xdoclet.retest.test;

import xdoclet.retest.util.ClassComparator;
import xdoclet.retest.util.ComparisonResultSet;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.4 $
 */
public class CMPEntityBeanRegressionTestCase
extends EntityBeanRegressionTestCase
{

    public CMPEntityBeanRegressionTestCase(String name)
    {
        super(name);
    }

    public CMPEntityBeanRegressionTestCase(String name,String cn)
    {
        super(name,cn);
    }

    public Test getSuite()
    {
        TestSuite suite = new TestSuite();
        Test t = super.getSuite();
        suite.addTest(t);
        suite.addTest(new CMPEntityBeanRegressionTestCase("testImpl",getClassName()));
        return suite;
    }


    public void testImpl()
    throws Exception
    {
        ClassComparator comparator;
        ComparisonResultSet res;
        Class ref = Class.forName(refBase+ ".ejb." + getClassName() + "CMP");
        Class gen = Class.forName(genBase+ ".ejb." + getClassName() + "CMP");
        comparator = new ClassComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Home Interface comparison failed "+res, ! res.error());
    }

}
