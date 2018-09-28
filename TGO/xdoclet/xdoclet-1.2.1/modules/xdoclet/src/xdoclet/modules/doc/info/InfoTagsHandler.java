/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc.info;

import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.modules.doc.XDocletModulesDocMessages;
import xdoclet.tagshandler.*;
import xdoclet.template.TemplateException;
import xdoclet.util.Translator;

/**
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Info"
 * @version              $Revision: 1.8 $
 */
public class InfoTagsHandler extends XDocletTagSupport
{
    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param properties            Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifTagCountNotZero(String template, Properties properties) throws XDocletException
    {
        String level = properties.getProperty("level");
        int tagCount = tagCount_Impl(level);

        if (tagCount != 0) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param properties            Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String tagCount(Properties properties) throws XDocletException
    {
        String level = properties.getProperty("level");
        int tagCount = tagCount_Impl(level);

        return String.valueOf(tagCount);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String classTagValue() throws XDocletException
    {
        return getClassTagsHandler().classTagValue(getProperties());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String fieldTagValue() throws XDocletException
    {
        return getFieldTagsHandler().fieldTagValue(getProperties());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String constructorTagValue() throws XDocletException
    {
        return getConstructorTagsHandler().constructorTagValue(getProperties());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String methodTagValue() throws XDocletException
    {
        return getMethodTagsHandler().methodTagValue(getProperties());
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllClassTags(String template) throws XDocletException
    {
        getClassTagsHandler().forAllClassTags(template, getProperties());
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllConstructorTags(String template) throws XDocletException
    {
        getConstructorTagsHandler().forAllConstructorTags(template, getProperties());
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllMethodTags(String template) throws XDocletException
    {
        getMethodTagsHandler().forAllMethodTags(template, getProperties());
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllFieldTags(String template) throws XDocletException
    {
        getFieldTagsHandler().forAllFieldTags(template, getProperties());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String projectname() throws XDocletException
    {
        InfoSubTask infoSubTask = (InfoSubTask) getDocletContext().getActiveSubTask();

        return infoSubTask.getProjectname();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String rootlink() throws XDocletException
    {
        String packageName = "";

        if (getPackageTagsHandler().packageName() != null) {
            packageName = getPackageTagsHandler().packageName();
        }

        StringTokenizer st = new StringTokenizer(packageName, ".");
        int n = st.countTokens();
        StringBuffer sbuf = new StringBuffer();

        for (int i = 0; i < n; i++) {
            sbuf.append("../");
        }
        return sbuf.toString();
    }

    /**
     * Gets the Properties attribute of the InfoTagsHandler object
     *
     * @return   The Properties value
     */
    private Properties getProperties()
    {
        Properties properties = ((InfoSubTask) getDocletContext().getActiveSubTask()).getProperties();

        return properties;
    }

    /**
     * Gets the MethodTagsHandler attribute of the InfoTagsHandler object
     *
     * @return                      The MethodTagsHandler value
     * @exception XDocletException  Describe the exception
     */
    private MethodTagsHandler getMethodTagsHandler() throws XDocletException
    {
        try {
            return ((MethodTagsHandler) getEngine().getTagHandlerFor("Method"));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.TAGSHANDLER_NOT_FOUND, new String[]{"MethodTagsHandler", "Method"}));
        }
    }

    /**
     * Gets the FieldTagsHandler attribute of the InfoTagsHandler object
     *
     * @return                      The FieldTagsHandler value
     * @exception XDocletException  Describe the exception
     */
    private FieldTagsHandler getFieldTagsHandler() throws XDocletException
    {
        try {
            return ((FieldTagsHandler) getEngine().getTagHandlerFor("Field"));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.TAGSHANDLER_NOT_FOUND, new String[]{"FieldTagsHandler", "Field"}));
        }
    }

    /**
     * Gets the ConstructorTagsHandler attribute of the InfoTagsHandler object
     *
     * @return                      The ConstructorTagsHandler value
     * @exception XDocletException  Describe the exception
     */
    private ConstructorTagsHandler getConstructorTagsHandler() throws XDocletException
    {
        try {
            return ((ConstructorTagsHandler) getEngine().getTagHandlerFor("Constructor"));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.TAGSHANDLER_NOT_FOUND, new String[]{"ConstructorTagsHandler", "Constructor"}));
        }
    }

    /**
     * Gets the ClassTagsHandler attribute of the InfoTagsHandler object
     *
     * @return                      The ClassTagsHandler value
     * @exception XDocletException  Describe the exception
     */
    private ClassTagsHandler getClassTagsHandler() throws XDocletException
    {
        try {
            return ((ClassTagsHandler) getEngine().getTagHandlerFor("Class"));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.TAGSHANDLER_NOT_FOUND, new String[]{"ClassTagsHandler", "Class"}));
        }
    }

    /**
     * Gets the PackageTagsHandler attribute of the InfoTagsHandler object
     *
     * @return                      The PackageTagsHandler value
     * @exception XDocletException  Describe the exception
     */
    private PackageTagsHandler getPackageTagsHandler() throws XDocletException
    {
        try {
            return ((PackageTagsHandler) getEngine().getTagHandlerFor("Package"));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.TAGSHANDLER_NOT_FOUND, new String[]{"PackageTagsHandler", "Package"}));
        }
    }

    /**
     * Describe what the method does
     *
     * @param level                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    private int tagCount_Impl(String level) throws XDocletException
    {
        int tagCount = 0;

        if ("all".equals(level)) {
            tagCount = tagCountInAll_Impl(getProperties());
        }
        else if ("package".equals(level)) {
            tagCount = tagCountInPackage_Impl(getProperties(), getCurrentPackage());
        }
        else if ("whole-class".equals(level)) {
            tagCount = tagCountInClass_Impl(getProperties(), getCurrentClass(), true, true, true, true);
        }
        else if ("class".equals(level)) {
            tagCount = tagCountInClass_Impl(getProperties(), getCurrentClass(), true, false, false, false);
        }
        else if ("field".equals(level)) {
            tagCount = tagCountInClass_Impl(getProperties(), getCurrentClass(), false, true, false, false);
        }
        else if ("constructor".equals(level)) {
            tagCount = tagCountInClass_Impl(getProperties(), getCurrentClass(), false, false, true, false);
        }
        else if ("method".equals(level)) {
            tagCount = tagCountInClass_Impl(getProperties(), getCurrentClass(), false, false, false, true);
        }
        else {
            throw new XDocletException(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.BAD_LEVEL, new String[]{level}));
        }
        return tagCount;
    }

    /**
     * Describe what the method does
     *
     * @param attributes            Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    private int tagCountInAll_Impl(Properties attributes) throws XDocletException
    {
        int tagCount = 0;
        Collection classes = getXJavaDoc().getSourceClasses();
        SortedSet packages = new TreeSet();

        for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
            packages.add(i.next().getContainingPackage());
        }

        for (PackageIterator packageIterator = XCollections.packageIterator(packages); packageIterator.hasNext(); ) {
            XPackage cur_package = packageIterator.next();

            tagCount += tagCountInPackage_Impl(attributes, cur_package);
        }

        return tagCount;
    }

    /**
     * Describe what the method does
     *
     * @param attributes  Describe what the parameter does
     * @param pakkage     Describe what the parameter does
     * @return            Describe the return value
     */
    private int tagCountInPackage_Impl(Properties attributes, XPackage pakkage)
    {
        int tagCount = 0;
        Collection classes = pakkage.getClasses();

        for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
            tagCount += tagCountInClass_Impl(attributes, i.next(), true, true, true, true);
        }
        return tagCount;
    }

    /**
     * Describe what the method does
     *
     * @param attributes         Describe what the parameter does
     * @param clazz              Describe what the parameter does
     * @param countClass         Describe what the parameter does
     * @param countFields        Describe what the parameter does
     * @param countConstructors  Describe what the parameter does
     * @param countMethods       Describe what the parameter does
     * @return                   Describe the return value
     */
    private int tagCountInClass_Impl(Properties attributes, XClass clazz, boolean countClass, boolean countFields, boolean countConstructors, boolean countMethods)
    {
        int tagCount = 0;
        String tagName = attributes.getProperty("tagName");

        if (countClass) {
            tagCount += tagCount(clazz, tagName);
        }
        if (countConstructors) {
            Collection constructors = clazz.getConstructors();

            tagCount += tagCount(constructors, tagName);
        }
        if (countMethods) {
            Collection methods = clazz.getMethods();

            tagCount += tagCount(methods, tagName);
        }
        if (countFields) {
            Collection fields = clazz.getFields();

            tagCount += tagCount(fields, tagName);
        }
        return tagCount;
    }

    /**
     * Describe what the method does
     *
     * @param programElement  Describe what the parameter does
     * @param tagName         Describe what the parameter does
     * @return                Describe the return value
     */
    private int tagCount(XProgramElement programElement, String tagName)
    {
        return programElement.getDoc().getTags(tagName, false).size();
    }

    /**
     * Describe what the method does
     *
     * @param programElements  Describe what the parameter does
     * @param tagName          Describe what the parameter does
     * @return                 Describe the return value
     */
    private int tagCount(Collection programElements, String tagName)
    {
        int count = 0;

        for (ProgramElementIterator i = XCollections.programElementIterator(programElements); i.hasNext(); ) {
            count += tagCount(i.next(), tagName);
        }
        return count;
    }
}
