/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.util.LogUtil;

/**
 * Simple tag support class.
 *
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              October 12, 2001
 * @xdoclet.taghandler   namespace="Type"
 * @version              $Revision: 1.13 $
 */
public class TypeTagsHandler extends XDocletTagSupport
{

    /**
     * Used by isOfType. isOfType searches for the type according to the type parameter. TYPE_CONCRETE_TYPE specifies
     * that only the type of the current entity (class, method return type, parameter type depdening on the context)
     * should be checked for equality.
     */
    public final static int TYPE_CONCRETE_TYPE = 0;

    /**
     * Used by isOfType. isOfType searches for the type according to the type parameter. TYPE_SUPERCLASS specifies that
     * not only the type of the current entity (class, method return type, parameter type depdening on the context)
     * should be checked for equality, but also direct superclasses and interfaces of the entity.
     */
    public final static int TYPE_SUPERCLASS = 1;

    /**
     * Used by isOfType. isOfType searches for the type according to the type parameter. TYPE_HIERARCHY specifies that
     * not only the type of the current entity (class, method return type, parameter type depdening on the context)
     * should be checked for equality, but also superclasses and interfaces of the entity and recursively superclasses
     * and interfaces.
     */
    public final static int TYPE_HIERARCHY = 2;

    /**
     * Returns true if name is a primitive type, in that case name contains the string "int"/"float"/etc.
     *
     * @param name  Description of Parameter
     * @return      The PrimitiveType value
     */
    public static boolean isPrimitiveType(String name)
    {
        //strip [] away, if an array type
        int index = name.indexOf('[');
        int end_index = name.lastIndexOf(']');

        if (index != -1 && end_index != -1) {
            String before_braket = name.substring(0, index);
            String after_braket = end_index + 1 < name.length() ? name.substring(end_index + 1) : "";

            name = before_braket.trim() + after_braket.trim();
        }

        return name.equals("int") ||
            name.equals("long") ||
            name.equals("float") ||
            name.equals("double") ||
            name.equals("boolean") ||
            name.equals("byte") ||
            name.equals("short") ||
            name.equals("char");
    }

    /**
     * Returns true if name is a primitive type and is an array (ends with [])
     *
     * @param name  The name of the type.
     * @return      The PrimitiveType value
     * @see         #isPrimitiveType
     */
    public static boolean isPrimitiveArray(String name)
    {
        return (name.indexOf('[') == name.length() - 2) &&
            (name.indexOf(']') == name.length() - 1) &&
            (isPrimitiveType(name));
    }

    /**
     * Returns true if cur_class is of type type. It searches for type in cur_class's hierarchy according to the value
     * of extent parameter.
     *
     * @param type    Description of Parameter
     * @param clazz
     * @param extent
     * @return        The OfType value
     * @todo          move this to xjavadoc
     */
    public static boolean isOfType(XClass clazz, String type, int extent)
    {
        boolean result = false;
        StringTokenizer st = new StringTokenizer(type, ",");

        while (st.hasMoreTokens()) {
            String type_str = st.nextToken();

            switch (extent) {
            case TYPE_CONCRETE_TYPE:
                if (clazz.getQualifiedName().equals(type_str)) {
                    result = true;
                }
                break;
            case TYPE_SUPERCLASS:

                if (clazz.isInterface() == false) {
                    if (clazz.getSuperclass().isSubclassOf(type_str, false)) {
                        result = true;
                    }
                }
                else {
                    if (clazz.getSuperclass().isImplementingInterface(type_str, false)) {
                        result = true;
                    }
                }
                break;
            case TYPE_HIERARCHY:
                if (clazz.isA(type_str, true)) {
                    result = true;
                }
                break;
            default:
                throw new IllegalArgumentException("Bad extent");
            }
        }

        return result;
    }

    /**
     * Return the integer constact based on the extent_str. Used by forAllClasses and ifIsOfType_Impl. If the string
     * doesn't have one of the expected values TYPE_HIERARCHY is returned.
     *
     * @param extent_str  Description of Parameter
     * @return            Description of the Returned Value
     * @see               ClassTagsHandler#forAllClasses(java.lang.String,java.util.Properties)
     * @see               #ifIsOfType_Impl(java.lang.String,java.util.Properties,boolean)
     * @see               #TYPE_HIERARCHY
     * @see               #TYPE_CONCRETE_TYPE
     * @see               #TYPE_SUPERCLASS
     */
    public static int extractExtentType(String extent_str)
    {
        if (extent_str == null)
            return TYPE_HIERARCHY;
        else if (extent_str.equalsIgnoreCase("concrete-type"))
            return TYPE_CONCRETE_TYPE;
        else if (extent_str.equalsIgnoreCase("superclass"))
            return TYPE_SUPERCLASS;
        else if (extent_str.equalsIgnoreCase("hierarchy"))
            return TYPE_HIERARCHY;
        else
            return TYPE_HIERARCHY;
    }

    /**
     * Gets the PrimitiveOrString attribute of the TypeTagsHandler class
     *
     * @param value  Describe what the parameter does
     * @return       The PrimitiveOrString value
     */
    private static boolean isPrimitiveOrString(String value)
    {
        return isPrimitiveType(value) || value.equals("java.lang.String") || value.equals("String");
    }

    /**
     * Evaluate the body block if the value is of a primitive type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotPrimitive(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveType(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsPrimitive(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(TypeTagsHandler.class, "ifIsPrimitive");

        String value = attributes.getProperty("value");

        if (log.isDebugEnabled()) {
            log.debug("value=" + value);
        }

        if (isPrimitiveType(value)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if the value is of a primitive array type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotPrimitiveArray(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveArray(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsPrimitiveArray(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(TypeTagsHandler.class, "ifIsPrimitiveArray");

        String value = attributes.getProperty("value");

        if (log.isDebugEnabled()) {
            log.debug("value=" + value);
        }

        if (isPrimitiveArray(value)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if the value is not of a primitive array type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsPrimitiveArray(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveArray(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsNotPrimitiveArray(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(TypeTagsHandler.class, "ifIsNotPrimitiveArray");

        String value = attributes.getProperty("value");

        if (log.isDebugEnabled()) {
            log.debug("value=" + value);
        }

        if (!isPrimitiveArray(value)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if the value is of a primitive type or String.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotPrimitive(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveType(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsPrimitiveOrString(String template, Properties attributes) throws XDocletException
    {
        ifIsPrimitiveOrString_Impl(attributes, template, true);
    }

    /**
     * Evaluate the body block if the value is of a primitive type or String.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotPrimitive(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveType(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsNotPrimitiveOrString(String template, Properties attributes) throws XDocletException
    {
        ifIsPrimitiveOrString_Impl(attributes, template, false);
    }

    /**
     * Evaluate the body block if the value is not of a primitive type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsPrimitive(java.lang.String,java.util.Properties)
     * @see                         #isPrimitiveType(java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" description="A string containsing the type name."
     */
    public void ifIsNotPrimitive(String template, Properties attributes) throws XDocletException
    {
        String value = attributes.getProperty("value");

        if (!isPrimitiveType(value)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if the entity is not of the specified type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsOfType(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="false" values="class,return-type" description="If class then
     *      check current class's type, if return-type then check current method return type."
     * @doc.param                   name="type" optional="false" description="The type we are checking against."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void ifIsNotOfType(String template, Properties attributes) throws XDocletException
    {
        ifIsOfType_Impl(template, attributes, false);
    }

    /**
     * Evaluate the body block if the entity is of the specified type.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsNotOfType(java.lang.String,java.util.Properties)
     * @doc.tag                     type="block"
     * @doc.param                   name="value" optional="true" values="class,return-type" description="If class then
     *      check current class's type, if return-type then check current method return type. Default is class."
     * @doc.param                   name="type" optional="false" description="The type we are checking against."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void ifIsOfType(String template, Properties attributes) throws XDocletException
    {
        ifIsOfType_Impl(template, attributes, true);
    }

    /**
     * Returns the type specified with the <code>type</code> parameter without dimensions.
     *
     * @param attributes
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     * @doc.param                   name="type" optional="false" description="Specifies the type to return without
     *      dimensions. So, the value <code>String[][]</code> will be returned as <code>String</code>."
     */
    public String typeWithoutDimensions(Properties attributes) throws XDocletException
    {
        String typeName = attributes.getProperty("type", "").trim();
        int index = typeName.indexOf('[');

        if (index < 0) {
            return typeName;
        }
        else {
            return typeName.substring(0, index);
        }
    }

    /**
     * Implementation of ifIsOfType and ifIsNotOfType tags.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @param condition             Description of Parameter
     * @exception XDocletException  Description of Exception
     * @see                         #ifIsOfType(java.lang.String,java.util.Properties)
     * @see                         #ifIsNotOfType(java.lang.String,java.util.Properties)
     */
    protected void ifIsOfType_Impl(String template, Properties attributes, boolean condition) throws XDocletException
    {
        String value = attributes.getProperty("value");
        String typeName = attributes.getProperty("type");
        int extent = extractExtentType(attributes.getProperty("extent"));
        String typeDim = "";
        XClass currentType = null;
        String curDimStr = "";

        //strip away [] from typeName and put it in typeDim
        int array_dim_in_type_name_index = typeName.indexOf('[');

        if (array_dim_in_type_name_index != -1) {
            typeDim = typeName.substring(array_dim_in_type_name_index);
            typeName = typeName.substring(0, array_dim_in_type_name_index);
        }

        if (value == null) {
            currentType = getCurrentClass();
        }
        else {
            if (value.equalsIgnoreCase("class")) {
                currentType = getCurrentClass();
            }
            else if (value.equalsIgnoreCase("return-type")) {
                currentType = getCurrentMethod().getReturnType().getType();
                curDimStr = getCurrentMethod().getReturnType().getDimensionAsString();
            }
            else {
                //full class name literally specified
                currentType = getXJavaDoc().getXClass(value);
            }
        }

        // if of that type and dimension
        if (isOfType(currentType, typeName, extent) && typeDim.equals(curDimStr)) {
            if (condition == true)
                generate(template);
        }
        else {
            if (condition == false)
                generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param attributes            Describe what the parameter does
     * @param template              Describe what the parameter does
     * @param condition             Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    private void ifIsPrimitiveOrString_Impl(Properties attributes, String template, boolean condition) throws XDocletException
    {
        Log log = LogUtil.getLog(TypeTagsHandler.class, "ifIsPrimitiveOrString_Impl");

        String value = attributes.getProperty("value");

        if (log.isDebugEnabled()) {
            log.debug("value=" + value);
        }

        if (isPrimitiveOrString(value) == condition) {
            generate(template);
        }
    }
}
