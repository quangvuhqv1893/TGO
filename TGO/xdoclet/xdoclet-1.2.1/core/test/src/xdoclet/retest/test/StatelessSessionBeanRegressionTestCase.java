package xdoclet.retest.test;

import junit.textui.TestRunner;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import xdoclet.retest.util.ClassComparator;
import xdoclet.retest.util.ComparisonResultSet;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class StatelessSessionBeanRegressionTestCase
        extends SessionBeanRegressionTestCase
{

    public StatelessSessionBeanRegressionTestCase(String name)
    {
        super(name);
    }

    public StatelessSessionBeanRegressionTestCase(String name,String cn)
    {
        super(name,cn);
    }

}
