/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.lang.reflect.Modifier;

import java.util.Collection;
import java.util.Iterator;

import xjavadoc.*;
import xdoclet.DocletSupport;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.entity.BmpTagsHandler;
import xdoclet.modules.ejb.entity.CmpTagsHandler;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbEntity"
 * @version              $Revision: 1.10 $
 */
public class EntityTagsHandler extends EjbTagsHandler
{
    /**
     * Returns true if clazz is an entity bean, false otherwise.
     *
     * @param clazz  Description of Parameter
     * @return       The Entity value
     */
    public static boolean isEntity(XClass clazz)
    {
        return clazz.isA("javax.ejb.EntityBean");
    }

    public static boolean isEjbSelectMethod(XMethod method)
    {
        // if prefixed ejbSelect exactly
        if (method.getName().startsWith("ejbSelect") == false)
            return false;

        // if ejbSelect<blabla>
        if (method.getName().length() <= "ejbSelect".length())
            return false;

        // if is abstract and public
        if ((method.getModifierSpecifier() & Modifier.ABSTRACT) == 0 || (method.getModifierSpecifier() & Modifier.PUBLIC) == 0)
            return false;

        // if has a return type of non-void
        if (method.getReturnType().getType().getName().equals("void"))
            return false;

        // if throws FindException
        //if (method.throwsException("javax.ejb.FinderException") == false)
        //    return false;

        // if defines the ejbql (later we can make it optional and guess the ejbql from the method name and cmp field names)
        //if (method.getDoc().hasTag("ejb:select") == false)
        //    return false;

        return true;
    }

    /**
     * Evaluate the body block if current class is of an entity type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntity(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifEntity(String template) throws XDocletException
    {
        if (isEntity(getCurrentClass())) {
            generate(template);
        }
    }


    /**
     * Returns the persistent type of current bean.
     *
     * @return                      "Container" or "Bean".
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.CmpTagsHandler#isEntityCmp(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.entity.BmpTagsHandler#isEntityBmp(xjavadoc.XClass)
     * @doc.tag                     type="content"
     */
    public String persistenceType() throws XDocletException
    {
        if (CmpTagsHandler.isEntityCmp(getCurrentClass()) &&
            !BmpTagsHandler.isEntityBmp(getCurrentClass())) {
            return "Container";
        }
        else {
            return "Bean";
        }
    }


    /**
     * Evaluates the body block for each EJBean derived from EntityBean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntity(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllEntityBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isEntity(getCurrentClass())) {
                generate(template);
            }
        }
    }


    /**
     * Returns True if ejb:bean reentrant is true, False otherwise. It does the case conversion trick from true to True
     * and false to False.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @see                         #isEntity(xjavadoc.XClass)
     * @doc.tag                     type="content"
     */
    public String reentrant() throws XDocletException
    {
        return getTagValue(
            FOR_CLASS,
            "ejb:bean",
            "reentrant",
            null,
            "False",
            true,
            false
            );
    }

    /**
     * Evaluates the body block for each ejbSelect<blabla> method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isEntity(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllEjbSelectMethods(String template) throws XDocletException
    {
        XMethod old_cur_method = getCurrentMethod();

        for (Iterator i = getCurrentClass().getMethods(true).iterator(); i.hasNext(); ) {
            XMethod method = (XMethod) i.next();

            if (isEjbSelectMethod(method)) {
                setCurrentMethod(method);
                generate(template);
            }
        }

        setCurrentMethod(old_cur_method);
    }
}
