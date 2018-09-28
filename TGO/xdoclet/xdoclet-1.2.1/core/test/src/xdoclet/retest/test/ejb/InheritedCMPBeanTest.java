/*
 * $Id: InheritedCMPBeanTest.java,v 1.2 2002/04/02 06:13:42 vharcq Exp $
 */
package xdoclet.retest.test.ejb;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import xdoclet.retest.test.DeploymentDescriptorsRegressionTestCase;
import xdoclet.retest.test.CMPEntityBeanRegressionTestCase;

public class InheritedCMPBeanTest
        extends TestCase
{

    public InheritedCMPBeanTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        CMPEntityBeanRegressionTestCase slt = new CMPEntityBeanRegressionTestCase("CMP Bean","InheritedCMPSub");
        suite.addTest(slt.getSuite());
        slt = new CMPEntityBeanRegressionTestCase("CMP Bean","InheritedCMPSuper");
        suite.addTest(slt.getSuite());
        DeploymentDescriptorsRegressionTestCase jbtc = new DeploymentDescriptorsRegressionTestCase("CMP Bean","InheritedCMP");
        suite.addTest(jbtc.getSuite());
        jbtc = new DeploymentDescriptorsRegressionTestCase("CMP Bean","InheritedCMP");
        suite.addTest(jbtc.getSuite());
        return suite;
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

}
