/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.DocletContext;
import xdoclet.DocletSupport;
import xdoclet.DocletTask;
import xdoclet.SubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.modules.ejb.entity.*;
import xdoclet.modules.ejb.home.HomeInterfaceSubTask;
import xdoclet.modules.ejb.home.LocalHomeInterfaceSubTask;
import xdoclet.modules.ejb.intf.LocalInterfaceSubTask;
import xdoclet.modules.ejb.intf.RemoteInterfaceSubTask;
import xdoclet.modules.ejb.mdb.MdbTagsHandler;
import xdoclet.modules.ejb.session.SessionSubTask;
import xdoclet.modules.ejb.session.SessionTagsHandler;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Ejb"
 * @version              $Revision: 1.23 $
 */
public class EjbTagsHandler extends XDocletTagSupport
{
    protected final static String LOCAL_SUFFIX = "Local";

    /**
     * Gets the AConcreteEJBean attribute of the EjbTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The AConcreteEJBean value
     * @exception XDocletException
     */
    public static boolean isAConcreteEJBean(XClass clazz) throws XDocletException
    {
        XTag beanTag = clazz.getDoc().getTag("ejb:bean");

        if (beanTag != null) {
            String generateStr = beanTag.getAttributeValue("generate");

            if (generateStr != null) {
                boolean generate = TypeConversionUtil.stringToBoolean(generateStr, true);

                // generate="true" specifically
                if (generate == true) {
                    return true;
                }
                else {
                    // generate="false" specifically
                    return false;
                }
            }

            // ejb:beam name specified, so it's a concrete ejb
            if (beanTag.getAttributeValue("name") != null) {
                return true;
            }
        }

        // now try to guess because it wasn't specifically specified whether
        // it should be generated or not
        SubTask subtask = getSubTaskClassForClass(clazz);

        if (clazz.isAbstract() == true) {
            if (hasANonDocletGeneratedSubClass(clazz) == true) {
                return false;
            }

            // an abstract mdb/etc?
            if (subtask == null) {
                return false;
            }

            // if <entitycmp/bmp/session/> is on, then do the best to guess correctly
            if (DocletContext.getInstance().isSubTaskDefined(subtask.getSubTaskName()) == true) {
                //none of the above guesses worked, assume it's concrete!
                return true;
            }
            else {
                // if <entitycmp/bmp/session/> is off, so if class is abstract then the bean is abstract except for entity cmp beans in ejb2 cmp2
                if (CmpTagsHandler.isEntityCmp(clazz) && CmpTagsHandler.isUsingCmp2Impl(clazz)) {
                    return true;
                }

                return false;
            }
        }
        else {
            // if <entitycmp/bmp/> is on, then it's an error or not specify the class abstract, except for <session/> that non-abstract is also legal
            if (subtask != null && DocletContext.getInstance().isSubTaskDefined(subtask.getSubTaskName())) {
                if (subtask.getSubTaskName().equals(DocletTask.getSubTaskName(SessionSubTask.class))) {
                    return true;
                }

                String currentClassName = clazz.getQualifiedName();

                throw new XDocletException(Translator.getString("xdoclet.modules.ejb.Messages", "class_not_abstract",
                    new String[]{currentClassName, DocletTask.getSubTaskName(SessionSubTask.class)}));
            }
            else {
                return true;
            }
        }
    }

    /**
     * Returns the EJB name of the clazz by seaching for ejb:bean's name parameter. If that is not found, it uses the
     * class' name minus any suffix from the list in the 'ejbClassNameSuffix' config parameter ("Bean,EJB,Ejb" by
     * default).
     *
     * @param clazz  The EJB bean class for which we want the EJB name
     * @return       The EjbName value
     * @see          #ejbName(java.util.Properties)
     */
    public static String getEjbNameFor(XClass clazz)
    {
        XTag beanTag = clazz.getDoc().getTag("ejb:bean");
        String paramValue = null;

        if (beanTag != null) {
            paramValue = beanTag.getAttributeValue("name");
        }

        if (paramValue == null) {
            String className = clazz.getName();

            // remove any suffix from ejbClassNameSuffix list
            String suffixlist = (String) getDocletContext().getConfigParam("ejbClassNameSuffix");
            StringTokenizer st = new StringTokenizer(suffixlist, ",");

            while (st.hasMoreTokens()) {
                String suffix = st.nextToken();

                if (className.endsWith(suffix)) {
                    int index = className.lastIndexOf(suffix);

                    className = className.substring(0, index);
                    break;
                }
            }

            return className;
        }

        return paramValue;
    }

    /**
     * Returns short version of the EJB name of the clazz.
     *
     * @param clazz  the class we want its short EJB name
     * @return       The shortEjbName value
     * @see          #shortEjbName()
     */
    public static String getShortEjbNameFor(XClass clazz)
    {
        Log log = LogUtil.getLog(EjbTagsHandler.class, "shortEjbName");

        // Find the last part of the name
        StringTokenizer ejbNameTokens = new StringTokenizer(getEjbNameFor(clazz), ":./\\-");
        String name;

        do {
            name = ejbNameTokens.nextToken();
        } while (ejbNameTokens.hasMoreTokens());

        if (log.isDebugEnabled()) {
            log.debug("Name=" + name);
        }

        return name;
    }

    /**
     * @param clazz  Description of Parameter
     * @return       a unique id for clazz
     */
    public static String getEjbIdFor(XClass clazz)
    {
        return getEjbNameFor(clazz).replace('/', '_');
    }

    /**
     * Returns the EJB specification version used. The generated files will be compatible with the version specified.
     *
     * @return   The Ejbspec value
     */
    public static String getEjbSpec()
    {
        return (String) getDocletContext().getConfigParam("EjbSpec");
    }

    public static boolean isLocalEjb(XClass clazz) throws XDocletException
    {
        return isViewtypeEjb(clazz, "local");
    }

    public static boolean isRemoteEjb(XClass clazz) throws XDocletException
    {
        return isViewtypeEjb(clazz, "remote");
    }

    /**
     * Returns true if clazz is only a local EJB by looking at ejb:bean's view-type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The OnlyLocalEjb value
     * @exception XDocletException
     */
    public static boolean isOnlyLocalEjb(XClass clazz) throws XDocletException
    {
        return isLocalEjb(clazz) && !isRemoteEjb(clazz);
    }

    /**
     * Returns true if clazz is only a remote EJB by looking at ejb:bean's view-type parameter.
     *
     * @param clazz                 Description of Parameter
     * @return                      The OnlyRemoteEjb value
     * @exception XDocletException
     */
    public static boolean isOnlyRemoteEjb(XClass clazz) throws XDocletException
    {
        return isRemoteEjb(clazz) && !isLocalEjb(clazz);
    }

    /**
     * Returns the class with the specified ejb name
     *
     * @param name
     * @return
     * @exception XDocletException
     */
    public static XClass getEjb(String name) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (name.equals(getEjbNameFor(clazz))) {
                return clazz;
            }
        }
        return null;
    }

    /**
     * Returns modified package name for a package name. If package name ends with one of the toReplace Strings, then
     * it's substituted by the replaceWith String. If packagePattern not null then it's roughly used.
     *
     * @param packageName     The name of the package name the new package name will be derived from
     * @param packagePattern  The package pattern to use. Can be null
     * @param subtask
     * @return                Description of the Returned Value
     * @todo                  this method is really an utility method that should be deprecated here and moved to
     *      PackageTagsHandler or even somewhere else
     */
    public static String choosePackage(String packageName, String packagePattern, String subtask)
    {
        Log log = LogUtil.getLog(EjbTagsHandler.class, "choosePackage");

        ArrayList packageSubstitutions = PackageTagsHandler.getPackageSubstitutions(subtask);

        if (log.isDebugEnabled()) {
            log.debug("Package name=" + packageName + " - Pattern=" + packagePattern);
        }

        if (packagePattern != null) {
            // later we may do some parametric {0} fancy stuff here
            return packagePattern;
        }
        else {
            for (int i = 0; i < packageSubstitutions.size(); i++) {
                PackageTagsHandler.PackageSubstitution ps = (PackageTagsHandler.PackageSubstitution) packageSubstitutions.get(i);
                StringTokenizer st = new StringTokenizer(ps.getPackages(), ",", false);

                if (ps.getUseFirst() == false) {
                    while (st.hasMoreTokens()) {
                        String packages = st.nextToken();
                        String suffix = "." + packages;

                        if (packageName.endsWith(suffix)) {
                            packageName = packageName.substring(0, packageName.length() - suffix.length()) + '.' + ps.getSubstituteWith();
                            break;
                        }
                    }
                }
                else {
                    packageName = PackageTagsHandler.replaceInline(packageName, ps.getPackages(), ps.getSubstituteWith());
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("packageName=" + packageName);
        }

        return packageName;
    }

    /**
     * Returns the name of EJB ref.
     *
     * @return                      The name of current EJB bean.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public static String ejbRefName() throws XDocletException
    {
        String ejbRefName = null;

        String refName = getCurrentClassTag().getAttributeValue("ref-name");

        if (refName != null) {
            ejbRefName = refName;
        }
        else {
            ejbRefName = prefixWithEjbSlash(getEjbNameFor(getCurrentClass()));

            String type = getCurrentClassTag().getAttributeValue("view-type");

            if (type != null && type.equals("local") && isLocalEjb(getCurrentClass()) && isRemoteEjb(getCurrentClass())) {
                ejbRefName = ejbRefName + LOCAL_SUFFIX;
            }
        }

        return ejbRefName;
    }

    /**
     * Replace &quot;.&quot; by &quot;/&quot; and add &quot;ejb/&quot; to the parameter.
     *
     * @param ejbName  The string to parse
     * @return         The parsed String
     */
    protected static String prefixWithEjbSlash(String ejbName)
    {
        ejbName = ejbName.replace('.', '/');
        if (ejbName.startsWith("ejb/")) {
            return ejbName;
        }
        else {
            return "ejb/" + ejbName;
        }
    }

    private static boolean isViewtypeEjb(XClass clazz, String viewtype) throws XDocletException
    {
        String value = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:bean",
            "view-type",
            "remote,local,both",
            null,
            true,
            false
            );

        if (value == null) {
            //default is both if ejb2, remote if ejb1.1
            return true;
        }
        else
            return value.indexOf(viewtype) != -1 || value.indexOf("both") != -1;
    }

    /**
     * Gets the SubTaskClassForClass attribute of the EjbTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The SubTaskClassForClass value
     * @exception XDocletException
     */
    private static SubTask getSubTaskClassForClass(XClass clazz) throws XDocletException
    {
        if (CmpTagsHandler.isEntityCmp(clazz)) {
            return DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityCmpSubTask.class));
        }
        else if (BmpTagsHandler.isEntityBmp(clazz)) {
            return DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(EntityBmpSubTask.class));
        }
        else if (SessionTagsHandler.isSession(clazz)) {
            return DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(SessionSubTask.class));
        }
        else {
            return null;
        }
    }

    /**
     * Describe what the method does
     *
     * @param currentClass  Describe what the parameter does
     * @return              Describe the return value
     */
    private static boolean hasANonDocletGeneratedSubClass(XClass currentClass)
    {
        // check if it's abstract and has a non-xdoclet-generated derived class
        String fullClassName = currentClass.getQualifiedName();
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (fullClassName.equals(clazz.getQualifiedName()) == false &&
                !clazz.getDoc().hasTag("xdoclet-generated") &&
                clazz.isA(fullClassName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the name of current EJB bean.
     *
     * @param attributes            The attributes of the template tag
     * @return                      The name of current EJB bean.
     * @exception XDocletException
     * @see                         #getEjbNameFor(xjavadoc.XClass)
     * @doc.tag                     type="content"
     * @doc.param                   name="prefixWithEjbSlash" optional="true" values="true,false" description="Specifies
     *      whether to prefix it with ejb/ or not. False by default."
     */
    public String ejbName(Properties attributes) throws XDocletException
    {
        String prefixWithEjbSlashStr = attributes.getProperty("prefixWithEjbSlash");
        boolean prefixWithEjbSlash = TypeConversionUtil.stringToBoolean(prefixWithEjbSlashStr, false);
        String ejbName = getEjbNameFor(getCurrentClass());

        if (prefixWithEjbSlash == true) {
            return prefixWithEjbSlash(ejbName);
        }
        else {
            return ejbName;
        }
    }

    /**
     * Returns the name of EJB ref.
     *
     * @return                      The name of current EJB bean.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String ejbExternalRefName() throws XDocletException
    {
        String ejbRefName = null;
        String refName = getCurrentClassTag().getAttributeValue("ref-name");

        if (refName != null) {
            ejbRefName = refName;
        }
        else {
            ejbRefName = prefixWithEjbSlash(getCurrentClassTag().getAttributeValue("ejb-name"));
        }

        return ejbRefName;
    }

    /**
     * Returns the symbolic name of the current class. For an EJBean it's the value of ejb:bean's name parameter.
     *
     * @return                      The symbolic name of the current class
     * @exception XDocletException
     * @see                         #shortEjbName()
     * @doc.tag                     type="content"
     */
    public String symbolicClassName() throws XDocletException
    {
        return shortEjbName();
    }

    /**
     * Returns short version of ejbName(). Example: "foo.bar.MyBean" ->"MyBean", "foo/bar/MyBean" ->"MyBean"
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @see                         #getShortEjbNameFor(xjavadoc.XClass)
     * @doc.tag                     type="content"
     */
    public String shortEjbName() throws XDocletException
    {
        return getShortEjbNameFor(getCurrentClass());
    }

    /**
     * Evaluates the body block for each EJBean derived from one of the three EJB types: EntityBean, SessionBean or
     * MessageDrivenBean.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.EntityTagsHandler#isEntity(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.session.SessionTagsHandler#isSession(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.mdb.MdbTagsHandler#isMessageDriven(xjavadoc.XClass)
     * @doc.tag                     type="block"
     */
    public void forAllBeans(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            if (DocletSupport.isDocletGenerated(getCurrentClass())) {
                continue;
            }

            if (EntityTagsHandler.isEntity(getCurrentClass()) || SessionTagsHandler.isSession(getCurrentClass()) ||
                MdbTagsHandler.isMessageDriven(getCurrentClass())) {
                generate(template);
            }
        }
    }

    /**
     * Evaluates the body block if current bean is a concrete bean meaning the generate parameter of ejb:bean is either
     * not specified or equals to "true", otherwise the bean is just an abstract base class bean not meant to be used as
     * a EJBean but serve as the base for other EJBeans.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifIsAConcreteEJBean(String template, Properties attributes) throws XDocletException
    {
        if (isAConcreteEJBean(getCurrentClass()) == true) {
            generate(template);
        }
    }

    /**
     * Returns Bean type : "Entity", "Session" or "Message Driven".
     *
     * @return                      "Entity", "Session" or "Message Driven".
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.EntityTagsHandler#isEntity(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.session.SessionTagsHandler#isSession(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.mdb.MdbTagsHandler#isMessageDriven(xjavadoc.XClass)
     * @doc.tag                     type="content"
     */
    public String beanType() throws XDocletException
    {
        if (EntityTagsHandler.isEntity(getCurrentClass())) {
            return "Entity";
        }
        else if (SessionTagsHandler.isSession(getCurrentClass())) {
            return "Session";
        }
        else if (MdbTagsHandler.isMessageDriven(getCurrentClass())) {
            return "Message Driven";
        }
        else {
            return "Unknown";
        }
    }

    /**
     * Returns the full-qualified name of the current class's concrete class. This is the class that is generated and is
     * derived from current class.
     *
     * @return                      The full-qualified name of the current class's concrete class
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.session.SessionTagsHandler#sessionClass()
     * @see                         xdoclet.modules.ejb.entity.BmpTagsHandler#entityBmpClass()
     * @see                         xdoclet.modules.ejb.entity.CmpTagsHandler#entityCmpClass()
     * @see                         xdoclet.modules.ejb.mdb.MdbTagsHandler#messageDrivenClass()
     * @doc.tag                     type="content"
     */
    public String concreteFullClassName() throws XDocletException
    {
        XTag beanTag = getCurrentClass().getDoc().getTag("ejb.bean");
        String paramValue = null;

        if (beanTag != null) {
            paramValue = beanTag.getAttributeValue("impl-class-name");
        }

        if (SessionTagsHandler.isSession(getCurrentClass())) {
            if (DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(SessionSubTask.class))) {
                return SessionTagsHandler.getSessionClassFor(getCurrentClass());
            }
            else {
                return (paramValue != null) ? paramValue : getCurrentClass().getQualifiedName();
            }
        }
        else if (BmpTagsHandler.isEntityBmp(getCurrentClass())) {
            if (DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(EntityBmpSubTask.class))) {
                return BmpTagsHandler.getEntityBmpClassFor(getCurrentClass());
            }
            else {
                return (paramValue != null) ? paramValue : getCurrentClass().getQualifiedName();
            }
        }
        else if (CmpTagsHandler.isEntityCmp(getCurrentClass())) {
            if (DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(EntityCmpSubTask.class))) {
                return CmpTagsHandler.getEntityCmpClassFor(getCurrentClass());
            }
            else {
                return (paramValue != null) ? paramValue : getCurrentClass().getQualifiedName();
            }
        }
        else if (MdbTagsHandler.isMessageDriven(getCurrentClass())) {
            return (paramValue != null) ? paramValue : MdbTagsHandler.getMessageDrivenClassFor(getCurrentClass());
        }
        else {
            return null;
        }
    }

    /**
     * Returns unique id for current ejb.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String id() throws XDocletException
    {
        return getEjbIdFor(getCurrentClass());
    }

    /**
     * @param template              Description of Parameter
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifLocalEjb(String template) throws XDocletException
    {
        if (isLocalEjb(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * @param template              Description of Parameter
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifRemoteEjb(String template) throws XDocletException
    {
        if (isRemoteEjb(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifNotLocalEjb(String template) throws XDocletException
    {
        if (!isLocalEjb(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifNotRemoteEjb(String template) throws XDocletException
    {
        if (!isRemoteEjb(getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Returns true of clazz is an EJB (derived from an EJB type), false otherwise.
     *
     * @param clazz                 Description of Parameter
     * @return                      The Ejb value
     * @exception XDocletException
     */
    protected boolean isEjb(XClass clazz) throws XDocletException
    {
        return clazz.isA("javax.ejb.SessionBean")
            || clazz.isA("javax.ejb.EntityBean")
            || clazz.isA("javax.ejb.MessageDrivenBean");
    }

    /**
     * sub-classes which deal with patternized class names return a reasonable value
     *
     * @param clazz                 the class
     * @param type                  type value used for view-type of remote/local
     * @return                      dependent class name for the class and type
     * @exception XDocletException
     */
    protected String getDependentClassFor(XClass clazz, String type) throws XDocletException
    {
        return null;
    }

    /**
     * Gets the DependentClassTagName attribute of the EjbTagsHandler object
     *
     * @return   The DependentClassTagName value
     */
    protected String getDependentClassTagName()
    {
        //it's too much dependency, we should find a better way
        if (getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(DataObjectSubTask.class))) {
            return "ejb:data-object";
        }
        else if (getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(EntityBmpSubTask.class)) ||
            getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(EntityCmpSubTask.class))) {
            return "ejb:bean";
        }
        else if (getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(RemoteInterfaceSubTask.class)) ||
            getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(LocalInterfaceSubTask.class))) {
            return "ejb:interface";
        }
        else if (getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(HomeInterfaceSubTask.class)) ||
            getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(LocalHomeInterfaceSubTask.class))) {
            return "ejb:interface";
        }
        else if (getDocletContext().getActiveSubTask().getSubTaskName().equals(DocletTask.getSubTaskName(EntityPkSubTask.class))) {
            return "ejb:pk";
        }
        else {
            return null;
        }
    }

    /**
     * Returns true if class/method denoted by doc has ejb:transaction tag, false otherwise.
     *
     * @param doc                   Description of Parameter
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    protected boolean hasTransaction(XDoc doc) throws XDocletException
    {
        return doc.hasTag("ejb:transaction");
    }

    /**
     * Returns the name of the class pk/etc class extends.
     *
     * @param clazz                 the class
     * @param tagName               name of the tag (ejb:bean for example, used for getting generate parameter)
     * @param type                  type value used for view type of remote/local
     * @param extendsParamName      extends parameter name (is "extends" for ejb:bean but is "local-extends" for local)
     * @param defaultBaseClassName  default base class name, returned when not deriving from another base class
     * @return                      correct value for the extends statement of a generated class
     * @exception XDocletException
     */
    protected String extendsFromFor(XClass clazz, String tagName, String type, String extendsParamName, String defaultBaseClassName) throws XDocletException
    {
        Log log = LogUtil.getLog(EjbTagsHandler.class, "extendsFromFor");

        log.debug("Looking " + type + " extendsFrom for class " + clazz.getName());

        // see ejb:pk/etc generate="?" in superclass
        XClass superclass = clazz.getSuperclass();

        boolean generateSuper;

        if (superclass.getDoc().hasTag(tagName)) {
            String generateSuperStr = getTagValue(
                FOR_CLASS,
                superclass.getDoc(),
                tagName,
                "generate",
                null,
                null,
                false,
                false
                );

            generateSuper = TypeConversionUtil.stringToBoolean(generateSuperStr, true);
        }
        else {
            // Two Cases : PersonBean and BaseEntityBean
            generateSuper = false;

            Collection interfaces = clazz.getSuperclass().getInterfaces();

            for (Iterator i = interfaces.iterator(); i.hasNext(); ) {
                XClass intf = (XClass) i.next();

                // if superclass is not javax.ejb.EntityBean then we have a superclass which
                // is itself deriving from javax.ejb.EntityBean
                if (intf.getQualifiedName().equals("javax.ejb.EntityBean") ||
                    intf.getQualifiedName().equals("javax.ejb.SessionBean") ||
                    intf.getQualifiedName().equals("javax.ejb.MessageDrivenBean")) {
                    //it derives from javax.ejb.*Bean and no superclass for pk/etc class is explicitly defined
                    generateSuper = true;
                }
            }
        }

        log.debug(clazz.getName() + " superclass is generatable? " + generateSuper);


        // note: look for ejb:pk/etc extends in superclasses also only if generate="false" in superclass
        // so extends attribute is inherited only if superclass's pk/etc is not to be generated
        String extendsValue = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            tagName,
            extendsParamName,
            null,
            null,
            !generateSuper,
            false
            );

        // if explicitly specified
        if (extendsValue != null) {
            log.debug(clazz.getName() + " contains an explicit extends. Returning " + extendsValue);
            return extendsValue;
        }
        else {
            // now try to guess if we are deriving from another ejb bean, and if so, then derive from
            // that bean's pk class too (if generate="true" for superclass's pk/etc class)
            log.debug(clazz.getName() + " does not contains an explicit extends. Trying to guess it");

            // java.lang.Object (the only that have no superclass)
            if (superclass.getSuperclass() == null) {
                log.debug("Top of class hierachy reached. Returning default extends: " + defaultBaseClassName);
                return defaultBaseClassName;
            }
            // if a superclass with generate="true"
            else if (generateSuper == true) {
                String className = getDependentClassFor(superclass, type);

                if (className != null) {
                    log.debug("Superclass " + superclass.getName() + " has a dependent class: " + className);
                    return className;
                }
                else {
                    log.debug("No dependent class for " + superclass.getName() + ". Returning default extends: " + defaultBaseClassName);
                    return defaultBaseClassName;
                }
            }
            else {
                // so we have a superclass with pk-generate="false", look at superclass of that superclass!
                log.debug("Can't guess now. Going deeper into class hierarchy");
                return extendsFromFor(superclass, tagName, type, extendsParamName, defaultBaseClassName);
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @param tagName               Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean shouldTraverseSuperclassForDependentClass(XClass clazz, String tagName) throws XDocletException
    {
        Log log = LogUtil.getLog(EjbTagsHandler.class, "shouldTraverseSuperclassForDependentClass");

        if (clazz.getQualifiedName().equals("java.lang.Object")) {
            log.debug("clazz = java.lang.Object");

            return true;
        }

        if (!clazz.isA("javax.ejb.EntityBean")
            && !clazz.isA("javax.ejb.SessionBean")) {
            log.debug(clazz.getQualifiedName() + " is _not_ of type javax.ejb.EntityBean,javax.ejb.SessionBean");

            return true;
        }
        else {
            log.debug(clazz.getQualifiedName() + " _is_ of type javax.ejb.EntityBean,javax.ejb.SessionBean");
        }

        // see ejb:bean generate="?" in superclass
        String generateBeanStr = getTagValue(
            FOR_CLASS,
            clazz.getDoc(),
            "ejb:bean",
            "generate",
            null,
            "true",
            false,
            false
            );

        boolean generateBean = TypeConversionUtil.stringToBoolean(generateBeanStr, true);

        if (generateBean == false) {
            log.debug("generateBean == false");

            return true;
        }

        boolean generate = false;

        if (tagName != null) {
            // see ejb:pk/etc generate="?" in superclass
            String generateStr = getTagValue(
                FOR_CLASS,
                clazz.getDoc(),
                tagName,
                "generate",
                null,
                "true",
                false,
                false
                );

            generate = TypeConversionUtil.stringToBoolean(generateStr, true);
        }

        if (generate == false) {
            log.debug("generate == false");

            return true;
        }
        else {
            log.debug("generate == true");

            return false;
        }
    }
}
