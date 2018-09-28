/*
 * $Id: SimpleCMPBeanTest.java,v 1.1 2002/03/05 22:47:06 vharcq Exp $
 */
package xdoclet.retest.test.ejb;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import xdoclet.retest.test.DeploymentDescriptorsRegressionTestCase;
import xdoclet.retest.test.CMPEntityBeanRegressionTestCase;

public class SimpleCMPBeanTest
        extends TestCase
{

    public SimpleCMPBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        CMPEntityBeanRegressionTestCase slt = new CMPEntityBeanRegressionTestCase("CMP Bean","SimpleCMP");
        suite.addTest(slt.getSuite());
        DeploymentDescriptorsRegressionTestCase jbtc = new DeploymentDescriptorsRegressionTestCase("CMP Bean","SimpleCMP");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
