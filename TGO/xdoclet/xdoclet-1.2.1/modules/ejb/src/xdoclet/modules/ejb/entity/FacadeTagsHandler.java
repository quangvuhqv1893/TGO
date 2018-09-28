/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.text.MessageFormat;
import java.util.*;

import xjavadoc.*;
import xdoclet.DocletContext;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.entity.EntityFacadeSubTask;
import xdoclet.modules.ejb.session.RemoteFacadeSubTask;

/**
 * @author               Konstantin Pribluda (kpriblouda@yahoo.com)
 * @created              September 8, 2002
 * @xdoclet.taghandler   namespace="EjbFacade"
 * @version              $Revision: 1.3 $
 */
public class FacadeTagsHandler extends EntityTagsHandler
{

    public static String getEntityFacadeClassFor(XClass clazz)
    {
        String fileName = clazz.getContainingPackage().getName();
        String entityName = MessageFormat.format(getEntityFacadeClassPattern(), new Object[]{EjbTagsHandler.getShortEjbNameFor(clazz)});


        // Fix package name
        fileName = choosePackage(fileName, null, DocletTask.getSubTaskName(EntityFacadeSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += entityName;

        return fileName;
    }

    public static String getRemoteFacadeClassFor(XClass clazz)
    {
        String fileName = clazz.getContainingPackage().getName();
        String entityName = MessageFormat.format(getRemoteFacadeClassPattern(), new Object[]{EjbTagsHandler.getShortEjbNameFor(clazz)});


        // Fix package name
        fileName = choosePackage(fileName, null, DocletTask.getSubTaskName(RemoteFacadeSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += entityName;

        return fileName;
    }

    /**
     * Gets the EntityFacadeClassPattern attribute of the CmpTagsHandler class
     *
     * @return   The EntityFacadeClassPattern value
     */
    protected static String getRemoteFacadeClassPattern()
    {
        RemoteFacadeSubTask remoteFacadeSubtask = ((RemoteFacadeSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(RemoteFacadeSubTask.class)));

        if (remoteFacadeSubtask != null) {
            return remoteFacadeSubtask.getRemoteFacadeClassPattern();
        }
        else {
            return RemoteFacadeSubTask.DEFAULT_REMOTE_FACADE_CLASS_PATTERN;
        }
    }


    protected static String getEntityFacadeClassPattern()
    {
        EntityFacadeSubTask entityFacadeSubtask = ((EntityFacadeSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityFacadeSubTask.class)));

        if (entityFacadeSubtask != null) {
            return entityFacadeSubtask.getEntityFacadeClassPattern();
        }
        else {
            return EntityFacadeSubTask.DEFAULT_ENTITY_FACADE_CLASS_PATTERN;
        }
    }


    /**
     * Gets the EntityFacadeEjbNamePattern attribute of the FacadeTagsHandler class
     *
     * @return   The EntityFacadeEjbNamePattern value
     */
    protected static String getEntityFacadeEjbNamePattern()
    {
        EntityFacadeSubTask entityFacadeSubtask = ((EntityFacadeSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityFacadeSubTask.class)));

        if (entityFacadeSubtask != null) {
            return entityFacadeSubtask.getEntityFacadeEjbNamePattern();
        }
        else {
            return EntityFacadeSubTask.DEFAULT_FACADE_EJB_NAME_PATTERN;
        }
    }

    public String getFacadeEjbNameFor(XClass clazz) throws XDocletException
    {
        // do we have name specified?
        String name = clazz.getDoc().getTagAttributeValue("ejb.facade", "name", false);

        if (name != null && !"".equals(name)) {
            return name;
        }
        else {
            return MessageFormat.format(getEntityFacadeEjbNamePattern(), new Object[]{EjbTagsHandler.getShortEjbNameFor(clazz)});
        }

    }

    /**
     * produce class name for ejb facade
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String entityFacadeClass() throws XDocletException
    {
        return getEntityFacadeClassFor(XDocletTagSupport.getCurrentClass());
    }

    /**
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String remoteFacadeClass() throws XDocletException
    {
        return getRemoteFacadeClassFor(XDocletTagSupport.getCurrentClass());
    }


    /**
     * prodice facade ejb name. Default would be
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String facadeEjbName() throws XDocletException
    {
        return getFacadeEjbNameFor(XDocletTagSupport.getCurrentClass());
    }

    public boolean canUseLocal() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();

        return !EjbTagsHandler.isOnlyRemoteEjb(clazz);
    }

    /**
     * decide whether we have to use local interface of the bean
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifUseLocalInterface(String template, Properties attributes) throws XDocletException
    {
        if (canUseLocal()) {
            generate(template);
        }
    }

    /**
     * decide whether we have to use remote interface of the bean
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifUseRemoteInterface(String template, Properties attributes) throws XDocletException
    {
        if (!canUseLocal()) {
            generate(template);
        }
    }

    /**
     * provide session type
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String sessionType() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();
        String type = clazz.getDoc().getTagAttributeValue("ejb.facade", "type", false);

        if ("Statefull".equals(type)) {
            return "Statefull";
        }
        else {
            return "Stateless";
        }
    }

    /**
     * provide view type for facade bean - inherit from class if not specified
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String viewType() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();
        String type = clazz.getDoc().getTagAttributeValue("ejb.bean", "view-type", false);
        String facadeType = clazz.getDoc().getTagAttributeValue("ejb.facade", "view-type", false);

        if (facadeType != null) {
            return facadeType;
        }

        if (type != null) {
            return type;
        }

        return "remote";
    }

    /**
     * local jndi name if any
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String localJndiName() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();
        String jndi = clazz.getDoc().getTagAttributeValue("ejb.facade", "local-jndi-name", false);

        if (jndi != null) {
            return jndi;
        }

        return facadeEjbName() + "Local";
    }

    /**
     * jndi name if any or default
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String jndiName() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();
        String jndi = clazz.getDoc().getTagAttributeValue("ejb.facade", "jndi-name", false);

        if (jndi != null) {
            return jndi;
        }

        return "ejb/" + facadeEjbName();
    }

    /**
     * generate permission spec - inherit from bean
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */

    public String beanPermission() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();

        String permission = clazz.getDoc().getTagAttributeValue("ejb.facade", "permission", false);

        if (permission != null) {
            // explicit spec, return it
            return "* @ejb.permission role-name=\"" + permission + "\"";
        }

        String unchecked = clazz.getDoc().getTagAttributeValue("ejb.facade", "unchecked", false);

        if (unchecked != null) {
            // unchecked perm
            return "* @ejb.permission unchecked=\"true\"";
        }

        // the same from bean...
        permission = clazz.getDoc().getTagAttributeValue("ejb.permission", "role-name", true);

        if (permission != null) {
            // explicit spec, return it
            return "* @ejb.permission role-name=\"" + permission + "\"";
        }

        unchecked = clazz.getDoc().getTagAttributeValue("ejb.permission", "unchecked", true);

        if (unchecked != null) {
            // unchecked perm
            return "* @ejb.permission unchecked=\"true\"";
        }

        return "*";
    }

    /**
     * generate bean reference
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */

    public String beanRef() throws XDocletException
    {
        XClass clazz = XDocletTagSupport.getCurrentClass();

        if (canUseLocal()) {
            return "* @ejb.ejb-ref ejb-name=\"" + EjbTagsHandler.getEjbNameFor(clazz) + "\" view-type=\"local\"";
        }
        else {
            return "* @ejb.ejb-ref ejb-name=\"" + EjbTagsHandler.getEjbNameFor(clazz) + "\"view-type=\"remote\"";
        }
    }
}
