/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;


import java.util.Properties;
import java.util.StringTokenizer;

import xjavadoc.*;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.template.TemplateTagHandler;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * Derives from TemplateTagHandler and adds handy support methods for working with Javadoc Doclet classes.
 *
 * @author    Dmitri Colebatch (dim@bigpond.net.au)
 * @created   October 12, 2001
 * @version   $Revision: 1.59 $
 */
public abstract class XDocletTagSupport extends TemplateTagHandler
{
    public final static int FOR_CLASS = 0;
    public final static int FOR_METHOD = 1;
    public final static int FOR_FIELD = 2;
    public final static int FOR_CONSTRUCTOR = 3;

    /**
     * Default delimiter used inside a xdoclet tag attribute.
     */
    protected final static String PARAMETER_DELIMITER = ",";

    /**
     * @return   The current tag.
     */
    public static XTag getCurrentMethodTag()
    {
        return getDocletContext().getActiveSubTask().getCurrentMethodTag();
    }

    /**
     * @return   The current tag.
     */
    public static XTag getCurrentClassTag()
    {
        return getDocletContext().getActiveSubTask().getCurrentClassTag();
    }

    /**
     * @return   The current tag.
     */
    public static XTag getCurrentFieldTag()
    {
        return getDocletContext().getActiveSubTask().getCurrentFieldTag();
    }

    /**
     * Provides the current method in the XDoclet build, or null if there is no current method.
     *
     * @return   The CurrentMethod value
     * @see      #setCurrentMethod
     */
    public static XMethod getCurrentMethod()
    {
        return getDocletContext().getActiveSubTask().getCurrentMethod();
    }

    /**
     * Provides the current field in the XDoclet build, or null if there is no current field.
     *
     * @return   The CurrentField value
     * @see      #setCurrentField
     */
    public static XField getCurrentField()
    {
        return getDocletContext().getActiveSubTask().getCurrentField();
    }

    /**
     * Provides the current constructor in the XDoclet build, or null if there is no current constructor.
     *
     * @return   The CurrentConstructor value
     * @see      #setCurrentConstructor
     */
    public static XConstructor getCurrentConstructor()
    {
        return getDocletContext().getActiveSubTask().getCurrentConstructor();
    }

    /**
     * Provides the current class in the XDoclet build, or null if there is no current class.
     *
     * @return   The CurrentClass value
     * @see      #pushCurrentClass
     * @see      #popCurrentClass
     */
    public static XClass getCurrentClass()
    {
        return getDocletContext().getActiveSubTask().getCurrentClass();
    }

    /**
     * Provides the current package in the XDoclet build, or null if there is no current package.
     *
     * @return   The CurrentPackage value
     */
    public static XPackage getCurrentPackage()
    {
        return getDocletContext().getActiveSubTask().getCurrentPackage();
    }

    /**
     * Set the current method.
     *
     * @param method  The method to make the current method
     * @see           #getCurrentMethod
     */
    public static void setCurrentMethod(XMethod method)
    {
        getDocletContext().getActiveSubTask().setCurrentMethod(method);
    }

    /**
     * Set the current constructor.
     *
     * @param constructor  The constructor to make the current constructor
     * @see                #getCurrentConstructor
     */
    public static void setCurrentConstructor(XConstructor constructor)
    {
        getDocletContext().getActiveSubTask().setCurrentConstructor(constructor);
    }

    /**
     * Sets current class to clazz by clearing currentClassStack stack and pushing clazz into top of it.
     *
     * @param clazz  The new CurrentClass value
     * @see          #getCurrentClass()
     */
    public static void setCurrentClass(XClass clazz)
    {
        getDocletContext().getActiveSubTask().setCurrentClass(clazz);
    }

    /**
     * Set the current package
     *
     * @param pakkage  The new CurrentPackage value
     * @see            #getCurrentPackage
     */
    public static void setCurrentPackage(XPackage pakkage)
    {
        getDocletContext().getActiveSubTask().setCurrentPackage(pakkage);
    }

    /**
     * Sets the CurrentMethodTag attribute of the XDocletTagSupport class
     *
     * @param currentTag  The new CurrentTag value
     */
    public static void setCurrentMethodTag(XTag currentTag)
    {
        getDocletContext().getActiveSubTask().setCurrentMethodTag(currentTag);
    }

    /**
     * Sets the CurrentClassTag attribute of the XDocletTagSupport class
     *
     * @param currentTag  The new CurrentTag value
     */
    public static void setCurrentClassTag(XTag currentTag)
    {
        getDocletContext().getActiveSubTask().setCurrentClassTag(currentTag);
    }

    /**
     * Sets the CurrentFieldTag attribute of the XDocletTagSupport field
     *
     * @param currentTag  The new CurrentTag value
     */
    public static void setCurrentFieldTag(XTag currentTag)
    {
        getDocletContext().getActiveSubTask().setCurrentFieldTag(currentTag);
    }

    /**
     * Set the current field.
     *
     * @param field  The field to make the current field
     * @see          #getCurrentMethod
     */
    public static void setCurrentField(XField field)
    {
        getDocletContext().getActiveSubTask().setCurrentField(field);
    }

    /**
     * Push the specified class to the top of the current class stack making it effectively the current class.
     *
     * @param clazz  The class to push onto the top of the class stack.
     * @return       The class on the top of the stack.
     * @see          #getCurrentClass
     * @see          #popCurrentClass
     */
    public static XClass pushCurrentClass(XClass clazz)
    {
        return getDocletContext().getActiveSubTask().pushCurrentClass(clazz);
    }

    /**
     * Pop the current class off the top of the class stack.
     *
     * @return   The class popped off the top of the stack.
     * @see      #getCurrentClass
     * @see      #pushCurrentClass
     */
    public static XClass popCurrentClass()
    {
        return getDocletContext().getActiveSubTask().popCurrentClass();
    }

    /**
     * @return   the context object casted to DocletContext
     */
    protected static DocletContext getDocletContext()
    {
        return DocletContext.getInstance();
    }

    /**
     * The <code>getExpandedDelimitedTagValue</code> method returns a delimited version with class names expanded if
     * requested of the tag value.
     *
     * @param attributes            a <code>Properties</code> value
     * @param forType               an <code>int</code> value
     * @return                      a <code>String</code> value
     * @exception XDocletException  if an error occurs
     */
    protected static String getExpandedDelimitedTagValue(Properties attributes, int forType) throws XDocletException
    {
        String tagValue = getTagValue(attributes, forType);

        tagValue = delimit(tagValue, attributes);
        tagValue = expandClassName(tagValue, attributes);
        return tagValue;
    }

    /**
     * Return the Value of a tag specified in a Properties object. This method work on the currentTag object variable,
     * matchs it against the XTag specified in the attributes Properties and returns the value of the specified tag.
     *
     * @param attributes            The attributes of the template tag
     * @param forType               if FOR_CLASS, then a fifth property superclasses is searched, if this is set to
     *      true, then the tag is also searched in all superclasses of current class. If forType is set to FOR_METHOD or
     *      FOR_CONSTRUCTOR or FOR_FIELD, current method or field is searched for the tag.
     * @return                      The TagValue value
     * @exception XDocletException  Description of Exception
     */
    protected static String getTagValue(Properties attributes, int forType) throws XDocletException
    {
        String tagName = attributes.getProperty("tagName");
        String paramName = attributes.getProperty("paramName");
        String validValues = attributes.getProperty("values");
        String defaultValue = attributes.getProperty("default");
        String paramNum = attributes.getProperty("paramNum");
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), true);
        boolean mandatory = TypeConversionUtil.stringToBoolean(attributes.getProperty("mandatory"), false);

        /*
         * Handles multiple tags/parameters. Multiple tags/parameter are specified
         * as alternatives, and are meant to be used as a "backward compatibility"
         * helper. So, if the template has <hasTag tagName="tag1,tag2"
         * paramName="param1,param2">, xDoclet will first look for param1 in tag1.
         * If it's not found, it will look for param2 in tag2 and so on, until
         * it finds a valid tag/parameter.
         *
         * If tagName has more alternatives than paramName, it is assumed that the
         * corresponding paramName is null. If paramNum is supplied (it also can have
         * multiple alternatives), the tag value is checked.
         * (This is mainly to support jboss:table-name and friends. Ugly hack!)
         */
        StringTokenizer tagTokenizer = new StringTokenizer(tagName, ",");
        StringTokenizer paramTokenizer = new StringTokenizer(paramName != null ? paramName : "", ",");
        StringTokenizer paramNumTokenizer = new StringTokenizer(paramNum != null ? paramNum : "", ",");

        // We can't have more parameters then tags
        if (paramTokenizer.countTokens() > tagTokenizer.countTokens()) {
            throw new XDocletException("paramName can't contain more alternatives than tagName");
        }

        // Loop until we get a non-null tag/parameter or there aren't any more alternatives
        String tagValue = null;

        while (tagTokenizer.hasMoreTokens() && tagValue == null) {
            String currentTag = tagTokenizer.nextToken().trim();
            String currentParam = null;

            if (paramTokenizer.hasMoreTokens()) {
                currentParam = paramTokenizer.nextToken().trim();
                if ("".equals(currentParam)) {
                    currentParam = null;
                }
            }
//		String currentTag = tagName;
//		String currentParam = paramName;
//		String
            tagValue = getTagValue(
                forType,
                currentTag,
                currentParam,
                validValues,
                defaultValue,
                superclasses,
                mandatory
                );

            // Case of jboss:table-name "abc"
            if (tagValue == null && currentParam == null) {
                String currentParamNumber = null;

                if (paramNumTokenizer.hasMoreTokens()) {
                    currentParamNumber = paramNumTokenizer.nextToken().trim();
                }

//			String currentParamNumber = paramNum;

                if (currentParamNumber != null) {
                    XProgramElement programElement = getProgramElement(forType);
                    XDoc doc = programElement.getDoc();
                    XTag tag = doc.getTag(currentTag, superclasses);

                    if (tag != null) {
                        tagValue = tag.getValue();
                    }
                    if (tagValue != null && tagValue.trim().length() == 0) {
                        tagValue = null;
                    }
                }
            }
            // end of hack

            if (tagValue != null && tagValue.startsWith("\"")) {
                tagValue = tagValue.substring(1, tagValue.length() - 1);
            }

        }

        // tagValue = delimit( tagValue, attributes );
        return tagValue;
    }

    /**
     * Gets the TagValue attribute of the XDocletTagSupport class
     *
     * @param forType               Describe what the parameter does
     * @param tagName               Describe what the parameter does
     * @param paramName             Describe what the parameter does
     * @param validValues           Describe what the parameter does
     * @param defaultValue          Describe what the parameter does
     * @param superclasses          Describe what the parameter does
     * @param mandatory             Describe what the parameter does
     * @return                      The TagValue value
     * @exception XDocletException  Describe the exception
     */
    protected static String getTagValue(
        int forType,
        String tagName,
        String paramName,
        String validValues,
        String defaultValue,
        boolean superclasses,
        boolean mandatory
        ) throws XDocletException
    {

        XProgramElement programElement = getProgramElement(forType);

        if (programElement == null) {
            return null;
        }

        XDoc doc = programElement.getDoc();

        return getTagValue(
            forType,
            doc,
            tagName,
            paramName,
            validValues,
            defaultValue,
            superclasses,
            mandatory
            );
    }

    /**
     * @param doc                   Describe what the parameter does
     * @param tagName               Describe what the parameter does
     * @param paramName             Describe what the parameter does
     * @param validValues           Describe what the parameter does
     * @param defaultValue          Describe what the parameter does
     * @param superclasses          Describe what the parameter does
     * @param mandatory             Describe what the parameter does
     * @param forType
     * @return                      The TagValue value
     * @exception XDocletException  Describe the exception
     * @todo                        (Aslak) maybe this method ought to be moved to xjavadoc.XDoc? Not a big deal though.
     */
    protected static String getTagValue(
        int forType,
        XDoc doc,
        String tagName,
        String paramName,
        String validValues,
        String defaultValue,
        boolean superclasses,
        boolean mandatory) throws XDocletException
    {
        XTag tag = null;

        // first try to get current tag
        if (forType == FOR_METHOD || forType == FOR_CONSTRUCTOR)
            tag = getCurrentMethodTag();
        else if (forType == FOR_CLASS)
            tag = getCurrentClassTag();
        else if (forType == FOR_FIELD)
            tag = getCurrentFieldTag();

        boolean noCurrentTag = tag == null;

        if (tag != null && !tag.getName().equals(XDoc.dotted(tagName)))
            tag = null;

        if (tag == null) {
            // if there is no current tag, look in the doc
            tag = doc.getTag(tagName, superclasses);
        }

        String value = null;

        // check if we have a tag at all
        if (tag != null) {
            if (paramName == null) {
                // the value of the tag is requested
                value = tag.getValue();
            }
            else {
                value = tag.getAttributeValue(paramName);
            }
        }

        // if there was no current tag, no value yet, and we are looking for a parameter, we must search
        // all super class class tags in case THEY define the parameter we are looking for
        //
        if (noCurrentTag && value == null && paramName != null && superclasses) {
            value = doc.getTagAttributeValue(tagName, paramName, superclasses);
        }

        if (value == null) {
            // nothing found in javadocs
            if (mandatory) {
                // throws XDocletException
                mandatoryParamNotFound(doc, paramName, tagName);
            }
            if (defaultValue != null) {
                return defaultValue;
            }
            else {
                return null;
            }
        }
        else {
            // a value was found. perform sanity checks on valid values
            if (validValues != null) {
                // check if the value is among the valid values
                StringTokenizer st = new StringTokenizer(validValues, ",");

                while (st.hasMoreTokens()) {
                    if (st.nextToken().equals(value)) {
                        return value;
                    }
                }
                invalidParamValueFound(doc, paramName, tagName, value, validValues);
            }
        }
        return value;
    }

    /**
     * A utility method used by ifMethodTagValueEquals/ifMethodTagValueNotEquals and
     * ifClassTagValueEquals/ifClassTagValueNotEquals, return true if the value of the tag/XParameter equals with value.
     *
     * @param attributes            The attributes of the template tag
     * @param forType               Describe what the parameter does
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    protected static boolean isTagValueEqual(Properties attributes, int forType) throws XDocletException
    {
        // the value to check for
        String value = attributes.getProperty("value");

        if (value == null) {
            throw new XDocletException("The value property is not specified");
        }

        String attributeValue = null;

        // if currentTag is set first check against it, this is needed for forAll... tags
        // where currentTag is set by these tags and we don't need/want to delegate the
        // lookup to the surrounding current class or method.
        String tagName = attributes.getProperty("tagName");
        String paramName = attributes.getProperty("paramName");

        XTag currentTag = null;

        if (forType == FOR_METHOD || forType == FOR_CONSTRUCTOR)
            currentTag = getCurrentMethodTag();
        else if (forType == FOR_CLASS)
            currentTag = getCurrentClassTag();
        else if (forType == FOR_FIELD)
            currentTag = getCurrentFieldTag();

        if (currentTag != null && currentTag.getName().equals(XDoc.dotted(tagName))) {
            attributeValue = currentTag.getAttributeValue(paramName);
        }
        else {
            attributeValue = getTagValue(attributes, forType);
        }
        attributeValue = delimit(attributeValue, attributes);

        return value.equals(attributeValue);
    }

    protected static String expandClassName(String value, Properties attributes)
    {
        boolean expand = TypeConversionUtil.stringToBoolean(attributes.getProperty("expandClassName"), false);

        if (expand) {
            value = getCurrentClass().qualify(value).getQualifiedName();
        }
        // end of if ()
        return value;
    }

    /**
     * Throws an XDocletException exception to stop the build process. The exception has an informative message to help
     * user find out the cause of the error (not specifying a mandatory parameter for a tag).
     *
     * @param paramName             Description of Parameter
     * @param tagName               Description of Parameter
     * @param doc                   Describe what the parameter does
     * @exception XDocletException  Description of Exception
     */
    protected static void mandatoryParamNotFound(XDoc doc, String paramName, String tagName) throws XDocletException
    {
        XProgramElement programElement = doc.getOwner();

        if (programElement instanceof XMethod) {
            XMethod method = (XMethod) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.MANDATORY_TAG_PARAM_MISSING_METHOD,
                new String[]{paramName, tagName, method.getName(), method.getContainingClass().getQualifiedName()}));
        }
        else if (programElement instanceof XClass) {
            XClass clazz = (XClass) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.MANDATORY_TAG_PARAM_MISSING_CLASS,
                new String[]{paramName, tagName, clazz.getQualifiedName()}));
        }
        else if (programElement instanceof XConstructor) {
            XConstructor constructor = (XConstructor) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.MANDATORY_TAG_PARAM_MISSING_CONSTRUCTOR,
                new String[]{paramName, tagName, constructor.getContainingClass().getQualifiedName()}));
        }
        else if (programElement instanceof XField) {
            XField field = (XField) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.MANDATORY_TAG_PARAM_MISSING_FIELD,
                new String[]{paramName, tagName, field.getName(), field.getContainingClass().getQualifiedName()}));
        }
        else {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.BAD_PRGELEMDOC_TYPE,
                new String[]{programElement.toString()}));
        }
    }

    /**
     * A utility method used by ifHasClassTag/ifDoesntHaveClassTag and ifHasMethodTag/ifDoesntHaveMethodTag, return true
     * if at least one tag exists with the specified name.
     *
     * @param attributes            The attributes of the template tag
     * @param forType
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    protected static boolean hasTag(Properties attributes, int forType) throws XDocletException
    {
        return getTagValue(attributes, forType) != null;
    }

    /**
     * @param attributeValue  Describe what the parameter does
     * @param attributes      Describe what the parameter does
     * @return                Describe the return value
     * @todo                  fix the () equals test, it is not nice. Test : finder Home definition on AccountBean
     */
    protected static String delimit(String attributeValue, Properties attributes)
    {
        // Optional Parameter
        String delim = attributes.getProperty("delimiter");
        String tokenNumberStr = attributes.getProperty("tokenNumber");
        int tokenNumber = 0;

        if (tokenNumberStr != null) {
            tokenNumber = Integer.parseInt(tokenNumberStr);
        }
        if (delim != null) {
            if (delim.equals("()") && attributeValue.indexOf(delim) != -1) {
                attributeValue = null;
            }
            else {
                StringTokenizer st = new StringTokenizer(attributeValue, delim);
                String tok = null;

                for (int i = 0; i <= tokenNumber; i++) {
                    if (st.hasMoreTokens()) {
                        tok = st.nextToken();
                    }
                    else {
                        tok = null;
                    }
                }
                attributeValue = tok;
            }
        }
        return attributeValue;
    }

    /**
     * Gets the XProgramElement attribute of the XDocletTagSupport class.
     *
     * @param forType               Describe what the parameter does
     * @return                      The PrgElem value
     * @exception XDocletException  Describe the exception
     */
    private static XProgramElement getProgramElement(int forType) throws XDocletException
    {
        XProgramElement programElement = null;

        switch (forType) {
        case FOR_CLASS:
            programElement = getCurrentClass();
            break;
        case FOR_METHOD:
            programElement = getCurrentMethod();
            break;
        case FOR_CONSTRUCTOR:
            programElement = getCurrentConstructor();
            break;
        case FOR_FIELD:
            programElement = getCurrentField();
            break;
        default:
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.BAD_TAGVALUE_TYPE));
        }
        return programElement;
    }

    /**
     * Throws an XDocletException exception to stop the build process. The exception has an informative message to help
     * user find out the cause of the error (specifying an incorrect value for a parameter of a tag).
     *
     * @param paramName             Description of Parameter
     * @param tagName               Description of Parameter
     * @param value                 Description of Parameter
     * @param validValues           Description of Parameter
     * @param doc                   Describe what the parameter does
     * @exception XDocletException  Description of Exception
     */
    private static void invalidParamValueFound(XDoc doc, String paramName, String tagName, String value, String validValues) throws XDocletException
    {
        XProgramElement programElement = doc.getOwner();

        if (programElement instanceof XMethod) {
            XMethod method = (XMethod) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.INVALID_TAG_PARAM_VALUE_METHOD,
                new String[]{value, paramName, tagName, method.getName(), method.getContainingClass().getQualifiedName(), validValues}));
        }
        else if (programElement instanceof XClass) {
            XClass clazz = (XClass) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.INVALID_TAG_PARAM_VALUE_CLASS,
                new String[]{value, paramName, tagName, clazz.getQualifiedName(), validValues}));
        }
        else if (programElement instanceof XConstructor) {
            XConstructor constructor = (XConstructor) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.INVALID_TAG_PARAM_VALUE_CONSTRUCTOR,
                new String[]{value, paramName, tagName, constructor.getContainingClass().getQualifiedName(), validValues}));
        }
        else if (programElement instanceof XField) {
            XField field = (XField) programElement;

            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.INVALID_TAG_PARAM_VALUE_FIELD,
                new String[]{value, paramName, tagName, field.getName(), field.getContainingClass().getQualifiedName(), validValues}));
        }
        else {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.INVALID_TAG_PARAM_VALUE_METHOD,
                new String[]{programElement.toString()}));
        }
    }

    /**
     * Gets the Engine attribute of the TemplateTagHandler object.
     *
     * @return   The Engine value
     */
    public TemplateEngine getEngine()
    {
        return ((TemplateSubTask) getDocletContext().getActiveSubTask()).getEngine();
    }

    /**
     * @param template
     * @exception XDocletException
     * @todo                        throw TemplateException instead
     */
    public void generate(String template) throws XDocletException
    {
        try {
            getEngine().generate(template);
        }
        catch (TemplateException e) {
            if (e instanceof XDocletException) {
                throw (XDocletException) e;
            }
            else {

                throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED) + ": " + e.toString());
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @param forType               Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    protected String modifiers(int forType) throws XDocletException
    {
        return getProgramElement(forType).getModifiers();
    }

    /**
     * @param templateTagName
     * @param paramName
     * @exception XDocletException
     */
    protected void mandatoryTemplateTagParamNotFound(String templateTagName, String paramName) throws XDocletException
    {
        throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.MANDATORY_TAG_PARAM_MISSING_TEMPLATE,
            new String[]{paramName, templateTagName}));
    }
}
