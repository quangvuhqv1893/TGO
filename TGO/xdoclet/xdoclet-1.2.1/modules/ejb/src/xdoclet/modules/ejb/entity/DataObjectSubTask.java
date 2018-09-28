/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.util.StringTokenizer;

import xjavadoc.XClass;
import xjavadoc.XTag;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;

import xdoclet.util.Translator;

/**
 * Creates "data objects" for Entity EJBs. This task is currently being deprecated in favour of <a
 * href="ValueObjectSubTask.html">Value Object</a> which is more powerful in terms of relationships (1-1, 1-n and n-m).
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Data Object" name="dataobject" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @xdoclet.merge-file   file="dataobject-custom.xdt" relates-to="{0}Data.java" description="A text file containing
 *      custom template and/or java code to include in the data object class."
 * @version              $Revision: 1.9 $
 */
public class DataObjectSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_DATAOBJECT_CLASS_PATTERN = "{0}Data";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/dataobject.xdt";

    /**
     * A configuration parameter for specifying the data object class name pattern. By default the value is used for
     * deciding the entity data object class name. {0} in the value mean current class's symbolic name which for an
     * EJBean is the EJB name.
     *
     * @see   #getDataObjectClassPattern()
     */
    protected String dataObjectClassPattern;


    /**
     * Describe what the DataObjectSubTask constructor does
     */
    public DataObjectSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getDataObjectClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
    }

    /**
     * Returns the configuration parameter for specifying the data object class name pattern. By default the value is
     * used for deciding the entity data object class name. {0} in the value mean current class's symbolic name which
     * for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}Data" is used by default.
     *
     * @return   The DataObjectClassPattern value
     * @see      #dataObjectClassPattern
     */
    public String getDataObjectClassPattern()
    {
        if (dataObjectClassPattern != null) {
            return dataObjectClassPattern;
        }
        else {
            return DEFAULT_DATAOBJECT_CLASS_PATTERN;
        }
    }

    /**
     * The pattern by which the data object classes are named. {0}designates the EJB name.
     *
     * @param new_pattern  The new Pattern value
     * @ant.not-required   No, default is "{0}Data"
     */
    public void setPattern(String new_pattern)
    {
        dataObjectClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getDataObjectClassPattern() == null || getDataObjectClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getDataObjectClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the DataObjectSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(DataObjectTagsHandler.getDataObjectClassFor(getCurrentClass())) + ".java";
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

        if (DataObjectTagsHandler.isGenerationNeeded(getCurrentClass())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @exception XDocletException
     */
    protected void generateForClass(XClass clazz) throws XDocletException
    {
        if (DataObjectTagsHandler.hasCustomBulkData(getCurrentClass())) {
            // Don't make a new DataObject class; use the custom one instead

            XTag bdo = getCurrentClass().getDoc().getTag("ejb:bulk-data");
            StringTokenizer enum = new StringTokenizer(bdo.getValue(), " ");

            DataObjectTagsHandler.setCurrentDataObjectClassname(enum.nextToken().trim());
            DataObjectTagsHandler.putDataObjectClassnames(getCurrentClass().getQualifiedName(), DataObjectTagsHandler.getCurrentDataObjectClassname());
        }
        else {
            DataObjectTagsHandler.setCurrentDataObjectClassname(DataObjectTagsHandler.generateDataObjectClass(clazz));
            DataObjectTagsHandler.putDataObjectClassnames(getCurrentClass().getQualifiedName(), DataObjectTagsHandler.getCurrentDataObjectClassname());
        }

        super.generateForClass(clazz);
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_DATAOBJECT_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }
}
