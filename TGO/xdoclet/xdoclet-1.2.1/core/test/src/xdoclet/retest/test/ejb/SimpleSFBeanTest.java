package xdoclet.retest.test.ejb;

import xdoclet.retest.test.StatefulSessionBeanRegressionTestCase;
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
public class SimpleSFBeanTest
        extends TestCase
{

    public SimpleSFBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        StatefulSessionBeanRegressionTestCase sft = new StatefulSessionBeanRegressionTestCase("Stateful Session Bean","SimpleSF");
        suite.addTest(sft.getSuite());
        DeploymentDescriptorsRegressionTestCase jbtc = new DeploymentDescriptorsRegressionTestCase("Stateless Session Bean","SimpleSF");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
