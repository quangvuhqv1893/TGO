/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.hibernate;

import java.text.MessageFormat;

import xjavadoc.XClass;

import xdoclet.XDocletException;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * This task generates Hibernate xml mapping file for a given class. Supports Hibernate 1.1 &amp; 2.0.
 *
 * @author               Sébastien Guimont (sebastieng@sympatico.ca)
 * @created              August 9th, 2002
 * @version              $Revision: 1.10 $
 * @ant.element          name="hibernate" display-name="Hibernate Mapping File"
 *      parent="xdoclet.modules.hibernate.HibernateDocletTask"
 * @xdoclet.merge-file   file="hibernate-properties.xml" relates-to="{0}.hbm.xml" description="An XML unparsed entity
 *      containing additional property mappings for all classes."
 * @xdoclet.merge-file   file="hibernate-properties-{0}.xml" relates-to="{0}.hbm.xml" description="An XML unparsed
 *      entity containing additional property mappings for a class."
 */
public class HibernateSubTask
     extends XmlSubTask
{
    public final static String DEFAULT_HIBERNATE_CLASS_PATTERN = "{0}";

    //~ Instance/static variables ......................................................................................

    private final static String HIBERNATE_PUBLICID_11 = "-//Hibernate/Hibernate Mapping DTD 1.1//EN";

    private final static String HIBERNATE_SYSTEMID_11 = "http://hibernate.sourceforge.net/hibernate-mapping-1.1.dtd";

    private final static String DTD_FILE_NAME_11 = "resources/hibernate-mapping_1_1.dtd";

    private final static String HIBERNATE_PUBLICID_20 = "-//Hibernate/Hibernate Mapping DTD 2.0//EN";

    private final static String HIBERNATE_SYSTEMID_20 = "http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd";

    private final static String DTD_FILE_NAME_20 = "resources/hibernate-mapping_2_0.dtd";

    /**
     * Default template to use for hibernate files.
     */
    private static String DEFAULT_HIBERNATE_TEMPLATE_FILE = "resources/hibernate.xdt";

    /**
     * Pattern for generation of hibernate files.
     */
    private static String GENERATED_HIBERNATE_FILE_NAME = "{0}.hbm.xml";

    /**
     * Defaults to Hibernate 1.1.
     */
    private String  hibernateVersion = HibernateVersion.HIBERNATE_1_1;

    //~ Constructors ...................................................................................................

    /**
     * Constructor for the HibernateSubTask object.
     */
    public HibernateSubTask()
    {
        setHavingClassTag("hibernate.class");
        setTemplateURL(getClass().getResource(DEFAULT_HIBERNATE_TEMPLATE_FILE));
        setDestinationFile(GENERATED_HIBERNATE_FILE_NAME);
    }

    public String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return super.getGeneratedFileName(clazz);
    }

    public String getMappingURL(XClass clazz)
    {
        return MessageFormat.format(getDestinationFile(), new Object[]{clazz.getTransformedQualifiedName().replace('.', '/'), ""});
    }

    /**
     * Get the Hibernate version.
     *
     * @return
     */
    public String getVersion()
    {
        return hibernateVersion;
    }

    /**
     * Sets the hibernate version to use. Legal values are "1.1" and "2.0".
     *
     * @param version
     * @ant.not-required   No. Default is "1.1".
     */
    public void setVersion(HibernateVersion version)
    {
        hibernateVersion = version.getValue();
    }

    /**
     * Generate Mapping file (*.hbm.xml).
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {

        if (hibernateVersion.equals(HibernateVersion.HIBERNATE_1_1)) {
            setPublicId(HIBERNATE_PUBLICID_11);
            setSystemId(HIBERNATE_SYSTEMID_11);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_11));
        }
        else {
            setPublicId(HIBERNATE_PUBLICID_20);
            setSystemId(HIBERNATE_SYSTEMID_20);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_20));
        }
        startProcess();
    }

    //~ Methods ........................................................................................................

    /**
     * Called when the engine is started
     *
     * @exception XDocletException  Thrown in case of problem
     */
    protected void engineStarted()
         throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesHibernateMessages.class,
            XDocletModulesHibernateMessages.GENERATING_HIBERNATE_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

    /**
     * Since we want to support static inner classes, we set this to true.
     *
     * @return   <code>true</code>
     */
    protected boolean processInnerClasses()
    {
        return true;
    }

    /**
     * @author    <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
     * @created   February 23, 2003
     */
    public static class HibernateVersion extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String HIBERNATE_1_1 = "1.1";
        public final static String HIBERNATE_2_0 = "2.0";

        /**
         * Gets the Values attribute of the HibernateVersion object.
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                HIBERNATE_1_1, HIBERNATE_2_0
                });
        }
    }
}
