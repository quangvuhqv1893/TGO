package xdoclet.retest.util;

import xdoclet.XDocletException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.2 $
 */
public abstract class AbstractComparator
{

    protected ComparisonResultSet resultSet;

    public abstract ComparisonResultSet compare()
    throws XDocletException;

    public String shortClassName(Class clazz)
    {
        String name = clazz.getName();
        StringTokenizer st = new StringTokenizer(name,".");
        String ret = name;
        while (st.hasMoreTokens()) ret = st.nextToken();
        return ret.trim();
    }

    public String shortMethodName(Method method)
    {
        String name =  "[" + shortClassName(method.getReturnType()) + "] " + method.getName() + "(" ;
        Class[] params = method.getParameterTypes();
        for (int i = 0; i< params.length;i++)
        {
            name += shortClassName(params[i]);
            if ( i < params.length - 1) name += ",";
        }
        name += ") E{";
        Class[] es = method.getExceptionTypes();
        for (int i = 0; i< es.length;i++)
        {
            name += shortClassName(es[i]);
            if ( i < es.length - 1) name += ",";
        }
        name += "}";
        return name.trim();
    }

}
