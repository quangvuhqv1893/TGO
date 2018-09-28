package xdoclet.retest.util;

import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

import java.lang.reflect.Method;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class MethodComparator
extends AbstractComparator
{
    protected Method one;
    protected Method two;
    protected ComparisonResultSet resultSet;

    public MethodComparator(Method one,Method two)
    {
        super();
        this.one = one;
        this.two = two;
        resultSet = new ComparisonResultSet();
    }

    public ComparisonResultSet compare()
    throws XDocletException
    {
        if ( ! one.getName().equals(two.getName()))
        {
            resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_NAME,new String[] {one.getName(),two.getName()});
            return resultSet;
        }
        if ( ! shortClassName(one.getReturnType()).equals(shortClassName(two.getReturnType())))
        {
            resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_RETURN_TYPE,new String[] {one.getName(),shortClassName(one.getDeclaringClass()),shortClassName(two.getDeclaringClass())});
            return resultSet;
        }
        Class[] onePs = one.getParameterTypes();
        Class[] twoPs = two.getParameterTypes();
        if (onePs.length != twoPs.length)
        {
            resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_NUMBER_OF_PARAM,new String[] {one.getName(),shortClassName(one.getDeclaringClass()),shortClassName(two.getDeclaringClass())});
            return resultSet;
        }
        for (int i=0; i<onePs.length; i++)
        {
            if ( ! shortClassName(onePs[i]).equals(shortClassName(twoPs[i]))){
                resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_TYPE_OF_PARAM,new String[] {one.getName(),shortClassName(one.getDeclaringClass()),shortClassName(two.getDeclaringClass())});
                return resultSet;
            }
        }
        Class[] oneEs = one.getExceptionTypes();
        Class[] twoEs = two.getExceptionTypes();
        if (oneEs.length != twoEs.length)
        {
            resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_NUMBER_OF_EXCEPTIONS,new String[] {one.getName(),shortClassName(one.getDeclaringClass()),shortClassName(two.getDeclaringClass())});
            return resultSet;
        }
        for (int i=0; i<oneEs.length; i++)
        {
            if ( ! shortClassName(oneEs[i]).equals(shortClassName(twoEs[i]))){
                resultSet.addError(XDocletRetestMessages.METHOD_DOES_NOT_HAVE_SAME_TYPE_OF_EXCEPTIONS,new String[] {one.getName(),shortClassName(one.getDeclaringClass()),shortClassName(two.getDeclaringClass())});
                return resultSet;
            }
        }

        return resultSet;
    }


}