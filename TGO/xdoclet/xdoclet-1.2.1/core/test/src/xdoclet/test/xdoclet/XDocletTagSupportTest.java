package xdoclet.test.xdoclet;

import xdoclet.test.XDocletTestCase;
import xdoclet.XDocletTagSupport;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test case for xdoclet.XDocletTagSupport
 *
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   March 11, 2002
 * @version   $Revision: 1.2 $
 */
public class XDocletTagSupportTest
        extends XDocletTestCase
{

    public void testReplaceProperties()
    throws Exception{
        Map map = new HashMap();
        map.put( "prop", "val" );
        map.put( "prop2", "val2" );
        assertEquals( "$123" , XDocletTagSupport.replaceProperties( "$123", map ) );
        assertEquals( "val" , XDocletTagSupport.replaceProperties( "${prop}", map ) );
        assertEquals( "val,val2" , XDocletTagSupport.replaceProperties( "${prop},${prop2}", map ) );
        assertEquals( "xyz" , XDocletTagSupport.replaceProperties( "xyz", map ) );
        // cause a OutOfMemory error assertEquals( "${prop" , XDocletTagSupport.replaceProperties( "${prop", map ) );
        assertEquals( "${no.prop}" , XDocletTagSupport.replaceProperties( "${no.prop}", map ) );
    }

    public XDocletTagSupportTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(XDocletTagSupportTest.class);
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }


}
