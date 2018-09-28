package xdoclet.retest.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.1 $
 */
public abstract class XDocletRegressionTestCase
        extends TestCase
{

    protected String refBase = "xdoclet.retest.ref.ejb";
    protected String genBase = "xdoclet.retest.bean.ejb";
    protected String refXmlBase = "xml-ref";
    protected String genXmlBase = "xml-src";

    public XDocletRegressionTestCase(String name)
    {
        super(name);
    }

    public XDocletRegressionTestCase(String name,String cn)
    {
        super(name);
        this.className = cn;
    }

    private String className;

    protected String getClassName()
    {
        return className;
    }

    protected abstract Test getSuite();

}
