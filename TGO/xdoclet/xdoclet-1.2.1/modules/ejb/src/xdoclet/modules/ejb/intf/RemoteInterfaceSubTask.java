/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.intf;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;
import xjavadoc.XTag;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;

import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.tagshandler.PackageTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates remote interfaces for EJBs.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Remote Interface" name="remoteinterface"
 *      parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="remote-custom.xdt" relates-to="{0}.java" description="A text file containing custom
 *      template and/or java code to include in the remote interface."
 */
public class RemoteInterfaceSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public static String DEFAULT_REMOTE_CLASS_PATTERN = "{0}";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/remote.xdt";

    /**
     * A configuration parameter for specifying the remote interface name pattern. By default the value is used for
     * deciding the remote interface name. {0} in the value mean current class's symbolic name which for an EJBean is
     * the EJB name.
     *
     * @see   #getRemoteClassPattern()
     */
    protected String remoteClassPattern;

    /**
     * Describe what the RemoteInterfaceSubTask constructor does
     */
    public RemoteInterfaceSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getRemoteClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("javax.ejb.SessionBean");
    }

    /**
     * Returns the configuration parameter for specifying the remote interface name pattern. By default the value is
     * used for deciding the remote interface name. {0} in the value mean current class's symbolic name which for an
     * EJBean is the EJB name. If nothing explicitly specified by user then "{0}" is used by default.
     *
     * @return   The RemoteClassPattern value
     * @see      #remoteClassPattern
     */
    public String getRemoteClassPattern()
    {
        if (remoteClassPattern != null) {
            return remoteClassPattern;
        }
        else {
            return DEFAULT_REMOTE_CLASS_PATTERN;
        }
    }

    /**
     * The pattern by which the interfaces are named. {0} designates the EJB name.
     *
     * @param newPattern   The new Pattern value
     * @ant.not-required   No, defaults to {0}
     */
    public void setPattern(String newPattern)
    {
        remoteClassPattern = newPattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getRemoteClassPattern() == null || getRemoteClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getRemoteClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }


    /**
     * Gets the GeneratedFileName attribute of the RemoteInterfaceSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(InterfaceTagsHandler.getComponentInterface("remote", getCurrentClass())) + ".java";
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        if (super.matchesGenerationRules(clazz) == false) {
            return false;
        }

        Log log = LogUtil.getLog(RemoteInterfaceSubTask.class, "matchesGenerationRules");

        if (InterfaceTagsHandler.isOnlyLocalEjb(getCurrentClass())) {
            log.debug("Reject file " + clazz.getQualifiedName() + " because of view-type local");
            return false;
        }

        XTag interfaceTag = getCurrentClass().getDoc().getTag("ejb:interface");

        if (interfaceTag == null) {
            return true;
        }

        String generate = interfaceTag.getAttributeValue("generate");

        if ((generate != null) && (generate.indexOf("remote") == -1)) {
            log.debug("Skip remote interface for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        return true;
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_REMOTE_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }
}
