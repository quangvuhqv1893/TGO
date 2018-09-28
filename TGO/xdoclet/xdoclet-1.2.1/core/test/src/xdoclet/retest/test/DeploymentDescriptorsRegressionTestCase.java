package xdoclet.retest.test;

import junit.framework.TestSuite;
import junit.framework.Test;
import xdoclet.retest.util.XMLComparator;
import xdoclet.retest.util.ComparisonResultSet;
import org.w3c.dom.Node;

import java.io.File;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.2 $
 */
public class DeploymentDescriptorsRegressionTestCase
        extends XDocletRegressionTestCase
{

    public DeploymentDescriptorsRegressionTestCase(String name)
    {
        super(name);
    }

    public DeploymentDescriptorsRegressionTestCase(String name,String cn)
    {
        super(name,cn);
    }

    /**
     * @todo Add Weblogic, Orion, ... deployment descriptor TestCase
     */
    public Test getSuite()
    {
        TestSuite suite = new TestSuite();
        DDEjbJarTestCase ejbtc = new DDEjbJarTestCase("ejb-jar DD",getClassName());
        suite.addTest(ejbtc.getSuite());
        DDJBossTestCase jbtc = new DDJBossTestCase("JBoss DD",getClassName());
        suite.addTest(jbtc.getSuite());
        // Add WebLogic, Orion, ...
        return suite;
    }

}
