/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.ejb;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.EjbDocletTask;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * Creates jboss.xml, jaws.xml and/or jbosscmp-jdbc.xml deployment descriptors for JBoss.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              Oct 15, 2001
 * @ant.element          display-name="JBoss" name="jboss" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.29 $
 * @xdoclet.merge-file   file="jboss-security.xml" relates-to="jboss.xml" description="An XML unparsed entity containing
 *      the optional enforce-ejb-restrictions, security-domain and/or unauthenticated-principal elements for jboss.xml"
 * @xdoclet.merge-file   file="jboss-beans.xml" relates-to="jboss.xml" description="An XML unparsed entity containing
 *      the session, entity and message-driven elements for beans you wish to include which aren't processed by
 *      XDoclet."
 * @xdoclet.merge-file   file="jboss-{0}.xml" relates-to="jboss.xml" description="An XML document containing the
 *      session, entity or message-driven element for a bean, to be used instead of generating it from the bean's tags."
 * @xdoclet.merge-file   file="jboss-resource-managers.xml" relates-to="jboss.xml" description="An XML document
 *      containing the resource-managers element, to use instead of generating it from jboss.resource-manager tags."
 * @xdoclet.merge-file   file="jboss-container.xml" relates-to="jboss.xml" description="An XML document containing the
 *      optional container-configurations element for jboss.xml"
 * @xdoclet.merge-file   file="jbosscmp-jdbc-defaults.xml" relates-to="jbosscmp-jdbc.xml" description="An XML document
 *      containing the defaults element for jbosscmp-jdbc.xml"
 * @xdoclet.merge-file   file="jbosscmp-jdbc-beans.xml" relates-to="jbosscmp-jdbc.xml" description="An XML unparsed
 *      entity containing entity elements for any beans you wish to include which aren't processed by XDoclet."
 * @xdoclet.merge-file   file="jbosscmp-jdbc-db-settings-{0}.xml" relates-to="jbosscmp-jdbc.xml" description="An XML
 *      unparsed entity containing various database settings for a bean. The contents should consist of (ejb-name,
 *      (datasource, datasource-mapping)?, create-table?, remove-table?, read-only?, read-time-out?, row-locking?,
 *      pk-constraint?, read-ahead?, list-cache-max?, fetch-size?, table-name?) elements."
 * @xdoclet.merge-file   file="jbosscmp-jdbc-{0}.xml" relates-to="jbosscmp-jdbc.xml" description="An XML unparsed entity
 *      containing various other settings for a bean. Should consist of the (cmp-field*, load-groups?,
 *      eager-load-group?, lazy-load-groups?, query*) elements."
 * @xdoclet.merge-file   file="jbosscmp-jdbc-dvc.xml" relates-to="jbosscmp-jdbc.xml" description="An XML unparsed entity
 *      containing any additional dependent-value-class elements not generated from jboss.dvc tags."
 * @xdoclet.merge-file   file="jbosscmp-jdbc-typemappings.xml" relates-to="jbosscmp-jdbc.xml" description="An XML
 *      document containing the optional type-mappings element."
 * @xdoclet.merge-file   file="jbosscmp-jdbc-entity-commands.xml" relates-to="jbosscmp-jdbc.xml" description="An XML
 *      document containing the optional entity-commands element."
 * @xdoclet.merge-file   file="jaws-db-settings-{0}.xml" relates-to="jboss-jaws.xml" description="An XML unparsed entity
 *      containing various database settings for a bean. The contents should consist of (ejb-name , datasource?)
 *      elements."
 * @xdoclet.merge-file   file="jaws-{0}.xml" relates-to="jboss-jaws.xml" description="An XML unparsed entity containing
 *      (cmp-field* , finder*) elements for a bean."
 * @xdoclet.merge-file   file="jaws-db-more-settings-{0}.xml" relates-to="jboss-jaws.xml" description="An XML unparsed
 *      entity containing various database settings for a bean. The contents should consist of (read-only? , table-name?
 *      , tuned-updates? , create-table? , remove-table? , row-locking? , time-out? , pk-constraint?) elements."
 */
public class JBossSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String DEFAULT_JBOSS_XML_TEMPLATE_FILE = "resources/jboss_xml.xdt";

    private final static String JBOSS_XML_FILE_NAME = "jboss.xml";

    private final static String DEFAULT_JAWS_XML_TEMPLATE_FILE = "resources/jboss-jaws_xml.xdt";

    private final static String JAWS_XML_FILE_NAME = "jaws.xml";

    private final static String DEFAULT_JBOSSCMP_TEMPLATE_FILE = "resources/jbosscmp-jdbc_xml.xdt";

    private final static String JBOSSCMP_DD_FILE_NAME = "jbosscmp-jdbc.xml";

    private final static String JBOSS_DD_PUBLICID_24 = "-//JBoss//DTD JBOSS 2.4//EN";

    private final static String JBOSS_DD_SYSTEMID_24 = "http://www.jboss.org/j2ee/dtd/jboss_2_4.dtd";

    private final static String JBOSS_DTD_FILE_NAME_24 = "resources/jboss_2_4.dtd";

    private final static String JBOSS_DD_PUBLICID_30 = "-//JBoss//DTD JBOSS 3.0//EN";

    private final static String JBOSS_DD_SYSTEMID_30 = "http://www.jboss.org/j2ee/dtd/jboss_3_0.dtd";

    private final static String JBOSS_DTD_FILE_NAME_30 = "resources/jboss_3_0.dtd";

    private final static String JBOSS_DD_PUBLICID_32 = "-//JBoss//DTD JBOSS 3.2//EN";

    private final static String JBOSS_DD_SYSTEMID_32 = "http://www.jboss.org/j2ee/dtd/jboss_3_2.dtd";

    private final static String JBOSS_DTD_FILE_NAME_32 = "resources/jboss_3_2.dtd";

    private final static String JAWS_DD_PUBLICID_24 = "-//JBoss//DTD JAWS 2.4//EN";

    private final static String JAWS_DD_SYSTEMID_24 = "http://www.jboss.org/j2ee/dtd/jaws_2_4.dtd";

    private final static String JAWS_DTD_FILE_NAME_24 = "resources/jaws_2_4.dtd";

    private final static String JAWS_DD_PUBLICID_30 = "-//JBoss//DTD JAWS 3.0//EN";

    private final static String JAWS_DD_SYSTEMID_30 = "http://www.jboss.org/j2ee/dtd/jaws_3_0.dtd";

    private final static String JAWS_DTD_FILE_NAME_30 = "resources/jaws_3_0.dtd";

    private final static String JBOSSCMP_DD_PUBLICID_30 = "-//JBoss//DTD JBOSSCMP-JDBC 3.0//EN";

    private final static String JBOSSCMP_DD_SYSTEMID_30 = "http://www.jboss.org/j2ee/dtd/jbosscmp-jdbc_3_0.dtd";

    private final static String JBOSSCMP_DTD_FILE_NAME_30 = "resources/jbosscmp-jdbc_3_0.dtd";

    private final static String JBOSSCMP_DD_PUBLICID_32 = "-//JBoss//DTD JBOSSCMP-JDBC 3.2//EN";

    private final static String JBOSSCMP_DD_SYSTEMID_32 = "http://www.jboss.org/j2ee/dtd/jbosscmp-jdbc_3_2.dtd";

    private final static String JBOSSCMP_DTD_FILE_NAME_32 = "resources/jbosscmp-jdbc_3_2.dtd";

    private final static String JBOSSCMP_DD_PUBLICID_40 = "-//JBoss//DTD JBOSSCMP-JDBC 4.0//EN";

    private final static String JBOSSCMP_DD_SYSTEMID_40 = "http://www.jboss.org/j2ee/dtd/jbosscmp-jdbc_4_0.dtd";

    private final static String JBOSSCMP_DTD_FILE_NAME_40 = "resources/jbosscmp-jdbc_4_0.dtd";

    private String  version = JBossVersionTypes.VERSION_2_4;

    private String  datasource;

    private String  datasourceMapping;

    private String  generateRelations = "false";

    private String  preferredRelationMapping;

    private String  createTable;

    private String  removeTable;

    private String  securityDomain = "";

    private String  unauthenticatedPrincipal = "";

    private String  debug = "false";

    private URL     jbossTemplateURL = null;

    private URL     jawsTemplateURL = null;

    private URL     jbosscmpTemplateURL = null;

    /**
     * Gets the template URL for jboss.xml.
     *
     * @return   the jboss.xml template URL
     */
    public URL getJbossTemplateURL()
    {
        return jbossTemplateURL;
    }

    /**
     * Gets the template URL for jaws.xml .
     *
     * @return   the jaws.xml template URL
     */
    public URL getJawsTemplateURL()
    {
        return jawsTemplateURL;
    }

    /**
     * Gets the template URL for jbosscmp-jdbc.xml.
     *
     * @return   the jbosscmp-jdbc.xml template URL
     */
    public URL getJbosscmpTemplateURL()
    {
        return jbosscmpTemplateURL;
    }

    /**
     * Gets the Debug attribute of the JBossSubTask object
     *
     * @return   The Debug value
     */
    public String getDebug()
    {
        return debug;
    }

    /**
     * Gets the Version attribute of the JBossSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Gets the SecurityDomain attribute of the JBossSubTask object
     *
     * @return   The SecurityDomain value
     */
    public String getSecurityDomain()
    {
        return securityDomain;
    }

    /**
     * Gets the unauthenticated principal name to use.
     *
     * @return   The unauthenticated principal name
     */
    public String getUnauthenticatedPrincipal()
    {
        return unauthenticatedPrincipal;
    }

    /**
     * Gets the Datasource attribute of the JBossSubTask object
     *
     * @return   The Datasource value
     */
    public String getDatasource()
    {
        return datasource;
    }

    /**
     * Gets the DatasourceMapping attribute of the JBossSubTask object
     *
     * @return   The DatasourceMapping value
     */
    public String getDatasourceMapping()
    {
        return datasourceMapping;
    }

    /**
     * Gets the Typemapping attribute of the JBossSubTask object
     *
     * @return   The Typemapping value
     */
    public String getTypemapping()
    {
        return datasourceMapping;
    }

    /**
     * Gets the GenerateRelations attribute of the JBossSubTask object
     *
     * @return   The GenerateRelations value
     */
    public String getGenerateRelations()
    {
        return generateRelations;
    }

    /**
     * Gets the preferredRelationMapping attribute of the JBossSubTask object.
     *
     * @return   The preferredRelationMapping value
     */
    public String getPreferredRelationMapping()
    {
        return preferredRelationMapping;
    }

    /**
     * Gets the createTable attribute of the JBossSubTask object.
     *
     * @return   The createTable value
     */
    public String getCreateTable()
    {
        return createTable;
    }

    /**
     * Gets the createTable attribute of the JBossSubTask object.
     *
     * @return   The removeTable value
     */
    public String getRemoveTable()
    {
        return removeTable;
    }

    /**
     * Sets the template file for jboss.xml.
     *
     * @param jbossTemplateFile     the jboss.xml file
     * @exception XDocletException
     */
    public void setJbossTemplateFile(File jbossTemplateFile) throws XDocletException
    {
        this.jbossTemplateURL = toURL(jbossTemplateFile);
    }

    /**
     * Sets the template file for jaws.xml.
     *
     * @param jawsTemplateFile      the jaws.xml file
     * @exception XDocletException
     */
    public void setJawsTemplateFile(File jawsTemplateFile) throws XDocletException
    {
        this.jawsTemplateURL = toURL(jawsTemplateFile);
    }

    /**
     * Sets the template file for jbosscmp-jdbc.xml.
     *
     * @param jbosscmpTemplateFile  the jbosscmp-jdbc.xml file
     * @exception XDocletException
     */
    public void setJbosscmpTemplateFile(File jbosscmpTemplateFile) throws XDocletException
    {
        this.jbosscmpTemplateURL = toURL(jbosscmpTemplateFile);
    }

    /**
     * Debug flag for jaws.xml.
     *
     * @param debug  No, default is "false"
     */
    public void setDebug(String debug)
    {
        this.debug = debug;
    }

    /**
     * The version of JBoss. Supported versions are 2.4, 3.0, 3.0.1, 3.0.2, 3.0.3, 3.2 and 4.0.
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is "2.4".
     */
    public void setVersion(JBossVersionTypes version)
    {
        this.version = version.getValue();
    }

    /**
     * The security domain to use.
     *
     * @param aSecurityDomain  The new SecurityDomain value
     */
    public void setSecurityDomain(String aSecurityDomain)
    {
        securityDomain = aSecurityDomain;
    }

    /**
     * The unauthenticated principal name to use.
     *
     * @param anUnauthenticatedPrincipal  The new unauthenticated principal name
     */
    public void setUnauthenticatedPrincipal(String anUnauthenticatedPrincipal)
    {
        unauthenticatedPrincipal = anUnauthenticatedPrincipal;
    }

    /**
     * Sets the Datasource attribute of the JBossSubTask object
     *
     * @param datasource  The new Datasource value
     */
    public void setDatasource(String datasource)
    {
        this.datasource = datasource;
    }

    /**
     * @param datasourceMapping
     */
    public void setDatasourceMapping(String datasourceMapping)
    {
        this.datasourceMapping = datasourceMapping;
    }

    /**
     * @param typemapping
     */
    public void setTypemapping(String typemapping)
    {
        datasourceMapping = typemapping;
    }

    /**
     * @param generateRelations
     */
    public void setGenerateRelations(String generateRelations)
    {
        this.generateRelations = generateRelations;
    }

    /**
     * @param preferredRelationMapping
     */
    public void setPreferredRelationMapping(String preferredRelationMapping)
    {
        this.preferredRelationMapping = preferredRelationMapping;
    }

    /**
     * @param createTable
     */
    public void setCreateTable(String createTable)
    {
        this.createTable = createTable;
    }

    /**
     * @param removeTable
     */
    public void setRemoveTable(String removeTable)
    {
        this.removeTable = removeTable;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        if (jbossTemplateURL == null)
            this.jbossTemplateURL = getClass().getResource(DEFAULT_JBOSS_XML_TEMPLATE_FILE);
        if (jbosscmpTemplateURL == null)
            this.jbosscmpTemplateURL = getClass().getResource(DEFAULT_JBOSSCMP_TEMPLATE_FILE);
        if (jawsTemplateURL == null)
            this.jawsTemplateURL = getClass().getResource(DEFAULT_JAWS_XML_TEMPLATE_FILE);

        // JBoss does not require a template url or a destination file
        //
        // super.validateOptions();

        if ((hasDatasource() && !hasDatasourceMapping()) ||
            (!hasDatasource() && hasDatasourceMapping())) {
            throw new XDocletException(Translator.getString(XDocletModulesJBossEjbMessages.class, XDocletModulesJBossEjbMessages.DATASOURCE_DATASOURCEMAPPING_PARAMETER_MISSING, new String[]{hasDatasource() ? "datasource" : "datasourcemapping"}));
        }
    }

    /**
     * @exception XDocletException  Description of Exception
     */
    public void execute() throws XDocletException
    {
        String jbossVersion = getVersion();

        URL url = getJbossTemplateURL();

        if (url == null) {
            throw new XDocletException("can't find template for " + JBOSS_XML_FILE_NAME);
        }

        setTemplateURL(url);
        setDestinationFile(JBOSS_XML_FILE_NAME);
        if (jbossVersion.equals(JBossVersionTypes.VERSION_3_2) ||
            jbossVersion.equals(JBossVersionTypes.VERSION_4_0)) {
            setPublicId(JBOSS_DD_PUBLICID_32);
            setSystemId(JBOSS_DD_SYSTEMID_32);
            setDtdURL(getClass().getResource(JBOSS_DTD_FILE_NAME_32));
        }
        else if (jbossVersion.equals(JBossVersionTypes.VERSION_3_0) ||
            jbossVersion.equals(JBossVersionTypes.VERSION_3_0_1) ||
            jbossVersion.equals(JBossVersionTypes.VERSION_3_0_2) ||
            jbossVersion.equals(JBossVersionTypes.VERSION_3_0_3)) {
            setPublicId(JBOSS_DD_PUBLICID_30);
            setSystemId(JBOSS_DD_SYSTEMID_30);
            setDtdURL(getClass().getResource(JBOSS_DTD_FILE_NAME_30));
        }
        else {
            setPublicId(JBOSS_DD_PUBLICID_24);
            setSystemId(JBOSS_DD_SYSTEMID_24);
            setDtdURL(getClass().getResource(JBOSS_DTD_FILE_NAME_24));
        }

        startProcess();

        String ejbSpec = (String) getContext().getConfigParam("EjbSpec");

        // if there is one CMP 1.x bean or we are using JBoss 2.4, JBoss needs a jaws.xml
        //
        if (atLeastOneCmp1EntityBeanExists() || jbossVersion.equals(JBossVersionTypes.VERSION_2_4)) {

            url = getJawsTemplateURL();
            if (url == null) {
                throw new XDocletException("can't find template for " + JAWS_XML_FILE_NAME);
            }
            setTemplateURL(url);
            setDestinationFile(JAWS_XML_FILE_NAME);
            if (jbossVersion.equals(JBossVersionTypes.VERSION_3_0) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_1) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_2) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_3) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_2) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_4_0)) {
                setPublicId(JAWS_DD_PUBLICID_30);
                setSystemId(JAWS_DD_SYSTEMID_30);
                setDtdURL(getClass().getResource(JAWS_DTD_FILE_NAME_30));
            }
            else if (ejbSpec.equals(EjbDocletTask.EjbSpecVersion.EJB_1_1)) {
                setPublicId(JAWS_DD_PUBLICID_24);
                setSystemId(JAWS_DD_SYSTEMID_24);
                setDtdURL(getClass().getResource(JAWS_DTD_FILE_NAME_24));
            }
            startProcess();
        }

        // if we are doing ejb spec 2.0, JBoss 3.0, and one CMP 2.x bean exists, JBoss needs
        // the jbosscmp-jdbc.xml file
        //
        if (ejbSpec.equals(EjbDocletTask.EjbSpecVersion.EJB_2_0) &&
            atLeastOneCmp2EntityBeanExists()) {

            url = getJbosscmpTemplateURL();
            if (url == null) {
                throw new XDocletException("can't find template for " + JBOSSCMP_DD_FILE_NAME);
            }

            setTemplateURL(url);

            setDestinationFile(JBOSSCMP_DD_FILE_NAME);

            if (jbossVersion.equals(JBossVersionTypes.VERSION_3_0) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_1) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_2) ||
                jbossVersion.equals(JBossVersionTypes.VERSION_3_0_3)) {

                setPublicId(JBOSSCMP_DD_PUBLICID_30);
                setSystemId(JBOSSCMP_DD_SYSTEMID_30);
                setDtdURL(getClass().getResource(JBOSSCMP_DTD_FILE_NAME_30));
            }
            else if (jbossVersion.equals(JBossVersionTypes.VERSION_3_2)) {

                setPublicId(JBOSSCMP_DD_PUBLICID_32);
                setSystemId(JBOSSCMP_DD_SYSTEMID_32);
                setDtdURL(getClass().getResource(JBOSSCMP_DTD_FILE_NAME_32));
            }
            else if (jbossVersion.equals(JBossVersionTypes.VERSION_4_0)) {

                setPublicId(JBOSSCMP_DD_PUBLICID_40);
                setSystemId(JBOSSCMP_DD_SYSTEMID_40);
                setDtdURL(getClass().getResource(JBOSSCMP_DTD_FILE_NAME_40));
            }
            // end of else
            else {
                throw new XDocletException("Unknown jboss version number for cmp2 " + jbossVersion);
            }
            // end of else

            startProcess();
        }
    }

    /**
     * @exception XDocletException  Description of Exception
     */
    protected void engineStarted() throws XDocletException
    {
        if (getDestinationFile().equals(JBOSS_XML_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{JBOSS_XML_FILE_NAME}));
        }
        else if (getDestinationFile().equals(JAWS_XML_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{JAWS_XML_FILE_NAME}));
        }
        else if (getDestinationFile().equals(JBOSSCMP_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{JBOSSCMP_DD_FILE_NAME}));
        }
    }

    private boolean hasDatasource()
    {
        return getDatasource() != null && getDatasource().trim().length() > 0;
    }

    private boolean hasDatasourceMapping()
    {
        return getDatasourceMapping() != null && getDatasourceMapping().trim().length() > 0;
    }


    private URL toURL(File file) throws XDocletException
    {
        if (file.exists()) {
            try {
                return file.toURL();
            }
            catch (MalformedURLException mue) {
                throw new XDocletException(mue, "Illegal file url: " + file.getAbsolutePath());
            }
        }
        else {
            throw new XDocletException("Couldn't find file: " + file.getAbsolutePath());
        }
    }

    /**
     * @author    Ara Abrahamian (ara_e@email.com)
     * @created   October 20, 2001
     */
    public static class JBossVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_2_4 = "2.4";
        public final static String VERSION_3_0 = "3.0";
        public final static String VERSION_3_0_1 = "3.0.1";
        public final static String VERSION_3_0_2 = "3.0.2";
        public final static String VERSION_3_0_3 = "3.0.3";
        public final static String VERSION_3_2 = "3.2";
        public final static String VERSION_4_0 = "4.0";

        /**
         * Gets the Values attribute of the JBossVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return new String[]{VERSION_2_4, VERSION_3_0, VERSION_3_0_1, VERSION_3_0_2, VERSION_3_0_3, VERSION_3_2, VERSION_4_0};
        }
    }
}
