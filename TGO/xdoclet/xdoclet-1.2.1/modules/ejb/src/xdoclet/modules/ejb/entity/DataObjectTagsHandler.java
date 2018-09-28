/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

////import xdoclet.util.serialveruid.*;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.entity.DataObjectSubTask;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              13. juni 2002
 * @xdoclet.taghandler   namespace="EjbDataObj"
 * @version              $Revision: 1.11 $
 */
public class DataObjectTagsHandler extends EjbTagsHandler
{
    protected static HashMap dataObjectClassnames = new HashMap();

    protected static String currentDataObjectClassname = "";

    /**
     * @param clazz                 Description of Parameter
     * @return                      the full qualified data-object class name
     * @exception XDocletException
     */
    public static String getDataMostSuperObjectClass(XClass clazz) throws XDocletException
    {
        String currentDataClass = (String) dataObjectClassnames.get(clazz.getName());
        // Begin at the first super class

        XClass cur_clazz = clazz;

        currentDataClass = generateDataObjectClass(cur_clazz);

        loopa :
        do {
            // Find if we have an abstract data class definition to generate
            Collection methods = cur_clazz.getMethods();
            boolean found = false;

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (method.getName().equals("getData")) {
                    found = true;
                    currentDataClass = generateDataObjectClass(cur_clazz);
                }

                if (found) {
                    break loopa;
                }
            }
            cur_clazz = cur_clazz.getSuperclass();
        } while (cur_clazz != null);

        if (currentDataClass == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.DATACLASS_NOT_FOUND));
        }
        return currentDataClass;
    }

    /**
     * Gets the DataObjectMethod attribute of the DataObjectTagsHandler class
     *
     * @param method                Describe what the parameter does
     * @return                      The DataObjectMethod value
     * @exception XDocletException
     */
    public static boolean isDataObjectMethod(XMethod method) throws XDocletException
    {
        if (EntityTagsHandler.isEntity(method.getContainingClass()) == false) {
            return false;
        }

        boolean is_getData = isGetDataMethod(method);

        if (is_getData) {
            return true;
        }
        else {
            return isSetDataMethod(method);
        }
    }

    /**
     * @param clazz  Description of Parameter
     * @return       the full qualified data-object class name
     */
    public static String getDataObjectClassFor(XClass clazz)
    {
        if (dataObjectClassnames.get(clazz.getQualifiedName()) != null) {
            return (String) dataObjectClassnames.get(clazz.getQualifiedName());
        }

        String fileName = clazz.getContainingPackage().getName();
        String name_pattern = null;
        String package_pattern = null;

        String dataObjectClass = clazz.getDoc().getTagAttributeValue("ejb:data-object", "class", false);

        if (dataObjectClass != null) {
            return dataObjectClass;
        }

        name_pattern = clazz.getDoc().getTagAttributeValue("ejb:data-object", "pattern", false);

        if (name_pattern == null) {
            name_pattern = getDataObjectClassPattern();
        }

        package_pattern = clazz.getDoc().getTagAttributeValue("ejb:data-object", "package", false);

        String dataObjectClass_name = null;

        if (name_pattern.indexOf("{0}") != -1) {
            dataObjectClass_name = MessageFormat.format(name_pattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            dataObjectClass_name = name_pattern;
        }

        // Fix package name
        fileName = choosePackage(fileName, package_pattern, DocletTask.getSubTaskName(DataObjectSubTask.class));
        if (fileName.length() > 0) {
            fileName += ".";
        }

        fileName += dataObjectClass_name;

        return fileName;
    }

    /**
     * @return   the current data-object class name
     */
    public static String getCurrentDataObjectClassname()
    {
        return currentDataObjectClassname;
    }

    /**
     * Returns true if ejb:data-object defined and generate param is true, false if not true.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     */
    public static boolean isGenerationNeeded(XClass clazz)
    {
        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        // When the tag is there and is set to "false" then return false
        if (clazz.getDoc().hasTag("ejb:data-object")) {
            String generate_str = clazz.getDoc().getTagAttributeValue("ejb:data-object", "generate", false);
            boolean generate = TypeConversionUtil.stringToBoolean(generate_str, true);

            return generate;
        }
        else {
            return true;
        }
    }

    /**
     * @param current_data_object_classname
     */
    public static void setCurrentDataObjectClassname(String current_data_object_classname)
    {
        currentDataObjectClassname = current_data_object_classname;
    }

    /**
     * @param name   Description of Parameter
     * @param value  Description of Parameter
     */
    public static void putDataObjectClassnames(String name, String value)
    {
        dataObjectClassnames.put(name, value);
    }

    /**
     * Used by dataMostSuperObjectClass() to get the data object's full qualified class name. If name and package
     * parameters of ejb:data-object defined, theire values are used, otherwise defaults are used.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @see          #dataMostSuperObjectClass()
     */
    public static String generateDataObjectClass(XClass clazz)
    {
        String fileName = clazz.getContainingPackage().getName();
        String name_pattern = null;
        String package_pattern = null;

        name_pattern = clazz.getDoc().getTagAttributeValue("ejb:data-object", "name", false);
        if (name_pattern == null) {
            name_pattern = getDataObjectClassPattern();
        }

        package_pattern = clazz.getDoc().getTagAttributeValue("ejb:data-object", "package", false);

        String dataobject_name = null;

        if (name_pattern.indexOf("{0}") != -1) {
            dataobject_name = MessageFormat.format(name_pattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            dataobject_name = name_pattern;
        }

        // Fix package name
        fileName = choosePackage(fileName, package_pattern, DocletTask.getSubTaskName(DataObjectSubTask.class));
        fileName += "." + dataobject_name;

        return fileName;
    }

    /**
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     */
    public static boolean hasCustomBulkData(XClass clazz)
    {
        Log log = LogUtil.getLog(DataObjectSubTask.class, "hasCustomBulkData");

        if (log.isDebugEnabled()) {
            log.debug("Searching @ejb:bulk-data in Class " + clazz);
        }

        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        return clazz.getDoc().hasTag("ejb:bulk-data");
    }

    /**
     * @return   the data-object class pattern
     */
    protected static String getDataObjectClassPattern()
    {
        final DataObjectSubTask dataobject_subtask = ((DataObjectSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(DataObjectSubTask.class)));

        if (dataobject_subtask != null) {
            return dataobject_subtask.getDataObjectClassPattern();
        }
        else {
            return DataObjectSubTask.DEFAULT_DATAOBJECT_CLASS_PATTERN;
        }
    }

    /**
     * @param clazz
     * @return                      True if there is a Data Container equals() needed because user set tag "data-equals"
     *      to true or ommitted it
     * @exception XDocletException
     */
    protected static boolean hasDataEquals(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(DataObjectSubTask.class, "hasDataEquals");

        if (log.isDebugEnabled()) {
            log.debug("Searching @ejb:data-object equals for Class " + clazz.getQualifiedName());
        }

        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        String value = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:data-object",
            "equals",
            "true,false",
            "true",
            true,
            false
            );

        return TypeConversionUtil.stringToBoolean(value, true);
    }

    /**
     * Gets the DataObjectSubTaskActive attribute of the DataObjectTagsHandler class
     *
     * @return   The DataObjectSubTaskActive value
     */
    private static boolean isDataObjectSubTaskActive()
    {
        return DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(DataObjectSubTask.class));
    }

    /**
     * Returns true if method is getData()
     *
     * @param method                Description of Parameter
     * @return                      The GetDataMethod value
     * @exception XDocletException
     */
    private static boolean isGetDataMethod(XMethod method) throws XDocletException
    {
        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        return method.isAbstract() && method.equals("getData") && DataObjectTagsHandler.getDataMostSuperObjectClass(method.getContainingClass()).equals(method.getReturnType().getType());
    }

    /**
     * Returns true if method is setData()
     *
     * @param method  Description of Parameter
     * @return        The SetDataMethod value
     */
    private static boolean isSetDataMethod(XMethod method)
    {
        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        if (method.isAbstract() && method.equals("setData")) {
            Collection params = method.getParameters();

            if (params.size() == 1 &&
                ((XParameter) params.iterator().next()).getType().getQualifiedName().equals(DataObjectTagsHandler.getDataObjectClassFor(method.getContainingClass()))
                ) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if ejb:data-object defined and setdata param is true, false if not true.
     *
     * @param clazz
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    private static boolean hasDataMethod(XClass clazz) throws XDocletException
    {
        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        String value = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:data-object",
            "setdata",
            "true,false",
            "true",
            true,
            false
            );

        boolean result = TypeConversionUtil.stringToBoolean(value, false);

        return result;
    }

    /**
     * @param pTemplate             Description of Parameter
     * @exception XDocletException
     */
    public void isDataContentEquals(String pTemplate) throws XDocletException
    {
        if (hasDataEquals(getCurrentClass())) {
            generate(pTemplate);
        }
    }

    /**
     * Returns data-object class name for the bean.
     *
     * @return                      The data-object class name for the bean.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String dataObjectClass() throws XDocletException
    {
        return getDataObjectClassFor(getCurrentClass());
    }

    /**
     * Returns the data-object class name highest in the hierarchy of derived beans. Because of possible inheritance
     * between entity bean, the type of the generated getData method must be the one of the most super class of the
     * current entity bean. The current Data class must extend the corresponding super Data class.
     *
     * @return                      The data-object class name highest in the hierarchy of derived beans.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String dataMostSuperObjectClass() throws XDocletException
    {
        return getDataMostSuperObjectClass(getCurrentClass());
    }

    /**
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String generateDataObjectClass() throws XDocletException
    {
        return generateDataObjectClass(getCurrentClass());
    }

    /**
     * Evaluate the body block if ejb:data-object setdata="true". If not defined then default is true.
     *
     * @param pTemplate             Description of Parameter
     * @exception XDocletException
     * @see                         #ifIsWithDataContainer(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsWithDataMethod(String pTemplate) throws XDocletException
    {
        if (hasDataMethod(getCurrentClass())) {
            generate(pTemplate);
        }
    }

    /**
     * Evaluate the body block if ejb:data-object container="true". If not defined then default is true.
     *
     * @param pTemplate             Description of Parameter
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsWithDataContainer(String pTemplate) throws XDocletException
    {
        if (isGenerationNeeded(getCurrentClass())) {
            generate(pTemplate);
        }
        // We can also have a generate false because we use the same dataclass
        // that the super class (e.g. BMP extends CMP)
        else {
            String hasClass = getTagValue(
                FOR_CLASS,
                getCurrentClass().getDoc(),
                "ejb:data-object",
                "class",
                null,
                null,
                false,
                false
                );

            if (hasClass != null) {
                generate(pTemplate);
            }
        }
    }

    /**
     * Evaluate the body block if ejb:aggregate is defined for current getter method, denoting that the specified getter
     * method returns an aggregated object.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #ifIsNotAggregate(java.lang.String)
     * @see                         #isAggregate(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     */
    public void ifIsAggregate(String template) throws XDocletException
    {
        if (isAggregate(getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if ejb:aggregate is not defined for current getter method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #ifIsAggregate(java.lang.String)
     * @see                         #isAggregate(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     */
    public void ifIsNotAggregate(String template) throws XDocletException
    {
        if (!isAggregate(getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block for each setData method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllSuper(java.lang.String,java.lang.String)
     * @doc.tag                     type="block"
     */
    public void forAllSuperSetData(String template) throws XDocletException
    {
        forAllSuper(template, "setData");
    }

    /**
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    public String parentDataObjectClass() throws XDocletException
    {
        Log log = LogUtil.getLog(DataObjectSubTask.class, "parentDataObjectClass");

        if (log.isDebugEnabled()) {
            log.debug("Searching for @ejb:data-object in Current/Super Class " + getCurrentClass().getName());
        }

        if (getCurrentClass().getDoc().hasTag("ejb:data-object", false)) {
            String extends_class = getCurrentClass().getDoc().getTagAttributeValue("ejb:data-object", "extends", false);

            return extends_class != null ? extends_class : "java.lang.Object";
        }

        if (getCurrentClass().getSuperclass() != null && isEjb(getCurrentClass().getSuperclass())) {
            pushCurrentClass(getCurrentClass().getSuperclass());

            String clazz = dataObjectClass();

            popCurrentClass();

            if (clazz == null) {
                return "java.lang.Object";
            }

            return clazz;
        }
        else {
            return "java.lang.Object";
        }
    }

    /**
     * Returns the name of the class dataobject class extends.
     *
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String extendsFrom() throws XDocletException
    {
        return extendsFromFor(getCurrentClass(), "ejb:data-object", null, "extends", "java.lang.Object");
    }

    /**
     * Return the dataobject class name from interface name.
     *
     * @return                      the data-object class name
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String dataObjectClassNameFromInterfaceName() throws XDocletException
    {
        String return_type = MethodTagsHandler.getMethodTypeFor(getCurrentMethod());

        if (return_type == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_NOT_FOUND, new String[]{getCurrentMethod().getName()}));
        }

        String bean_class_name = InterfaceTagsHandler.getBeanClassNameFromInterfaceNameFor(return_type);

        if (bean_class_name == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_IMPL_NOT_FOUND, new String[]{return_type}));
        }

        XClass bean_class = getXJavaDoc().getXClass(bean_class_name);

        if (bean_class == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_IMPL_COULDNT_LOAD, new String[]{return_type}));
        }

        String dataobject_class_name = DataObjectTagsHandler.getDataObjectClassFor(bean_class);

        if (dataobject_class_name == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_DATAOBJECT_NOT_KNOWN, new String[]{return_type}));
        }

        return dataobject_class_name;
    }

    /**
     * Returns true if method has ejb:aggregate, false otherwise.
     *
     * @param method                Description of Parameter
     * @return                      The Aggregate value
     * @exception XDocletException
     */
    protected boolean isAggregate(XMethod method) throws XDocletException
    {
        if (isDataObjectSubTaskActive() == false) {
            return false;
        }

        return method.getDoc().hasTag("ejb:aggregate");
    }

    /**
     * Gets the DependentClassFor attribute of the DataObjectTagsHandler object
     *
     * @param clazz                 Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The DependentClassFor value
     * @exception XDocletException
     */
    protected String getDependentClassFor(XClass clazz, String type) throws XDocletException
    {
        return getDataObjectClassFor(clazz);
    }

    /**
     * Browse all super classes and search for a special method to generate it in the current CMP/BMP class.
     *
     * @param template              The body of the block tag
     * @param methodName            Description of Parameter
     * @exception XDocletException
     */
    protected void forAllSuper(String template, String methodName) throws XDocletException
    {
        XClass oldClass = getCurrentClass();
        XClass superclass = null;
        List already = new ArrayList();

        // Begin at the first super class
        //pushCurrentClass( getCurrentClass().superclass() );

        do {
            // Find if we have an abstract data class definition to generate
            Collection methods = getCurrentClass().getMethods();
            XMethod methodFound = null;

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (method.getName().equals(methodName)) {
                    methodFound = method;
                }

                if (methodFound != null) {
                    break;
                }
            }

            if (methodFound != null) {
                // We can not use contains because it is based on equals() and
                // we need to compare based on compareTo()
                Iterator i = already.iterator();
                boolean contained = false;

                while (i.hasNext()) {
                    if (((XMethod) i.next()).compareTo(methodFound) == 0) {
                        contained = true;
                    }

                    if (contained) {
                        break;
                    }
                }

                if (contained == false) {
                    generate(template);
                    already.add(methodFound);
                }
            }

            superclass = getCurrentClass().getSuperclass();

            if (superclass != null) {
                pushCurrentClass(superclass);
            }

        } while (superclass != null);

        setCurrentClass(oldClass);
    }
}
