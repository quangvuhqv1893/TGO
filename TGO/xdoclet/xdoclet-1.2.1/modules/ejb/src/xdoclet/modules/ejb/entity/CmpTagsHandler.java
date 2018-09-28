/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.text.MessageFormat;
import java.util.*;

import xjavadoc.*;
import xdoclet.DocletContext;
import xdoclet.DocletSupport;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbDocletTask;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.entity.EntityCmpSubTask;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbCmp"
 * @version              $Revision: 1.14 $
 */
public class CmpTagsHandler extends EntityTagsHandler
{
    /**
     * Gets the EntityCmpClassFor attribute of the CmpTagsHandler class
     *
     * @param clazz  Describe what the parameter does
     * @return       The EntityCmpClassFor value
     */
    public static String getEntityCmpClassFor(XClass clazz)
    {
        String fileName = clazz.getContainingPackage().getName();
        String entityName = MessageFormat.format(getEntityCmpClassPattern(), new Object[]{EjbTagsHandler.getShortEjbNameFor(clazz)});

        // Fix package name
        fileName = choosePackage(fileName, null, DocletTask.getSubTaskName(EntityCmpSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += entityName;

        return fileName;
    }

    /**
     * Returns true if clazz is an CMP entity bean, false otherwise. Entity type is determined by looking at the
     * ejb:bean's type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The EntityCmp value
     * @exception XDocletException
     * @todo                        refactor this method up in superclass with isEntityBmp
     */
    public static boolean isEntityCmp(XClass clazz) throws XDocletException
    {
        if (isEntity(clazz) == false) {
            return false;
        }

        XTag beanTag = clazz.getDoc().getTag("ejb:bean");

        // assume CMP if not specified
        if (beanTag == null) {
            return true;
        }

        String value = beanTag.getAttributeValue("type");

        // assume CMP if not specified
        if (value == null) {
            return true;
        }

        if (value.equals("CMP")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if ejbspec config parameter is "2.0" and ejb:bean's cmp-version either not defined or is "2.x",
     * false otherwise.
     *
     * @param clazz
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    public static boolean isUsingCmp2Impl(XClass clazz) throws XDocletException
    {
        String bmp = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:bean",
            "type",
            "CMP,BMP",
            null,
            true,
            false
            );

        if (bmp != null && bmp.equalsIgnoreCase("BMP")) {
            return false;
        }

        boolean ejbspec2 = EjbTagsHandler.getEjbSpec().equals(EjbDocletTask.EjbSpecVersion.EJB_2_0);

        if (ejbspec2 == false) {
            return false;
        }

        String cmp = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:bean",
            "cmp-version",
            EntityCmpSubTask.CmpSpecVersion.getVersions(),
            null,
            true,
            false
            );

        if (cmp == null) {
            EntityCmpSubTask entityCmpSubtask = ((EntityCmpSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityCmpSubTask.class)));

            if (entityCmpSubtask != null) {
                cmp = entityCmpSubtask.getCmpSpec();
            }
            else {
                throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.CANT_GUESS_CMP_VERSION, new String[]{clazz.getQualifiedName()}));
            }
        }

        return EntityCmpSubTask.CmpSpecVersion.CMP_2_0.equals(cmp);
    }

    /**
     * Gets the EntityCmpClassPattern attribute of the CmpTagsHandler class
     *
     * @return   The EntityCmpClassPattern value
     */
    protected static String getEntityCmpClassPattern()
    {
        EntityCmpSubTask entityCmpSubtask = ((EntityCmpSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityCmpSubTask.class)));

        if (entityCmpSubtask != null) {
            return entityCmpSubtask.getEntityCmpClassPattern();
        }
        else {
            return EntityCmpSubTask.DEFAULT_ENTITYCMP_CLASS_PATTERN;
        }
    }

    /**
     * Returns the name of generated CMP class.
     *
     * @return                      The name of generated CMP class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String entityCmpClass() throws XDocletException
    {
        return getEntityCmpClassFor(getCurrentClass());
    }

    /**
     * Evaluate the body block if using EJB 2.0 and CMP version 2.x.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isUsingCmp2Impl(xjavadoc.XClass)
     * @see                         #ifNotUsingCmp2(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifUsingCmp2(String template) throws XDocletException
    {
        if (isUsingCmp2Impl(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if not using EJB 2.0 or using EJB 2.0 but CMP version 1.x.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isUsingCmp2Impl(xjavadoc.XClass)
     * @see                         #ifUsingCmp2(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifNotUsingCmp2(String template) throws XDocletException
    {
        if (!isUsingCmp2Impl(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if current class is an CMP entity bean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifEntityIsCmp(String template) throws XDocletException
    {
        if (isEntityCmp(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block for each EJBean derived from EntityBean which is CMP.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntityCmp(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllCmpEntityBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isEntityCmp(getCurrentClass())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluates the body block for each persistent field of current class (if entity CMP). Looks at super classes as
     * well. Searches for the getter methods which has ejb:persistent-field defined.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntityCmp(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.entity.PersistentTagsHandler#isPersistentField(xjavadoc.XMethod)
     * @see                         xdoclet.tagshandler.MethodTagsHandler#isGetter(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void forAllCmpFields(String template) throws XDocletException
    {
        if (isEntityCmp(getCurrentClass())) {
            XClass oldClass = getCurrentClass();
            XClass superclass = null;
            List already = new ArrayList();

            do {
                Collection methods = getCurrentClass().getMethods();
                XMethod oldCurrentMethod = getCurrentMethod();

                for (Iterator j = methods.iterator(); j.hasNext(); ) {
                    XMethod currentMethod = (XMethod) j.next();

                    if (!already.contains(currentMethod.getName())) {
                        setCurrentMethod(currentMethod);

                        if (PersistentTagsHandler.isPersistentField(currentMethod) && MethodTagsHandler.isGetter(currentMethod.getName())) {
                            generate(template);
                        }

                        already.add(currentMethod.getName());
                    }
                }

                setCurrentMethod(oldCurrentMethod);

                // Add super class info
                superclass = getCurrentClass().getSuperclass();

                if (superclass != null) {
                    pushCurrentClass(superclass);
                }

            } while (superclass != null);

            setCurrentClass(oldClass);
        }
    }

    /**
     * Returns the dbms column. Looks for ejb.persistence column-name, then for legacy app-server specific tags, then
     * propertyName as a fall-back
     *
     * @return
     * @exception XDocletException
     * @todo                        add more tags/params here
     */
    public String dbmsColumn() throws XDocletException
    {
        String tags = "ejb.persistence,jboss.column-name,jboss.column-name,weblogic.dbms-column";
        String attribs = "column-name,name,,";
        Properties props = new Properties();

        props.setProperty("tagName", tags);
        props.setProperty("paramName", attribs);

        String result = getTagValue(props, FOR_METHOD);

        if (result == null) {
            result = MethodTagsHandler.getPropertyNameFor(getCurrentMethod());
        }
        return result;
    }

    /**
     * Returns the table name for the current class.
     *
     * @return
     * @exception XDocletException
     * @todo                        add more tags/params here
     */
    public String dbmsTable() throws XDocletException
    {
        String tags = "ejb.persistence,jboss.table-name,jboss.table-name,weblogic.table-name";
        String attribs = "table-name,name,,";
        Properties props = new Properties();

        props.setProperty("tagName", tags);
        props.setProperty("paramName", attribs);

        String result = getTagValue(props, FOR_CLASS);

        if (result == null) {
            result = getCurrentClass().getName();
        }
        return result;
    }

    public void ifIsPersistent(String template) throws XDocletException
    {
        boolean persistent = getCurrentClass().getMethodTags("ejb.persistence", true).size() != 0;

        persistent = persistent || getCurrentClass().getMethodTags("jboss.table-name", true).size() != 0;
        persistent = persistent || getCurrentClass().getMethodTags("weblogic.table-name", true).size() != 0;

        if (persistent) {
            generate(template);
        }
    }
}
