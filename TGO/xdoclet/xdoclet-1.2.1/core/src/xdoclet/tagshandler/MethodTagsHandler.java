/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;


import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.util.DocletUtil;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Method"
 * @version              $Revision: 1.29 $
 */
public class MethodTagsHandler extends AbstractProgramElementTagsHandler
{
    public static String getMethodTypeFor(XMethod method)
    {
        return method.getReturnType().getType().getQualifiedName() + method.getReturnType().getDimensionAsString();
    }

    /**
     * Merge with modified SubTask.methodNameWithoutPrefix
     *
     * @param currentMethod  Description of Parameter
     * @return               Description of the Returned Value
     */
    public static String getMethodNameWithoutPrefixFor(XMethod currentMethod)
    {
        String propertyName = currentMethod.getPropertyName();

        if (Character.isLowerCase(propertyName.charAt(0)))
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        else
            return propertyName;
    }

    /**
     * Gets the PropertyNameFor attribute of the MethodTagsHandler class
     *
     * @param method  Describe what the parameter does
     * @return        The PropertyNameFor value
     */
    public static String getPropertyNameFor(XMethod method)
    {
        return method.getPropertyName();
    }

    /**
     * Returns true if the str string starts with a getter prefix ("get" or "is").
     *
     * @param str  Description of Parameter
     * @return     The Getter value
     */
    public static boolean isGetter(String str)
    {
        return str.startsWith("get") || str.startsWith("is");
    }

    /**
     * Returns true if the str string starts with "set" prefix.
     *
     * @param str
     * @return
     */
    public static boolean isSetter(String str)
    {
        return str.startsWith("set");
    }

    public static boolean isGetterMethod(XMethod method)
    {
        return method.isPropertyAccessor();
    }

    public static boolean isSetterMethod(XMethod method)
    {
        return method.isPropertyMutator();
    }

    /**
     * Returns true if a method with the specified methodName+parameters is found in the class clazz. The parameters
     * array can be empty, if so any method with any set of parameters is considered equal to the method we're searching
     * for. if not empty all parameters of the method must be equal to the ones specified in parameters array to have
     * "method equality".
     *
     * @param clazz                 Description of Parameter
     * @param methodName            Description of Parameter
     * @param parameters            Description of Parameter
     * @param setCurrentMethod
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    public static boolean hasMethod(XClass clazz, String methodName, String[] parameters, boolean setCurrentMethod)
         throws XDocletException
    {
        return hasExecutableMember(clazz, methodName, parameters, setCurrentMethod, FOR_METHOD);
    }

    /**
     * Returns 'get' or 'is' getter prefix part of the current method. Returns empty string if the method doesn't start
     * with either of the two getter prefixes.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String getterPrefix() throws XDocletException
    {
        if (getCurrentMethod().getName().startsWith("get")) {
            return "get";
        }
        else if (getCurrentMethod().getName().startsWith("is")) {
            return "is";
        }
        else if (getCurrentMethod().getName().startsWith("set")) {
            // for boolean here we don't know if it is get or is, lets find it
            String[] params = {getCurrentMethod().getReturnType().getType().getQualifiedName()};

            if (hasMethod(getCurrentClass(), "is" + methodNameWithoutPrefix(), params, false)) {
                return "is";
            }
            else {
                return "get";
            }
        }

        return "";
    }

    /**
     * Returns the getter method name for the current method by prefixing the method name with the proper getter prefix.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         #methodNameWithoutPrefix()
     * @see                         #setterMethod()
     * @see                         #getterPrefix()
     * @doc.tag                     type="content"
     */
    public String getterMethod() throws XDocletException
    {
        return getterPrefix() + methodNameWithoutPrefix();
    }

    /**
     * Returns the setter method name for the current method by prefixing the method name with a 'set' and removing the
     * getter method's 'get' or 'is' prefixes, if any.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         #methodNameWithoutPrefix()
     * @see                         #getterMethod()
     * @doc.tag                     type="content"
     */
    public String setterMethod() throws XDocletException
    {
        return "set" + methodNameWithoutPrefix();
    }

    /**
     * Evaluate the body if current class has a method with the specified name+parameters. If parameters not specified
     * then any method with the given name and any set of parameters is considered equal to the given method name and so
     * the test result is positive and the body is evaluated. This method change the current method to the one
     * specified.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasMethod(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the method we're searching for
     *      its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a method that has
     *      the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void setCurrentMethod(String template, Properties attributes) throws XDocletException
    {
        String methodName = attributes.getProperty("name");
        String parametersStr = attributes.getProperty("parameters");
        String delimiter = attributes.getProperty("delimiter");

        String[] parameters = null;

        if (parametersStr != null) {
            if (delimiter == null) {
                delimiter = PARAMETER_DELIMITER;
            }

            parameters = DocletUtil.tokenizeDelimitedToArray(parametersStr, delimiter);
        }

        XMethod oldMethod = getCurrentMethod();

        if (hasMethod(getCurrentClass(), methodName, parameters, true)) {
            generate(template);
        }

        setCurrentMethod(oldMethod);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @doc.tag                     type="content"
     */
    public String modifiers() throws XDocletException
    {
        return modifiers(FOR_METHOD);
    }

    /**
     * The comment for the current method.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         ClassTagsHandler#classComment(java.util.Properties)
     * @doc.tag                     type="content"
     * @doc.param                   name="no-comment-signs" optional="true" values="true,false" description="If true
     *      then don't decorate the comment with comment signs."
     */
    public String methodComment(Properties attributes) throws XDocletException
    {
        return memberComment(attributes, FOR_METHOD);
    }

    /**
     * Evaluates the body block if current method has a javadoc comment.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void ifHasMethodComment(String template) throws XDocletException
    {
        Properties attributes = new Properties();

        attributes.setProperty("no-comment-signs", "true");

        String comment = methodComment(attributes);

        if (!comment.trim().equals("")) {
            generate(template);
        }
    }

    /**
     * Iterates over all exceptions thrown by the current method and returns a string containing definition of all those
     * exceptions.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="method" optional="true" description="The method name of which exceptions list
     *      is extracted. If not specified then current method is used."
     * @doc.param                   name="skip" optional="true" description="A comma-separated list of exceptions that
     *      should be skipped and not put into the list."
     * @doc.param                   name="append" optional="true" description="A comma-separated list of exceptions that
     *      should be always appended regardless if current method has that method defined or not."
     */
    public String exceptionList(Properties attributes) throws XDocletException
    {
        return exceptionList(attributes, FOR_METHOD);
    }

    /**
     * Evaluate the body block if current method is abstract.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotAbstract(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="method" optional="true" description="The method name of which abstractness is
     *      evaluated. If not specified then current method is used."
     */
    public void ifIsAbstract(String template, Properties attributes) throws XDocletException
    {
        if (isAbstract(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if current method is not abstract.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsAbstract(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="method" optional="true" description="The method name of which exceptions list
     *      is extracted. If not specified then current method is used."
     */
    public void ifIsNotAbstract(String template, Properties attributes) throws XDocletException
    {
        if (!isAbstract(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if current method returns void.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifDoesntReturnVoid(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="method" optional="true" description="The method name whose return type is
     *      checked. If not specified then current method is used."
     */
    public void ifReturnsVoid(String template, Properties attributes) throws XDocletException
    {
        if (returnsVoid(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if current method doesn't return void.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifReturnsVoid(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="method" optional="true" description="The method name whose return type is
     *      checked. If not specified then current method is used."
     */
    public void ifDoesntReturnVoid(String template, Properties attributes) throws XDocletException
    {
        if (!returnsVoid(attributes)) {
            generate(template);
        }
    }

    /**
     * Loops through all methods for all classes after first sorting all the methods.
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
    public void forAllClassMethods(String template, Properties attributes) throws XDocletException
    {
        String typeName = attributes.getProperty("type");
        int extent = TypeTagsHandler.extractExtentType(attributes.getProperty("extent"));

        Collection classes = getAllClasses();
        SortedSet methods = new TreeSet();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (typeName == null || TypeTagsHandler.isOfType(clazz, typeName, extent)) {
                Collection classMethods = clazz.getMethods();

                methods.addAll(classMethods);

            }
        }

        Iterator methodIterator = methods.iterator();

        while (methodIterator.hasNext()) {
            XMethod current = (XMethod) methodIterator.next();

            setCurrentClass(current.getContainingClass());
            setCurrentMethod(current);

            generate(template);
        }
    }

    /**
     * Iterates over all methods of current class and evaluates the body of the tag for each method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="If true then
     *      traverse superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="sort" optional="true" values="true,false" description="If true then sort the
     *      methods list."
     */
    public void forAllMethods(String template, Properties attributes) throws XDocletException
    {
        forAllMembers(template, attributes, FOR_METHOD);
    }

    /**
     * Evaluates the body if current method doesn't have at least one tag with the specified name.
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
    public void ifDoesntHaveMethodTag(String template, Properties attributes) throws XDocletException
    {
        if (!hasTag(attributes, FOR_METHOD)) {
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
     * Evaluates the body if current method has at least one tag with the specified name.
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
    public void ifHasMethodTag(String template, Properties attributes) throws XDocletException
    {
        if (hasTag(attributes, FOR_METHOD)) {
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
     * Evaluate the current block, and then restore the current method before continuing.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void executeAndRestoreMethod(String template, Properties attributes) throws XDocletException
    {
        XMethod method = getCurrentMethod();

        generate(template);
        setCurrentMethod(method);
    }

    /**
     * Evaluates the body if value for the method tag equals the specified value.
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
    public void ifMethodTagValueEquals(String template, Properties attributes) throws XDocletException
    {
        if (isTagValueEqual(attributes, FOR_METHOD)) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if method name equals to the specified value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The method name."
     */
    public void ifMethodNameEquals(String template, Properties attributes) throws XDocletException
    {
        ifMethodNameEquals_Impl(template, attributes, true);
    }

    /**
     * Evaluates the body if method name equals to the specified value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The method name."
     */
    public void ifMethodNameNotEquals(String template, Properties attributes) throws XDocletException
    {
        ifMethodNameEquals_Impl(template, attributes, false);
    }

    /**
     * Evaluates the body if value for the method tag not equals the specified value.
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
    public void ifMethodTagValueNotEquals(String template, Properties attributes) throws XDocletException
    {
        if (!isTagValueEqual(attributes, FOR_METHOD)) {
            generate(template);
        }
    }

    /**
     * Iterates over all method tags with the specified tagName for the current method probably inside of a
     * forAllMethodTags body.
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
    public String methodTagValue(Properties attributes) throws XDocletException
    {
        return getExpandedDelimitedTagValue(attributes, FOR_METHOD);
    }

    /**
     * Iterates over all tags of current method and evaluates the body of the tag for each method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name."
     */
    public void forAllMethodTags(String template, Properties attributes) throws XDocletException
    {
        forAllMemberTags(template, attributes, FOR_METHOD, XDocletTagshandlerMessages.ONLY_CALL_METHOD_NOT_NULL, new String[]{"forAllMethodTags"});
    }

    /**
     * Iterates over all tokens in current method tag with the name tagName and evaluates the body for every token.
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
    public void forAllMethodTagTokens(String template, Properties attributes) throws XDocletException
    {
        forAllMemberTagTokens(template, attributes, FOR_METHOD);
    }

    /**
     * Return standard javadoc of current method.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String firstSentenceDescriptionOfCurrentMethod() throws XDocletException
    {
        return firstSentenceDescriptionOfCurrentMember(getCurrentMethod());
    }

    /**
     * Returns the return type of the current method.
     *
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodType(Properties attributes) throws XDocletException
    {
        return getMethodTypeFor(getCurrentMethod());
    }

    public void ifIsOfType(String template, Properties attributes) throws XDocletException
    {
        if (ifIsOfTypeImpl(template, attributes))
            generate(template);
    }

    public void ifIsNotOfType(String template, Properties attributes) throws XDocletException
    {
        if (!ifIsOfTypeImpl(template, attributes))
            generate(template);
    }

    public boolean ifIsOfTypeImpl(String template, Properties attributes) throws XDocletException
    {
        return methodType(attributes).equals(attributes.getProperty("type"));
    }

    /**
     * Returns the name of the current method.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodName(Properties attributes) throws XDocletException
    {
        if (attributes != null) {
            String value = (String) attributes.get("value");

            if (value != null) {
                String m = getCurrentMethod().getName().substring(Integer.parseInt(value));
                // replace first character to lowercase
                char firstU = m.charAt(0);
                char firstL = Character.toLowerCase(firstU);

                return firstL + m.substring(1);
            }
        }

        return getCurrentMethod() != null ? getCurrentMethod().getName() : "";
    }

    /**
     * Returns the name of the current method without the first three characters. Used for cases where the method name
     * without the get/set prefix is needed.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodNameWithoutPrefix() throws XDocletException
    {
        return getMethodNameWithoutPrefixFor(getCurrentMethod());
    }

    /**
     * Returns the current method name. Used inside block elements.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    public String currentMethodName() throws XDocletException
    {
        return getCurrentMethod().getName();
    }

    /**
     * Returns the property name extracted from the current method name. Remove any getter/setter prefix from method
     * name and decapitalize it.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String propertyName() throws XDocletException
    {
        return getPropertyNameFor(getCurrentMethod());
    }

    /**
     * Evaluate the body if current class has a method with the specified name+parameters. If parameters not specified
     * then any method with the given name and any set of parameters is considered equal to the given method name and so
     * the test result is positive and the body is evaluated. This method does not change the current method to the one
     * specified.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifDoesntHaveMethod(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the method we're searching for
     *      its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a method that has
     *      the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void ifHasMethod(String template, Properties attributes) throws XDocletException
    {
        ifHasMethod_Impl(template, attributes, true);
    }

    /**
     * Evaluate the body if current class doesn't have a method with the specified name+parameters. If parameters not
     * specified then any method with the given name and any set of parameters is considered equal to the given method
     * name and so the test result is positive and the body is evaluated.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasMethod(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The name of the method we're searching for
     *      its existence in current class."
     * @doc.param                   name="parameters" optional="true" description="We're searching for a method that has
     *      the exact set of parameters specified in parameters param."
     * @doc.param                   name="delimiter" optional="true" description="The parameters param is delimited by
     *      the string specified in delimiter parameter."
     */
    public void ifDoesntHaveMethod(String template, Properties attributes) throws XDocletException
    {
        ifHasMethod_Impl(template, attributes, false);
    }

    /**
     * Evaluates the body if value for the method tag equals the specified value.
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
    public void ifIsGetter(String template, Properties attributes) throws XDocletException
    {
        String method_name = attributes.getProperty("method");

        if (method_name != null) {
            if (isGetter(method_name)) {
                generate(template);
            }
        }
        else {
            if (isGetterMethod(getCurrentMethod())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluates the body if value for the method tag equals the specified value.
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
    public void ifIsSetter(String template, Properties attributes) throws XDocletException
    {
        String method_name = attributes.getProperty("method");

        if (method_name != null) {
            if (isSetter(method_name)) {
                generate(template);
            }
        }
        else {
            if (isSetterMethod(getCurrentMethod())) {
                generate(template);
            }
        }
    }

    public void ifIsPublic(String template) throws XDocletException
    {
        if (getCurrentMethod().isPublic()) {
            generate(template);
        }
    }

    private boolean isAbstract(Properties attributes) throws XDocletException
    {
        String methodName = attributes.getProperty("method");

        if (methodName == null) {
            return getCurrentMethod().isAbstract();
        }
        else {
            XMethod method = (XMethod) getXExecutableMemberForMemberName(methodName, true, FOR_METHOD);

            // no method with the specified name found in class
            if (method == null) {
                throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.METHOD_NOT_FOUND, new String[]{methodName}));
            }

            return method.isAbstract();
        }
    }

    private boolean returnsVoid(Properties attributes) throws XDocletException
    {
        String methodName = attributes.getProperty("method");

        if (methodName == null) {
            return ("void".equals(getMethodTypeFor(getCurrentMethod())));
        }
        else {
            XMethod method = (XMethod) getXExecutableMemberForMemberName(methodName, true, FOR_METHOD);

            // no method with the specified name found in class
            if (method == null) {
                throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.METHOD_NOT_FOUND, new String[]{methodName}));
            }

            return ("void".equals(getMethodTypeFor(method)));
        }
    }

    private void ifMethodNameEquals_Impl(String template, Properties attributes, boolean condition) throws XDocletException
    {
        String method_name = attributes.getProperty("name");

        if (getCurrentMethod().getName().equals(method_name) == condition) {
            generate(template);
        }
    }

    /**
     * The implementation of ifHasMethod and ifDoesntHaveMethod tags.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @param hasMethod             Description of Parameter
     * @exception XDocletException  Description of Exception
     * @see                         #ifHasMethod(java.lang.String,java.util.Properties)
     * @see                         #ifDoesntHaveMethod(java.lang.String,java.util.Properties)
     * @see                         #hasMethod(xjavadoc.XClass,java.lang.String,java.lang.String[],boolean)
     */
    private void ifHasMethod_Impl(String template, Properties attributes, boolean hasMethod) throws XDocletException
    {
        Log log = LogUtil.getLog(MethodTagsHandler.class, "ifHasMethod_Impl");

        String methodName = attributes.getProperty("name");
        String parametersStr = attributes.getProperty("parameters");
        String delimiter = attributes.getProperty("delimiter");

        String[] parameters = null;

        if (log.isDebugEnabled()) {
            log.debug("methodName=" + methodName);
            log.debug("parametersStr=" + parametersStr);
            log.debug("delimiter=" + delimiter);
            log.debug("hasMethod=" + hasMethod);
            log.debug("getCurrentClass()=" + getCurrentClass());
        }

        if (parametersStr != null) {
            if (delimiter == null) {
                delimiter = PARAMETER_DELIMITER;
            }

            parameters = DocletUtil.tokenizeDelimitedToArray(parametersStr, delimiter);

            if (log.isDebugEnabled()) {
                log.debug("parameters.length=" + parameters.length);
                if (parameters.length > 0)
                    log.debug("parameters[0]=" + parameters[0]);
            }
        }
        if (hasMethod(getCurrentClass(), methodName, parameters, false) == hasMethod) {
            log.debug("method found.");
            generate(template);
        }
        else {
            log.debug("method not found.");
        }
    }
}
