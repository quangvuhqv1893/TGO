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
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;

import xdoclet.util.Translator;

/**
 * @author        Konstantin Pribluda (kpriblouda@yahoo.com)
 * @created       September 8, 2002
 * @ant.element   display-name="Facade" name="entityfacade" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.2 $
 */

public class EntityFacadeSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_ENTITY_FACADE_CLASS_PATTERN = "{0}FacadeEJB";

    public final static String DEFAULT_FACADE_EJB_NAME_PATTERN = "{0}Facade";
    protected static String DEFAULT_TEMPLATE_FILE = "resources/entityfacade.xdt";

    /**
     * A configuration parameter for specifying the entity bean facade EJB class name pattern. By default the value is
     * used for deciding the entity bean facade class name. {0} in the value mean current class's symbolic name which
     * for an EJBean is the EJB name.
     */
    protected String entityFacadeClassPattern;
    /**
     * a configuration parameter for specifying facade ejb names pattern {0} means ejb name
     */
    protected String entityFacadeEjbNamePattern;

    public EntityFacadeSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getEntityFacadeClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        setPackageSubstitutionInheritanceSupported(false);

    }

    /**
     * Returns the configuration parameter for specifying the entity bean facade class name pattern. By default the
     * value is used for deciding the concrete CMP entity bean class name. {0} in the value mean current class's
     * symbolic name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}FacadeEJB" is
     * used by default.
     *
     * @return   The EntityCmpClassPattern value
     * @see      #entityCmpFacadePattern
     */
    public String getEntityFacadeClassPattern()
    {
        if (entityFacadeClassPattern != null) {
            return entityFacadeClassPattern;
        }
        else {
            return DEFAULT_ENTITY_FACADE_CLASS_PATTERN;
        }
    }


    public String getEntityFacadeEjbNamePattern()
    {
        if (entityFacadeEjbNamePattern != null) {
            return entityFacadeEjbNamePattern;
        }
        else {
            return DEFAULT_FACADE_EJB_NAME_PATTERN;
        }
    }

    /**
     * Sets the Pattern attribute of the EntityFacadeSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        entityFacadeClassPattern = new_pattern;
    }

    public void setEjbNamePattern(String new_pattern)
    {
        entityFacadeEjbNamePattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getEntityFacadeClassPattern() == null || getEntityFacadeClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getEntityFacadeClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }

        if (getEntityFacadeEjbNamePattern() == null || getEntityFacadeEjbNamePattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"ejbNamePattern"}));
        }
        if (getEntityFacadeEjbNamePattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the EntityCmpSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(FacadeTagsHandler.getEntityFacadeClassFor(getCurrentClass())) + ".java";
    }


    /**
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     * @todo                        refactor/merge this method with matchesGenerationRules from EntityBmpSubTask
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(EntityFacadeSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        if (EntityTagsHandler.isEntity(getCurrentClass()) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because of it's not entity.");
            return false;
        }

        String generate = clazz.getDoc().getTagAttributeValue("ejb:bean", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip entity class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        if (!clazz.getDoc().hasTag("ejb.facade")) {
            log.debug("Skip facade class for " + clazz.getQualifiedName() + " because ejb.facade is absent");
            return false;
        }

        String facade = clazz.getDoc().getTagAttributeValue("ejb.facade", "generate", false);

        if ((facade != null) && (facade.equals("false") || facade.equals("no"))) {
            log.debug("Skip facade class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        if (EjbTagsHandler.isAConcreteEJBean(getCurrentClass())) {
            return true;
        }
        else {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because it's not a concrete bean.");
            return false;
        }
    }


    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_FACADE_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

}

