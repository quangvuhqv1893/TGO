/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.mdb;

import java.util.Collection;
import java.util.Iterator;

import xjavadoc.*;

import xdoclet.DocletSupport;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbMdb"
 * @version              $Revision: 1.9 $
 */
public class MdbTagsHandler extends EjbTagsHandler
{
    /**
     * Returns true if clazz is a message-driven bean, false otherwise.
     *
     * @param clazz  Description of Parameter
     * @return       The MessageDriven value
     */
    public static boolean isMessageDriven(XClass clazz)
    {
        return clazz.isA("javax.ejb.MessageDrivenBean");
    }

    /**
     * Gets the MessageDrivenClassFor attribute of the MdbTagsHandler class
     *
     * @param clazz  Describe what the parameter does
     * @return       The MessageDrivenClassFor value
     */
    public static String getMessageDrivenClassFor(XClass clazz)
    {
        return clazz.getQualifiedName();
    }

    /**
     * Evaluates the body block for each EJBean derived from MessageDrivenBean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isMessageDriven(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllMDBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (isMessageDriven(getCurrentClass())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluate the body block if current class is of a message driven bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifMessageDriven(String template) throws XDocletException
    {
        if (isMessageDriven(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current class is not of a message driven bean type.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifNotMessageDriven(String template) throws XDocletException
    {
        if (!isMessageDriven(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Returns the name of message-driven bean class.
     *
     * @return                      The name of generated message-driven bean class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String messageDrivenClass() throws XDocletException
    {
        return getMessageDrivenClassFor(getCurrentClass());
    }
}

