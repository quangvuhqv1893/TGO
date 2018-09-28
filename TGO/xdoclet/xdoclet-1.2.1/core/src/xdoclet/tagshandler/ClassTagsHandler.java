/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.text.DateFormat;
import java.util.Calendar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.*;
import xdoclet.template.*;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * Tags relating to classes being processed and class-level attributes.
 *
 * @author               Ara Abrahamian (ara_e_w@yahoo.com)
 * @created              Oct 14, 2001
 * @xdoclet.taghandler   namespace="Class"
 * @version              $Revision: 1.19 $
 */
public class ClassTagsHandler extends AbstractProgramElementTagsHandler
{
    /**
     * Used for setting the timestamp for xdoclet-generated marker in generated files.
     */
    protected final static DateFormat dateFormatter = DateFormat.getDateTimeInstance();

    protected final static Calendar now = Calendar.getInstance();

    /**
     * Returns the not-full-qualified name of the specified class without the package name.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @todo         duplicate in AbstractProgramElementTagsHandler
     * @deprecated   use XClass.name()
     */
    public static String getClassNameFor(XClass clazz)
    {
        return clazz.getName();
    }

    /**
     * Returns the full-qualified name of the specified class with the package name.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @deprecated   use XClass.qualifiedName()
     */
    public static String getFullClassNameFor(XClass clazz)
    {
        if (clazz.getContainingClass() != null) {
            return clazz.getContainingClass().getQualifiedName();
        }
        return clazz.getQualifiedName();
    }

    /**
     * Iterates over all tags named according to tagName in a non-duplicated manner. The paramName parameter should be
     * the tag parameter that should be unique during the iteration. Duplicated tags will generate a warning message.
     * Please note that this tag already processes all classes. There is no need to wrap it inside a
     * &lt;XDtClass:forAllClasses&gt; tag or any other tag that processes a group of classes.
     *
     * @param engine
     * @param template           The body of the block tag
     * @param tagName            The tag to iterate
     * @param paramName          The tag parameter that should be used as identifier.
     * @throws XDocletException  if something goes wrong
     */
    public static void forAllDistinctClassTags(TemplateEngine engine, String template, String tagName, String paramName) throws XDocletException
    {
        // A set to store already processed references
        Set tagKeys = new HashSet();
        Log log = LogUtil.getLog(ClassTagsHandler.class, "forAllDistinctClassTags");

        try {

            for (Iterator i = getXJavaDoc().getSourceClasses().iterator(); i.hasNext(); ) {
                XClass clazz = (XClass) i.next();

                setCurrentClass(clazz);

                Collection tags = clazz.getDoc().getTags(tagName);

                if (tags == null)
                    continue;

                for (Iterator j = tags.iterator(); j.hasNext(); ) {
                    XTag tag = (XTag) j.next();

                    setCurrentClassTag(tag);
                    if (!tagKeys.contains(tag.getAttributeValue(paramName))) {
                        tagKeys.add(tag.getAttributeValue(paramName));
                        engine.generate(template);
                    }
                    else {
                        log.warn(Translator.getString(XDocletMessages.class, XDocletMessages.DUPLICATED_TAG,
                            new String[]{tagName, paramName, tag.getAttributeValue(paramName)}));
                    }
                }
            }
        }
        catch (TemplateException e) {
            if (e instanceof XDocletException) {
                throw (XDocletException) e;
            }
            else {
                throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
            }
        }
    }

    /**
     * Iterates over all tags named according to tagName in a non-duplicated manner. The paramName parameter specifies
     * the tag parameter that should be unique during the iteration. Duplicated tags will generate a warning message.
     * Please note that this tag already processes all classes. There is no need to wrap it inside a
     * &lt;XDtClass:forAllClasses&gt; tag or any other tag that processes a group of classes.
     *
     * @param template           The body of the block tag
     * @param attributes         The attributes of the template tag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     * @doc.param                name="tagName" optional="false" description="The tag to iterate."
     * @doc.param                name="tagKey" optional="false" description="The tag parameter that should be used as
     *      identifier."
     */
    public void forAllDistinctClassTags(String template, Properties attributes) throws XDocletException
    {
        String tagName = attributes.getProperty("tagName");
        String paramName = attributes.getProperty("paramName");

        forAllDistinctClassTags(getEngine(), template, tagName, paramName);
    }

    /**
     * Returns the not-fully-qualified name of the current class without the package name.
     *
     * @return                      the name of the current class
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="content"
     */
    public String className() throws XDocletException
    {
        return getCurrentClass().getName();
    }

    /**
     * Returns the full-qualified name of the current class.
     *
     * @return                      the name of the current class
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="content"
     */
    public String fullClassName() throws XDocletException
    {
        return getCurrentClass().getQualifiedName();
    }

    /**
     * Returns the transformed name of the current class with package name.
     *
     * @return                      the name of the current class
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="content"
     */
    public String transformedClassName() throws XDocletException
    {
        return getCurrentClass().getTransformedName();
    }

    /**
     * Returns the fully-qualified transformed name of the current class with package name.
     *
     * @return                      the name of the current class
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="content"
     */
    public String fullTransformedClassName() throws XDocletException
    {
        return getCurrentClass().getTransformedQualifiedName();
    }

    /**
     * Returns the full-qualified name of the superclass of the current class.
     *
     * @return                      the name of the superclass of the current class
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="content"
     */
    public String fullSuperclassName() throws XDocletException
    {
        return getFullSuperclassNameFor(getCurrentClass());
    }

    /**
     * Returns the not-full-qualified name of the full-qualified class name specified in the body of this tag.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  if something goes wrong
     * @doc.tag                     type="block"
     */
    public void classOf(String template) throws XDocletException
    {
        try {
            String fullClassName = getEngine().outputOf(template);
            String result = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

            getEngine().print(result);
            // use out.print because it's a block tag and can't return a String
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.METHOD_FAILED, new String[]{"ClassTagsHandler.classOf"}));
        }
    }

    /**
     * Evaluate the body block if current class is abstract.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsClassNotAbstract(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsClassAbstract(String template) throws XDocletException
    {
        if (getCurrentClass().isAbstract()) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current class is not abstract.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsClassAbstract(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsClassNotAbstract(String template) throws XDocletException
    {
        if (!getCurrentClass().isAbstract()) {
            generate(template);
        }
    }

    /**
     * Pushes the class specified by value parameter to top of stack making it the current class.
     *
     * @param attributes            The attributes of the template tag
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" values="return-type,whatever class name"
     *      description="If return-type specified then push current method return type, else find the XClass for the
     *      class name and push it."
     */
    public void pushClass(String template, Properties attributes) throws XDocletException
    {
        String value = attributes.getProperty("value", null);
        XClass currentClass = null;

        if (value == null) {
            try {
                value = getEngine().outputOf(template);
            }
            catch (TemplateException ex) {
                throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.METHOD_FAILED, new String[]{"pushClass"}));
            }
        }

        currentClass = getXJavaDoc().getXClass(value);

        if (currentClass == null) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.JAVADOC_COULDNT_LOAD_CLASS, new String[]{value}));
        }

        XMethod oldMethod = getCurrentMethod();

        pushCurrentClass(currentClass);
        setCurrentMethod(null);
        generate(template);
        popCurrentClass();
        setCurrentMethod(oldMethod);
    }

    /**
     * Iterates over all classes loaded by xjavadoc and evaluates the body of the tag for each class. It discards
     * classes that have a xdoclet-generated class tag defined.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="abstract" optional="true" values="true,false" description="If true then accept
     *      abstract classes also; otherwise don't."
     * @doc.param                   name="type" optional="true" description="For all classes by the type."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void forAllClasses(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(ClassTagsHandler.class, "forAllClasses");

        String abstractStr = attributes.getProperty("abstract");
        boolean acceptAbstractClasses = TypeConversionUtil.stringToBoolean(abstractStr, true);
        String typeName = attributes.getProperty("type");
        String extentStr = attributes.getProperty("extent");
        int extent = TypeTagsHandler.extractExtentType(extentStr);

        if (log.isDebugEnabled()) {
            log.debug("acceptAbstractClasses=" + acceptAbstractClasses);
            log.debug("typeName=" + typeName);
            log.debug("extentStr=" + extentStr);
            log.debug("extent=" + extent);
        }

        for (Iterator i = getAllClasses().iterator(); i.hasNext(); ) {
            XClass currentClass = (XClass) i.next();

            setCurrentClass(currentClass);
            log.debug("currentClass=" + currentClass);

            if (DocletSupport.isDocletGenerated(getCurrentClass()) || (getCurrentClass().isAbstract() && acceptAbstractClasses == false)) {
                log.debug("isDocletGenerated or isAbstract");
                continue;
            }

            if (typeName != null) {
                if (TypeTagsHandler.isOfType(currentClass, typeName, extent)) {
                    log.debug("isOfType true, generate().");
                    generate(template);
                }
                else {
                    log.debug("isOfType false");
                }
            }
            else {
                log.debug("typeName=null, generate().");
                generate(template);
            }
        }
    }

    /**
     * The current class' modifiers.
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String modifiers() throws XDocletException
    {
        return modifiers(FOR_CLASS);
    }

    /**
     * Returns the symbolic name of the current class. For a java bean it's the same as the class name.
     *
     * @return                      The symbolic name of the current class
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String symbolicClassName() throws XDocletException
    {
        // (Aslak) not sure if this is correct
        return getCurrentClass().getName();
    }

    /**
     * Evaluates the body if current class doesn't have at least one tag with the specified name.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifDoesntHaveClassTag(String template, Properties attributes) throws XDocletException
    {
        if (!hasTag(attributes, FOR_CLASS)) {
            generate(template);
        }
        else {
            String error = attributes.getProperty("error");

            if (error != null) {
                getEngine().print(error);
            }
        }
    }

    /**
     * Evaluates the body if current class has at least one tag with the specified name.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifHasClassTag(String template, Properties attributes) throws XDocletException
    {
        if (hasTag(attributes, FOR_CLASS)) {
            generate(template);
        }
        else {
            String error = attributes.getProperty("error");

            if (error != null) {
                getEngine().print(error);
            }
        }
    }

    /**
     * Evaluate the body if the match variable equals with the value of the specified tag/parameter.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @todo                        (Aslak) It appears that this method does the same job as ifClassTagValueEquals. It
     *      also appears that no templates are using it. Candidate for removal?
     * @doc.tag                     type="block"
     * @doc.param                   name="values" description="The valid values for the parameter, comma separated. An
     *      error message is printed if the parameter value is not one of the values."
     * @doc.param                   name="default" description="The default value is returned if parameter not specified
     *      by user for the tag."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     */
    public void ifClassTagValueMatches(String template, Properties attributes) throws XDocletException
    {
        String wantedTagValue = getTagValue(attributes, FOR_CLASS);

        if (wantedTagValue.equals(matchPattern)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if value for the class tag equals the specified value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifClassTagValueEquals(String template, Properties attributes) throws XDocletException
    {
        if (isTagValueEqual(attributes, FOR_CLASS)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if value for the class tag not equals the specified value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifClassTagValueNotEquals(String template, Properties attributes) throws XDocletException
    {
        if (!isTagValueEqual(attributes, FOR_CLASS)) {
            generate(template);
        }
    }

    /**
     * Iterates over all class tags with the specified tagName and evaluates the body of the tag for each class tag.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     * @doc.param                   name="values" description="The valid values for the parameter, comma separated. An
     *      error message is printed if the parameter value is not one of the values."
     * @doc.param                   name="default" description="The default value is returned if parameter not specified
     *      by user for the tag."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     */
    public String classTagValue(Properties attributes) throws XDocletException
    {
        return getExpandedDelimitedTagValue(attributes, FOR_CLASS);
    }

    /**
     * Sets the value of match variable. Match variable serves as a variable for templates, you set it somewhere in
     * template and look it up somewhere else in temaplte. This tag does not return any content, it just sets the match
     * variable.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="paramName" description="The parameter name. If not specified, then the raw
     *      content of the tag is returned."
     * @doc.param                   name="paramNum" description="The zero-based parameter number. It's used if the user
     *      used the space-separated format for specifying parameters."
     */
    public String classTagValueMatch(Properties attributes) throws XDocletException
    {
        matchPattern = getTagValue(attributes, FOR_CLASS);
        return "";
    }

    /**
     * Iterates over all tags of current class with the name tagName and evaluates the body of the tag for each method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="tagKey" description="A tag property that will be used as a unique key. This is
     *      used to avoid duplicate code due to similar tags in superclasses."
     */
    public void forAllClassTags(String template, Properties attributes) throws XDocletException
    {
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), true);
        Collection tags = getCurrentClass().getDoc().getTags(attributes.getProperty("tagName"), superclasses);
        Set done = new HashSet();

        matchPattern = null;

        String tagKey = attributes.getProperty("tagKey");

        for (Iterator i = tags.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();

            if (tagKey != null) {
                String key = tag.getAttributeValue(tagKey);

                if (!done.add(key)) {
                    // no change to set, therefore we must have already done this tag
                    continue;
                }
            }

            setCurrentClassTag(tag);

            generate(template);
        }
        setCurrentClassTag(null);
        matchPattern = null;
    }

    /**
     * Iterates over all tokens in specified class tag with the name tagName and evaluates the body for every token.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The name of the tag to look in."
     * @doc.param                   name="paramName" optional="false" description="The parameter of the tag whose value
     *      is to be tokenized."
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="delimiter" description="delimiter for the StringTokenizer. consult javadoc for
     *      java.util.StringTokenizer default is ','"
     * @doc.param                   name="skip" description="how many tokens to skip on start"
     */
    public void forAllClassTagTokens(String template, Properties attributes) throws XDocletException
    {
        forAllMemberTagTokens(template, attributes, FOR_CLASS);
    }

    /**
     * The comment for the current class.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         MethodTagsHandler#methodComment(java.util.Properties)
     * @see                         #classCommentText(java.util.Properties)
     * @see                         #classCommentTags(java.util.Properties)
     * @doc.tag                     type="content"
     * @doc.param                   name="no-comment-signs" optional="true" values="true,false" description="If true
     *      then don't decorate the comment with comment signs. Default is false."
     */
    public String classComment(Properties attributes) throws XDocletException
    {
        // Leave out the tags if no comment delimiters; I can't think of a reason you'd
        // still want them, since they won't do anything outside of javadoc comments
        boolean noCommentSigns = TypeConversionUtil.stringToBoolean(attributes.getProperty("no-comment-signs"), false);

        if (noCommentSigns) {
            return getCurrentClass().getDoc().getCommentText();
        }

        char[] spaces = getIndentChars(attributes);
        StringBuffer sbuf = new StringBuffer();

        sbuf.append(spaces).append("/**").append(PrettyPrintWriter.LINE_SEPARATOR);
        sbuf.append(classCommentText(attributes));
        sbuf.append(classCommentTags(attributes));
        sbuf.append(spaces).append(" */");

        return sbuf.toString();
    }

    /**
     * The text of the javadoc comment for the current class.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         MethodTagsHandler#methodComment(java.util.Properties)
     * @see                         #classComment(java.util.Properties)
     * @doc.tag                     type="content"
     * @doc.param                   name="no-comment-signs" optional="true" values="true,false" description="If true
     *      then don't decorate the comment with comment signs. Default is false."
     */
    public String classCommentText(Properties attributes) throws XDocletException
    {
        boolean noCommentSigns = TypeConversionUtil.stringToBoolean(attributes.getProperty("no-comment-signs"), false);

        if (noCommentSigns) {
            return getCurrentClass().getDoc().getCommentText();
        }

        char[] spaces = getIndentChars(attributes);
        StringBuffer sbuf = new StringBuffer();

        sbuf.append(spaces).append(" * ").append(getCurrentClass().getDoc().getCommentText()).append(PrettyPrintWriter.LINE_SEPARATOR);

        return sbuf.toString();
    }

    /**
     * The javadoc comment tags for the current class (plus xdoclet-generated).
     *
     * @param attributes            The attributes of the template tag
     * @return                      The class-level tags
     * @exception XDocletException  Description of Exception
     * @see                         MethodTagsHandler#methodComment(java.util.Properties)
     * @see                         #classComment(java.util.Properties)
     * @doc.tag                     type="content"
     */
    public String classCommentTags(Properties attributes) throws XDocletException
    {
        char[] spaces = getIndentChars(attributes);
        StringBuffer sbuf = new StringBuffer();
        Collection classTags = getCurrentClass().getDoc().getTags();

        for (Iterator i = classTags.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();

            String classTag = tag.getName();

            // omit ejbdoclet-specific tags, which all have a ":" or "."
            if (classTag.indexOf(':') == -1 && classTag.indexOf('.') == -1) {
                // omit excluded tags
                if (getDocletContext().getExcludedTags().indexOf(classTag) == -1) {
                    sbuf.append(spaces).append(" * @").append(classTag).append(' ');
                    sbuf.append(tag.getValue()).append(PrettyPrintWriter.LINE_SEPARATOR);
                }
            }
        }

        if (getDocletContext().getAddedTags() != null) {
            StringTokenizer st = new StringTokenizer(getDocletContext().getAddedTags(), ",");

            while (st.hasMoreTokens()) {
                sbuf.append(spaces).append(" * ").append(st.nextToken()).append(PrettyPrintWriter.LINE_SEPARATOR);
            }
        }

        return sbuf.toString();
    }

    /**
     * Return standard javadoc of current class.
     *
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="no-description-if-lacking" optional="true" description="Returns 'No
     *      Description' if comment is lacking."
     */
    public String firstSentenceDescription(Properties attributes) throws XDocletException
    {
        String desc = getCurrentClass().getDoc().getFirstSentence();

        if (desc == null) {
            String noDescriptionIfLackingStr = attributes.getProperty("no-description-if-lacking");
            boolean noDescriptionIfLacking = TypeConversionUtil.stringToBoolean(noDescriptionIfLackingStr, true);

            if (noDescriptionIfLacking) {
                desc = Translator.getString(XDocletMessages.class, XDocletMessages.NO_DESCRIPTION);
            }
        }

        // Check if there is a \n and replace it by a space
        return checkForWrap(desc.trim());
    }

    /**
     * Iterates over all imported classes and packages imported in the current class and returns the list. The composed
     * string has 'import ' in front of each import statement, and each import is in a separate line.
     *
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @deprecated                  Make sure the template file uses full qualified class names everywhere instead.
     * @doc.tag                     type="content"
     */
    public String importedList(Properties attributes) throws XDocletException
    {
        String currentClass = attributes.getProperty("currentClass");

        if (currentClass == null) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.TAG_MUST_INCLUDE_A_PROPERTY, new String[]{"importList", "currentClass"}));
        }

        String currentPackage = PackageTagsHandler.getPackageNameFor(currentClass);

        StringBuffer sbuf = new StringBuffer();

        Collection packages = getCurrentClass().getImportedPackages();

        for (Iterator i = packages.iterator(); i.hasNext(); ) {
            XPackage pakkage = (XPackage) i.next();

            if (!pakkage.getName().equals(currentPackage)) {
                sbuf.append("import ").append(pakkage.getName()).append(".*;").append(PrettyPrintWriter.LINE_SEPARATOR);
            }
        }

        for (Iterator j = getCurrentClass().getImportedClasses().iterator(); j.hasNext(); ) {
            XClass clazz = (XClass) j.next();

            if (!PackageTagsHandler.getPackageNameFor(clazz.toString()).equals(currentPackage)) {
                sbuf.append("import ").append(clazz.toString()).append(';').append(PrettyPrintWriter.LINE_SEPARATOR);
            }
        }

        return sbuf.toString();
    }
}
