package xdoclet.retest.test.ejb;

import xdoclet.retest.test.DDEjbJarTestCase;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mar 10, 2002
 * @version   $Revision: 1.1 $
 */
public class TransactionBeanTest
        extends TestCase
{

    public TransactionBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        DDEjbJarTestCase jbtc = new DDEjbJarTestCase("DD","Transaction");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
