/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jdo;

import java.util.Collection;
import xjavadoc.XClass;
import xjavadoc.XConstructor;
import xjavadoc.XField;
import xjavadoc.XMethod;
import xjavadoc.XPackage;
import xdoclet.SubTask;
import xdoclet.XDocletException;

/**
 * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
 * @created   10 October 2002
 * @version   $Revision: 1.1 $
 */
public abstract class VendorExtensionsSubTask extends SubTask
{

    // Standard tags for vendor extensions
    public final static String SQL_TABLE_TAG = "sql.table";
    public final static String SQL_FIELD_TAG = "sql.field";
    public final static String TABLE_NAME_ATTR = "table-name";
    public final static String COLUMN_NAME_ATTR = "column-name";
    public final static String RELATED_FIELD_ATTR = "related-field";

    public final static String SQL_RELATION_TAG = "sql.relation";
    public final static String STYLE_ATTR = "style";
    public final static String STYLE_FOREIGN_KEY_VALUE = "foreign-key";
    public final static String STYLE_RELATION_TABLE_VALUE = "relation-table";

    // uses TABLE_NAME_ATTR
    // uses RELATED_FIELD_ATTR


    public abstract String getVendorName();

    public abstract String getVendorDescription();

    // Redirect all get/setCurrentXXX methods to the metadataSubTask

    /**
     * Peeks and return the current class from top of currentClassStack stack.
     *
     * @return   The CurrentClass value
     * @see      #setCurrentClass(xjavadoc.XClass)
     */
    public XClass getCurrentClass()
    {
        return getMetadataSubTask().getCurrentClass();
    }

    /**
     * Returns current package.
     *
     * @return   The CurrentPackage value
     * @see      #setCurrentPackage(xjavadoc.XPackage)
     */
    public XPackage getCurrentPackage()
    {
        return getMetadataSubTask().getCurrentPackage();
    }

    /**
     * Returns current method.
     *
     * @return   The CurrentMethod value
     * @see      #setCurrentMethod(xjavadoc.XMethod)
     */
    public XMethod getCurrentMethod()
    {
        return getMetadataSubTask().getCurrentMethod();
    }

    /**
     * Returns current constructor.
     *
     * @return   The CurrentConstructor value
     * @see      #setCurrentConstructor(xjavadoc.XConstructor)
     */
    public XConstructor getCurrentConstructor()
    {
        return getMetadataSubTask().getCurrentConstructor();
    }

    /**
     * Returns current field.
     *
     * @return   The CurrentField value
     * @see      #setCurrentField(xjavadoc.XField)
     */
    public XField getCurrentField()
    {
        return getMetadataSubTask().getCurrentField();
    }

    /**
     * Returns current package.
     *
     * @param pakkage  The new CurrentPackage value
     * @see            #setCurrentPackage(xjavadoc.XPackage)
     * @ant.ignore
     */
    public void setCurrentPackage(XPackage pakkage)
    {
        getMetadataSubTask().setCurrentPackage(pakkage);
    }

    /**
     * Sets the CurrentMethod attribute of the DocletSupport object
     *
     * @param method  The new CurrentMethod value
     * @ant.ignore
     */
    public void setCurrentMethod(XMethod method)
    {
        getMetadataSubTask().setCurrentMethod(method);
    }

    /**
     * Sets the CurrentConstructor attribute of the DocletSupport object
     *
     * @param constructor  The new CurrentConstructor value
     * @ant.ignore
     */
    public void setCurrentConstructor(XConstructor constructor)
    {
        getMetadataSubTask().setCurrentConstructor(constructor);
    }

    /**
     * Sets the CurrentField attribute of the DocletSupport object
     *
     * @param field  The new CurrentField value
     * @ant.ignore
     */
    public void setCurrentField(XField field)
    {
        getMetadataSubTask().setCurrentField(field);
    }

    /**
     * Sets current class to clazz by clearing currentClassStack stack and pushing clazz into top of it.
     *
     * @param clazz  The new CurrentClass value
     * @see          #getCurrentClass()
     * @ant.ignore
     */
    public void setCurrentClass(XClass clazz)
    {
        getMetadataSubTask().setCurrentClass(clazz);
    }

    /**
     * Pushes class clazz to top of currentClassStack stack, making it effectively the current class.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @see          #getCurrentClass()
     * @see          #setCurrentClass(xjavadoc.XClass)
     * @see          #popCurrentClass()
     */
    public XClass pushCurrentClass(XClass clazz)
    {
        return getMetadataSubTask().pushCurrentClass(clazz);
    }

    /**
     * Popes current class from top currentClassStack stack. The poped class is no longer the current class.
     *
     * @return   Description of the Returned Value
     * @see      #getCurrentClass()
     * @see      #setCurrentClass(xjavadoc.XClass)
     * @see      #pushCurrentClass(xjavadoc.XClass)
     */
    public XClass popCurrentClass()
    {
        return getMetadataSubTask().popCurrentClass();
    }

    /**
     * @exception XDocletException
     * @see                         xdoclet.SubTask#execute()
     */
    public void execute() throws XDocletException
    {
        // noop
    }

    protected JdoXmlMetadataSubTask getMetadataSubTask()
    {
        return (JdoXmlMetadataSubTask) getContext().getSubTaskBy("jdometadata");
    }

    protected Collection getExtensions(String level) throws XDocletException
    {
        if ("class".equals(level))
            return getClassExtensions();
        else if ("field".equals(level))
            return getFieldExtensions();
        else if ("collection".equals(level))
            return getCollectionExtensions();
        else if ("array".equals(level))
            return getArrayExtensions();
        else if ("map".equals(level))
            return getMapExtensions();
        else
            return null;
    }

    protected abstract Collection getClassExtensions() throws XDocletException;

    protected abstract Collection getFieldExtensions() throws XDocletException;

    protected abstract Collection getCollectionExtensions() throws XDocletException;

    protected abstract Collection getArrayExtensions() throws XDocletException;

    protected abstract Collection getMapExtensions() throws XDocletException;
}
