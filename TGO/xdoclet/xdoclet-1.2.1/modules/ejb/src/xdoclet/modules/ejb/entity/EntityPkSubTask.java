/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates primary key classes for entity EJBs.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="PK Class" name="entitypk" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="entitypk-custom.xdt" relates-to="{0}PK.java" description="A text file containing custom
 *      template and/or java code to include in the primary key class."
 */
public class EntityPkSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_ENTITY_PK_CLASS_PATTERN = "{0}PK";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/entitypk.xdt";

    /**
     * A configuration parameter for specifying the entity bean primary class name pattern. By default the value is used
     * for deciding the entity bean primary class name. {0} in the value mean current class's symbolic name which for an
     * EJBean is the EJB name.
     *
     * @see   #getEntityPkClassPattern()
     */
    protected String entityPkClassPattern;

    /**
     * Describe what the EntityPkSubTask constructor does
     */
    public EntityPkSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getEntityPkClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
    }

    /**
     * Returns the configuration parameter for specifying the entity bean primary class name pattern. By default the
     * value is used for deciding the entity bean primary class name. {0} in the value mean current class's symbolic
     * name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}PK" is used by
     * default.
     *
     * @return   The EntityPkClassPattern value
     * @see      #entityPkClassPattern
     */
    public String getEntityPkClassPattern()
    {
        if (entityPkClassPattern != null) {
            return entityPkClassPattern;
        }
        else {
            return DEFAULT_ENTITY_PK_CLASS_PATTERN;
        }
    }

    /**
     * The pattern by which the primary key classes are named. {0} designates the EJB name.
     *
     * @param new_pattern  The new Pattern value
     * @ant.not-required   No, defaults to {0}PK
     */
    public void setPattern(String new_pattern)
    {
        entityPkClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getEntityPkClassPattern() == null || getEntityPkClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getEntityPkClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the EntityPkSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(PkTagsHandler.getPkClassFor(getCurrentClass())) + ".java";
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

        Log log = LogUtil.getLog(EntityPkSubTask.class, "matchesGenerationRules");

        if (log.isDebugEnabled()) {
            log.debug("clazz=" + clazz);
        }

        if ("false".equalsIgnoreCase(clazz.getDoc().getTagAttributeValue("ejb:pk", "generate", false))) {
            log.debug("Skip primary key for " + clazz.getQualifiedName() + " because of false generate flag");
            return false;
        }

        if (PkTagsHandler.classHasPrimkeyField(clazz)) {
            log.debug("Skip primary key for " + clazz.getQualifiedName() + " because it has a PK Field.");
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
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_PK_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

}
