/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.lookup;

import java.text.MessageFormat;
import java.util.Properties;
import org.apache.commons.logging.Log;

import xjavadoc.XClass;
import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;

import xdoclet.modules.ejb.lookup.LookupObjectSubTask;
import xdoclet.modules.ejb.session.SessionTagsHandler;
import xdoclet.util.LogUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="EjbUtilObj"
 * @version              $Revision: 1.10 $
 */
public class LookupUtilTagsHandler extends EjbTagsHandler
{
    /**
     * Similar to {@link xdoclet.modules.ejb.intf.InterfaceTagsHandler#getComponentInterface}. Relies on the ejb:home
     * tag, which has the following relevant properties:
     * <ul>
     *   <li> remote-class: The fully qualified name of the remote class - overrides all set patterns
     *   <li> local-class: The fully qualified name of the local class - overrides all set patterns
     *   <li> remote-pattern: The pattern to be used to determine the unqualified name of the remote class
     *   <li> local-pattern: The pattern to be used to determine the unqualified name of the local class
     *   <li> pattern: The pattern to be used in determining the unqualified remote and/or local home interface name -
     *   used where remote- or local- pattern are not specified.
     *   <li> remote-package: The package the remote home interface is to be placed in
     *   <li> local-package: The package the local home interface is to be placed in
     *   <li> package: The package the remote and/or local home interface is to be placed in - used where remote- or
     *   local- package are not specified.
     * </ul>
     *
     *
     * @param clazz  Description of Parameter
     * @return       The HomeInterface value
     */
    public static String getUtilClassFor(XClass clazz)
    {
        Log log = LogUtil.getLog(LookupUtilTagsHandler.class, "utilClassName");

        String fileName = clazz.getContainingPackage().getName();
        String utilPattern = null;

        if (log.isDebugEnabled()) {
            log.debug("utility object for " + clazz.getQualifiedName());
        }

        utilPattern = getUtilClassPattern();

        String ejbName = null;
        String packagePattern = null;

        if (utilPattern.indexOf("{0}") != -1) {
            ejbName = MessageFormat.format(utilPattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            ejbName = utilPattern;
        }

        // Fix package name
        fileName = choosePackage(fileName, packagePattern, DocletTask.getSubTaskName(LookupObjectSubTask.class));
        fileName += '.' + ejbName;

        return fileName;
    }

    /**
     * Gets the UtilClassPattern attribute of the UtilTagsHandler class
     *
     * @return   The UtilClassPattern value
     */
    protected static String getUtilClassPattern()
    {
        LookupObjectSubTask utilSubtask = ((LookupObjectSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(LookupObjectSubTask.class)));

        if (utilSubtask != null) {
            return utilSubtask.getUtilClassPattern();
        }
        else {
            return LookupObjectSubTask.DEFAULT_UTIL_CLASS_PATTERN;
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String lookupKind() throws XDocletException
    {
        String kind = getTagValue(
            FOR_CLASS,
            getCurrentClass().getDoc(),
            "ejb:util",
            "generate",
            "physical,logical",
            null,
            true,
            false
            );

        //if explicitly specified then use it, otherwise use the kind config param of <utilobject/>
        if (kind == null) {
            kind = (String) getDocletContext().getConfigParam(DocletTask.getSubTaskName(LookupObjectSubTask.class) + ".kind");
        }

        if (kind.equals("physical")) {
            return "JNDI_NAME";
        }
        else {
            return "COMP_NAME";
        }
    }

    /**
     * Returns the full qualified utility class name for the bean
     *
     * @param attributes            The attributes of the template tag
     * @return                      Utility class name
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String utilClass(Properties attributes) throws XDocletException
    {
        return getUtilClassFor(getCurrentClass());
    }

}

