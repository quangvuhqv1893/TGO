/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.lookup;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;

import xdoclet.util.Translator;

/**
 * @author               Konstantin Pribluda
 * @created              October 3, 2001
 * @ant.element          display-name="Lookup Object" name="utilobject" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.12 $
 * @xdoclet.merge-file   file="util-custom.xdt" relates-to="{0}Util.java" description="A text file containing custom
 *      template and/or java code to include in the utility class."
 * @xdoclet.merge-file   file="lookup-custom.xdt" relates-to="{0}Util.java" description="A text file containing custom
 *      template and/or java code to include in the utility class."
 */
public class LookupObjectSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_UTIL_CLASS_PATTERN = "{0}Util";

    private static String DEFAULT_TEMPLATE_FILE = "resources/lookup.xdt";

    /**
     * A configuration parameter for specifying the utility class name pattern. By default the value is used for
     * deciding the utility name. {0} in the value mean current class's symbolic name which for an EJBean is the EJB
     * name.
     *
     * @see   #getUtilClassPattern()
     */
    private String  utilClassPattern;

    /**
     * Include a performant GUID generator in the util object.
     */
    private boolean includeGUID = false;

    /**
     * Cache the homes?
     */
    private boolean cacheHomes = false;

    /**
     * The preferred kind of lookup code, which is either logical or physical.
     */
    private String  kind = LookupKind.LOGICAL;

    /**
     * should local proxies to session beans be generated
     */
    private boolean localProxies = false;

    /**
     * Describe what the UtilObjectSubTask constructor does
     */
    public LookupObjectSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getUtilClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("javax.ejb.SessionBean");
    }

    /**
     * should local proxies be returned instead of home interfaces be returned for SLSBs?
     *
     * @return
     */
    public boolean getLocalProxies()
    {
        return localProxies;
    }

    /**
     * Gets the Kind attribute of the UtilObjectSubTask object
     *
     * @return   The Kind value
     */
    public String getKind()
    {
        return kind;
    }

    /**
     * Include a performant GUID generator in the util object.
     *
     * @return
     */
    public boolean getIncludeGUID()
    {
        return includeGUID;
    }

    /**
     * Cache the homes?
     *
     * @return
     */
    public boolean getCacheHomes()
    {
        return cacheHomes;
    }

    /**
     * Returns the configuration parameter for specifying the utility class name pattern. By default the value is used
     * for deciding the utility name. {0} in the value mean current class's symbolic name which for an EJBean is the EJB
     * name. If nothing explicitly specified by user then "{0}Util" is used by default.
     *
     * @return   The UtilClassPattern value
     */
    public String getUtilClassPattern()
    {
        if (utilClassPattern != null) {
            return utilClassPattern;
        }
        else {
            return DEFAULT_UTIL_CLASS_PATTERN;
        }
    }

    /**
     * Should local proxies be dynamically generated for Stateless session beans? (Typically used with Hibernate instead
     * of Entity Beans to develop outside the container.)
     *
     * @param localProxies
     */
    public void setLocalProxies(boolean localProxies)
    {
        this.localProxies = localProxies;
    }

    /**
     * Sets the Kind attribute of the UtilObjectSubTask object
     *
     * @param kind  The new Kind value
     */
    public void setKind(LookupKind kind)
    {
        this.kind = kind.getValue();
    }

    /**
     * Sets the Pattern attribute of the UtilObjectSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        utilClassPattern = new_pattern;
    }

    /**
     * Include a performant GUID generator in the util object.
     *
     * @param includeGUID  include the GUID generator or not
     */
    public void setIncludeGUID(boolean includeGUID)
    {
        this.includeGUID = includeGUID;
    }

    /**
     * Cache the homes?
     *
     * @param cacheHomes
     */
    public void setCacheHomes(boolean cacheHomes)
    {
        this.cacheHomes = cacheHomes;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getUtilClassPattern() == null || getUtilClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getUtilClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the UtilObjectSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(LookupUtilTagsHandler.getUtilClassFor(getCurrentClass())) + ".java";
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_UTIL_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
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
        Log log = LogUtil.getLog(LookupObjectSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        if (!EjbTagsHandler.isAConcreteEJBean(getCurrentClass())) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because it's not a concrete bean.");
            return false;
        }

        String generate = getCurrentClass().getDoc().getTagAttributeValue("ejb:util", "generate", true);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip util class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        return true;
    }

    /**
     * @author    Ara Abrahamian (ara_e_w@yahoo.com)
     * @created   March 6, 2002
     */
    public static class LookupKind extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String PHYSICAL = "physical";

        public final static String LOGICAL = "logical";

        /**
         * Gets the Values attribute of the LookupKind object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{
                PHYSICAL, LOGICAL
                });
        }
    }
}
