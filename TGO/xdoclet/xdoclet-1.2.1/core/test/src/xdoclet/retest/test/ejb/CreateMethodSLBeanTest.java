/*
 * $Id: CreateMethodSLBeanTest.java,v 1.1 2002/03/05 21:27:26 vharcq Exp $
 */
package xdoclet.retest.test.ejb;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import xdoclet.retest.test.StatelessSessionBeanRegressionTestCase;
import xdoclet.retest.test.DeploymentDescriptorsRegressionTestCase;

public class CreateMethodSLBeanTest
        extends TestCase
{

    public CreateMethodSLBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        StatelessSessionBeanRegressionTestCase slt = new StatelessSessionBeanRegressionTestCase("Stateless Session Bean","CreateMethodSL");
        suite.addTest(slt.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
