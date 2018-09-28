/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dao;

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
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              February 8, 2002
 * @ant.element          display-name="Data Access Object" name="dao" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.3 $
 * @xdoclet.merge-file   file="dao-custom.xdt" relates-to="{0}DAO.java" description="A text file containing custom
 *      template and/or java code to include in the data access object interface."
 */
public class DaoSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_DAO_CLASS_PATTERN = "{0}DAO";

    private static String DEFAULT_TEMPLATE_FILE = "resources/dao.xdt";

    /**
     * A configuration parameter for specifying the DAO class name pattern. By default the value is used for deciding
     * the DAO class name. {0} in the value mean current class's symbolic name which for an EJBean is the EJB name.
     *
     * @see   #getDaoClassPattern()
     */
    private String  daoClassPattern;

    /**
     * Describe what the DaoSubTask constructor does
     */
    public DaoSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getDaoClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("javax.ejb.SessionBean");
        setHavingClassTag("ejb.dao");
        setPackageSubstitutionInheritanceSupported(false);
    }

    /**
     * Returns the configuration parameter for specifying the DAO class name pattern. By default the value is used for
     * deciding the DAO class name. {0} in the value mean current class's symbolic name which for an EJBean is the EJB
     * name. If nothing explicitly specified by user then "{0}DAO" is used by default.
     *
     * @return   The daoClassPattern value
     */
    public String getDaoClassPattern()
    {
        if (daoClassPattern != null) {
            return daoClassPattern;
        }
        else {
            return DEFAULT_DAO_CLASS_PATTERN;
        }
    }

    /**
     * Sets the Pattern attribute of the DaoSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        daoClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getDaoClassPattern() == null || getDaoClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getDaoClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the DaoSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(DaoTagsHandler.getDaoClassFor(getCurrentClass())) + ".java";
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_DAO_FOR,
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
        Log log = LogUtil.getLog(DaoSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        String generate = clazz.getDoc().getTagAttributeValue("ejb.dao", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip DAO class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        return true;
    }

}
