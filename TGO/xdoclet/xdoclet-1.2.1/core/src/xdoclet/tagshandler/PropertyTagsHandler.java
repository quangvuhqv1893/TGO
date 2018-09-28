/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.util.LogUtil;

/**
 * PropertyTagsHandler.java
 *
 * @author               <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created              Wed Feb 27 21:53:15 2002
 * @xdoclet.taghandler   namespace="Property"
 * @version              $Revision: 1.12 $
 */
public class PropertyTagsHandler extends AbstractProgramElementTagsHandler
{
    /**
     * Searches for the XMethod of the method with name methodName and returns it. Copied from MethodTagsHandler
     *
     * @param methodName  Description of Parameter
     * @return            The XMethodForMethodName value or null if not found
     */
    public static XMethod getXMethodForMethodName(String methodName)
    {
        return getXMethodForMethodName(methodName, false);
    }

    public static XMethod getXMethodForMethodName(String methodName, boolean superclasses)
    {
        if (methodName != null) {
            return extractXMethod(getCurrentClass(), methodName, superclasses);
        }

        return null;
    }

    /**
     * Extracts the <code>XMethod</code> from the specified <code>XClass</code>.
     *
     * @param clazz         the class to search
     * @param methodName    the method name to look for
     * @param superclasses  <code>true</code> if super classes are to be searched; <code>false</code> otherwise
     * @return              the <code>XMethod</code> or <code>null</code> if it wasn't found
     */
    private static XMethod extractXMethod(XClass clazz, String methodName, boolean superclasses)
    {
        Collection methods = clazz.getMethods();

        for (Iterator i = methods.iterator(); i.hasNext(); ) {
            XMethod method = (XMethod) i.next();

            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        if (superclasses) {
            if (clazz.getSuperclass() != null) {
                return extractXMethod(clazz.getSuperclass(), methodName, superclasses);
            }
        }

        return null;
    }

    /**
     * Evaluates the body block for each property of current mbean.You may set whether superclasses are examined also
     * with the superclass attribute. Finds properties with getter, setter, or both. The getter and setter should have
     * javabean naming convention. Only methods with the supplied tag are considered in looking for properties.
     *
     * @param template              The body of the block tag
     * @param attributes
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="Include
     *      properties of superclasses. True by default."
     * @doc.param                   name="tagName" optional="false" description="The required tag for methods to be
     *      considered a getter or setter. For example, jmx:managed-attribute."
     */
    public void forAllPropertiesWithTag(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(PropertyTagsHandler.class, "forAllPropertiesWithTag");

        log.debug("in forAllPropertiesWithTag");

        String requiredTag = attributes.getProperty("tagName");

        if (requiredTag == null) {
            throw new XDocletException("missing required tag parameter in forAllPropertiesHavingTag");
        }

        XClass oldClass = getCurrentClass();
        XClass superclass = null;
        Collection already = new ArrayList();

        // loop over superclasses
        do {
            XMethod oldCurrentMethod = getCurrentMethod();

            Collection methods = getCurrentClass().getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod currentMethod = (XMethod) j.next();

                log.debug("looking at method " + currentMethod.getName());
                if (currentMethod.getDoc().hasTag(requiredTag)) {
                    setCurrentMethod(currentMethod);

                    String propertyName = currentMethod.getPropertyName();

                    log.debug("property identified " + propertyName);

                    if (!already.contains(propertyName)) {
                        generate(template);

                        already.add(propertyName);
                    }
                }

                setCurrentMethod(oldCurrentMethod);
            }
            // Add super class info
            superclass = getCurrentClass().getSuperclass();

            if (superclass != null) {
                pushCurrentClass(superclass);
            }

        } while (superclass != null);

        setCurrentClass(oldClass);
    }

    /**
     * The block tag <code>ifHasGetMethodWithTag</code> looks for a get method based on the attribute name from the
     * current method, sets the current method to that get method, and applies the template if found. This is used to
     * look for getters for mbean managed attributes. The get method found may be the current method.
     *
     * @param template              a <code>String</code> value
     * @param attributes            a <code>Properties</code> value
     * @exception XDocletException  if an error occurs
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The required tag for methods to be
     *      considered a getter or setter. For example, jmx:managed-attribute."
     */
    public void ifHasGetMethodWithTag(String template, Properties attributes) throws XDocletException
    {
        XMethod getMethod = getGetMethodWithTag(attributes);

        if (getMethod != null) {
            XMethod oldMethod = getCurrentMethod();

            setCurrentMethod(getMethod);
            try {
                generate(template);
            }
            finally {
                setCurrentMethod(oldMethod);
            }
        }
    }

    /**
     * The block tag <code>ifHasSetMethodWithTag</code> looks for a set method based on the attribute name from the
     * current method, sets the current method to that set method, and applies the template if found. This is used to
     * look for setters for mbean managed attributes. The set method found may be the current method.
     *
     * @param template              a <code>String</code> value
     * @param attributes            a <code>Properties</code> value
     * @exception XDocletException  if an error occurs
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The required tag for methods to be
     *      considered a getter or setter. For example, jmx:managed-attribute."
     */
    public void ifHasSetMethodWithTag(String template, Properties attributes) throws XDocletException
    {
        XMethod setMethod = getSetMethodWithTag(attributes);

        if (setMethod != null) {
            XMethod oldMethod = getCurrentMethod();

            setCurrentMethod(setMethod);

            try {
                generate(template);
            }
            finally {
                setCurrentMethod(oldMethod);
            }
        }
    }

    /**
     * The <code>propertyTypeWithTag</code> method figures out the type for the current property with tag by looking for
     * a getter, then a setter.
     *
     * @param attributes            a <code>Properties</code> value including the tagName required.
     * @return                      the <code>String</code> fully qualified name of the property type.
     * @exception XDocletException  if an error occurs
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The required tag for methods to be
     *      considered a getter or setter. For example, jmx:managed-attribute."
     */
    public String propertyTypeWithTag(Properties attributes) throws XDocletException
    {
        XMethod getter = getGetMethodWithTag(attributes);

        if (getter != null) {
            return MethodTagsHandler.getMethodTypeFor(getter);
        }

        XMethod setter = getSetMethodWithTag(attributes);

        if (setter != null) {
            XParameter parameter = (XParameter) setter.getParameters().iterator().next();

            return parameter.getType().getQualifiedName();
        }
        throw new XDocletException("no current property found for method " + getCurrentMethod().getName());
    }


    /**
     * Looks for a get or set method with the required tag for the current property that also has the requested
     * parameter, and returns the value of the requested parameter if present.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The tag name required for a getter or
     *      setter to belong to a property."
     * @doc.param                   name="paramName" description="The parameter name. Required for property parameter
     *      values. content of the tag is returned."
     * @doc.param                   name="default" description="The default value is returned if there is no value for
     *      the parameter requested.
     */
    public String paramValueWithTag(Properties attributes) throws XDocletException
    {

        XMethod oldMethod = getCurrentMethod();

        XMethod getter = getGetMethodWithTag(attributes);

        if (getter != null) {
            setCurrentMethod(getter);

            String value = delimit(getTagValue(attributes, FOR_METHOD), attributes);

            if (value != null) {
                setCurrentMethod(oldMethod);
                return value;
            }
            // end of if ()
        }

        XMethod setter = getSetMethodWithTag(attributes);

        if (setter != null) {
            setCurrentMethod(setter);

            String value = delimit(getTagValue(attributes, FOR_METHOD), attributes);

            if (value != null) {
                setCurrentMethod(oldMethod);
                return value;
            }
            // end of if ()
        }
        setCurrentMethod(oldMethod);
        return attributes.getProperty("default");
    }

    /**
     * Determines if there is a get or set method with the required tag for the current property that also has the
     * requested parameter.
     *
     * @param attributes            The attributes of the template tag
     * @param template
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name required for a getter or
     *      setter to belong to a property."
     * @doc.param                   name="paramName" description="The parameter name. Required for property parameter
     *      values. content of the tag is returned."
     */
    public void ifHasParamWithTag(String template, Properties attributes) throws XDocletException
    {

        XMethod oldMethod = getCurrentMethod();

        XMethod getter = getGetMethodWithTag(attributes);

        if (getter != null) {
            setCurrentMethod(getter);

            boolean value = hasTag(attributes, FOR_METHOD);

            if (value) {
                setCurrentMethod(oldMethod);
                generate(template);
                return;
            }

        }

        XMethod setter = getSetMethodWithTag(attributes);

        if (setter != null) {
            setCurrentMethod(setter);

            boolean value = hasTag(attributes, FOR_METHOD);

            if (value) {
                setCurrentMethod(oldMethod);
                generate(template);
                return;
            }
        }
        setCurrentMethod(oldMethod);
    }


    /**
     * Gets the GetMethodWithTag attribute of the PropertyTagsHandler object
     *
     * @param attributes            Describe what the parameter does
     * @return                      The GetMethodWithTag value
     * @exception XDocletException  Describe the exception
     */
    private XMethod getGetMethodWithTag(Properties attributes) throws XDocletException
    {
        String requiredTag = attributes.getProperty("tagName");

        if (requiredTag == null) {
            throw new XDocletException("missing required tag parameter in forAllPropertiesHavingTag");
        }

        XMethod currentMethod = getCurrentMethod();

        if (currentMethod.getName().startsWith("get") || currentMethod.getName().startsWith("is")) {
            if (currentMethod.getDoc().hasTag(requiredTag)) {
                return currentMethod;
            }

            return null;
        }

        String attributeName = MethodTagsHandler.getMethodNameWithoutPrefixFor(currentMethod);
        XMethod getter = getXMethodForMethodName("get" + attributeName);

        if (getter != null) {
            if (getter.getDoc().hasTag(requiredTag)) {

                return getter;
            }

            return null;
        }
        getter = getXMethodForMethodName("is" + attributeName);

        // not too safe.. should check it's boolean.
        if (getter != null && getter.getDoc().hasTag(requiredTag)) {
            return getter;
        }
        return null;
    }

    /**
     * Gets the SetMethodWithTag attribute of the PropertyTagsHandler object
     *
     * @param attributes            Describe what the parameter does
     * @return                      The SetMethodWithTag value
     * @exception XDocletException  Describe the exception
     */
    private XMethod getSetMethodWithTag(Properties attributes) throws XDocletException
    {
        String requiredTag = attributes.getProperty("tagName");

        if (requiredTag == null) {
            throw new XDocletException("missing required tag parameter in forAllPropertiesHavingTag");
        }

        XMethod currentMethod = getCurrentMethod();

        if (currentMethod.getName().startsWith("set")) {
            if (currentMethod.getDoc().hasTag(requiredTag)) {
                return currentMethod;
            }

            return null;
        }

        String attributeName = MethodTagsHandler.getMethodNameWithoutPrefixFor(currentMethod);
        XMethod setter = getXMethodForMethodName("set" + attributeName);

        if (setter != null && setter.getDoc().hasTag(requiredTag)) {
            return setter;
        }

        return null;
    }
}
