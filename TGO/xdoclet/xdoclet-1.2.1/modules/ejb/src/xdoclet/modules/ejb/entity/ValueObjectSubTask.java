/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;

import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.Translator;

/**
 * Creates "value objects" for Entity EJBs. This is task replaces <a href="DataObjectSubTask.html">Data Object</a> .
 *
 * @author               Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created              Feb 5, 2002
 * @ant.element          display-name="Value Object" name="valueobject" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.13 $
 * @xdoclet.merge-file   file="valueobject-custom.xdt" relates-to="{0}Value.java" description="A text file containing
 *      custom template and/or java code to include in the value object class."
 */
public class ValueObjectSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_DATAOBJECT_CLASS_PATTERN = "{0}Value";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/valueobject.xdt";

    private static String currentValueObjectClass;

    private static String currentValueObjectName;

    private static String currentValueObjectMatch;
    private static String currentValueObjectImplements;

    /**
     * A configuration parameter for specifying the data object class name pattern. By default the value is used for
     * deciding the entity data object class name. {0} in the value mean current class's symbolic name which for an
     * EJBean is the EJB name.
     *
     * @see   #getValueObjectClassPattern()
     */
    protected String valueObjectClassPattern;

    /**
     * Form tag being processed right now
     */
    protected XTag  currentDataObjectTag;

    /**
     * Whether to generate a single-parameter constructor, only setting up the PK
     */
    private boolean generatePKConstructor = false;

    /**
     * Describe what the ValueObjectSubTask constructor does
     */
    public ValueObjectSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getValueObjectClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
    }


    /**
     * Gets the CurrentValueObjectClass attribute of the ValueObjectSubTask class
     *
     * @return   The CurrentValueObjectClass value
     */
    public static String getCurrentValueObjectClass()
    {
        return currentValueObjectClass;
    }

    public static String getCurrentValueObjectImplements()
    {
        return currentValueObjectImplements;
    }

    /**
     * Gets the CurrentValueObjectName attribute of the ValueObjectSubTask class
     *
     * @return   The CurrentValueObjectName value
     */
    public static String getCurrentValueObjectName()
    {
        return currentValueObjectName;
    }


    /**
     * Gets the CurrentValueObjectMatch attribute of the ValueObjectSubTask class
     *
     * @return   The CurrentValueObjectMatch value
     */
    public static String getCurrentValueObjectMatch()
    {
        return currentValueObjectMatch;
    }

    /**
     * Returns the configuration parameter for specifying the data object class name pattern. By default the value is
     * used for deciding the entity data object class name. {0} in the value mean current class's symbolic name which
     * for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}Value" is used by default.
     *
     * @return   The ValueObjectClassPattern value
     * @see      #valueObjectClassPattern
     */
    public String getValueObjectClassPattern()
    {
        if (valueObjectClassPattern != null) {
            return valueObjectClassPattern;
        }
        else {
            return DEFAULT_DATAOBJECT_CLASS_PATTERN;
        }
    }

    public boolean getGeneratePKConstructor()
    {
        return generatePKConstructor;
    }


    /**
     * The pattern by which the value object classes are named. {0} designates the EJB name.
     *
     * @param new_pattern  The new Pattern value
     * @ant.not-required   No, default is "{0}Value"
     */
    public void setPattern(String new_pattern)
    {
        valueObjectClassPattern = new_pattern;
    }

    public void setGeneratePKConstructor(String generatePKConstructor)
    {
        this.generatePKConstructor = generatePKConstructor.substring(0, 1).equalsIgnoreCase("t");
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getValueObjectClassPattern() == null || getValueObjectClassPattern().trim().equals("")) {
            throw new XDocletException(
                Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getValueObjectClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }

    }


    /**
     * Gets the GeneratedFileName attribute of the ValueObjectSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(getCurrentValueObjectClass()) + ".java";
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

        if (ValueObjectTagsHandler.isGenerationNeeded(getCurrentClass())) {
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

        Collection dos = getCurrentClass().getDoc().getTags("ejb:value-object");

//		System.out.println( "Generating Value Object classes for '" + getCurrentClass().qualifiedName() + "'." );

        for (Iterator i = dos.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();

            currentValueObjectClass = ValueObjectTagsHandler.getCurrentValueObjectClass(getCurrentClass(), tag);
            currentValueObjectName = ValueObjectTagsHandler.getCurrentValueObjectName(tag);
            currentValueObjectMatch = ValueObjectTagsHandler.getCurrentValueObjectMatch(tag);
            currentValueObjectImplements = ValueObjectTagsHandler.getCurrentValueObjectImplements(tag);
            super.generateForClass(clazz);
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_VALUEOBJECT_FOR,
            new String[]{getCurrentClass().getQualifiedName() + "--> " + getCurrentValueObjectClass()}));
    }

}
