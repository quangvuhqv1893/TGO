/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.intf;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;

import xjavadoc.*;
import xdoclet.DocletContext;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.*;
import xdoclet.modules.ejb.entity.EntityTagsHandler;
import xdoclet.modules.ejb.home.HomeTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="EjbIntf"
 * @version              $Revision: 1.17 $
 */
public class InterfaceTagsHandler extends EjbTagsHandler
{
    private String  currentMethodViewType = null;

    /**
     * Return the fully qualified name of the component interface of type specified. Works based on the <code>ejb:interface</code>
     * class level tag. Relevant parameters for the <code>ejb:interface</code> tag are:
     * <ul>
     *   <li> remote-class: The fully qualified name of the remote class - overrides all set patterns
     *   <li> local-class: The fully qualified name of the local class - overrides all set patterns
     *   <li> remote-pattern: The pattern to be used to determine the unqualified name of the remote class
     *   <li> local-pattern: The pattern to be used to determine the unqualified name of the local class
     *   <li> pattern: The pattern to be used in determining the unqualified remote and/or local interface name - used
     *   where remote- or local- pattern are not specified.
     *   <li> remote-package: The package the remote interface is to be placed in
     *   <li> local-package: The package the local interface is to be placed in
     *   <li> package: The package the remote and/or local interface is to be placed in - used where remote- or local-
     *   package are not specified.
     * </ul>
     *
     *
     * @param type                  Can be remote or local. Defaults to remote.
     * @param clazz                 Description of Parameter
     * @return                      The fully qualified name of the interface.
     * @exception XDocletException
     */
    public static String getComponentInterface(String type, XClass clazz) throws XDocletException
    {
        // validate type
        if (!"remote".equals(type) && !"local".equals(type)) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_INVALID_TYPE, new String[]{type}));
        }

        String fileName = clazz.getContainingPackage().getName();
        String name_pattern = null;
        String package_pattern = null;
        String component_interface = null;

        component_interface = clazz.getDoc().getTagAttributeValue("ejb:interface", type + "-class");
        if (component_interface != null) {
            return component_interface;
        }

        name_pattern = clazz.getDoc().getTagAttributeValue("ejb:interface", type + "-pattern");
        if (name_pattern == null) {
            name_pattern = clazz.getDoc().getTagAttributeValue("ejb:interface", "pattern");
            if (name_pattern == null) {
                name_pattern = "remote".equals(type) ? getRemoteClassPattern() : getLocalClassPattern();
            }
        }

        package_pattern = clazz.getDoc().getTagAttributeValue("ejb:interface", type + "-package");
        if (package_pattern == null) {
            package_pattern = clazz.getDoc().getTagAttributeValue("ejb:interface", "package");
        }

        String ejb_name = null;

        if (name_pattern.indexOf("{0}") != -1) {
            ejb_name = MessageFormat.format(name_pattern, new Object[]{getShortEjbNameFor(clazz)});
        }
        else {
            ejb_name = name_pattern;
        }

        String subtask_name = null;

        if (type.equals("remote")) {
            subtask_name = DocletTask.getSubTaskName(RemoteInterfaceSubTask.class);
        }
        else {
            subtask_name = DocletTask.getSubTaskName(LocalInterfaceSubTask.class);
        }

        // Fix package name
        fileName = choosePackage(fileName, package_pattern, subtask_name);
        fileName += "." + ejb_name;

        return fileName;
    }

    /**
     * Returns true if method is an interface method, false otherwise. Interface methods are
     * remote/create/remove/finder/home methods.
     *
     * @param method  Description of Parameter
     * @return        The InterfaceMethod value
     */
    public static boolean isInterfaceMethod(XMethod method)
    {
        boolean result = isComponentInterfaceMethod(method) ||
            HomeTagsHandler.isCreateMethod(method) ||
            HomeTagsHandler.isRemoveMethod(method) ||
            HomeTagsHandler.isFinderMethod(method) ||
            HomeTagsHandler.isHomeMethod(method);

        return result;
    }

    /**
     * Returns true if method is a component interface method, false otherwise. Component interface methods are marked
     * with a ejb:interface-method tag.
     *
     * @param method  Description of Parameter
     * @return        The RemoteMethod value
     */
    public static boolean isComponentInterfaceMethod(XMethod method)
    {
        return method.getDoc().hasTag("ejb:interface-method");
    }

    /**
     * Gets the BeanClassNameFromInterfaceNameFor attribute of the InterfaceTagsHandler class
     *
     * @param return_type           Describe what the parameter does
     * @return                      The BeanClassNameFromInterfaceNameFor value
     * @exception XDocletException
     */
    public static String getBeanClassNameFromInterfaceNameFor(String return_type) throws XDocletException
    {
        Log log = LogUtil.getLog(InterfaceTagsHandler.class, "getBeanClassNameFromInterfaceName");

        Collection classes = getXJavaDoc().getSourceClasses();

        if (log.isDebugEnabled()) {
            log.debug("return_type=" + return_type);
        }
        if (return_type == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.ASK_FOR_BEAN_FROM_NULL_INTERFACE, new String[]{return_type}));
        }

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (log.isDebugEnabled()) {
                log.debug("clazz=" + clazz);
            }

            if (EntityTagsHandler.isEntity(clazz)) {
                String remote_intf_name = getComponentInterface("remote", clazz);
                String local_intf_name = getComponentInterface("local", clazz);

                if (log.isDebugEnabled()) {
                    log.debug("remote_intf_name=" + remote_intf_name);
                    log.debug("local_intf_name=" + local_intf_name);
                }

                if (return_type.equals(remote_intf_name) || return_type.equals(local_intf_name)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Found! beanClassNameFromInterfaceName returns with: " + clazz.getQualifiedName());
                    }

                    return clazz.getQualifiedName();
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.warn("NOT FOUND! bean class coreesponding to IF " + return_type);
        }
        throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.BEAN_CLASS_NOT_FOUND_FOR_INTERFACE, new String[]{return_type}));
    }

    /**
     * Returns true if method is a remote interface method by looking at view-type parameter.
     *
     * @param method                Description of Parameter
     * @return                      The isRemoteMethod value
     * @exception XDocletException
     */
    public static boolean isRemoteMethod(XMethod method) throws XDocletException
    {
        return isComponentInterfaceMethodOfViewType(method, "remote");
    }

    /**
     * Returns true if method is a local interface method by looking at view-type parameter.
     *
     * @param method                Description of Parameter
     * @return                      The isRemoteMethod value
     * @exception XDocletException
     */
    public static boolean isLocalMethod(XMethod method) throws XDocletException
    {
        return isComponentInterfaceMethodOfViewType(method, "local");
    }

    /**
     * Gets the RemoteClassPattern attribute of the InterfaceTagsHandler class
     *
     * @return   The RemoteClassPattern value
     */
    protected static String getRemoteClassPattern()
    {
        RemoteInterfaceSubTask remoteintf_subtask = ((RemoteInterfaceSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(RemoteInterfaceSubTask.class)));

        if (remoteintf_subtask != null) {
            return remoteintf_subtask.getRemoteClassPattern();
        }
        else {
            return RemoteInterfaceSubTask.DEFAULT_REMOTE_CLASS_PATTERN;
        }
    }

    /**
     * Gets the LocalClassPattern attribute of the InterfaceTagsHandler class
     *
     * @return   The LocalClassPattern value
     */
    protected static String getLocalClassPattern()
    {
        LocalInterfaceSubTask localintf_subtask = ((LocalInterfaceSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(LocalInterfaceSubTask.class)));

        if (localintf_subtask != null) {
            return localintf_subtask.getLocalClassPattern();
        }
        else {
            return LocalInterfaceSubTask.DEFAULT_LOCAL_CLASS_PATTERN;
        }
    }

    /**
     * Gets the ComponentInterfaceMethodOfViewType attribute of the InterfaceTagsHandler class
     *
     * @param method                Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The ComponentInterfaceMethodOfViewType value
     * @exception XDocletException
     */
    private static boolean isComponentInterfaceMethodOfViewType(XMethod method, String type) throws XDocletException
    {
        //if a home method like create/remote/etc
        if (isComponentInterfaceMethod(method) == false) {
            //you can't specify method-intf for ejbCreate/ejbRemote/etc for now
            //so return false and methodIntf will use Home/LocalHome
            return false;
        }
        else {
            String view_type = getTagValue(
                FOR_CLASS,
                method.getDoc(),
                "ejb:interface-method",
                "view-type",
                "local,remote,both",
                "both",
                false,
                false
                );

            if (view_type.equals(type) || view_type.equals("both")) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Gets the ViewTypesFromString attribute of the InterfaceTagsHandler class
     *
     * @param viewType  Describe what the parameter does
     * @return          The ViewTypesFromString value
     */
    private static String[] getViewTypesFromString(String viewType)
    {
        if (viewType != null) {
            if (viewType.equalsIgnoreCase("both")) {
                return new String[]{"local", "remote"};
            }

            if (viewType.equalsIgnoreCase("remote")) {
                return new String[]{"remote"};
            }

            if (viewType.equalsIgnoreCase("local")) {
                return new String[]{"local"};
            }
        }

        // If we're using EJB 1.1, the default is "remote", otherwise it's "both"
        if (EjbTagsHandler.getEjbSpec().equals("1.1"))
            return new String[]{"remote"};
        else
            return new String[]{"local", "remote"};
    }

    /**
     * Returns the full qualified local or remote interface name for the bean, depending on the value of type parameter.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     * @doc.param                   name="type" optional="false" values="remote,local" description="Specifies the type
     *      of component interface."
     */
    public String componentInterface(Properties attributes) throws XDocletException
    {
        String type = attributes.getProperty("type");

        type = type != null ? type : "remote";

        return getComponentInterface(type, getCurrentClass());
    }

    /**
     * Evaluate the body block if the current method is not an EJB local or remote interface method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="interface" optional="false" description="The type of interface to check for
     *      the methods validity in. Can be either \"local\" or \"remote\"."
     */
    public void ifIsNotInterfaceMethod(String template, Properties attributes) throws XDocletException
    {
        String intFace = attributes.getProperty("interface");

        if (intFace == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.TAG_MISSING_INTERFACE_PARAMETER, new String[]{"<XDtEjbIntf:ifIsNotInterfaceMethod>"}));
        }

        if (!isInterfaceMethod(intFace)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body block if the current method is an EJB local or remote interface method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="interface" optional="false" description="The type of interface to check for
     *      the methods validity in. Can be either \"local\" or \"remote\"."
     * @doc.param                   name="superclasses" optional="true" description="Traverse superclasses too. With
     *      false value used in remote/local. Default is True."
     */
    public void ifIsInterfaceMethod(String template, Properties attributes) throws XDocletException
    {
        String intf_view_type = attributes.getProperty("interface");
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        if (intf_view_type == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.TAG_MISSING_INTERFACE_PARAMETER, new String[]{"<XDtEjbIntf:ifIsInterfaceMethod>"}));
        }

        if (isInterfaceMethod(intf_view_type)) {
            boolean currentMethodDoesntBelongsToCurrentClass = !getCurrentMethod().getContainingClass().equals(getCurrentClass());

            if (superclasses == false && currentMethodDoesntBelongsToCurrentClass == true) {
                return;
            }

            generate(template);
        }
    }

    /**
     * Evaluates the body block for each view-type of current method. Sets currentMethodViewType internal variable of
     * tag handler class, used by nested methodIntf.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllInterfaceViewTypes(String template, Properties attributes) throws XDocletException
    {
        String[] view_types = null;

        if (isComponentInterfaceMethod(getCurrentMethod())) {
            //is a component intf method
            String view_type = getTagValue(
                FOR_METHOD,
                getCurrentMethod().getDoc(),
                "ejb.interface-method",
                "view-type",
                "local,remote,both",
                null,
                true,
                false
                );

            if (view_type == null) {
                view_type = getTagValue(
                    FOR_CLASS,
                    getCurrentClass().getDoc(),
                    "ejb.bean",
                    "view-type",
                    "local,remote,both",
                    null,
                    true,
                    false
                    );
            }

            view_types = getViewTypesFromString(view_type);
        }
        else {
            //is a home intf method
            String view_type = getTagValue(
                FOR_METHOD,
                getCurrentMethod().getDoc(),
                "ejb.home-method",
                "view-type",
                "local,remote,both",
                null,
                true,
                false
                );

            view_types = getViewTypesFromString(view_type);
        }

        for (int i = 0; i < view_types.length; i++) {
            currentMethodViewType = view_types[i];

            generate(template);
        }

        currentMethodViewType = null;
    }

    /**
     * Evaluates the body block if ejb:interface-method defined for current method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isInterfaceMethod(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     */
    public void ifIsInterfaceMethod(String template) throws XDocletException
    {
        if (isInterfaceMethod(getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Returns "Remote" is current method has ejb:remote-method defined, "Home" otherwise.
     *
     * @param attributes            The attributes of the template tag
     * @return                      "Remote" or "Home".
     * @exception XDocletException
     * @see                         #isRemoteMethod(xjavadoc.XMethod)
     * @doc.tag                     type="content"
     */
    public String methodIntf(Properties attributes) throws XDocletException
    {
        String view_type = attributes.getProperty("interface");

        if (EjbTagsHandler.getEjbSpec().equals("1.1"))
            view_type = "remote";

        if (view_type == null) {
            //if not explicitly specified for this call, then use currentMethodViewType
            if (currentMethodViewType != null) {
                view_type = currentMethodViewType;
            }
            else {
                if (EjbTagsHandler.isOnlyLocalEjb(getCurrentClass())) {
                    view_type = "local";
                }
                else if (EjbTagsHandler.isOnlyRemoteEjb(getCurrentClass())) {
                    view_type = "remote";
                }
                else if (EntityTagsHandler.isEntity(getCurrentClass())) {
                    view_type = "local";
                }
                else {
                    view_type = "remote";
                }
            }
        }

        if (view_type.equals("remote")) {
            return (isRemoteMethod(getCurrentMethod()) ? "Remote" : "Home");
        }
        else {
            return (isLocalMethod(getCurrentMethod()) ? "Local" : "LocalHome");
        }
    }

    /**
     * Returns interface method name for the current interface method.
     *
     * @return                      "Remote" or "Home".
     * @exception XDocletException
     * @see                         #getInterfaceMethodName(java.lang.String)
     * @doc.tag                     type="content"
     */
    public String interfaceMethodName() throws XDocletException
    {
        return getInterfaceMethodName(getCurrentMethod().getName());
    }

    /**
     * Returns the bean implementation class name for the interface name specified as the return type of current method
     * or the method specified by parameter interface if any.
     *
     * @param attributes
     * @return                      Bean class name
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String beanClassNameFromInterfaceName(Properties attributes) throws XDocletException
    {
        String return_type = attributes.getProperty("interface");

        if (return_type == null) {
            return_type = MethodTagsHandler.getMethodTypeFor(getCurrentMethod());
        }

        String bean_class_name = getBeanClassNameFromInterfaceNameFor(return_type);

        if (bean_class_name == null) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.INTERFACE_IMPL_NOT_FOUND, new String[]{return_type}));
        }

        return bean_class_name;
    }

    /**
     * Returns the name of the class home interface extends.
     *
     * @param attributes
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String extendsFrom(Properties attributes) throws XDocletException
    {
        String type = attributes.getProperty("type");

        type = type != null ? type : "remote";

        String extends_param_name = type.equals("remote") ? "extends" : "local-extends";
        String def_base_class_name = type.equals("remote") ? "javax.ejb.EJBObject" : "javax.ejb.EJBLocalObject";

        return extendsFromFor(getCurrentClass(), "ejb:interface", type, extends_param_name, def_base_class_name);
    }

    /**
     * Implements functionality required by {@link #ifIsInterfaceMethod} and {@link #ifIsNotInterfaceMethod}. To
     * determine what interfaces the method should appear in, check the first for a <code>view-type</code> parameter to
     * the method level <code>ejb:interface-method</code> tag. If that is absent use the <code>view-type</code> tag from
     * <code>ejb:bean</code> .
     *
     * @param intFace               The type of interface to test the method for.
     * @return                      true if the method should occur in the specified interface.
     * @exception XDocletException
     */
    protected boolean isInterfaceMethod(String intFace) throws XDocletException
    {
        if (!getCurrentMethod().getDoc().hasTag("ejb:interface-method")) {
            return false;
        }

        String viewType = getTagValue(
            FOR_CLASS,
            getCurrentMethod().getDoc(),
            "ejb:interface-method",
            "view-type",
            "local,remote,both",
            null,
            false,
            false
            );

        if ("both".equals(viewType)) {
            viewType = "local,remote";
        }

        if (viewType == null) {
            viewType = getTagValue(
                FOR_CLASS,
                getCurrentClass().getDoc(),
                "ejb:bean",
                "view-type",
                "local,remote,both",
                "both",
                true,
                false
                );

            if ("both".equals(viewType)) {
                viewType = "local,remote";
            }
        }

        return viewType.indexOf(intFace) >= 0;
    }

    /**
     * Returns the interface method name depending on its type.
     *
     * @param name                  Description of Parameter
     * @return                      "create" if ejbCreate, "remote" if ejbRemove, find <blabl>if ejbFind, home <blabla>
     *      if ejbHome.
     * @exception XDocletException
     */
    protected String getInterfaceMethodName(String name) throws XDocletException
    {
        if (name.startsWith("ejbCreate")) {
            return HomeTagsHandler.toCreateMethod(name);
        }
        else if (name.equals("ejbRemove")) {
            return "remove";
        }
        else if (name.startsWith("ejbFind")) {
            return HomeTagsHandler.toFinderMethod(name);
        }
        else if (name.startsWith("ejbHome")) {
            return HomeTagsHandler.toHomeMethod(name);
        }
        else {
            return name;
        }
    }

    /**
     * Gets the DependentClassFor attribute of the InterfaceTagsHandler object
     *
     * @param clazz                 Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The DependentClassFor value
     * @exception XDocletException
     */
    protected String getDependentClassFor(XClass clazz, String type) throws XDocletException
    {
        if ((type.equals("local") && isLocalEjb(clazz)) || (type.equals("remote") && isRemoteEjb(clazz))) {
            return getComponentInterface(type, clazz);
        }
        else {
            return null;
        }
    }

    /**
     * Loops over all classes and if value equals to local or remote interface name of an EJBean full qualified name of
     * that EJB is returned.
     *
     * @param value                 Description of Parameter
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    protected String fromInterfaceToBean(String value) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (isEjb(clazz)) {
                String remote_interface_name = getComponentInterface("remote", clazz);
                String local_interface_name = getComponentInterface("local", clazz);

                if (value.equals(remote_interface_name) || value.equals(local_interface_name)) {
                    return clazz.getQualifiedName();
                }
            }
        }

        return value;
    }
}
