package xdoclet.retest.test.ejb;

import xdoclet.retest.test.StatelessSessionBeanRegressionTestCase;
import xdoclet.retest.test.DeploymentDescriptorsRegressionTestCase;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.1 $
 */
public class SimpleSLBeanTest
        extends TestCase
{

    public SimpleSLBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        StatelessSessionBeanRegressionTestCase slt = new StatelessSessionBeanRegressionTestCase("Stateless Session Bean","SimpleSL");
        suite.addTest(slt.getSuite());
        DeploymentDescriptorsRegressionTestCase jbtc = new DeploymentDescriptorsRegressionTestCase("Stateless Session Bean","SimpleSL");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
