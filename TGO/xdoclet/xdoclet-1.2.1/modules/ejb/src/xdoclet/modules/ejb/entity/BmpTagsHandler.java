/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import xjavadoc.*;
import xdoclet.DocletContext;
import xdoclet.DocletSupport;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.entity.EntityBmpSubTask;

import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbBmp"
 * @version              $Revision: 1.7 $
 */
public class BmpTagsHandler extends EntityTagsHandler
{

    /**
     * Gets the EntityBmpClassFor attribute of the BmpTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The EntityBmpClassFor value
     * @exception XDocletException
     */
    public static String getEntityBmpClassFor(XClass clazz) throws XDocletException
    {
        String fileName = clazz.getContainingPackage().getName();
        String entity_name = MessageFormat.format(getEntityBmpClassPattern(), new Object[]{EjbTagsHandler.getShortEjbNameFor(clazz)});

        // Fix package name
        fileName = choosePackage(fileName, null, DocletTask.getSubTaskName(EntityBmpSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += entity_name;

        return fileName;
    }


    /**
     * Returns true if clazz is an BMP entity bean, false otherwise. Entity type is determined by looking at the
     * ejb:bean's type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The EntityBmp value
     * @exception XDocletException
     */
    public static boolean isEntityBmp(XClass clazz) throws XDocletException
    {
        if (isEntity(clazz) == false) {
            return false;
        }

        XTag beanTag = clazz.getDoc().getTag("ejb:bean");

        //assume CMP if not specified
        if (beanTag == null) {
            return false;
        }

        String value = beanTag.getAttributeValue("type");

        //assume CMP if not specified
        if (value == null) {
            return false;
        }

        if (value.equals("BMP")) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Returns true if clazz has ejb:use-soft-locking tag, false otherwise.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     */
    public static boolean useSoftLocking(XClass clazz)
    {
        String soft_locking_str = clazz.getDoc().getTagAttributeValue("ejb:bean", "use-soft-locking", false);
        boolean soft_locking = TypeConversionUtil.stringToBoolean(soft_locking_str, false);

        return soft_locking;
    }


    /**
     * Gets the EntityBmpClassPattern attribute of the BmpTagsHandler class
     *
     * @return   The EntityBmpClassPattern value
     */
    protected static String getEntityBmpClassPattern()
    {
        EntityBmpSubTask entitybmp_subtask = ((EntityBmpSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityBmpSubTask.class)));

        if (entitybmp_subtask != null) {
            return entitybmp_subtask.getEntityBmpClassPattern();
        }
        else {
            return EntityBmpSubTask.DEFAULT_ENTITYBMP_CLASS_PATTERN;
        }
    }

    /**
     * Returns the name of generated BMP class.
     *
     * @return                      The name of generated BMP class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String entityBmpClass() throws XDocletException
    {
        return getEntityBmpClassFor(XDocletTagSupport.getCurrentClass());
    }


    /**
     * Evaluates the body block if current class is an BMP entity bean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifEntityIsBmp(String template) throws XDocletException
    {
        if (isEntityBmp(XDocletTagSupport.getCurrentClass())) {
            generate(template);
        }
    }


    /**
     * Evaluates the body block for each EJBean derived from EntityBean which is BMP.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntityBmp(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllBmpEntityBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            XDocletTagSupport.setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(XDocletTagSupport.getCurrentClass())) {
                continue;
            }

            if (isEntityBmp(XDocletTagSupport.getCurrentClass())) {
                generate(template);
            }
        }
    }


    /**
     * Evaluates the body block if ejb:use-soft-locking is set for current class.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #useSoftLocking(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifUseSoftLocking(String template) throws XDocletException
    {
        if (useSoftLocking(XDocletTagSupport.getCurrentClass())) {
            generate(template);
        }
    }
}
