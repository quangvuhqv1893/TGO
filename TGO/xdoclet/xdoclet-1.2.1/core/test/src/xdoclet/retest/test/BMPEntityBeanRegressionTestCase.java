package xdoclet.retest.test;

import xdoclet.retest.util.ClassComparator;
import xdoclet.retest.util.ComparisonResultSet;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class BMPEntityBeanRegressionTestCase
        extends EntityBeanRegressionTestCase
{

    public BMPEntityBeanRegressionTestCase(String name)
    {
        super(name);
    }

    public void testImpl()
    throws Exception
    {
        ClassComparator comparator;
        ComparisonResultSet res;
        Class ref = Class.forName(refBase+ ".ejb." + getClassName() + "BMP");
        Class gen = Class.forName(genBase+ ".ejb." + getClassName() + "BMP");
        comparator = new ClassComparator(ref,gen);
        res = comparator.compare();
        if (res.error()) System.out.println(res.toString());
        assertTrue("Home Interface comparison failed "+res, ! res.error());
    }


}
