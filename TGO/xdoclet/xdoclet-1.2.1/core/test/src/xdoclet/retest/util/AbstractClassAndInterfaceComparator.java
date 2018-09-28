package xdoclet.retest.util;

import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.4 $
 */
public abstract class AbstractClassAndInterfaceComparator
extends AbstractComparator
{

    protected Class one;
    protected Class two;

    // ------------------------------------------------------------ Constructors

    public AbstractClassAndInterfaceComparator(Class one, Class two)
    {
        super();
        this.one = one;
        this.two = two;
        resultSet = new ComparisonResultSet();
    }

    // ------------------------------------------------------------------ Public

    public ComparisonResultSet compareClassSignature()
    throws XDocletException
    {
        compareInterfaces();
        compareClass();
        return resultSet;
    }

    public ComparisonResultSet compareClassMethodsSignature()
    throws XDocletException
    {
        Method[] oneMs = one.getDeclaredMethods();
        Method[] twoMs = two.getDeclaredMethods();
        compareMethodSignatureOneByOne(oneMs,twoMs);
        compareMethodSignatureOneByOne(twoMs,oneMs);
        return resultSet;
    }

    // ----------------------------------------------------------------- Private

    private void compareInterfaces()
    throws XDocletException
    {
        Class[] oneIFs = one.getInterfaces();
        Class[] twoIFs = two.getInterfaces();
        if (oneIFs.length != twoIFs.length)
        {
            resultSet.addError(XDocletRetestMessages.MISMATCH_NUMBER_INTERFACE,new String[] {one.getName(),two.getName()});
        }
        compareInterfacesOneByOne(oneIFs,twoIFs);
        compareInterfacesOneByOne(twoIFs,oneIFs);
    }

    private void compareInterfacesOneByOne(Class[] oneIFs,Class[] twoIFs)
    throws XDocletException
    {
        for (int i = 0; i<oneIFs.length; i++){
            boolean found = false;
            for (int j = 0; j<twoIFs.length; j++){
                if (oneIFs[i].getName().equals(twoIFs[j].getName())){
                    found = true;
                    break;
                }
            }
            if ( ! found ){
                resultSet.addError(XDocletRetestMessages.INTERFACE_DEFINED_ONLY_IN,new String[] {oneIFs[i].getName(),two.getName()});
            }
        }
    }

    private void compareClass(){
        if (one.isInterface() && two.isInterface())
            return;
        //TODO : compare "extends" on the two classes
    }

    private void compareMethodSignatureOneByOne(Method[] oneMs,Method[] twoMs)
    throws XDocletException
    {
        for (int i = 0 ; i < oneMs.length; i++)
        {
            boolean found = false;
            for (int j=0;j<twoMs.length;j++)
            {
                MethodComparator comp = new MethodComparator(oneMs[i],twoMs[j]);
                ComparisonResultSet resultSet = comp.compare();
                if ( ! resultSet.error())
                {
                    found = true;
                    break;
                }
            }
            if ( ! found )
            {
                resultSet.addError(XDocletRetestMessages.METHOD_DEFINED_ONLY_IN,new String[] {shortMethodName(oneMs[i]),two.getName()});
            }
        }
    }

}
