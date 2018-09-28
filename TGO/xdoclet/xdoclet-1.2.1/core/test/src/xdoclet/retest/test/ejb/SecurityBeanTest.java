package xdoclet.retest.test.ejb;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import xdoclet.retest.test.DDEjbJarTestCase;

public class SecurityBeanTest
        extends TestCase
 {

    public SecurityBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        DDEjbJarTestCase jbtc = new DDEjbJarTestCase("DD","Security");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
