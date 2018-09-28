package xdoclet.retest.test.ejb;

import xdoclet.retest.test.StatelessSessionBeanRegressionTestCase;
import xdoclet.retest.test.DeploymentDescriptorsRegressionTestCase;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mar 10, 2002
 * @version   $Revision: 1.1 $
 */
public class EjbRefBeanTest
        extends TestCase
{

    public EjbRefBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        DeploymentDescriptorsRegressionTestCase jbtc = new DeploymentDescriptorsRegressionTestCase("DD","EjbRef");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
