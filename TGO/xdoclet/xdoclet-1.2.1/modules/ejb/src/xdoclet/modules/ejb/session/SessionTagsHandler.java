/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.session;

import java.text.MessageFormat;
import java.util.*;

import xjavadoc.*;
import xdoclet.DocletContext;
import xdoclet.DocletSupport;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.session.SessionSubTask;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbSession"
 * @version              $Revision: 1.12 $
 */
public class SessionTagsHandler extends EjbTagsHandler
{
    /**
     * Gets the SessionClassFor attribute of the SessionTagsHandler class
     *
     * @param clazz  Describe what the parameter does
     * @return       The SessionClassFor value
     */
    public static String getSessionClassFor(XClass clazz)
    {
        String fileName = clazz.getContainingPackage().getName();
        String sessionName = MessageFormat.format(getSessionClassPattern(), new Object[]{getShortEjbNameFor(clazz)});

        // Fix package name
        fileName = choosePackage(fileName, null, DocletTask.getSubTaskName(SessionSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += sessionName;

        return fileName;
    }

    /**
     * Returns true if clazz is a session bean, false otherwise.
     *
     * @param clazz  Description of Parameter
     * @return       The Session value
     */
    public static boolean isSession(XClass clazz)
    {
        return clazz.isA("javax.ejb.SessionBean");
    }

    /**
     * Gets the SessionClassPattern attribute of the SessionTagsHandler class
     *
     * @return   The SessionClassPattern value
     */
    protected static String getSessionClassPattern()
    {
        SessionSubTask sessionSubtask = ((SessionSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(SessionSubTask.class)));

        if (sessionSubtask != null) {
            return sessionSubtask.getSessionClassPattern();
        }
        else {
            return SessionSubTask.DEFAULT_SESSION_CLASS_PATTERN;
        }
    }

    /**
     * Returns true if clazz is a stateful session bean, false otherwise. Entity type is determined by looking at the
     * ejb:bean's type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The StatefulSession value
     * @exception XDocletException
     */
    public boolean isStatefulSession(XClass clazz) throws XDocletException
    {
        if (isSession(clazz) == false) {
            return false;
        }

        String value = getCurrentClass().getDoc().getTagAttributeValue("ejb:bean", "type", false);

        if (value != null && value.equals("Stateful")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if clazz is a stateless session bean, false otherwise. Entity type is determined by looking at the
     * ejb:bean's type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The StatelessSession value
     * @exception XDocletException
     */
    public boolean isStatelessSession(XClass clazz) throws XDocletException
    {
        if (isSession(clazz) == false) {
            return false;
        }

        String value = getCurrentClass().getDoc().getTagAttributeValue("ejb:bean", "type", false);

        if (value != null) {
            return value.equals("Stateless");
        }
        else {
            // it's stateful if it implements SessionSynchronization
            if (clazz.isA("javax.ejb.SessionSynchronization")) {
                return false;
            }

            // it's stateful if it has create methods with parameters,
            // stateless if has a single ejbCreate() method with no args and returning void
            Collection methods = clazz.getMethods();
            boolean hasEmptyCreateMethod = false;
            boolean hasOtherCreateMethods = false;

            for (Iterator i = methods.iterator(); i.hasNext(); ) {
                XMethod method = (XMethod) i.next();

                // if an empty create method
                if (method.getName().equals("ejbCreate") && method.getParameters().size() == 0) {
                    hasEmptyCreateMethod = true;
                }
                else if (method.getName().startsWith("ejbCreate") &&
                    method.getParameters().size() > 0 &&
                    method.getReturnType().getType().getQualifiedName().equals("void")) {
                    hasOtherCreateMethods = true;
                }
            }

            if (hasEmptyCreateMethod == true && hasOtherCreateMethods == false) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Returns the name of generated session class.
     *
     * @return                      The name of generated session class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String sessionClass() throws XDocletException
    {
        return getSessionClassFor(getCurrentClass());
    }

    /**
     * Evaluate the body block if current class is of an stateless session bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatelessSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifStatelessSession(String template) throws XDocletException
    {
        if (isStatelessSession(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current class is not of an stateless session bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatelessSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifNotStatelessSession(String template) throws XDocletException
    {
        if (!isStatelessSession(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current class is of an stateful session bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatefulSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifStatefulSession(String template) throws XDocletException
    {
        if (isStatefulSession(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current class is not of a stateful session bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatefulSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void ifNotStatefulSession(String template) throws XDocletException
    {
        if (!isStatefulSession(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block for each EJBean derived from SessionBean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllSessionBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isSession(getCurrentClass())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluates the body block for each EJBean derived from SessionBean which is stateful.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatefulSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllStatefulSessionBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isStatefulSession(getCurrentClass())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluates the body block for each EJBean derived from SessionBean which is stateless.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isStatelessSession(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllStatelessSessionBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isStatelessSession(getCurrentClass())) {
                generate(template);
            }
        }
    }
}
