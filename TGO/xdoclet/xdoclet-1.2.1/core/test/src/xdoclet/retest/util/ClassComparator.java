package xdoclet.retest.util;

import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class ClassComparator
    extends AbstractClassAndInterfaceComparator
{
    public ClassComparator(Class one,Class two)
    {
        super(one,two);
    }

    public ComparisonResultSet compare()
    throws XDocletException
    {
        if ( one.isInterface() )
        {
            resultSet.addError(XDocletRetestMessages.IS_NOT_A_CLASS,new String[] {one.getName()} );
            return resultSet;
        }
        if ( two.isInterface() )
        {
            resultSet.addError(XDocletRetestMessages.IS_NOT_A_CLASS,new String[] {two.getName()} );
            return resultSet;
        }
        compareClassSignature();
        compareClassMethodsSignature();
        //TODO compareClassFieldsSignature();
        //TODO compareClassContent();

        return resultSet;
   }


}
