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
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="CMP" name="entitycmp" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.7 $
 * @xdoclet.merge-file   file="entitycmp-custom.xdt" relates-to="{0}CMP.java" description="A text file containing custom
 *      template and/or java code to include in the EJB CMP class."
 */
public class EntityCmpSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_ENTITYCMP_CLASS_PATTERN = "{0}CMP";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/entitycmp.xdt";

    /**
     * A configuration parameter for specifying the concrete CMP entity bean class name pattern. By default the value is
     * used for deciding the concrete CMP entity bean class name. {0} in the value mean current class's symbolic name
     * which for an EJBean is the EJB name.
     *
     * @see   #getEntityCmpClassPattern()
     */
    protected String entityCmpClassPattern;

    private String  cmpspec = CmpSpecVersion.CMP_2_0;

    /**
     * Describe what the EntityCmpSubTask constructor does
     */
    public EntityCmpSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getEntityCmpClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        setPackageSubstitutionInheritanceSupported(false);
    }

    /**
     * Gets the CmpSpec attribute of the EntityCmpSubTask object
     *
     * @return   The CmpSpec value
     */
    public String getCmpSpec()
    {
        return cmpspec;
    }

    /**
     * Returns the configuration parameter for specifying the concrete CMP entity bean class name pattern. By default
     * the value is used for deciding the concrete CMP entity bean class name. {0} in the value mean current class's
     * symbolic name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}CMP" is used
     * by default.
     *
     * @return   The EntityCmpClassPattern value
     * @see      #entityCmpClassPattern
     */
    public String getEntityCmpClassPattern()
    {
        if (entityCmpClassPattern != null) {
            return entityCmpClassPattern;
        }
        else {
            return DEFAULT_ENTITYCMP_CLASS_PATTERN;
        }
    }

    /**
     * Sets the CmpSpec attribute of the EntityCmpSubTask object
     *
     * @param cmpspec  The new CmpSpec value
     */
    public void setCmpSpec(CmpSpecVersion cmpspec)
    {
        this.cmpspec = cmpspec.getValue();
    }

    /**
     * Sets the Pattern attribute of the EntityCmpSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        entityCmpClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getEntityCmpClassPattern() == null || getEntityCmpClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getEntityCmpClassPattern().indexOf("{0}") == -1) {
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
        return PackageTagsHandler.packageNameAsPathFor(CmpTagsHandler.getEntityCmpClassFor(getCurrentClass())) + ".java";
    }


    /**
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     * @todo                        refactor/merge this method with matchesGenerationRules from EntityBmpSubTask
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(EntityCmpSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        if (CmpTagsHandler.isEntityCmp(getCurrentClass()) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because of it's not BMP.");
            return false;
        }

        String generate = clazz.getDoc().getTagAttributeValue("ejb:bean", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip entity cmp class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
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
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_CMP_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }


    /**
     * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
     * @created   February 23, 2002
     */
    public static class CmpSpecVersion extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String CMP_1_1 = "1.x";
        public final static String CMP_2_0 = "2.x";

        /**
         * Gets the Versions attribute of the CmpSpecVersion class
         *
         * @return   The Versions value
         */
        public final static String getVersions()
        {
            return CMP_1_1 + ',' + CMP_2_0;
        }

        /**
         * Gets the Values attribute of the CmpSpecVersion object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{
                CMP_1_1, CMP_2_0
                });
        }
    }
}
