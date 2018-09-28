/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.mockobjects;

// Java import
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

// Div import
import org.apache.commons.logging.Log;

// XJavadoc import
import xjavadoc.ParameterIterator;
import xjavadoc.XClass;
import xjavadoc.XCollections;
import xjavadoc.XExecutableMember;
import xjavadoc.XMember;
import xjavadoc.XMethod;
import xjavadoc.XParameter;

// XDoclet import
import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.mockobjects.util.*;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.tagshandler.ParameterTagsHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.TypeConversionUtil;

/**
 * Tagshandler for mockobject.
 *
 * @author               Joe Walnes
 * @author               Stig J&oslash;rgensen
 * @created              5. februar 2003
 * @xdoclet.taghandler   namespace="MockObject"
 */
public class MockObjectTagsHandler extends ParameterTagsHandler
{
    final static Properties DUMMY = new Properties();

    /**
     * Comparator for comparing two XMembers.
     */
    final static Comparator methodComparator =
        new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                XMember m1 = (XMember) o1;
                XMember m2 = (XMember) o2;

                return m1.getName().compareTo(m2.getName());
            }

            public boolean equals(Object obj)
            {
                // dumb
                return obj == this;
            }
        };

    /**
     * Returns the fully classified name of the mock class from the specified class. It will do package substition (if
     * it is specified) and use the class pattern specified to generate the class name.
     *
     * @param clazz              the class to generate a mock classname for.
     * @return                   String the fully classified name of the mock class.
     * @throws XDocletException
     */
    public static String getMockClassFor(XClass clazz)
         throws XDocletException
    {
        Log log =
            LogUtil.getLog(MockObjectTagsHandler.class, "getMockClassFor");

        // This will do package substition as specified in ant's build.xml on the subtask
        String packageName =
            PackageTagsHandler.getPackageNameFor(clazz.getContainingPackage(),
            true);
        String mockClassName =
            clazz.getDoc().getTagAttributeValue("mock.generate", "class", false);

        if (log.isDebugEnabled()) {
            log.debug("MockObject for " + clazz.getQualifiedName());
        }

        if (mockClassName == null) {
            String packagePattern = null;
            String mockClassPattern = getMockClassPattern();

            if (mockClassPattern.indexOf("{0}") != -1) {
                String className = clazz.getName();

                // Remove the 'I' (for interface) from the class name if it is present
                // TODO: Come up with a better method for doing this; maybe use regexp?
                if ((className.length() >= 2) &&
                    (className.charAt(0) == 'I') &&
                    Character.isUpperCase(className.charAt(1))) {
                    className = className.substring(1);
                }

                mockClassName =
                    MessageFormat.format(mockClassPattern, new Object[]{className});
            }
            else {
                mockClassName = mockClassPattern;
            }
        }

        if ((mockClassName.indexOf('.') == -1) &&
            (packageName.length() > 0)) {
            mockClassName = packageName + "." + mockClassName;
        }

        if (log.isDebugEnabled()) {
            log.debug("clazz.getName()=" + clazz.getName());
            log.debug("clazz.getQualifiedName()=" + clazz.getQualifiedName());
            log.debug("mockClassName=" + mockClassName);
        }

        return mockClassName;
    }

    /**
     * Returns the pattern to be used for deciding the name of the class to be generated. This is retrieved from the
     * MockObjectSubTask instance.
     *
     * @return   String The pattern to be used for deciding the name of the class to be generated
     */
    protected static String getMockClassPattern()
    {
        MockObjectSubTask mockST =
            ((MockObjectSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(MockObjectSubTask.class)));

        if (mockST != null) {
            return mockST.getMockClassPattern();
        }
        else {
            return MockObjectSubTask.DEFAULT_MOCKCLASS_PATTERN;
        }
    }

    /**
     * Iterates over all parameters in current method and returns a string containing the types of all those parameters.
     *
     * @param attributes            The attributes of the template tag
     * @return                      a string containing the types of all the parameters for the current method.
     * @exception XDocletException
     */
    public String parameterTypeList(Properties attributes)
         throws XDocletException
    {
        Collection parameters;
        StringBuffer sbuf = new StringBuffer();

        boolean constr =
            TypeConversionUtil.stringToBoolean(attributes.getProperty("forConstructor"),
            false);

        if (constr == true) {
            parameters = getCurrentConstructor().getParameters();
        }
        else {
            parameters = getCurrentMethod().getParameters();
        }

        for (ParameterIterator iter =
            XCollections.parameterIterator(parameters); iter.hasNext(); ) {
            XParameter parameter = iter.next();
            XClass type = parameter.getType();

            if (type == null) {
                throw new XDocletException("FATAL: " + parameter);
            }

            sbuf.append(CodeUtils.capitalize(type.getName()));

            for (int cnt = parameter.getDimension(); cnt > 0; cnt--) {
                sbuf.append("Array");
            }
        }

        String result = sbuf.toString();

        if (result.indexOf("null") != -1) {
            throw new XDocletException("FATAL: " + result);
        }

        return result;
    }

    /**
     * Tag for wrapping a simple type in its object counterpart.
     *
     * @param props  holds the parameters for the tag: name & type
     * @return       code for wrapping a simple type in its object counterpart.
     */
    public String wrap(Properties props)
    {
        String name = props.getProperty("name");
        String type = props.getProperty("type");

        return CodeUtils.wrapValue(name, type);
    }

    /**
     * Tag for unwrapping a simple type out of its object counterpart.
     *
     * @param props  holds the parameters for the tag: name & type
     * @return       code for unwrapping a simple type out of its object counterpart.
     */
    public String unwrap(Properties props)
    {
        String name = props.getProperty("name");
        String type = props.getProperty("type");

        return CodeUtils.unwrapValue(name, type);
    }

    /**
     * Returns the mock classname for the current class.
     *
     * @return                   String the fully classified name of the mock class.
     * @throws XDocletException
     * @see                      #getMockClassFor(XClass)
     */
    public String mockClass() throws XDocletException
    {
        return getMockClassFor(getCurrentClass());
    }

    /**
     * Returns a String with the current method using the supplied template as a boilerplate.
     *
     * @param attributes         holds the parameters for the tag: template
     * @return                   String the current method concat with the param types using the supplied template as a
     *      boilerplate.
     * @throws XDocletException  not thrown.
     */
    public String uniqueMethodName(Properties attributes)
         throws XDocletException
    {
        StringBuffer result = new StringBuffer();
        String template = attributes.getProperty("template");
        Object[] args;

        if (null == template) {
            // Use a default template
            template = "{0}{1}";
        }

        args =
            new Object[]
            {
            CodeUtils.capitalize(getCurrentMethod().getName()),
            parameterTypeList(DUMMY)
            };

        return MessageFormat.format(template, args);
    }

    /**
     * Returns a String with the current method concat with the param types using the supplied template as a
     * boilerplate.
     *
     * @param attributes         holds the parameters for the tag: template
     * @return                   String the current method concat with the param types using the supplied template as a
     *      boilerplate.
     * @throws XDocletException  not thrown.
     */
    public String uniqueMethodNameAndParam(Properties attributes)
         throws XDocletException
    {
        StringBuffer result = new StringBuffer();
        String template = attributes.getProperty("template");
        Object[] args;

        if (null == template) {
            // Use a default template
            template = "{0}{1}{3}";
        }

        args =
            new Object[]
            {
            CodeUtils.capitalize(getCurrentMethod().getName()),
            parameterTypeList(DUMMY),
            CodeUtils.capitalize(currentMethodParameter.getName())
            };

        return MessageFormat.format(template, args);
    }

    /**
     * Iterates over all the exceptions for the current method.
     *
     * @param template              the text inside the tag that should be processed.
     * @param attributes            the parameters for the tag; not used.
     * @exception XDocletException  not thrown by our code; might be thrown from <code>generate</code>.
     */
    public void forAllExceptions(String template, Properties attributes)
         throws XDocletException
    {
        Collection exceptions;
        XExecutableMember executableMember = getCurrentMethod();

        if (executableMember == null) {
            exceptions = new ArrayList();
        }
        else {
            exceptions = executableMember.getThrownExceptions();
        }

        for (Iterator iter = exceptions.iterator(); iter.hasNext(); ) {
            pushCurrentClass((XClass) iter.next());
            generate(template);
            popCurrentClass();
        }
    }

    /**
     * Returns the current exception.
     *
     * @param attributes            the parameters for the tag; not used.
     * @return
     * @exception XDocletException  not thrown by our code; might be thrown from <code>getCurrentClass</code>.
     */
    public String currentException(Properties attributes)
         throws XDocletException
    {
        return getCurrentClass() == null ? "" : getCurrentClass().getQualifiedName();
    }

    /**
     * Processes the text inside the tag if an exception is thrown by the current exception.
     *
     * @param template              the text inside the tag that should be processed.
     * @param attributes            the parameters for the tag; not used.
     * @exception XDocletException  not thrown by our code; might be thrown from <code>generate</code>.
     */
    public void ifThrowsException(String template, Properties attributes)
         throws XDocletException
    {
        Collection exceptions;
        XExecutableMember executableMember = getCurrentMethod();

        if (executableMember == null) {
            exceptions = new ArrayList();
        }
        else {
            exceptions = executableMember.getThrownExceptions();
        }

        if ((exceptions != null) && (exceptions.size() > 0)) {
            generate(template);
        }
    }

    protected String getTagParam(String tagName, String paramName,
        String defaultValue)
         throws XDocletException
    {
        Properties p = new Properties();

        p.setProperty("tagName", tagName);
        p.setProperty("paramName", paramName);
        p.setProperty("default", defaultValue);

        return getTagValue(p, FOR_CLASS);
    }
}
