/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jmx;

import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;

import xdoclet.util.Translator;

/**
 * @author               Jerome Bernard (jerome.bernard@xtremejava.com)
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              31 January 2002
 * @xdoclet.taghandler   namespace="Jmx"
 * @version              $Revision: 1.11 $
 * @todo                 attributes - XXX: Does this need to be synchronized?
 * @todo                 ifIsGetterMethod, ifIsSetterMethod - TODO: There is a big overlap here with stuff in ejb - have
 *      a look.
 */
public class JMXTagsHandler extends AbstractProgramElementTagsHandler
{
    /**
     * For use in extracting method names.
     */
    protected MethodTagsHandler handler = new MethodTagsHandler();

    /**
     * Collection of attributes. XXX: Does this need to be synchronized?
     */
    protected Map   attributes = Collections.synchronizedMap(new HashMap());

    /**
     * For looping through indexed tags.
     */
    protected int   index = 0;

    /**
     * Returns the MBean name for the current class. Looks for the name parameter in the jmx:mbean tag on the current
     * class.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content" description="Returns the MBean name for the current class. Looks for
     *      the name parameter in the jmx:mbean tag on the current class."
     * @see                         #getMBeanName
     */
    public String mbeanName() throws XDocletException
    {
        return getMBeanName(getCurrentClass());
    }

    /**
     * TODO: There is a big overlap here with stuff in ejb - have a look.
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block" description="Executes the block if the current method is a getter"
     */
    public void ifIsGetterMethod(String template, Properties attributes) throws XDocletException
    {
        if (isGetterMethod()) {
            generate(template);
        }
    }

    /**
     * TODO: There is a big overlap here with stuff in ejb - have a look.
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block" description="Executes the block if the current method is a setter"
     */
    public void ifIsSetterMethod(String template, Properties attributes) throws XDocletException
    {
        if (isSetterMethod()) {
            generate(template);
        }
    }

    /**
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasAttributeDescription(String template, Properties attributes) throws XDocletException
    {
        boolean hasGetterMethod = false;
        String name = handler.methodNameWithoutPrefix();
        String description = getTagValue(
            FOR_METHOD,
            "jmx:managed-attribute",
            "description",
            null,
            null,
            true,
            false
            );

        Collection methods = getCurrentClass().getMethods();

        for (MethodIterator i = XCollections.methodIterator(methods); i.hasNext(); ) {
            XMethod method = i.next();

            if (method.getName().equals("get" + name) || method.getName().equals("is" + name)) {
                hasGetterMethod = true;
            }
        }

        if ((isSetterMethod() && !hasGetterMethod) || isGetterMethod()) {
            attributes.put(name, description);
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllIndexedMethodParams(String template, Properties attributes) throws XDocletException
    {
        Collection tags = getCurrentMethod().getDoc().getTags("jmx:managed-operation-parameter");

        index = 0;
        for (int i = 0; i < tags.size(); i++) {
            generate(template);
            index++;
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllIndexedConstructorParams(String template, Properties attributes) throws XDocletException
    {
        Collection tags = getCurrentConstructor().getDoc().getTags("jmx:managed-constructor-parameter");

        index = 0;
        for (int i = 0; i < tags.size(); i++) {
            generate(template);
            index++;
        }
    }

    /**
     * @param attributes            Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @todo                        refactor common code with indexedConstructorParamValue into a private method
     */
    public String indexedMethodParamValue(Properties attributes) throws XDocletException
    {
        String tagName = attributes.getProperty("tagName");
        String paramName = attributes.getProperty("paramName");

        if (tagName == null || paramName == null) {
            throw new XDocletException(Translator.getString(XDocletModulesJmxMessages.class, XDocletModulesJmxMessages.MISSING_ATTRIBUTE));
        }

        List tags = Arrays.asList(getCurrentMethod().getDoc().getTags(tagName).toArray());
        XTag tag = (XTag) tags.get(index);
        String tagContent = tag.getValue();
        int begin = tagContent.indexOf(paramName + "=\"") + paramName.length() + 2;
        int end = tagContent.indexOf("\"", begin);

        return tagContent.substring(begin, end);
    }

    /**
     * Describe what the method does
     *
     * @param attributes            Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String indexedConstructorParamValue(Properties attributes) throws XDocletException
    {
        if (attributes == null)
            throw new XDocletException(Translator.getString(XDocletModulesJmxMessages.class, XDocletModulesJmxMessages.MISSING_ATTRIBUTE));
        String tagName = attributes.getProperty("tagName");
        String paramName = attributes.getProperty("paramName");

        if (tagName == null || paramName == null) {
            throw new XDocletException(Translator.getString(XDocletModulesJmxMessages.class, XDocletModulesJmxMessages.MISSING_ATTRIBUTE));
        }

        List tags = Arrays.asList(getCurrentConstructor().getDoc().getTags(tagName).toArray());
        XTag tag = (XTag) tags.get(index);

        // This is weird. XTag has methods for this! (Aslak)
        String tagContent = tag.getValue();
        int begin = tagContent.indexOf(paramName + "=\"") + paramName.length() + 2;
        int end = tagContent.indexOf("\"", begin);

        return tagContent.substring(begin, end);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String constructorSignature() throws XDocletException
    {
        XConstructor currentConstructor = getCurrentConstructor();
        String signature = currentConstructor.getSignature(false);

        // Remove spaces from the signature
        while (signature.indexOf(" ") != -1) {
            int index = signature.indexOf(" ");
            String before = signature.substring(0, index);
            String after = signature.substring(index + 1, signature.length());

            signature = before + after;
        }
        // Build the complete signature
        return "public " + currentConstructor.getName() + signature;
    }

    /**
     * Implementation of {@link #mbeanName}.
     *
     * @param clazz
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    protected String getMBeanName(XClass clazz) throws XDocletException
    {
        XTag bean_tag = clazz.getDoc().getTag("jmx:mbean");

        if (bean_tag == null) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.CLASS_TAG_EXPECTED,
                new String[]{"@jmx:mbean", clazz.getQualifiedName()}));
        }

        String param_val = bean_tag.getAttributeValue("name");

        if (param_val == null) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.CLASS_TAG_PARAMETER_EXPECTED,
                new String[]{"name", "@jmx:mbean", clazz.getQualifiedName()}));
        }

        return param_val;
    }

    /**
     * @return   The GetterMethod value
     * @todo     (Aslak) this is very general stuff. It should be implemented higher up in the hierarchy if it isn't
     *      already done somewhere
     */
    protected boolean isGetterMethod()
    {
        String methodName = getCurrentMethod().getName();
        XClass retType = getCurrentMethod().getReturnType().getType();
        Collection params = getCurrentMethod().getParameters();

        if (!retType.getQualifiedName().equals("void") && params.size() == 0) {
            if (methodName.startsWith("get") || (methodName.startsWith("is") && retType.getQualifiedName().equals("boolean"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return   The SetterMethod value
     * @todo     (Aslak) this is very general stuff. It should be implemented higher up in the hierarchy if it isn't
     *      already done somewhere
     */
    protected boolean isSetterMethod()
    {
        String methodName = getCurrentMethod().getName();
        XClass retType = getCurrentMethod().getReturnType().getType();
        Collection params = getCurrentMethod().getParameters();

        return (retType.getQualifiedName().equals("void") && params.size() == 1 && methodName.startsWith("set"));
    }

}
