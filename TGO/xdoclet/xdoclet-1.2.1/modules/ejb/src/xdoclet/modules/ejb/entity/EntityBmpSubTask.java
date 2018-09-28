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
 * Creates "entity bean classes" for BMP entity EJBs. The classes are derived from the abstract entity bean class. <br/>
 * <b>Attention:</b> To give the developer more control over when the EJB becomes dirty (data changed) there is now a
 * method called "makeDirty()" in the generated wrapper class. To use this please add to your EJB an abstract method
 * called "makeDirty" and call it when you change data w/o using the setter methods. As example you could store the Data
 * Object instead of storing all the attributes one by one (be aware that you have to clone the data object before
 * storing to avoid side effects).
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="BMP" name="entitybmp" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="entitybmp-custom.xdt" relates-to="{0}BMP.java" description="A text file containing custom
 *      template and/or java code to include in the EJB BMP class."
 */
public class EntityBmpSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_ENTITYBMP_CLASS_PATTERN = "{0}BMP";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/entitybmp.xdt";

    /**
     * A configuration parameter for specifying the concrete BMP entity bean class name pattern. By default the value is
     * used for deciding the concrete BMP entity bean class name. {0} in the value mean current class's symbolic name
     * which for an EJBean is the EJB name.
     *
     * @see   #getEntityBmpClassPattern()
     */
    protected String entityBmpClassPattern;


    /**
     * Describe what the EntityBmpSubTask constructor does
     */
    public EntityBmpSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getEntityBmpClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        setPackageSubstitutionInheritanceSupported(false);
    }


    /**
     * Returns the configuration parameter for specifying the concrete BMP entity bean class name pattern. By default
     * the value is used for deciding the concrete BMP entity bean class name. {0} in the value mean current class's
     * symbolic name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}BMP" is used
     * by default.
     *
     * @return   The EntityBmpClassPattern value
     * @see      #entityBmpClassPattern
     */
    public String getEntityBmpClassPattern()
    {
        if (entityBmpClassPattern != null) {
            return entityBmpClassPattern;
        }
        else {
            return DEFAULT_ENTITYBMP_CLASS_PATTERN;
        }
    }


    /**
     * The pattern by which the BMP implementation classes are named. {0} designates the EJB name.
     *
     * @param new_pattern  The new Pattern value
     * @ant.not-required   No, defaults to {0}BMP
     */
    public void setPattern(String new_pattern)
    {
        entityBmpClassPattern = new_pattern;
    }


    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getEntityBmpClassPattern() == null || getEntityBmpClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getEntityBmpClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }


    /**
     * Gets the GeneratedFileName attribute of the EntityBmpSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(BmpTagsHandler.getEntityBmpClassFor(getCurrentClass())) + ".java";
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
        Log log = LogUtil.getLog(EntityBmpSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        if (!BmpTagsHandler.isEntityBmp(getCurrentClass())) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because of it's not BMP.");
            return false;
        }

        String generate = clazz.getDoc().getTagAttributeValue("ejb:bean", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip entity bmp class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
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
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_BMP_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }
}
