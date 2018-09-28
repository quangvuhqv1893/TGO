/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dao;

import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;

import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.util.LogUtil;

/**
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              February 8, 2002
 * @xdoclet.taghandler   namespace="EjbDao"
 * @version              $Revision: 1.4 $
 */
public class DaoTagsHandler extends EjbTagsHandler
{
    /**
     * Similar to {@link xdoclet.modules.ejb.intf.InterfaceTagsHandler#getComponentInterface}. Relies on the ejb.dao
     * tag, which has the following relevant properties:
     * <ul>
     *   <li> class: The fully qualified name of the DAO class - overrides all set patterns
     *   <li> pattern: The pattern to be used to determine the unqualified name of the DAO class
     *   <li> package: The package the DAO is to be placed in
     * </ul>
     *
     *
     * @param clazz  Description of Parameter
     * @return       The DAO value
     */
    public static String getDaoClassFor(XClass clazz)
    {
        Log log = LogUtil.getLog(DaoTagsHandler.class, "daoClassName");

        String fileName = clazz.getContainingPackage().getName();
        String daoPattern = null;

        if (log.isDebugEnabled()) {
            log.debug("dao for " + clazz.getQualifiedName());
        }

        daoPattern = getDaoClassPattern();

        String daoClass = clazz.getDoc().getTagAttributeValue("ejb.dao", "class", false);

        if (daoClass != null) {
            return daoClass;
        }

        String ejbName = null;
        String packagePattern = null;

        if (daoPattern.indexOf("{0}") != -1) {
            ejbName = MessageFormat.format(daoPattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            ejbName = daoPattern;
        }

        // Fix package name
        fileName = choosePackage(fileName, packagePattern, DocletTask.getSubTaskName(DaoSubTask.class));
        fileName += "." + ejbName;

        return fileName;
    }


    /**
     * Gets the DaoClassPattern attribute of the DaoTagsHandler class
     *
     * @return   The DaoClassPattern value
     */
    protected static String getDaoClassPattern()
    {
        DaoSubTask daoSubtask = ((DaoSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(DaoSubTask.class)));

        if (daoSubtask != null) {
            return daoSubtask.getDaoClassPattern();
        }
        else {
            return DaoSubTask.DEFAULT_DAO_CLASS_PATTERN;
        }
    }


    /**
     * Gets the DaoSubTaskActive attribute of the DaoTagsHandler class
     *
     * @return   The DaoSubTaskActive value
     */
    private static boolean isDaoSubTaskActive()
    {
        return DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(DaoSubTask.class));
    }

    /**
     * Returns the full qualified dao class name for the bean
     *
     * @param attributes            The attributes of the template tag
     * @return                      DAO class name
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String daoClass(Properties attributes) throws XDocletException
    {
        return getDaoClassFor(getCurrentClass());
    }


    /**
     * Evaluate the body block if ejb.dao tag present and DAO subtask being used.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifUsingDao(String template) throws XDocletException
    {
        if (isDaoSubTaskActive() && getCurrentClass().getDoc().hasTag("ejb.dao", false)) {
            generate(template);
        }
    }

}
