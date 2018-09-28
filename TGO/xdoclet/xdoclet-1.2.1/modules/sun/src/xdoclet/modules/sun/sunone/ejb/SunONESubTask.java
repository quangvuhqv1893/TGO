/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.sun.sunone.ejb;

import org.apache.commons.logging.Log;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates configuration files for EJB jars in iPlanet/SunONE
 *
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              October 01, 2002
 * @ant.element          display-name="SunONE" name="sunone" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @xdoclet.merge-file   file="ejb-env-entries-{0}.xml" relates-to="ias-ejb-jar.xml" description="Same as for
 *      &lt;deploymentdescriptor&gt; subtask (only used for MDBs)."
 * @xdoclet.merge-file   file="mdb-ejbrefs-{0}.xml" relates-to="ias-ejb-jar.xml" description="Same as for
 *      &lt;deploymentdescriptor&gt; subtask, except root element name is mdb-ejb-ref rather than ejb-ref (only used for
 *      MDBs)."
 * @xdoclet.merge-file   file="mdb-resourcerefs-{0}.xml" relates-to="ias-ejb-jar.xml" description="Same as for
 *      &lt;deploymentdescriptor&gt; subtask, except root element name is mdb-resource-ref rather than resource-ref
 *      (only used for MDBs)."
 * @xdoclet.merge-file   file="ejb-resource-env-refs-{0}.xml" relates-to="ias-ejb-jar.xml" description="Same as for
 *      &lt;deploymentdescriptor&gt; subtask (only used for MDBs)."
 * @xdoclet.merge-file   file="sunone-role-mappings.ent" relates-to="ias-ejb-jar.xml, sun-ejb-jar.xml" description="An
 *      Unparsed XML Entity file that contains the &lt;role-mapping&gt; elements."
 * @xdoclet.merge-file   file="sunone-pm-descriptors.xml" relates-to="sun-ejb-jar.xml" description="An XML Document
 *      containing the &lt;pm-descriptors&gt; element."
 * @xdoclet.merge-file   file="sunone-ior-security-config-{0}.xml" relates-to="sun-ejb-jar.xml" description="An XML
 *      Document containing the &lt;ior-security-config&gt; element for a bean."
 * @version              $Revision: 1.3 $
 * @todo                 allow for overriding the CMP templates etc.
 * @todo                 secondary-table element is not currently supported in the cmp mappings file
 */
public class SunONESubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    /**
     * The default template file - either ias-ejb-jar_xml.xdt or sun-ejb-jar_xml.xdt, depending on version.
     */
    private final static String DEFAULT_TEMPLATE_FILE_70 = "resources/sun-ejb-jar_xml.xdt";
    private final static String DEFAULT_TEMPLATE_FILE_60 = "resources/ias-ejb-jar_xml.xdt";

    /**
     * The default generated file name - either ias-ejb-jar.xml or sun-ejb-jar.xml, depending on version.
     */
    private final static String GENERATED_FILE_NAME_70 = "sun-ejb-jar.xml";
    private final static String GENERATED_FILE_NAME_60 = "ias-ejb-jar.xml";

    /**
     * Public ID of the iPlanet/SunONE-specific DD's DTD.
     */
    private final static String SUNONE_DD_PUBLICID_70 = "-//Sun Microsystems, Inc.//DTD Sun ONE Application Server 7.0 EJB 2.0//EN";
    private final static String SUNONE_DD_PUBLICID_60 = "-//Sun Microsystems, Inc.//DTD iAS Enterprise JavaBeans 1.0//EN";
    private final static String SUNONE_DD_PUBLICID_61 = "-//Sun Microsystems, Inc.//DTD iAS Enterprise JavaBeans 1.1//EN";

    /**
     * System ID of the iPlanet/SunONE-specific DD's DTD.
     */
    private final static String SUNONE_DD_SYSTEMID_70 = "http://www.sun.com/software/sunone/appserver/dtds/sun-ejb-jar_2_0-0.dtd";
    private final static String SUNONE_DD_SYSTEMID_60 = "http://developer.iplanet.com/appserver/dtds/IASEjb_jar_1_0.dtd";
    private final static String SUNONE_DD_SYSTEMID_61 = "http://developer.iplanet.com/appserver/dtds/IASEjb_jar_1_1.dtd";

    /**
     * Path to the local copy of the iPlanet/SunONE-specific DD's DTD.
     */
    private final static String SUNONE_DD_DTD_FILE_NAME_70 = "resources/sun-ejb-jar_2_0-0.dtd";
    private final static String SUNONE_DD_DTD_FILE_NAME_60 = "resources/IASEjb_jar_1_0.dtd";
    private final static String SUNONE_DD_DTD_FILE_NAME_61 = "resources/IASEjb_jar_1_1.dtd";

    /**
     * The default CMP mappings template file - sun-cmp-mappings_xml.xdt
     */
    private final static String DEFAULT_CMP_TEMPLATE_FILE_70 = "resources/sun-cmp-mappings_xml.xdt";

    /**
     * The default generated CMP mappings file name.
     */
    private final static String GENERATED_CMP_FILE_NAME_70 = "sun-cmp-mappings.xml";
    private final static String GENERATED_CMP_FILE_NAME_60 = "???";
    // 6.0 CMP mappings are tricky; the filenames of the mapping files are specified
    // per-bean in <properties-file-location> elements in the ias-ejb-jar.xml
    // For now, you'll have to create them by hand.

    /**
     * Public ID of the iPlanet/SunONE-specific CMP mappings DTD.
     */
    private final static String SUNONE_CMP_PUBLICID_70 = "-//Sun Microsystems, Inc.//DTD Sun ONE Application Server 7.0 OR Mapping //EN";

    /**
     * System ID of the iPlanet/SunONE-specific CMP mappings DTD.
     */
    private final static String SUNONE_CMP_SYSTEMID_70 = "http://www.sun.com/software/sunone/appserver/dtds/sun-cmp-mapping_1_0.dtd";
    private final static String SUNONE_CMP_SYSTEMID_60 = "http://developer.iplanet.com/appserver/dtds/IASPersistence_manager_1_0.dtd";

    /**
     * Path to the local copy of the iPlanet/SunONE-specific CMP mappings DTD.
     */
    private final static String SUNONE_CMP_FILE_NAME_70 = "resources/sun-cmp-mapping_1_0.dtd";
    private final static String SUNONE_CMP_FILE_NAME_60 = "resources/IASPersistence_manager_1_0.dtd";

    /**
     * The iPlanet/SunONE version.
     */
    private String  version = SunONEVersionTypes.VERSION_7_0;

    /**
     * Optional transaction manager type.
     */
    private String  transactionManagerType = null;

    /**
     * Optional CMP Resource JNDI name.
     */
    private String  cmpResourceJndiName = null;

    /**
     * Optional CMP Resource Principal name.
     */
    private String  cmpResourcePrincipalName = null;

    /**
     * Optional CMP Resource Principal password.
     */
    private String  cmpResourcePrincipalPassword = null;

    /**
     * CMP Schema filename (without .dbschema extension), produced by Sun's utilities
     */
    private String  cmpSchema = null;

    private String  cmpTemplateFile = null;

    private String  cmpDestinationFile = null;

    public SunONESubTask()
    {
        setValidateXML(true);
    }

    /**
     * Return the iPlanet/SunONE version.
     *
     * @return   The iPlanet/SunONE version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Return the default transaction manager type.
     *
     * @return   The transaction manager type
     */
    public String getTransactionManagerType()
    {
        return transactionManagerType;
    }

    /**
     * Return the CMP Resource JNDI name.
     *
     * @return   The CMP Resource JNDI name
     */
    public String getCmpResourceJndiName()
    {
        return cmpResourceJndiName;
    }

    /**
     * Return the CMP Resource Principal name.
     *
     * @return   The CMP Resource Principal name
     */
    public String getCmpResourcePrincipalName()
    {
        return cmpResourcePrincipalName;
    }

    /**
     * Return the CMP Resource Principal password.
     *
     * @return   The CMP Resource Principal password
     */
    public String getCmpResourcePrincipalPassword()
    {
        return cmpResourcePrincipalPassword;
    }

    /**
     * Return the database schema filename (minus .dbschema extension), as captured using Sun's utilities.
     *
     * @return   The schema filename
     */
    public String getCmpSchema()
    {
        return cmpSchema;
    }

    /**
     * Gets the name of the sun-cmp-mappings.xml template file.
     *
     * @return   the custom template file
     */
    public String getCmpTemplateFile()
    {
        return cmpTemplateFile;
    }

    /**
     * Gets the destination filename of the CMP mappings file.
     *
     * @return   the destination filename
     */
    public String getCmpDestinationFile()
    {
        return cmpDestinationFile;
    }

    /**
     * The SunONE/iPlanet version. Supported versions are 6.0, 6.5 and 7.0.
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is "7.0".
     */
    public void setVersion(SunONEVersionTypes version)
    {
        this.version = version.getValue();
    }

    /**
     * Optional default transaction manager type for all components. Allowed values are 'local' and 'global'.
     *
     * @param transactionManagerType  "local" or "global"
     * @ant.not-required              Only used for version = 6.5, and optional even then.
     */
    public void setTransactionManagerType(String transactionManagerType)
    {
        this.transactionManagerType = transactionManagerType;
    }

    /**
     * Specifies the absolute jndi-name of the database to be used for storing CMP beans in the EJB JAR file.
     *
     * @param cmpResourceJndiName  String containing the JNDI name
     * @ant.not-required           Only used for version = 7.0, and optional even then.
     */
    public void setCmpResourceJndiName(String cmpResourceJndiName)
    {
        this.cmpResourceJndiName = cmpResourceJndiName;
    }

    /**
     * Specifies the default sign-on name to the resource manager.
     *
     * @param cmpResourcePrincipalName  String containing the name
     * @ant.not-required                Only used if cmpResourceJndiName parameter is specified, and optional even then.
     */
    public void setCmpResourcePrincipalName(String cmpResourcePrincipalName)
    {
        this.cmpResourcePrincipalName = cmpResourcePrincipalName;
    }

    /**
     * Specifies the default password to the resource manager.
     *
     * @param cmpResourcePrincipalPassword  String containing the password
     * @ant.not-required                    Only used if the cmpResourcePrincipalName parameter is specified.
     */
    public void setCmpResourcePrincipalPassword(String cmpResourcePrincipalPassword)
    {
        this.cmpResourcePrincipalPassword = cmpResourcePrincipalPassword;
    }

    /**
     * Specifies the database schema filename (minus .dbschema extension), as captured using Sun's utilities.
     *
     * @param cmpSchema    String containing the schema name
     * @ant.not-required   Only used with CMP entity beans.
     */
    public void setCmpSchema(String cmpSchema)
    {
        this.cmpSchema = cmpSchema;
    }

    /**
     * Sets the name of the sun-cmp-mappings.xml template file.
     *
     * @param cmpTemplateFile  the name of the custom template file
     * @ant.not-required
     */
    public void setCmpTemplateFile(String cmpTemplateFile)
    {
        this.cmpTemplateFile = cmpTemplateFile;
    }

    /**
     * Sets the destination filename of the CMP mappings file.
     *
     * @param cmpDestinationFile  the destination filename
     * @ant.not-required
     */
    public void setCmpDestinationFile(String cmpDestinationFile)
    {
        this.cmpDestinationFile = cmpDestinationFile;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // we don't call super.validateOptions() here, as the template file and
        // destination file may not have been set yet

        if (getVersion().equals(SunONEVersionTypes.VERSION_6_5)
            && getTransactionManagerType() != null) {
            if (!getTransactionManagerType().equals("local")
                && !getTransactionManagerType().equals("global")) {
                throw new XDocletException(Translator.getString(XDocletModulesSunONEEjbMessages.class,
                    XDocletModulesSunONEEjbMessages.INVALID_TRANSACTION_MANAGER_TYPE));
            }
        }
    }

    public void execute() throws XDocletException
    {
        if (getVersion().equals(SunONEVersionTypes.VERSION_6_0)) {
            if (getTemplateURL() == null)
                setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE_60));
            if (getDestinationFile() == null)
                setDestinationFile(GENERATED_FILE_NAME_60);
            if (getPublicId() == null)
                setPublicId(SUNONE_DD_PUBLICID_60);
            if (getSystemId() == null)
                setSystemId(SUNONE_DD_SYSTEMID_60);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(SUNONE_DD_DTD_FILE_NAME_60));
        }
        else if (getVersion().equals(SunONEVersionTypes.VERSION_6_5)) {
            if (getTemplateURL() == null)
                setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE_60));
            if (getDestinationFile() == null)
                setDestinationFile(GENERATED_FILE_NAME_60);
            if (getPublicId() == null)
                setPublicId(SUNONE_DD_PUBLICID_61);
            if (getSystemId() == null)
                setSystemId(SUNONE_DD_SYSTEMID_61);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(SUNONE_DD_DTD_FILE_NAME_61));
        }
        else if (getVersion().equals(SunONEVersionTypes.VERSION_7_0)) {
            if (getTemplateURL() == null)
                setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE_70));
            if (getDestinationFile() == null)
                setDestinationFile(GENERATED_FILE_NAME_70);
            if (getPublicId() == null)
                setPublicId(SUNONE_DD_PUBLICID_70);
            if (getSystemId() == null)
                setSystemId(SUNONE_DD_SYSTEMID_70);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(SUNONE_DD_DTD_FILE_NAME_70));
        }
        startProcess();

        if (atLeastOneCmpEntityBeanExists()) {
            if (getVersion().equals(SunONEVersionTypes.VERSION_7_0)) {
                if (getCmpTemplateFile() != null) {
                    setTemplateURL(getClass().getResource(getCmpTemplateFile()));
                }
                else {
                    setTemplateURL(getClass().getResource(DEFAULT_CMP_TEMPLATE_FILE_70));
                }
                if (getCmpDestinationFile() != null) {
                    setDestinationFile(getCmpDestinationFile());
                }
                else {
                    setDestinationFile(GENERATED_CMP_FILE_NAME_70);
                }
                setPublicId(SUNONE_CMP_PUBLICID_70);
                setSystemId(SUNONE_CMP_SYSTEMID_70);
                setDtdURL(getClass().getResource(SUNONE_CMP_FILE_NAME_70));

                startProcess();
            }
        }
    }

    /**
     * @created   22 January 2003
     */
    public static class SunONEVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_6_0 = "6.0";
        public final static String VERSION_6_5 = "6.5";
        public final static String VERSION_7_0 = "7.0";

        /**
         * Gets the Values attribute of the SunONEVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_6_0, VERSION_6_5, VERSION_7_0});
        }
    }

}
