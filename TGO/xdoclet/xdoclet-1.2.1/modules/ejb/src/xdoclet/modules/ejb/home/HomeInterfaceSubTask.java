/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.home;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;
import xjavadoc.XTag;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;

import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.home.HomeTagsHandler;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.tagshandler.PackageTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates remote home interfaces for EJBs.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Home Interface" name="homeinterface" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="home-custom.xdt" relates-to="{0}Home.java" description="A text file containing custom
 *      template and/or java code to include in the home interface."
 */
public class HomeInterfaceSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_HOMEINTERFACE_CLASS_PATTERN = "{0}Home";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/home.xdt";

    /**
     * A configuration parameter for specifying the home interface name pattern. By default the value is used for
     * deciding the home interface name. {0} in the value mean current class's symbolic name which for an EJBean is the
     * EJB name.
     *
     * @see   #getHomeClassPattern()
     */
    protected String homeClassPattern;

    /**
     * Describe what the HomeInterfaceSubTask constructor does
     */
    public HomeInterfaceSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getHomeClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("javax.ejb.SessionBean");
    }


    /**
     * Returns the configuration parameter for specifying the home interface name pattern. By default the value is used
     * for deciding the home interface name. {0} in the value mean current class's symbolic name which for an EJBean is
     * the EJB name. If nothing explicitly specified by user then "{0}Home" is used by default.
     *
     * @return   The HomeClassPattern value
     * @see      #homeClassPattern
     */
    public String getHomeClassPattern()
    {
        if (homeClassPattern != null) {
            return homeClassPattern;
        }
        else {
            return DEFAULT_HOMEINTERFACE_CLASS_PATTERN;
        }
    }


    /**
     * The pattern by which the home interfaces are named. {0} designates the EJB name.
     *
     * @param new_pattern  The new Pattern value
     * @ant.not-required   No, defaults to {0}Home
     */
    public void setPattern(String new_pattern)
    {
        Log log = LogUtil.getLog(HomeInterfaceSubTask.class, "setPattern");

        log.debug("Set pattern to " + new_pattern);

        homeClassPattern = new_pattern;
    }


    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getHomeClassPattern() == null || getHomeClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getHomeClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }


    /**
     * Gets the GeneratedFileName attribute of the HomeInterfaceSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(HomeTagsHandler.getHomeInterface("remote", getCurrentClass())) + ".java";
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

        Log log = LogUtil.getLog(HomeInterfaceSubTask.class, "matchesGenerationRules");

        if (InterfaceTagsHandler.isOnlyLocalEjb(getCurrentClass())) {
            log.debug("Reject file " + clazz.getQualifiedName() + " because of view-type local");
            return false;
        }

        XTag interfaceTag = getCurrentClass().getDoc().getTag("ejb:interface", false);

        if (interfaceTag == null) {
            return true;
        }

        String generate = interfaceTag.getAttributeValue("generate");

        if ((generate != null) && (generate.indexOf("remote") == -1)) {
            log.debug("Skip home interface for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
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
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_HOME_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

}
