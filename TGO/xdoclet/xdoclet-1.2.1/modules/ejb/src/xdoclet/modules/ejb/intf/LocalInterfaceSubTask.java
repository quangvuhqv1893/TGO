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
 * Generates local interfaces for EJBs.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Local Interface" name="localinterface" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="local-custom.xdt" relates-to="{0}Local.java" description="A text file containing custom
 *      template and/or java code to include in the local interface."
 */
public class LocalInterfaceSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_LOCAL_CLASS_PATTERN = "{0}Local";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/local.xdt";

    /**
     * A configuration parameter for specifying the local interface name pattern. By default the value is used for
     * deciding the local interface name. {0} in the value mean current class's symbolic name which for an EJBean is the
     * EJB name.
     *
     * @see   #getLocalClassPattern()
     */
    protected String localClassPattern;


    /**
     */
    public LocalInterfaceSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getLocalClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("javax.ejb.SessionBean");
    }

    /**
     * Returns the configuration parameter for specifying the local interface name pattern. By default the value is used
     * for deciding the local interface name. {0} in the value mean current class's symbolic name which for an EJBean is
     * the EJB name. If nothing explicitly specified by user then "{0}Local" is used by default.
     *
     * @return   The LocalClassPattern value
     * @see      #localClassPattern
     */
    public String getLocalClassPattern()
    {
        if (localClassPattern != null) {
            return localClassPattern;
        }
        else {
            return DEFAULT_LOCAL_CLASS_PATTERN;
        }
    }


    /**
     * The pattern by which the interfaces are named. {0} designates the EJB name.
     *
     * @param new_pattern
     * @ant.not-required   No, defaults to {0}Local
     */
    public void setPattern(String new_pattern)
    {
        localClassPattern = new_pattern;
    }


    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getLocalClassPattern() == null || getLocalClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getLocalClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }


    /**
     * Gets the GeneratedFileName attribute of the LocalInterfaceSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(InterfaceTagsHandler.getComponentInterface("local", getCurrentClass())) + ".java";
    }


    /**
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     * @todo                        (Aslak) This needs refactoring. Nearly all matchesGenerationRules implementations
     *      are the same!
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        if (super.matchesGenerationRules(clazz) == false) {
            return false;
        }

        Log log = LogUtil.getLog(LocalInterfaceSubTask.class, "matchesGenerationRules");

        if (InterfaceTagsHandler.isOnlyRemoteEjb(getCurrentClass())) {
            log.debug("Reject file " + clazz.getQualifiedName() + " because of view-type remote");
            return false;
        }

        XTag interfaceTag = getCurrentClass().getDoc().getTag("ejb:interface");

        if (interfaceTag == null) {
            return true;
        }

        String generate = interfaceTag.getAttributeValue("generate");

        if ((generate != null) && (generate.indexOf("local") == -1)) {
            log.debug("Skip remote interface for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        return true;
    }


    /**
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_LOCAL_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

}
