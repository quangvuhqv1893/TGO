/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.struts.ejb;

import java.util.*;
import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.apache.struts.StrutsFormTagsHandler;
import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;

import xdoclet.util.Translator;

/**
 * Generates a Struts ActionForm, based on an entity EJB. More information on Struts is available on the <a
 * href="http://jakarta.apache.org/struts">Struts website</a> , or the <a
 * href="http://jakarta.apache.org/struts/api/org/apache/struts/action/ActionForm.html">ActionForm API</a> .
 *
 * @author        Dmitri Colebatch (dim@bigpond.net.au)
 * @created       September 3, 2001
 * @ant.element   display-name="Struts Form" name="strutsform" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.9 $
 */
public class StrutsFormSubTask extends AbstractEjbCodeGeneratorSubTask
{
    /**
     * The default template file - struts_form.xdt.
     */
    protected static String DEFAULT_TEMPLATE_FILE = "resources/struts_form.xdt";

    /**
     * The pattern for the form class. Defaults to {0}{1}Form if not present.
     */
    protected String formClassPattern;

    /**
     * Form tag being processed right now.
     */
    protected XTag  currentFormTag;

    /**
     * Describe what the StrutsFormSubTask constructor does
     */
    public StrutsFormSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getStrutsFormClassPattern() + ".java");
        addOfType("javax.ejb.EntityBean");
        addOfType("java.lang.Object");
    }

    /**
     * Gets the CurrentFormTag attribute of the StrutsFormSubTask object
     *
     * @return   The CurrentFormTag value
     */
    public XTag getCurrentFormTag()
    {
        return currentFormTag;
    }

    /**
     * Return the class pattern.
     *
     * @return   The StrutsFormClassPattern value
     */
    public String getStrutsFormClassPattern()
    {
        if (formClassPattern != null) {
            return formClassPattern;
        }
        else {
            return "{0}{1}Form";
        }
    }

    /**
     * Sets the CurrentFormTag attribute of the StrutsFormSubTask object
     *
     * @param t  The new CurrentFormTag value
     */
    public void setCurrentFormTag(XTag t)
    {
        this.currentFormTag = t;
    }

    /**
     * Sets the Pattern attribute of the StrutsFormSubTask object
     *
     * @param newPattern  The new Pattern value
     */
    public void setPattern(String newPattern)
    {
        formClassPattern = newPattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getStrutsFormClassPattern() == null || getStrutsFormClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getStrutsFormClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
        if (getStrutsFormClassPattern().indexOf("{1}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesApacheStrutsEjbMessages.PATTERN_HAS_NO_FORM_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the StrutsFormSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException  Describe the exception
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(StrutsFormTagsHandler.getStrutsFormClassFor(getCurrentClass())) + ".java";
    }

    /**
     * Returns whether struts form[s] shall be generated for this class
     *
     * @param clazz                 Description of Parameter
     * @return                      is form tag shall be generated
     * @exception XDocletException  Description of Exception
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        if (super.matchesGenerationRules(clazz) == false) {
            return false;
        }

        if (StrutsFormTagsHandler.hasFormDefinition(getCurrentClass())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * iterate through all struts:form tags,and produce separate classes
     *
     * @param clazz                 Description of Parameter
     * @exception XDocletException  Description of Exception
     */
    protected void generateForClass(XClass clazz) throws XDocletException
    {
        Collection formTags = clazz.getDoc().getTags("struts.form");

        for (TagIterator i = XCollections.tagIterator(formTags); i.hasNext(); ) {
            XTag tag = i.next();

            setCurrentFormTag(tag);
            super.generateForClass(clazz);
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesApacheStrutsEjbMessages.class, XDocletModulesApacheStrutsEjbMessages.GENERATING_FILE,
            new String[]{StrutsFormTagsHandler.getStrutsFormClassName(getCurrentClass())}));
    }
}
