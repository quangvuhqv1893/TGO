/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;
import xdoclet.XDocletException;

import xdoclet.modules.ejb.entity.CmpTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 16, 2001
 * @xdoclet.taghandler   namespace="EjbPersistent"
 * @version              $Revision: 1.19 $
 */
public class PersistentTagsHandler extends CmpTagsHandler
{

    /**
     * Returns true if method is a persistent field, false otherwise. Persistent fields are getter methods marked with a
     * ejb:persistent-field tag.
     *
     * @param method  Description of Parameter
     * @return        The PersistentField value
     */
    public static boolean isPersistentField(XMethod method)
    {
        return method.getDoc().hasTag("ejb:persistent-field") || method.getDoc().hasTag("ejb:persistence");
    }


    /**
     * Gets the ValueObjectField attribute of the PersistentTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @param method                Describe what the parameter does
     * @param valueObject           Describe what the parameter does
     * @return                      The ValueObjectField value
     * @exception XDocletException
     */
    public static boolean isValueObjectField(XClass clazz, XMethod method, String valueObject) throws XDocletException
    {
        Log log = LogUtil.getLog(PersistentTagsHandler.class, "valueobject");
        boolean ret = method.getDoc().hasTag("ejb:value-object");

        log.debug("hasTag " + ret);
        if (ret) {
            Collection tags = method.getDoc().getTags("ejb:value-object");

            if (tags.size() == 0 && !"*".equals(valueObject)) {
                ret = false;
            }
            else {
                ret = false;

                boolean excluded = true;

                for (Iterator i = tags.iterator(); i.hasNext(); ) {
                    XTag tag = (XTag) i.next();
                    String exclude = tag.getAttributeValue("exclude");
                    String aggreg = tag.getAttributeValue("aggregate");
                    String comp = tag.getAttributeValue("compose");

                    if ("true".equals(exclude) || aggreg != null || comp != null) {
                        excluded = true;
                        ret = false;
                        break;
                    }

                    String value = tag.getAttributeValue("match");

                    log.debug(method.getName() + " " + value + "==" + valueObject);
                    if (valueObject.equals(value) || "*".equals(value) || "*".equals(valueObject)) {
                        ret = true;
                        break;
                    }
                }
                if ("*".equals(valueObject) && !excluded) {
                    ret = true;
                }
            }
        }
        else if ("*".equals(valueObject)) {
            log.debug("All Fields VO " + method.getName() + " " + isPersistentField(method));
            if (isPersistentField(method)) {
                ret = true;
            }
            else {
                ret = false;
            }
        }
        return ret;
    }


    /**
     * Returns true if method is a primary key field, false otherwise. PK fields are getter methods marked with a
     * ejb:pk-field tag.
     *
     * @param method  Description of Parameter
     * @return        The PkField value
     */
    public static boolean isPkField(XMethod method)
    {
        return method.getDoc().hasTag("ejb:pk-field");
    }

    /**
     * Returns true if clazz has ejb:pk-field defined.
     *
     * @param clazz  Description of Parameter
     * @return       The PkFieldInHeader value
     */
    public static boolean isPkFieldInHeader(XClass clazz)
    {
        return clazz.getDoc().hasTag("ejb:pk-field");
    }


    /**
     * Returns comma-separated list of fields, excluding fields that have tags of exclTag list, including fields that
     * have tags of inclTag list. If name_value_out is true, then the list is in fieldname="value" format.
     *
     * @param inclTag               Fields that have at least of the tags of this comma-separated list are used.
     * @param exclTag               Fields that don't have all of the tags of this comma-separated list are used.
     * @param type                  Type of return (0: comma separated list - 1: field=value comma separated list - 2:
     *      getter comma sperated list)
     * @param clazz                 Description of Parameter
     * @param valueObject
     * @param superclasses
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @todo                        This method only takes the field of the current class. I think it's ok. Any
     *      objection ?
     */
    public static String fieldList(XClass clazz, String inclTag, String exclTag, int type, String valueObject, boolean superclasses) throws XDocletException
    {
        Log log = LogUtil.getLog(PersistentTagsHandler.class, "fieldList");

        log.debug("fieldList(" + clazz.getName() + ",incl tag=" + inclTag + ",excl tag=" + exclTag + ",type=" + type);

        Map foundFields = new HashMap();
        StringBuffer st = new StringBuffer();
        String methodType = null;
        String name = null;

        do {
            log.debug(" --> " + clazz);

            Collection methods = clazz.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if ((isPersistentField(method) && MethodTagsHandler.isGetter(method.getName()) && !foundFields.containsKey(method.getName()) && (valueObject == null || isValueObjectField(clazz, method, valueObject)))
                    || (MethodTagsHandler.isGetter(method.getName()) && !foundFields.containsKey(method.getName()) && valueObject != null && isValueObjectField(clazz, method, valueObject))) {
                    if ((inclTag == null && exclTag == null)
                        || ((inclTag != null) && (method.getDoc().hasTag(inclTag)))
                        || ((exclTag != null) && (!method.getDoc().hasTag(exclTag)))) {
                        // Store that we found this field so we don't add it twice
                        foundFields.put(method.getName(), method);

                        name = MethodTagsHandler.getPropertyNameFor(method);

                        methodType = MethodTagsHandler.getMethodTypeFor(method);

                        switch (type) {
                        case 0:
                            if (foundFields.size() > 1) {
                                st.append(',');
                            }

                            st.append(methodType).append(' ').append(name);
                            break;
                        case 1:
                            //String value = MethodTagsHandler.getPropertyNameFor(methods[j]);

                            if (foundFields.size() > 1) {
                                st.append(" + " + "\" \"" + " + ");
                            }

                            //st.append("\"").append(name).append("=\" + ").append(value);
                            st.append("\"").append(name).append("=\" + ").append(method.getName()).append("()");
                            break;
                        case 2:
                            if (foundFields.size() > 1) {
                                st.append(',');
                            }
                            st.append(method.getName()).append("()");
                            break;
                        }
                    }
                }
            }

            //Add super class info
            clazz = clazz.getSuperclass();
        } while (clazz != null && superclasses);

        return st.toString();
    }

    /**
     * Evaluates the body if the class has at least one primary key field.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasAtLeastOnePkField(String template) throws XDocletException
    {
        XClass clazz = getCurrentClass();

        do {
            Collection methods = clazz.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (isPkField(method) && MethodTagsHandler.isGetter(method.getName())) {
                    generate(template);
                    return;
                }
            }

            // Add super class info
            clazz = clazz.getSuperclass();
        } while (clazz != null);
    }


    /**
     * Evaluates the body if the class has at least one persistent field.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasAtLeastOnePersistentField(String template) throws XDocletException
    {
        XClass clazz = getCurrentClass();

        do {
            Collection methods = clazz.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (isPersistentField(method) && MethodTagsHandler.isGetter(method.getName())) {
                    generate(template);
                    return;
                }
            }

            // Add super class info
            clazz = clazz.getSuperclass();
        } while (clazz != null);
    }


    /**
     * Evaluates the body for each persistent field. If only-pk="true" then use only primary keys, if not-pk="true" then
     * use only persistent fields that are not primary keys. By default use all regardless of being primary key field or
     * not.
     *
     * @param template              The body of the block tag
     * @param attributes            Description of Parameter
     * @exception XDocletException
     * @see                         #forAllPersistentMatchedFields(java.lang.String,java.lang.String,java.lang.String,boolean,java.lang.String)
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" values="true,false" description="Include
     *      persistent fields of superclasses. True by default."
     */
    public void forAllPersistentFields(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        String valueObject = attributes.getProperty("valueobject");

        if (attributes.getProperty("only-pk") != null) {
            forAllPersistentMatchedFields(template, "ejb:pk-field", null, superclasses, valueObject);
        }
        else if (attributes.getProperty("not-pk") != null) {
            forAllPersistentMatchedFields(template, null, "ejb:pk-field", superclasses, valueObject);
        }
        else {
            forAllPersistentMatchedFields(template, null, null, superclasses, valueObject);
        }
    }

    /**
     * Returns a string containing comma-separated list of persistent fields without their types in fieldname="value"
     * format.
     *
     * @param attributes
     * @return                      A string containing comma-separated list of persistent fields with their types like
     *      an ordinary method parameter definition.
     * @exception XDocletException
     * @see                         #fieldList(XClass, String, String, int, String, boolean)
     * @doc.tag                     type="content"
     */
    public String persistentfieldNameValueList(Properties attributes) throws XDocletException
    {
        String valueObject = attributes.getProperty("valueobject");
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        return fieldList(getCurrentClass(), null, null, 1, valueObject, superclasses);
    }


    /**
     * Returns a string containing comma-separated list of persistent fields with their types.
     *
     * @param attributes
     * @return                      A string containing comma-separated list of persistent fields without their types.
     * @exception XDocletException
     * @see                         #fieldList(XClass, String, String, int, String, boolean)
     * @doc.tag                     type="content"
     */
    public String persistentfieldList(Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        String valueObject = attributes.getProperty("valueobject");

        return fieldList(getCurrentClass(), null, null, 0, valueObject, superclasses);
    }


    /**
     * Returns a string containing comma-separated list of persistent fields without their types in fieldname="value"
     * format.
     *
     * @return                      A string containing comma-separated list of persistent fields with their types like
     *      an ordinary method parameter definition.
     * @exception XDocletException
     * @see                         #fieldList(XClass, String, String, int, String, boolean)
     * @doc.tag                     type="content"
     */
    public String persistentfieldNameValueList() throws XDocletException
    {
        return fieldList(getCurrentClass(), null, null, 1, null, true);
    }


    /**
     * Returns an array containing ejb:pk-field tags defined in class level.
     *
     * @return                      The PkFieldsInHeader value
     * @exception XDocletException
     */
    protected String[] getPkFieldsInHeader() throws XDocletException
    {
        ArrayList lPKs = new ArrayList();
        Collection lTags = getCurrentClass().getDoc().getTags("ejb:pk-field");

        for (Iterator i = lTags.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();

            lPKs.add(tag.getValue());
        }

        return (String[]) lPKs.toArray(new String[0]);
    }


    /**
     * Generate only for all Persisted Fields matching a specific XTag or Persisted fields that do not match a specific
     * Tag
     *
     * @param template              The body of the block tag
     * @param include_tags          only fields having these tags
     * @param exclude_tags          only fields not having these tags
     * @param superclasses          traverse superclasses too
     * @param valueObject
     * @exception XDocletException
     * @see                         #forAllPersistentFields(java.lang.String,java.util.Properties)
     */
    protected void forAllPersistentMatchedFields(String template, String include_tags, String exclude_tags, boolean superclasses, String valueObject) throws XDocletException
    {
        Log log = LogUtil.getLog(PersistentTagsHandler.class, "forAllPersistentFields");
        Map foundFields = new HashMap();
        XClass cur_class = getCurrentClass();
        XMethod cur_method = getCurrentMethod();

        if (log.isDebugEnabled()) {
            log.debug("Called.");
        }

        do {
            pushCurrentClass(cur_class);

            if (log.isDebugEnabled()) {
                log.debug("getCurrentClass()=" + getCurrentClass());
            }

            Collection methods = getCurrentClass().getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                setCurrentMethod(method);

                if ((isPersistentField(getCurrentMethod()) && MethodTagsHandler.isGetter(getCurrentMethod().getName()) && !foundFields.containsKey(getCurrentMethod().getName()) && (valueObject == null || isValueObjectField(getCurrentClass(), getCurrentMethod(), valueObject)))
                    || (MethodTagsHandler.isGetter(getCurrentMethod().getName()) && !foundFields.containsKey(getCurrentMethod().getName()) && valueObject != null && isValueObjectField(getCurrentClass(), getCurrentMethod(), valueObject))) {
                    if ((include_tags == null && exclude_tags == null)
                        || ((include_tags != null) && (getCurrentMethod().getDoc().hasTag(include_tags)))
                        || ((exclude_tags != null) && (!getCurrentMethod().getDoc().hasTag(exclude_tags)))) {
                        if (log.isDebugEnabled()) {
                            log.debug("METHOD(I=" + include_tags + " - E=" + exclude_tags + "=" + getCurrentMethod().getName());
                        }

                        // Store that we found this field so we don't add it twice
                        foundFields.put(getCurrentMethod().getName(), getCurrentMethod().getName());

                        generate(template);
                    }
                }
            }

            // Add super class info
            XClass cur = getCurrentClass();
            XClass sup = cur.getSuperclass();
            String qname = sup.getQualifiedName();
            boolean top = qname.equals("java.lang.Object");

            if (top) {
                popCurrentClass();
                break;
            }

            popCurrentClass();

            //superclasses is true for *CMP/BMP/Session
            if (superclasses == true) {
                cur_class = cur_class.getSuperclass();
            }
            else {
                if (shouldTraverseSuperclassForDependentClass(cur_class.getSuperclass(), getDependentClassTagName())) {
                    cur_class = cur_class.getSuperclass();
                }
                else {
                    break;
                }
            }
        } while (true);

        setCurrentMethod(cur_method);

        if (log.isDebugEnabled()) {
            log.debug("Finished.");
        }
    }

}
