/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.Collection;

import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;
import xjavadoc.XConstructor;
import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.util.DocletUtil;
import xdoclet.util.LogUtil;

/**
 * @author               Jerome Bernard (jerome.bernard@xtremejava.com)
 * @created              Jan 18, 2002
 * @xdoclet.taghandler   namespace="Constructor"
 * @version              $Revision: 1.11 $
 */
public class ConstructorTagsHandler extends AbstractProgramElementTagsHandler
{

    /**
     * Evaluate the body if current class has a constructor with the specified name+parameters. If parameters not
     * specified then any constructor with the given name and any set of parameters is considered equal to the given
     * constructor name and so the test result is positive and the body is evaluated. This constructor change the
     * current constructor to the one specified.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasConstructor(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the constructor we're
     *      searching for its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a constructor
     *      that has the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void setCurrentConstructor(String template, Properties attributes) throws XDocletException
    {
        String constructorName = attributes.getProperty("name");
        String parametersStr = attributes.getProperty("parameters");
        String delimiter = attributes.getProperty("delimiter");

        String[] parameters = null;

        if (parametersStr != null) {
            if (delimiter == null) {
                delimiter = PARAMETER_DELIMITER;
            }

            parameters = DocletUtil.tokenizeDelimitedToArray(parametersStr, delimiter);
        }

        XConstructor oldConstructor = getCurrentConstructor();

        if (hasConstructor(getCurrentClass(), constructorName, parameters, true)) {
            generate(template);
        }

        setCurrentConstructor(oldConstructor);
    }

    /**
     * The comment for the current constructor.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         ClassTagsHandler#classComment(java.util.Properties)
     * @doc.tag                     type="content"
     * @doc.param                   name="no-comment-signs" optional="true" values="true,false" description="If true
     *      then don't decorate the comment with comment signs."
     */
    public String constructorComment(Properties attributes) throws XDocletException
    {
        return memberComment(attributes, FOR_CONSTRUCTOR);
    }

    /**
     * Iterates over all exceptions thrown by the current constructor and returns a string containing definition of all
     * those exceptions.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="constructor" optional="true" description="The constructor name of which
     *      exceptions list is extracted. If not specified then current constructor is used."
     * @doc.param                   name="skip" optional="true" description="A comma-separated list of exceptions that
     *      should be skipped and not put into the list."
     * @doc.param                   name="append" optional="true" description="A comma-separated list of exceptions that
     *      should be always appended regardless if current constructor has that constructor defined or not."
     */
    public String exceptionList(Properties attributes) throws XDocletException
    {
        return exceptionList(attributes, FOR_CONSTRUCTOR);
    }

    /**
     * Loops through all constructors for all classes after first sorting all the constructors.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="type" optional="true" description="For all classes by the type."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void forAllClassConstructors(String template, Properties attributes) throws XDocletException
    {
        String typeName = attributes.getProperty("type");
        int extent = TypeTagsHandler.extractExtentType(attributes.getProperty("extent"));

        SortedSet constructors = new TreeSet();

        for (Iterator i = getAllClasses().iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (typeName == null || TypeTagsHandler.isOfType(clazz, typeName, extent)) {
                Collection classConstructors = clazz.getConstructors();

                constructors.addAll(classConstructors);

            }
        }

        Iterator constructorIterator = constructors.iterator();

        while (constructorIterator.hasNext()) {
            XConstructor current = (XConstructor) constructorIterator.next();

            setCurrentClass(current.getContainingClass());
            setCurrentConstructor(current);

            generate(template);
        }
    }

    /**
     * Iterates over all constructors of current class and evaluates the body of the tag for each constructor.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="If true then
     *      traverse superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="sort" optional="true" values="true,false" description="If true then sort the
     *      constructors list."
     */
    public void forAllConstructors(String template, Properties attributes) throws XDocletException
    {
        forAllMembers(template, attributes, FOR_CONSTRUCTOR);
    }

    /**
     * Evaluates the body if current constructor doesn't have at least one tag with the specified name.
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
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifDoesntHaveConstructorTag(String template, Properties attributes) throws XDocletException
    {
        if (!hasTag(attributes, FOR_CONSTRUCTOR)) {
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
     * Evaluates the body if current constructor has at least one tag with the specified name.
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
     * @doc.param                   name="error" description="Show this error message if no tag found."
     */
    public void ifHasConstructorTag(String template, Properties attributes) throws XDocletException
    {
        if (hasTag(attributes, FOR_CONSTRUCTOR)) {
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
     * Evaluate the current block, and then restore the current constructor before continuing.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void executeAndRestoreConstructor(String template, Properties attributes) throws XDocletException
    {
        XConstructor constructor = getCurrentConstructor();

        generate(template);
        setCurrentConstructor(constructor);
    }

    /**
     * Evaluates the body if value for the constructor tag equals the specified value.
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
     */
    public void ifConstructorTagValueEquals(String template, Properties attributes) throws XDocletException
    {
        if (isTagValueEqual(attributes, FOR_CONSTRUCTOR)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if value for the constructor tag not equals the specified value.
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
     */
    public void ifConstructorTagValueNotEquals(String template, Properties attributes) throws XDocletException
    {
        if (!isTagValueEqual(attributes, FOR_CONSTRUCTOR)) {
            generate(template);
        }
    }

    /**
     * Iterates over all constructor tags with the specified tagName for the current constructor probably inside of a
     * forAllConstructorTags body.
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
     */
    public String constructorTagValue(Properties attributes) throws XDocletException
    {
        return getExpandedDelimitedTagValue(attributes, FOR_CONSTRUCTOR);
    }

    /**
     * Iterates over all tags of current constructor and evaluates the body of the tag for each constructor.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     */
    public void forAllConstructorTags(String template, Properties attributes) throws XDocletException
    {
        forAllMemberTags(template, attributes, FOR_CONSTRUCTOR, XDocletTagshandlerMessages.ONLY_CALL_CONSTRUCTOR_NOT_NULL, new String[]{"forAllConstructorTags"});
    }

    /**
     * Iterates over all tokens in current constructor tag with the name tagName and evaluates the body for every token.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     * @doc.param                   name="delimiter" description="delimiter for the StringTokenizer. consult javadoc for
     *      java.util.StringTokenizer default is ','"
     * @doc.param                   name="skip" description="how many tokens to skip on start"
     */
    public void forAllConstructorTagTokens(String template, Properties attributes) throws XDocletException
    {
        forAllMemberTagTokens(template, attributes, FOR_CONSTRUCTOR);
    }

    /**
     * Return standard javadoc of current constructor.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String firstSentenceDescriptionOfCurrentConstructor() throws XDocletException
    {
        return firstSentenceDescriptionOfCurrentMember(getCurrentConstructor());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String modifiers() throws XDocletException
    {
        return modifiers(FOR_CONSTRUCTOR);
    }

    /**
     * Returns the name of the current constructor.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String constructorName(Properties attributes) throws XDocletException
    {
        if (attributes != null) {
            String value = (String) attributes.get("value");

            if (value != null) {
                String m = getCurrentConstructor().getName().substring(Integer.parseInt(value));
                // replace first character to lowercase
                char firstU = m.charAt(0);
                char firstL = Character.toLowerCase(firstU);

                return firstL + m.substring(1);
            }
        }
        return getCurrentConstructor() != null ? getCurrentConstructor().getName() : "";
    }

    /**
     * Returns the current constructor name. Used inside block elements.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    public String currentConstructorName() throws XDocletException
    {
        return getCurrentConstructor().getName();
    }

    /**
     * Evaluate the body if current class has a constructor with the specified name+parameters. If parameters not
     * specified then any constructor with the given name and any set of parameters is considered equal to the given
     * constructor name and so the test result is positive and the body is evaluated. This constructor does not change
     * the current constructor to the one specified.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifDoesntHaveConstructor(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the constructor we're
     *      searching for its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a constructor
     *      that has the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void ifHasConstructor(String template, Properties attributes) throws XDocletException
    {
        ifHasConstructor_Impl(template, attributes, true);
    }

    /**
     * Evaluate the body if current class doesn't have a constructor with the specified name+parameters. If parameters
     * not specified then any constructor with the given name and any set of parameters is considered equal to the given
     * constructor name and so the test result is positive and the body is evaluated.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasConstructor(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the constructor we're
     *      searching for its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a constructor
     *      that has the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void ifDoesntHaveConstructor(String template, Properties attributes) throws XDocletException
    {
        ifHasConstructor_Impl(template, attributes, false);
    }

    /**
     * Returns true if a constructor with the specified parameters is found in the class clazz. The parameters array can
     * be empty, if so any constructor with any set of parameters is considered equal to the constructor we're searching
     * for. If not empty, all parameters of the constructor must be equal to the ones specified in parameters array to
     * have "constructor equality".
     *
     * @param clazz                  Description of Parameter
     * @param constructorName        Description of Parameter
     * @param parameters             Description of Parameter
     * @param setCurrentConstructor
     * @return                       Description of the Returned Value
     * @exception XDocletException
     */
    private boolean hasConstructor(XClass clazz, String constructorName, String[] parameters, boolean setCurrentConstructor)
         throws XDocletException
    {
        return hasExecutableMember(clazz, constructorName, parameters, setCurrentConstructor, FOR_CONSTRUCTOR);
    }

    /**
     * The implementation of ifHasConstructor and ifDoesntHaveConstructor tags.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @param hasConstructor        Description of Parameter
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasConstructor(java.lang.String,java.util.Properties)
     * @see                         #ifDoesntHaveConstructor(java.lang.String,java.util.Properties)
     * @see                         #hasConstructor(xjavadoc.XClass,java.lang.String,java.lang.String[],boolean)
     */
    private void ifHasConstructor_Impl(String template, Properties attributes, boolean hasConstructor) throws XDocletException
    {
        Log log = LogUtil.getLog(ConstructorTagsHandler.class, "ifHasConstructor_Impl");

        String constructorName = attributes.getProperty("name");
        String parametersStr = attributes.getProperty("parameters");
        String delimiter = attributes.getProperty("delimiter");

        String[] parameters = null;

        if (log.isDebugEnabled()) {
            log.debug("constructorName=" + constructorName);
            log.debug("parametersStr=" + parametersStr);
            log.debug("delimiter=" + delimiter);
            log.debug("hasConstructor=" + hasConstructor);
            log.debug("getCurrentClass()=" + getCurrentClass());
        }

        if (parametersStr != null) {
            if (delimiter == null) {
                delimiter = PARAMETER_DELIMITER;
            }

            parameters = DocletUtil.tokenizeDelimitedToArray(parametersStr, delimiter);

            if (log.isDebugEnabled()) {
                log.debug("parameters.length=" + parameters.length);
                log.debug("parameters[0]=" + parameters[0]);
            }
        }
        if (hasConstructor(getCurrentClass(), constructorName, parameters, false) == hasConstructor) {
            log.debug("constructor found.");
            generate(template);
        }
        else {
            log.debug("constructor not found.");
        }
    }
}
