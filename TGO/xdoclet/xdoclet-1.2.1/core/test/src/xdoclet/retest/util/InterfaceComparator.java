package xdoclet.retest.util;

import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class InterfaceComparator
        extends AbstractClassAndInterfaceComparator
{
    public InterfaceComparator(Class one,Class two)
    {
        super(one,two);
    }

    public ComparisonResultSet compare() throws XDocletException
    {
        if ( ! one.isInterface() )
        {
            resultSet.addError(XDocletRetestMessages.IS_NOT_AN_INTERFACE,new String[] {one.getName()} );
            return resultSet;
        }
        if ( ! two.isInterface() )
        {
            resultSet.addError(XDocletRetestMessages.IS_NOT_AN_INTERFACE,new String[] {two.getName()} );
            return resultSet;
        }
        compareClassSignature();
        compareClassMethodsSignature();
        //TODO compareClassFieldsSignature();

        return resultSet;
    }


}
