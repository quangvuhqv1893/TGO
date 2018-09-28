/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

//import xdoclet.util.serialveruid.*;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import xjavadoc.XClass;
import xjavadoc.XMethod;
import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.home.HomeTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.tagshandler.PropertyTagsHandler;
import xdoclet.tagshandler.TypeTagsHandler;

import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              13. juni 2002
 * @xdoclet.taghandler   namespace="EjbPk"
 * @version              $Revision: 1.15 $
 */
public class PkTagsHandler extends EjbTagsHandler
{
    /**
     * @param clazz  The class to look into
     * @return       The value of the ejb:bean primkey-field parameter
     */
    public static String getPrimkeyFieldFor(XClass clazz)
    {
        // the primkey-field may be specified in a super class, so check them as well
        //
        String pkField = clazz.getDoc().getTagAttributeValue("ejb:bean", "primkey-field", true);

        return pkField;
    }

    /**
     * @param clazz                 The class to look into
     * @param method                The method to check for primkey-field
     * @return                      true if the method is a getter or setter for the primkey-field
     * @exception XDocletException
     */
    public static boolean isMethodPrimkeyField(XClass clazz, XMethod method)
         throws XDocletException
    {
        String pkField = getPrimkeyFieldFor(clazz);
        String propertyName = MethodTagsHandler.getPropertyNameFor(method);

        return propertyName.equals(pkField);
    }

    /**
     * Gets the PrimkeyGetterFor attribute of the PkTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The PrimkeyGetterFor value
     * @exception XDocletException
     */
    public static String getPrimkeyGetterFor(XClass clazz) throws XDocletException
    {
        if (!classHasPrimkeyField(clazz)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("get");
        buffer.append(Character.toUpperCase(getPrimkeyFieldFor(clazz).charAt(0)));
        buffer.append(getPrimkeyFieldFor(clazz).substring(1));

        String getterName = buffer.toString();

        if (MethodTagsHandler.hasMethod(clazz, getterName, null, false)) {
            return getterName;
        }
        else {
            return null;
        }
    }

    /**
     * Gets the PrimkeySetterFor attribute of the PkTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The PrimkeySetterFor value
     * @exception XDocletException
     */
    public static String getPrimkeySetterFor(XClass clazz) throws XDocletException
    {
        if (!classHasPrimkeyField(clazz)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("set");
        buffer.append(Character.toUpperCase(getPrimkeyFieldFor(clazz).charAt(0)));
        buffer.append(getPrimkeyFieldFor(clazz).substring(1));

        String setterName = buffer.toString();

        if (MethodTagsHandler.hasMethod(clazz, setterName, null, false)) {
            return setterName;
        }
        else {
            return null;
        }
    }

    /**
     * Gets the PkClassFor attribute of the PkTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The PkClassFor value
     * @exception XDocletException
     */
    public static String getPkClassFor(XClass clazz) throws XDocletException
    {
        if (classHasPrimkeyField(clazz)) {
            String fieldName = getPrimkeyFieldFor(clazz);
            String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            // the getter might be in a superclass, so check those as well
            //
            XMethod method = PropertyTagsHandler.getXMethodForMethodName(getter, true);

            if (method == null) {
                throw new XDocletException("Could not find method " + getter + " that is supposed to return the PrimKeyField.");
            }

            return method.getReturnType().getType().getQualifiedName();
        }

        String fileName = clazz.getContainingPackage().getName();

        String pkClass = clazz.getDoc().getTagAttributeValue("ejb:pk", "class", false);

        if (pkClass != null) {
            return pkClass;
        }

        String namePattern = clazz.getDoc().getTagAttributeValue("ejb:pk", "pattern", false);

        if (namePattern == null) {
            namePattern = getEntityPkClassPattern();
        }

        String package_pattern = clazz.getDoc().getTagAttributeValue("ejb:pk", "package", false);

        String pkclass_name = null;

        if (namePattern.indexOf("{0}") != -1) {
            pkclass_name = MessageFormat.format(namePattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            pkclass_name = namePattern;
        }

        // Fix package name
        fileName = choosePackage(fileName, package_pattern, DocletTask.getSubTaskName(EntityPkSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += pkclass_name;

        return fileName;
    }


    /**
     * Gets the PkClassForEjbJarXmlFor attribute of the PkTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The PkClassForEjbJarXmlFor value
     * @exception XDocletException
     */
    public static String getPkClassForEjbJarXmlFor(XClass clazz) throws XDocletException
    {
        XMethod createMethod = HomeTagsHandler.findFirstCreateMethodFor(clazz);
        String createMethodReturnType = createMethod != null ? createMethod.getReturnType().getType().getQualifiedName() : null;

        String generate_str = clazz.getDoc().getTagAttributeValue("ejb:pk", "generate", false);
        boolean generate = TypeConversionUtil.stringToBoolean(generate_str, true);

        if (isPkClassGenerateable(createMethodReturnType) == false) {
            return createMethodReturnType;
        }

        if (generate == true) {
            return getPkClassFor(clazz);
        }
        else {
            return createMethodReturnType;
        }
    }

    /**
     * Returns true if the clazz has a primkey-field defined on the ejb:bean tag (opposed to having a separate PK class)
     *
     * @param clazz                 The class to look into.
     * @return                      true if the class has a defined primkey-field
     * @exception XDocletException
     */
    public static boolean classHasPrimkeyField(XClass clazz) throws XDocletException
    {
        String pkField = getPrimkeyFieldFor(clazz);

        return (pkField != null);
    }

    /**
     * Gets the EntityPkClassPattern attribute of the PkTagsHandler class
     *
     * @return   The EntityPkClassPattern value
     */
    protected static String getEntityPkClassPattern()
    {
        EntityPkSubTask entitypk_subtask = ((EntityPkSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityPkSubTask.class)));

        if (entitypk_subtask != null) {
            return entitypk_subtask.getEntityPkClassPattern();
        }
        else {
            return EntityPkSubTask.DEFAULT_ENTITY_PK_CLASS_PATTERN;
        }
    }

    /**
     * Gets the PkClassGenerateable attribute of the PkTagsHandler class
     *
     * @param create_method_return_type  Describe what the parameter does
     * @return                           The PkClassGenerateable value
     */
    private static boolean isPkClassGenerateable(String create_method_return_type)
    {
        if (create_method_return_type == null) {
            return true;
        }

        if (create_method_return_type.equals("java.lang.Object") || TypeTagsHandler.isPrimitiveType(create_method_return_type)) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Process the tag body if the current class has a defined primkey-field
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasPrimkeyField(String template, Properties attributes)
         throws XDocletException
    {
        if (classHasPrimkeyField(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Process the tag body if the current method is a getter or setter for the primkey-field
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsPrimkeyField(String template, Properties attributes)
         throws XDocletException
    {
        if (isMethodPrimkeyField(getCurrentClass(), getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Process the tag body if the current method is not a getter or setter for the primkey-field
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsNotPrimkeyField(String template, Properties attributes)
         throws XDocletException
    {
        if (!isMethodPrimkeyField(getCurrentClass(), getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Process the tag body if the current class doesn't have a defined primkey-field
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifDoesntHavePrimkeyField(String template, Properties attributes)
         throws XDocletException
    {
        if (!classHasPrimkeyField(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Returns the primkey-field defined for the current class
     *
     * @param attributes
     * @return                      The value of the ejb:bean primkey-field parameter
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String primkeyField(Properties attributes)
         throws XDocletException
    {
        return getPrimkeyFieldFor(getCurrentClass());
    }

    /**
     * Returns the getter name for the primkey-field
     *
     * @param attributes
     * @return                      The primkey-field getter
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String primkeyGetter(Properties attributes)
         throws XDocletException
    {
        return getPrimkeyGetterFor(getCurrentClass());
    }

    /**
     * Returns the setter name for the primkey-field
     *
     * @param attributes
     * @return                      The primkey-field setter
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String primkeySetter(Properties attributes)
         throws XDocletException
    {
        return getPrimkeySetterFor(getCurrentClass());
    }

    /**
     * Process the tag body if the current class has defined a setter for the primkey-field.
     *
     * @param template
     * @param attributes
     * @exception XDocletException
     */
    public void ifHasPrimkeySetter(String template, Properties attributes)
         throws XDocletException
    {
        if (getPrimkeySetterFor(getCurrentClass()) != null) {
            generate(template);
        }
    }

    /**
     * Returns the name of generated PK class for the current class.
     *
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String pkClass() throws XDocletException
    {
        return getPkClassFor(getCurrentClass());
    }

    /**
     * Returns the name of PK class for the current class.
     *
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String pkClassForEjbJarXml() throws XDocletException
    {
        return getPkClassForEjbJarXmlFor(getCurrentClass());
    }

    /**
     * Returns a string containing comma-separated list of primary key fields with their types.
     *
     * @return                      A string containing comma-separated list of primary key fields without their types.
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.PersistentTagsHandler#fieldList(XClass, String, String,
     *      int, String, boolean)
     * @doc.tag                     type="content"
     */
    public String pkfieldList() throws XDocletException
    {
        return PersistentTagsHandler.fieldList(getCurrentClass(), "ejb:pk-field", null, 0, null, true);
    }

    /**
     * Returns a string containing comma-separated list of primary key fields getting from an object specified as
     * parameter.
     *
     * @param attributes
     * @return                      A string containing comma-separated list of primary key fields without their types.
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.PersistentTagsHandler#fieldList(XClass, String, String,
     *      int, String, boolean)
     * @doc.tag                     type="content"
     */
    public String pkfieldListFrom(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String commaSep = PersistentTagsHandler.fieldList(getCurrentClass(), "ejb:pk-field", null, 2, null, true);

        StringTokenizer st = new StringTokenizer(commaSep, ",");
        String ret = "";

        while (st.hasMoreTokens()) {
            String attr = st.nextToken();

            ret += name + "." + attr;
            if (st.hasMoreTokens()) {
                ret += ",";
            }
        }
        return ret;
    }

    /**
     * Returns the name of the class pk class extends.
     *
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String extendsFrom() throws XDocletException
    {
        return extendsFromFor(getCurrentClass(), "ejb:pk", null, "extends", "java.lang.Object");
    }

    /**
     * Evaluates the body if the current method is a primary key field.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsPkField(String template) throws XDocletException
    {
        if (PersistentTagsHandler.isPkField(getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Gets the DependentClassFor attribute of the PkTagsHandler object
     *
     * @param clazz                 Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The DependentClassFor value
     * @exception XDocletException
     */
    protected String getDependentClassFor(XClass clazz, String type) throws XDocletException
    {
        return getPkClassFor(clazz);
    }
}
