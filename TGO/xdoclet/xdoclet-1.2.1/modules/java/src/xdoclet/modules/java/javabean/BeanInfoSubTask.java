/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.java.javabean;


import xjavadoc.XClass;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.Translator;
/**
 * Generate the BeanInfo class for a given JavaBean class, and an optional messages properties bundle to go with it.
 *
 * @author        Laurent Etiemble (letiemble@users.sourceforge.net)
 * @created       June 20, 2002
 * @version       $Revision: 1.6 $
 * @ant.element   name="beaninfo" display-name="BeanInfo Class" parent="xdoclet.DocletTask"
 */
public class BeanInfoSubTask extends TemplateSubTask
{
    /**
     * Pattern for generation of BeanInfo files
     */
    public static String GENERATED_BEANINFO_FILE_NAME = "{0}BeanInfo.java";
    /**
     * Pattern for generation of BeanInfo files
     */
    public static String GENERATED_BEANINFO_CLASS_NAME = "{0}BeanInfo";
    /**
     * Default template to use for BeanInfo files
     */
    private static String DEFAULT_BEANINFO_TEMPLATE_FILE = "resources/beaninfo.xdt";
    /**
     * Default template to use for i18n properties files
     */
    private static String DEFAULT_PROPERTIES_TEMPLATE_FILE = "resources/i18n.xdt";
    /**
     * Pattern for generation of i18n properties files
     */
    private static String GENERATED_PROPERTIES_FILE_NAME = "{0}BeanInfo.properties";
    /**
     * Is the BeanInfo class built for I18N
     */
    protected boolean i18nGeneration = false;

    /**
     * Constructor for the BeanInfoSubTask object
     */
    public BeanInfoSubTask()
    {
        setHavingClassTag("javabean.class");
        setTemplateURL(getClass().getResource(DEFAULT_BEANINFO_TEMPLATE_FILE));
        setDestinationFile(GENERATED_BEANINFO_FILE_NAME);
    }

    /**
     * Get whether or not a I18N resource bundle will be generated, and the BeanInfo classes will use it
     *
     * @return   true if I18N is supported
     */
    public boolean isI18n()
    {
        return i18nGeneration;
    }

    /**
     * Set whether or not a I18N resource bundle will be generated, and the BeanInfo classes will use it
     *
     * @param value  true to support I18N
     */
    public void setI18n(boolean value)
    {
        i18nGeneration = value;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // JavaBeans does not require a template url or a destination file
        //
        // super.validateOptions();
    }

    /**
     * @exception XDocletException  Description of Exception
     */
    public void execute() throws XDocletException
    {
        startProcess();

        if (isI18n()) {
            setTemplateURL(getClass().getResource(DEFAULT_PROPERTIES_TEMPLATE_FILE));
            setDestinationFile(GENERATED_PROPERTIES_FILE_NAME);
            startProcess();
        }
    }


    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        if (getDestinationFile().equals(GENERATED_BEANINFO_FILE_NAME)) {
            return PackageTagsHandler.packageNameAsPathFor(JavaBeanTagsHandler.getBeanInfoClassFor(clazz)) + ".java";
        }
        return super.getGeneratedFileName(clazz);
    }

    /**
     * Called when the engine is started
     *
     * @exception XDocletException  Thrown in case of problem
     */
    protected void engineStarted() throws XDocletException
    {
        if (getDestinationFile().equals(GENERATED_BEANINFO_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletModulesJavabeanMessages.class,
                XDocletModulesJavabeanMessages.GENERATING_BEANINFO_FOR,
                new String[]{getCurrentClass().getQualifiedName()}));
        }
        else if (getDestinationFile().equals(GENERATED_PROPERTIES_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletModulesJavabeanMessages.class,
                XDocletModulesJavabeanMessages.GENERATING_PROPERTIES_FOR,
                new String[]{getCurrentClass().getQualifiedName()}));
        }
    }
}
