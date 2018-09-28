/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.home;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.entity.CmpTagsHandler;
import xdoclet.modules.ejb.entity.EntityTagsHandler;
import xdoclet.modules.ejb.entity.PkTagsHandler;
import xdoclet.modules.ejb.home.LocalHomeInterfaceSubTask;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.modules.ejb.session.SessionTagsHandler;
import xdoclet.util.DocletUtil;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="EjbHome"
 * @version              $Revision: 1.33 $
 */
public class HomeTagsHandler extends EjbTagsHandler
{

    private String  currentSignature;

    private String  currentExceptions;

    private String  currentPermission;

    /**
     * Similar to {@link xdoclet.modules.ejb.intf.InterfaceTagsHandler#getComponentInterface}. Relies on the ejb:home
     * tag, which has the following relevant properties:
     * <ul>
     *   <li> remote-class: The fully qualified name of the remote class - overrides all set patterns
     *   <li> local-class: The fully qualified name of the local class - overrides all set patterns
     *   <li> remote-pattern: The pattern to be used to determine the unqualified name of the remote class
     *   <li> local-pattern: The pattern to be used to determine the unqualified name of the local class
     *   <li> pattern: The pattern to be used in determining the unqualified remote and/or local home interface name -
     *   used where remote- or local- pattern are not specified.
     *   <li> remote-package: The package the remote home interface is to be placed in
     *   <li> local-package: The package the local home interface is to be placed in
     *   <li> package: The package the remote and/or local home interface is to be placed in - used where remote- or
     *   local- package are not specified.
     * </ul>
     *
     *
     * @param type                  The type of home interface - can be remote or local.
     * @param clazz                 Description of Parameter
     * @return                      The HomeInterface value
     * @exception XDocletException
     */
    public static String getHomeInterface(String type, XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(HomeTagsHandler.class, "getHomeInterface");

        // validate type
        if (!"remote".equals(type) && !"local".equals(type)) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.METHOD_ONLY_TAKES_REMOTE_OR_LOCAL, new String[]{"getHomeInterface", type}));
        }

        String fileName = clazz.getContainingPackage().getName();
        String name_pattern = null;
        String package_pattern = null;
        String home_interface = null;

        home_interface = clazz.getDoc().getTagAttributeValue("ejb:home", type + "-class");
        if (log.isDebugEnabled()) {
            log.debug(type + " home Interface for " + clazz.getQualifiedName() + " = " + home_interface);
        }

        if (home_interface != null) {
            return home_interface;
        }

        name_pattern = clazz.getDoc().getTagAttributeValue("ejb:home", type + "-pattern");
        if (name_pattern == null) {
            name_pattern = clazz.getDoc().getTagAttributeValue("ejb:home", "pattern");
            if (name_pattern == null) {
                name_pattern = "remote".equals(type) ? getHomeClassPattern() : getLocalHomeClassPattern();
            }
        }

        package_pattern = clazz.getDoc().getTagAttributeValue("ejb:home", type + "-package");
        if (package_pattern == null) {
            package_pattern = clazz.getDoc().getTagAttributeValue("ejb:home", "package");
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
            subtask_name = DocletTask.getSubTaskName(HomeInterfaceSubTask.class);
        }
        else {
            subtask_name = DocletTask.getSubTaskName(LocalHomeInterfaceSubTask.class);
        }

        // Fix package name
        fileName = choosePackage(fileName, package_pattern, subtask_name);
        fileName += "." + ejb_name;

        return fileName;
    }

    /**
     * Returns true if method is an ejbRemove method, false otherwise.
     *
     * @param method  Description of Parameter
     * @return        The RemoveMethod value
     */
    public static boolean isRemoveMethod(XMethod method)
    {
        return method.getName().equals("ejbRemove");
    }

    /**
     * Returns true if method is a create method marked with a ejb:create-method tag, false otherwise.
     *
     * @param method  Description of Parameter
     * @return        The CreateMethod value
     */
    public static boolean isCreateMethod(XMethod method)
    {
        return method.getDoc().hasTag("ejb:create-method");
    }

    /**
     * Returns true if method is a home method marked with a ejb:home-method tag, false otherwise.
     *
     * @param method  Description of Parameter
     * @return        The HomeMethod value
     */
    public static boolean isHomeMethod(XMethod method)
    {
        return method.getDoc().hasTag("ejb.home-method");
    }

    /**
     * Gets the CompNameFor attribute of the HomeTagsHandler class
     *
     * @param clazz  Describe what the parameter does
     * @param type   Describe what the parameter does
     * @return       The CompNameFor value
     */
    public static String getCompNameFor(XClass clazz, String type)
    {
        String compName = getEjbNameFor(clazz).replace('.', '/');

        if (type.equals("local")) {
            compName = compName + LOCAL_SUFFIX;
        }

        return compName;
    }

    /**
     * Returns true if method is an ejbFind method, false otherwise.
     *
     * @param method  Description of Parameter
     * @return        The FinderMethod value
     */
    public static boolean isFinderMethod(XMethod method)
    {
        return method.getName().startsWith("ejbFind");
    }

    /**
     * Gets the HomeDefinition attribute of the HomeTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @param method                Describe what the parameter does
     * @param tagType               Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The HomeDefinition value
     * @exception XDocletException
     */
    public static String getHomeDefinition(XClass clazz, XMethod method, String tagType, String type)
         throws XDocletException
    {
        String methodName = method.getName().substring(3);
        StringBuffer homeMethodName = new StringBuffer();

        if (tagType.equals("ejb:finder")) {
            String ejbReturn = method.getReturnType().getType().getQualifiedName();

            if (ejbReturn.equals("java.util.Collection") ||
                ejbReturn.equals("java.util.Enumeration") ||
                ejbReturn.equals("java.util.Set")) {
                homeMethodName.append(ejbReturn);

            }
            else {
                homeMethodName.append(InterfaceTagsHandler.getComponentInterface(type, clazz));

            }
        }
        else if (tagType.equals("ejb:create-method")) {
            homeMethodName.append(InterfaceTagsHandler.getComponentInterface(type, clazz));
        }
        homeMethodName.append(" ");
        homeMethodName.append(methodName.substring(0, 1).toLowerCase());
        homeMethodName.append(methodName.substring(1));
        homeMethodName.append("(");

        for (Iterator i = method.getParameters().iterator(); i.hasNext(); ) {
            homeMethodName.append(i.next());
            if (i.hasNext())
                homeMethodName.append(" , ");
        }
        homeMethodName.append(")");
        return fullPackageChange(homeMethodName.toString());
    }

    public static String getJndiNameOfTypeFor(String type, XClass clazz)
    {
        XTag bean_tag = clazz.getDoc().getTag("ejb:bean");
        String compName = getCompNameFor(clazz, type);

        if (bean_tag != null) {
            String jndiName = bean_tag.getAttributeValue("jndi-name");
            String localJndiName = bean_tag.getAttributeValue("local-jndi-name");

            //Return "local" jndi name
            if ("local".equals(type)) {
                return localJndiName != null ? localJndiName : compName;
            }

            //Didn't ask for local, assume remote
            return jndiName != null ? jndiName : compName;
        }

        //nothing specified so madeup one
        return compName;
    }

    /**
     * Converts ejbHome<em>blabla</em> to home<em>blabla</em> , the one that should appear in home interface.
     *
     * @param methodName  Method name to be converted.
     * @return            Equivalent home interface method name.
     */
    public static String toHomeMethod(String methodName)
    {
        // Remove "ejbHome" prefix and lower case first char in rest: "ejbHomeFoo"->"foo"
        return Character.toLowerCase(methodName.charAt(7)) + methodName.substring(8);
    }

    /**
     * Converts ejbCreate<em>blabla</em> to create<em>blabla</em> , the one that should appear in home interface.
     *
     * @param methodName  Method name to be converted.
     * @return            Equivalent home interface method name.
     */
    public static String toCreateMethod(String methodName)
    {
        if (methodName.length() > 9) {
            // Remove "ejbCreate" prefix and lower case first char in rest: "ejbCreateFoo"->"createFoo", EJB 2 only
            return "create" + Character.toUpperCase(methodName.charAt(9)) + methodName.substring(10);
        }
        else {
            return "create";
        }
    }

    /**
     * Describe what the method does
     *
     * @param clazz  Describe what the parameter does
     * @return       Describe the return value
     */
    public static XMethod findFirstCreateMethodFor(XClass clazz)
    {
        Collection methods = clazz.getMethods();

        do {
            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (HomeTagsHandler.isCreateMethod(method)) {
                    return method;
                }
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null);

        return null;
    }

    /**
     * Converts ejbFind<em>blabla</em> to find<em>blabla</em> , the one that should appear in home interface.
     *
     * @param methodName  Method name to be converted.
     * @return            Equivalent home interface method name.
     */
    public static String toFinderMethod(String methodName)
    {
        // Remove "ejb" prefix and lower case first char in rest: "ejbFindByPrimaryKey"->"findByPrimaryKey"
        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }

    /**
     * Convert various collection types within a string to their fully qualified forms.
     *
     * @param s  String to be converted.
     * @return   String with fully qualified collection types.
     */
    public static String fullPackageChange(String s)
    {
        StringTokenizer st = new StringTokenizer(s, " ");
        String sign = st.nextToken();
        StringBuffer ret = new StringBuffer();

        if (sign.equals("Collection")) {
            ret.append("java.util.Collection");
        }
        else if (sign.equals("Enumeration")) {
            ret.append("java.util.Enumeration");
        }
        else if (sign.equals("Set")) {
            ret.append("java.util.Set");
        }
        else {
            ret.append(sign);
        }
        while (st.hasMoreTokens()) {
            ret.append(" ").append(st.nextToken());
        }
        return ret.toString();
    }

    /**
     * Describe what the method does
     *
     * @param s                     Describe what the parameter does
     * @param clazz
     * @param type
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public static String finderSignatureFunger(String s, XClass clazz, String type) throws XDocletException
    {
        StringTokenizer st = new StringTokenizer(s, " ");
        String sign = st.nextToken();
        StringBuffer ret = new StringBuffer();

        if (sign.equals("Collection") || sign.equals("java.util.Collection")) {
            ret.append("java.util.Collection");
        }
        else if (sign.equals("Enumeration") || sign.equals("java.util.Enumeration")) {
            ret.append("java.util.Enumeration");
        }
        else if (sign.equals("Set") || sign.equals("java.util.Set")) {
            ret.append("java.util.Set");
        }
        else {
            ret.append(InterfaceTagsHandler.getComponentInterface(type, clazz));
        }
        while (st.hasMoreTokens()) {
            ret.append(" ").append(st.nextToken());
        }
        return ret.toString();
    }

    /**
     * Gets the LocalHomeClassPattern attribute of the HomeTagsHandler class.
     *
     * @return   The LocalHomeClassPattern value
     */
    protected static String getLocalHomeClassPattern()
    {
        LocalHomeInterfaceSubTask localhomeintf_subtask = ((LocalHomeInterfaceSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(LocalHomeInterfaceSubTask.class)));

        if (localhomeintf_subtask != null) {
            return localhomeintf_subtask.getLocalHomeClassPattern();
        }
        else {
            return LocalHomeInterfaceSubTask.DEFAULT_LOCALHOMEINTERFACE_CLASS_PATTERN;
        }
    }

    /**
     * Gets the HomeClassPattern attribute of the HomeTagsHandler class.
     *
     * @return   The HomeClassPattern value
     */
    protected static String getHomeClassPattern()
    {
        HomeInterfaceSubTask homeintf_subtask = ((HomeInterfaceSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(HomeInterfaceSubTask.class)));

        if (homeintf_subtask != null) {
            return homeintf_subtask.getHomeClassPattern();
        }
        else {
            return HomeInterfaceSubTask.DEFAULT_HOMEINTERFACE_CLASS_PATTERN;
        }
    }

    public void setCurrentPermission(String permission)
    {
        currentPermission = permission;
    }

    /**
     * Returns the full qualified local or remote home interface name for the bean, depending on the value of type
     * parameter.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     * @doc.param                   name="type" optional="false" values="remote,local" description="Specifies the type
     *      of component home interface."
     */
    public String homeInterface(Properties attributes) throws XDocletException
    {
        String type = attributes.getProperty("type");

        type = type != null ? type : "remote";

        return getHomeInterface(type, getCurrentClass());
    }

    /**
     * Evaluates the body block if current method is a create method. Create methods should have ejb:create-method
     * defined.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @see                         #isCreateMethod(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" description="Traverse superclasses too. With
     *      false value used in remote/local home interface templates. Default is False."
     */
    public void ifIsCreateMethod(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        if (isCreateMethod(getCurrentMethod())) {
            boolean currentMethodDoesntBelongToCurrentClass = !getCurrentMethod().getContainingClass().equals(getCurrentClass());
            boolean shouldTraverse = shouldTraverseSuperclassForDependentClass(getCurrentMethod().getContainingClass(), "ejb:home");

            if (superclasses == false && currentMethodDoesntBelongToCurrentClass == true && shouldTraverse == false) {
                return;
            }

            generate(template);
        }
    }

    /**
     * Evaluates the body block if current create method's ejbPostCreate method does not exist.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifDoesntHavePostCreateMethod(String template, Properties attributes)
         throws XDocletException
    {
        XMethod currentMethod = getCurrentMethod();

        if (!isCreateMethod(currentMethod)) {
            String msg = Translator.getString(XDocletModulesEjbMessages.class,
                XDocletModulesEjbMessages.CURRENT_METHOD_NOT_CREATE,
                new String[]{currentMethod.toString()});

            throw new XDocletException(msg);
        }

        StringBuffer currentMethodName = new StringBuffer(currentMethod.getNameWithSignature(false));

        currentMethodName.insert(3, "Post");

        XMethod ejbPostCreateMethod = getCurrentClass().getMethod(currentMethodName.toString());

        if (ejbPostCreateMethod == null) {
            generate(template);
        }
    }

    /**
     * Returns the appropriate ejbPostCreate method name for the current ejbCreate method.
     *
     * @param attributes  The attributes of the template tag
     * @return            Description of the Returned Value
     * @doc.tag           type="content"
     */
    public String ejbPostCreateSignature(Properties attributes)
    {
        StringBuffer currentMethodName = new StringBuffer(getCurrentMethod().getName());

        currentMethodName.insert(3, "Post");
        return currentMethodName.toString();
    }

    /**
     * Evaluates the body block if current method is a home method. Home methods should have ejb:home-method defined.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @see                         #isHomeMethod(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" description="Traverse superclasses too. With
     *      false value used in remote/local home interface templates. Default is False."
     */
    public void ifIsHomeMethod(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        if (isHomeMethod(getCurrentMethod())) {
            if (superclasses == false && getCurrentMethod().getContainingClass() != getCurrentClass() && shouldTraverseSuperclassForDependentClass(getCurrentMethod().getContainingClass(), "ejb:home") == false) {
                return;
            }

            generate(template);
        }
    }

    /**
     * Evaluates the body block if current method is ejbRemove method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #isRemoveMethod(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     */
    public void ifNotRemoveMethod(String template) throws XDocletException
    {
        if (!isRemoveMethod(getCurrentMethod())) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if current method is a ejbFind method.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @see                         #isFinderMethod(xjavadoc.XMethod)
     * @doc.tag                     type="block"
     * @doc.param                   name="superclasses" optional="true" description="Traverse superclasses too. With
     *      false value used in remote/local home interface templates. Default is False."
     */
    public void ifIsFinderMethod(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        if (isFinderMethod(getCurrentMethod())) {
            boolean currentMethodDoesntBelongToCurrentClass = !getCurrentMethod().getContainingClass().equals(getCurrentClass());
            boolean shouldTraverse = shouldTraverseSuperclassForDependentClass(getCurrentMethod().getContainingClass(), "ejb:home");

            if (superclasses == false && currentMethodDoesntBelongToCurrentClass == true && shouldTraverse == false) {
                return;
            }

            generate(template);
        }
    }

    /**
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     * @doc.param                   name="prefixWithEjbSlash" optional="true" values="true,false" description="Specifies
     *      whether to prefix it with ejb/ or not. False by default."
     * @doc.param                   name="type" optional="false" values="remote,local" description="Specifies if we want
     *      the jndi name value for local or remote lookup."
     */
    public String compName(Properties attributes) throws XDocletException
    {
        String prefix_with_ejbslash_str = attributes.getProperty("prefixWithEjbSlash");
        boolean prefix_with_ejbslash = TypeConversionUtil.stringToBoolean(prefix_with_ejbslash_str, false);
        String type = attributes.getProperty("type");

        String ejb_name = getCompNameFor(getCurrentClass(), type);

        String compName;

        if (prefix_with_ejbslash == true) {
            compName = prefixWithEjbSlash(ejb_name);
        }
        else {
            compName = ejb_name;
        }

        return compName;
    }

    /**
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     * @doc.param                   name="type" optional="false" values="remote,local" description="Specifies if we want
     *      the jndi name value for local or remote lookup."
     */
    public String jndiName(Properties attributes) throws XDocletException
    {
        return getJndiNameOfTypeFor(attributes.getProperty("type"), getCurrentClass());
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
        String def_base_class_name = type.equals("remote") ? "javax.ejb.EJBHome" : "javax.ejb.EJBLocalHome";

        return extendsFromFor(getCurrentClass(), "ejb:home", type, extends_param_name, def_base_class_name);
    }

    /**
     * Iterates over all home methods of specified type (finder or create method) defined in a class and super classes.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="tagName" optional="false" description="The tag name for the method type to
     *      iterate over." values="ejb:finder,ejb:create-method"
     * @doc.param                   name="superclasses" values="true,false" description="If true then traverse
     *      superclasses also, otherwise look up the tag in current concrete class only."
     * @doc.param                   name="tagKey" description="A tag property that will be used as a unique key. This is
     *      used to avoid duplicate code due to similar tags in superclasses."
     */
    public void forAllHomeMethods(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(HomeTagsHandler.class, "forAllHomeMethods");
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), false);
        String type = attributes.getProperty("type");
        String tagType = attributes.getProperty("tagName");

        if (type == null) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.ATTRIBUTE_NOT_PRESENT_ERROR,
                new String[]{"type"}));
        }

        Set already = new HashSet();
        // Exclude definition coming from super classes
        XClass currentClass = getCurrentClass().getSuperclass();

        while (currentClass != null) {
            if (log.isDebugEnabled()) {
                log.debug("Looking for super definition in " + currentClass.getName());
            }

            // 1. METHOD tags
            Collection methods = currentClass.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                // search all this methods tags to ensure that the type of
                // Home we are generating matches the view-type (if available)
                //
                Collection tags =
                    method.getDoc().getTags(tagType, superclasses);

                if (!matchesViewType(tags, type)) {
                    continue;
                }

                if (tagType.equals("ejb:finder")) {
                    if (!isFinderMethod(method)) {
                        continue;
                    }
                }
                else if (tagType.equals("ejb:create-method")) {
                    if (!isCreateMethod(method)) {
                        continue;
                    }
                }

                String signature = getHomeDefinition(currentClass, method, tagType, type);

                if (log.isDebugEnabled()) {
                    log.debug("Found " + signature);
                }
                already.add(signature);
            }

            // 2. CLASS tags
            Collection superTags = currentClass.getDoc().getTags(tagType, true);

            for (Iterator i = superTags.iterator(); i.hasNext(); ) {
                XTag superTag = (XTag) i.next();

                // ensure that the type of Home we are generating matches the
                // view-type (if available)
                //
                if (!matchesViewType(superTag, type)) {
                    continue;
                }

                String signature = fullPackageChange(superTag.getAttributeValue("signature"));
                String typeMapping = superTag.getAttributeValue("result-type-mapping");

                if (typeMapping == null || typeMapping.equalsIgnoreCase(type)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Found " + signature);
                    }
                    already.add(signature);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        // 1. Handle METHOD Tag level ejb:finder
        Collection methods = getCurrentClass().getMethods();
        boolean fbpkFound = false;

        for (Iterator j = methods.iterator(); j.hasNext(); ) {
            XMethod method = (XMethod) j.next();

            // search all this methods tags to ensure that the type of Home
            // we are generating matches the view-type (if available)
            //
            Collection tags =
                method.getDoc().getTags(tagType, superclasses);

            if (!matchesViewType(tags, type)) {
                continue;
            }

            String signature = null;

            if (method.getDoc().getTag("ejb.permission") != null) {
                setCurrentPermission("@ejb.permission " + method.getDoc().getTag("ejb.permission").getValue());
            }

            if (log.isDebugEnabled()) {
                log.debug("Finder Method = " + signature);
            }

            if (tagType.equals("ejb:finder")) {
                if (!isFinderMethod(method)) {
                    continue;
                }
                signature = getHomeDefinition(getCurrentClass(), method, tagType, type);

                if (!already.add(signature)) {
                    continue;
                }

            }
            else if (tagType.equals("ejb:create-method")) {
                if (!isCreateMethod(method)) {
                    continue;
                }
                signature = getHomeDefinition(getCurrentClass(), method, tagType, type);

                if (!already.add(signature)) {
                    continue;
                }
            }
            if (signature != null) {
                setCurrentSignature(signature);

                Collection exceptions = method.getThrownExceptions();
                StringBuffer exc = new StringBuffer();

                boolean comma = false;

                for (Iterator k = exceptions.iterator(); k.hasNext(); ) {
                    XClass exception = (XClass) k.next();

                    // Skip EJBException
                    if (exception.getQualifiedName().equals("javax.ejb.EJBException"))
                        continue;

                    if (comma) {
                        exc.append(',');
                    }
                    exc.append(exception.getQualifiedName());
                    comma = true;
                }
                if (exc.length() == 0) {
                    if (tagType.equals("ejb:finder")) {
                        exc.append("javax.ejb.FinderException");
                    }
                    else if (tagType.equals("ejb:create-method")) {
                        exc.append("javax.ejb.CreateException");
                    }
                }
                if (type.equalsIgnoreCase("remote")) {
                    exc.append(",java.rmi.RemoteException");
                }
                setCurrentExceptions(exc.toString());
                // For javadoc comment only
                setCurrentMethod(method);

                // If custom findByPrimaryKey exists then we should not add the
                // mandatory later
                if (method.getName().equals("findByPrimaryKey")) {
                    fbpkFound = true;
                }

                generate(template);
            }
        }

        // 2. Handle CLASS Tag level ejb:finder
        Collection tags = getCurrentClass().getDoc().getTags(tagType, superclasses);

        for (Iterator i = tags.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();

            // ensure that the type of Home we are generating matches the
            // view-type (if available)
            //
            if (!matchesViewType(tag, type)) {
                continue;
            }

            String signature = finderSignatureFunger(tag.getAttributeValue("signature"), getCurrentClass(), type);
            String typeMapping = tag.getAttributeValue("result-type-mapping");

            if (typeMapping == null || typeMapping.equalsIgnoreCase(type)) {
                if (!already.add(signature)) {
                    continue;
                }

                if (log.isDebugEnabled()) {
                    log.debug("Finder Method = " + signature);
                }

                if (signature.indexOf("findByPrimaryKey") != -1) {
                    fbpkFound = true;
                }

                setCurrentClassTag(tag);
                setCurrentSignature(signature);

                if (tag.getAttributeValue("unchecked") != null) {
                    setCurrentPermission("@ejb.permission unchecked=\"true\"");
                }
                else if (tag.getAttributeValue("role-name") != null) {
                    setCurrentPermission("@ejb.permission role-name=\"" + tag.getAttributeValue("role-name") + "\"");
                }
                else {
                    // nothing found - nothing set....
                    setCurrentPermission("");
                }

                StringBuffer exc = new StringBuffer();

                exc.append("javax.ejb.FinderException");
                if (type.equalsIgnoreCase("remote")) {
                    exc.append(",java.rmi.RemoteException");
                }
                setCurrentExceptions(exc.toString());

                generate(template);
            }
        }

        // Add mandatory findByPrimaryKey if not already there
        if (!fbpkFound && CmpTagsHandler.isEntityCmp(getCurrentClass()) && tagType.equals("ejb:finder") && EntityTagsHandler.isAConcreteEJBean(getCurrentClass())) {
            StringBuffer fbpkSign = new StringBuffer(InterfaceTagsHandler.getComponentInterface(type, getCurrentClass()));

            fbpkSign.append(" findByPrimaryKey(");
            fbpkSign.append(PkTagsHandler.getPkClassFor(getCurrentClass())).append(" pk").append(")");
            if (already.add(fbpkSign)) {
                setCurrentSignature(fbpkSign.toString());

                StringBuffer exc = new StringBuffer();

                exc.append("javax.ejb.FinderException");
                if (type.equalsIgnoreCase("remote")) {
                    exc.append(",java.rmi.RemoteException");
                }

                setCurrentExceptions(exc.toString());
                setCurrentMethod(null);
                generate(template);
            }
        }

        // Add mandatory create() method for stateless beans if the bean is not abstract
        if (SessionTagsHandler.isSession(getCurrentClass()) && tagType.equals("ejb:create-method")) {
            if (already.size() == 0 && SessionTagsHandler.isAConcreteEJBean(getCurrentClass())) {
                StringBuffer createSign = new StringBuffer(InterfaceTagsHandler.getComponentInterface(type, getCurrentClass()));

                createSign.append(" create()");
                setCurrentSignature(createSign.toString());

                StringBuffer exc = new StringBuffer();

                exc.append("javax.ejb.CreateException");
                if (type.equalsIgnoreCase("remote")) {
                    exc.append(",java.rmi.RemoteException");
                }
                setCurrentExceptions(exc.toString());
                setCurrentMethod(null);
                generate(template);
            }
        }

        setCurrentClassTag(null);
        setCurrentMethodTag(null);
        setCurrentSignature(null);
        setCurrentExceptions(null);
        setCurrentMethod(null);

    }

    /**
     * Return signature of current home method.
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String currentSignature() throws XDocletException
    {
        return currentSignature;
    }

    /**
     * Return permission for current home method.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String currentPermission() throws XDocletException
    {
        return currentPermission;
    }

    /**
     * Return type of current home method.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String currentType() throws XDocletException
    {
        String sig = currentSignature();

        int index = sig.indexOf(" ");

        if (index >= 0) {
            return sig.substring(0, index);
        }
        throw new XDocletException("can not parse signature: " + sig);
    }

    /**
     * generates name for finder utility class backing current finder
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String finderClass() throws XDocletException
    {
        String sig = currentSignature();
        int start = sig.indexOf("find");
        int end = sig.indexOf("(");

        if (start >= 0 && end > start) {
            return sig.substring(start, start + 1).toUpperCase() + sig.substring(start + 1, end);
        }
        throw new XDocletException("Cannot parse signature: " + sig);
    }

    /**
     * Whether current finder return collection.
     *
     * @param template
     * @param param
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsCollectionType(String template, Properties param) throws XDocletException
    {
        String currentType = currentType();

        if ("Collection".equals(currentType) ||
            "java.util.Collection".equals(currentType) ||
            "Set".equals(currentType) ||
            "java.util.Set".equals(currentType)) {
            generate(template);
        }
    }

    /**
     * Whether current finder return enumeration.
     *
     * @param template
     * @param param
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsEnumerationType(String template, Properties param) throws XDocletException
    {
        String currentType = currentType();

        if ("Enumeration".equals(currentType) || "java.util.Enumeration".equals(currentType)) {
            generate(template);
        }
    }

    /**
     * Whether current finder return interface.
     *
     * @param template
     * @param param
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsInterfaceType(String template, Properties param) throws XDocletException
    {

        String type = currentType();

        if (type.equals(InterfaceTagsHandler.getComponentInterface("local", getCurrentClass())) ||
            type.equals(InterfaceTagsHandler.getComponentInterface("remote", getCurrentClass()))) {
            generate(template);
        }
    }

    /**
     * Return name of current home method.
     *
     * @return                      Describe the return value
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String currentMethod() throws XDocletException
    {
        String sig = currentSignature();
        int start = sig.indexOf(" ");
        int stop = sig.indexOf("(");

        if (start < 0 || stop < 0) {
            throw new XDocletException("Cannot parse signature: " + sig);
        }
        return sig.substring(start + 1, stop);
    }

    /**
     * Return definition of parameter list for current home method.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterListDefinition() throws XDocletException
    {
        String sig = currentSignature();
        int start = sig.indexOf("(");
        int stop = sig.indexOf(")");

        if (start < 0 || stop < 0) {
            throw new XDocletException("Cannot parse signature: " + sig);
        }
        return sig.substring(start + 1, stop);
    }

    /**
     * Return parameter list for current home method.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterList() throws XDocletException
    {

        String[] parameters = DocletUtil.tokenizeDelimitedToArray(parameterListDefinition(), " ,");
        StringBuffer sb = new StringBuffer();

        for (int i = 1; i < parameters.length; i += 2) {
            if (i > 1) {
                sb.append(", ");
            }
            sb.append(parameters[i]);
        }
        return sb.toString();
    }

    /**
     * Return exceptions for current home method.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String currentExceptions() throws XDocletException
    {
        return currentExceptions;
    }

    /**
     * Gets the DependentClassFor attribute of the HomeTagsHandler object
     *
     * @param clazz                 Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @return                      The DependentClassFor value
     * @exception XDocletException
     */
    protected String getDependentClassFor(XClass clazz, String type) throws XDocletException
    {
        if ((type.equals("local") && InterfaceTagsHandler.isLocalEjb(clazz)) || (type.equals("remote") && InterfaceTagsHandler.isRemoteEjb(clazz))) {
            return getHomeInterface(type, clazz);
        }
        else {
            return null;
        }
    }

    /**
     * Sets the CurrentSignature attribute of the HomeTagsHandler object.
     *
     * @param cs  The new CurrentSignature value
     */
    protected void setCurrentSignature(String cs)
    {
        this.currentSignature = cs;
    }

    /**
     * Sets the CurrentExceptions attribute of the HomeTagsHandler object.
     *
     * @param es  The new CurrentExceptions value
     */
    protected void setCurrentExceptions(String es)
    {
        this.currentExceptions = es;
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @param tag_name              Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean shouldTraverseSuperclassForDependentClass(XClass clazz, String tag_name) throws XDocletException
    {
        if (super.shouldTraverseSuperclassForDependentClass(clazz, tag_name) == false) {
            return false;
        }

        //shouldn't include create methods of parent if parent is itself a concrete ejb
        if (isAConcreteEJBean(clazz)) {
            return false;
        }

        return true;
    }

    protected boolean matchesViewType(XTag tag, String type)
    {
        String attr = tag.getAttributeValue("view-type");

        return attr == null || attr.length() == 0 || attr.equals(type) || attr.equals("both");
    }

    protected boolean matchesViewType(Collection tags, String type)
    {
        // search all the tags looking for the view-type parameter
        //
        boolean matches = true;

        for (Iterator k = tags.iterator(); k.hasNext() && matches; ) {
            matches = matchesViewType((XTag) k.next(), type);
        }
        return matches;
    }
}
