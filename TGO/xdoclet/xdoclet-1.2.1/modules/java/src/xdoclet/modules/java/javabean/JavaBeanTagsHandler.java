/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.java.javabean;

import java.text.MessageFormat;

import java.util.Properties;
import xjavadoc.XClass;
import xjavadoc.XTag;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;

/**
 * Specific tags handler to make the template easy.
 *
 * @author               Laurent Etiemble (letiemble@users.sourceforge.net)
 * @created              June 20, 2002
 * @version              $Revision: 1.3 $
 * @xdoclet.taghandler   namespace="JavaBean"
 */
public class JavaBeanTagsHandler extends XDocletTagSupport
{

    public static String getBeanInfoClassFor(XClass clazz) throws XDocletException
    {
        XTag beanTag = clazz.getDoc().getTag("javabean.class");
        String className = null;

        if (beanTag != null) {
            // be paranoid...
            className = beanTag.getAttributeValue("class");
        }

        if (className == null) {
            className = clazz.getQualifiedName();
        }

        return MessageFormat.format(BeanInfoSubTask.GENERATED_BEANINFO_CLASS_NAME, new Object[]{className});
    }

    /**
     * Return the getter prefix according to the class tag that contains a class.
     *
     * @param attributes            XDoclet attributes
     * @return                      The getter prefix
     * @exception XDocletException  Thrown in case of problem
     */
    public String getterPrefix(Properties attributes) throws XDocletException
    {
        String name = getTagValue(attributes, FOR_CLASS);

        if ("boolean".equals(name)) {
            return "is";
        }
        if ("java.lang.Boolean".equals(name)) {
            return "is";
        }
        return "get";
    }

    /**
     * return configured bean class name or current class name
     *
     * @param attributes            XDoclet attributes
     * @return                      The getter prefix
     * @exception XDocletException  Thrown in case of problem
     */
    public String beanClass(Properties attributes) throws XDocletException
    {
        if (getTagValue(FOR_CLASS, "javabean.class", "class", null, null, false, false) != null) {
            return getTagValue(FOR_CLASS, "javabean.class", "class", null, null, false, false);
        }
        else {
            return getCurrentClass().getQualifiedName();
        }
    }

    /**
     * Capitalize the first letter of a class tag (i.e countToken => CountToken)
     *
     * @param attributes            XDoclet attributes
     * @return                      The class tag capitalized
     * @exception XDocletException  Thrown in case of problem
     */
    public String capitalizeClassTag(Properties attributes) throws XDocletException
    {
        String name = getTagValue(attributes, FOR_CLASS);

        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
            Character.isUpperCase(name.charAt(0))) {
            return name;
        }

        char chars[] = name.toCharArray();

        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
