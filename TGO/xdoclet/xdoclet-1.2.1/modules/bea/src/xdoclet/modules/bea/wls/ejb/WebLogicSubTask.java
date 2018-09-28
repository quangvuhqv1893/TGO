/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.bea.wls.ejb;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * This task can generate deployment descriptors for WLS 6.0, 6.1 and 7.0.
 *
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy </a>
 * @author               <a href="mailto:jerome.bernard@xtremejava.com">Jerome Bernard</a>
 * @created              Sept 11, 2001
 * @ant.element          display-name="WebLogic Server" name="weblogic" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.18 $
 * @xdoclet.merge-file   file="weblogic-enterprise-beans.xml" relates-to="weblogic-ejb-jar.xml" description="An XML
 *      unparsed entity containing weblogic-enterprise-bean elements for any beans not processed by XDoclet."
 * @xdoclet.merge-file   file="weblogic-security-role-assignment.xml" relates-to="weblogic-ejb-jar.xml" description="An
 *      XML unparsed entity containing security-role-assignment elements."
 */
public class WebLogicSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String WEBLOGIC_DEFAULT_TEMPLATE_FILE = "resources/weblogic-ejb-jar-xml.xdt";

    private final static String WEBLOGIC_DD_FILE_NAME = "weblogic-ejb-jar.xml";

    private final static String WEBLOGIC_DD_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN";

    private final static String WEBLOGIC_DD_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB//EN";

    private final static String WEBLOGIC_DD_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-ejb-jar.dtd";

    private final static String WEBLOGIC_DD_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-ejb-jar.dtd";

    private final static String WEBLOGIC_DTD_FILE_NAME_61 = "resources/weblogic600-ejb-jar.dtd";

    private final static String WEBLOGIC_DTD_FILE_NAME_70 = "resources/weblogic700-ejb-jar.dtd";

    private final static String WEBLOGIC_CMP_DEFAULT_TEMPLATE_FILE = "resources/weblogic-cmp-rdbms-jar-xml.xdt";

    private final static String WEBLOGIC_CMP_DD_FILE_NAME = "weblogic-cmp-rdbms-jar.xml";

    private final static String WEBLOGIC_CMP11_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN";

    private final static String WEBLOGIC_CMP11_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB 1.1 RDBMS Persistence//EN";

    private final static String WEBLOGIC_CMP20_PUBLICID_61 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB RDBMS Persistence//EN";

    private final static String WEBLOGIC_CMP20_PUBLICID_70 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB RDBMS Persistence//EN";

    private final static String WEBLOGIC_CMP11_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-rdbms11-persistence-600.dtd";

    private final static String WEBLOGIC_CMP11_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-rdbms11-persistence-700.dtd";

    private final static String WEBLOGIC_CMP20_SYSTEMID_61 = "http://www.bea.com/servers/wls600/dtd/weblogic-rdbms20-persistence-600.dtd";

    private final static String WEBLOGIC_CMP20_SYSTEMID_70 = "http://www.bea.com/servers/wls700/dtd/weblogic-rdbms20-persistence-700.dtd";

    private final static String WEBLOGIC_CMP11_DTD_FILE_NAME_61 = "resources/weblogic-rdbms11-persistence-600.dtd";

    private final static String WEBLOGIC_CMP11_DTD_FILE_NAME_70 = "resources/weblogic-rdbms11-persistence-700.dtd";

    private final static String WEBLOGIC_CMP20_DTD_FILE_NAME_61 = "resources/weblogic-rdbms20-persistence-600.dtd";

    private final static String WEBLOGIC_CMP20_DTD_FILE_NAME_70 = "resources/weblogic-rdbms20-persistence-700.dtd";

    private final static String DEFAULT_PERSISTENCE = "weblogic";

    private String  version = Version.VERSION_6_1;

    private String  dataSource = "";
    private String  poolName = "";

    private boolean createTables = false;
    private String  persistence = DEFAULT_PERSISTENCE;

    private String  validateDbSchemaWith = "";

    private String  databaseType = "";

    /**
     * Gets the database type specified in the weblogic deployment descriptor. This is a WLS 7.0 and higher feature.
     * Possible values: DB2 INFORMIX ORACLE SQL_SERVER SYBASE POINTBASE
     *
     * @return   DatabaseType
     */
    public String getDatabaseType()
    {
        return databaseType;
    }

    /**
     * Gets the Datasource attribute of the WebLogicSubTask object
     *
     * @return   The Datasource value
     */
    public String getDatasource()
    {
        return dataSource;
    }

    public String getPoolname()
    {
        return poolName;
    }

    /**
     * Gets the Version attribute of the WebLogicSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Gets the Createtables attribute of the WebLogicSubTask object
     *
     * @return   The Createtables value
     */
    public String getCreatetables()
    {
        return createTables ? "True" : "False";
    }

    public String getPersistence()
    {
        return persistence;
    }

    public String getValidateDbSchemaWith()
    {
        return validateDbSchemaWith;
    }

    /**
     * Sets the database type specified in the weblogic-cmp-rdbms-jar.xml deployment descriptor. This is a WLS 7.0 and
     * higher feature. Possible values: DB2 INFORMIX ORACLE SQL_SERVER SYBASE POINTBASE
     *
     * @param databaseType
     * @ant.not-required    No, only used with 7.0 upwards, and optional even then.
     */
    public void setDatabaseType(DatabaseTypes databaseType)
    {
        this.databaseType = databaseType.getValue();
    }

    /**
     * Specifies a default value for the pool-name element in the CMP descriptor, to use if no weblogic.pool-name tag
     * appears on a bean (only applies when ejbspec=1.1)
     *
     * @param s
     * @ant.not-required
     */
    public void setPoolname(String s)
    {
        poolName = s;
    }

    /**
     * Specifies a default value for the data-source-name element in the CMP descriptor, to use if no
     * weblogic.data-source-name tag appears on a bean.
     *
     * @param dataSource
     * @ant.not-required
     */
    public void setDatasource(String dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * Sets the target WebLogic version to generate for. Possible values are 6.1 and 7.0
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is 6.1
     */
    public void setVersion(Version version)
    {
        this.version = version.getValue();
    }

    /**
     * Sets the persistence type to use. Useful if you're using a different persistence manager like MVCSoft
     *
     * @param persistence
     * @ant.not-required   No, default is "weblogic"
     */
    public void setPersistence(String persistence)
    {
        this.persistence = persistence;
    }

    /**
     * If "True", then at deployment time if there is no Table in the Database for a CMP Bean, the Container will
     * attempt to CREATE the Table (based on information found in the deployment files and in the Bean Class).
     *
     * @param flag         The new Createtables value
     * @ant.not-required
     */
    public void setCreatetables(boolean flag)
    {
        createTables = flag;
    }

    /**
     * The CMP subsystem checks that beans have been mapped to a valid database schema at deployment time. A value of
     * 'MetaData' means that JDBC metadata is used to validate the schema. A value of 'TableQuery' means that tables are
     * queried directly to ascertain that they have the schema expected by the CMP runtime.
     *
     * @param type
     * @ant.not-required
     */
    public void setValidateDbSchemaWith(ValidateDbSchemaWithTypes type)
    {
        validateDbSchemaWith = type.getValue();
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // WebLogic does not require a template url or a destination file
        //
        // super.validateOptions();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void execute() throws XDocletException
    {
        setDestinationFile(WEBLOGIC_DD_FILE_NAME);
        setTemplateURL(getClass().getResource(WEBLOGIC_DEFAULT_TEMPLATE_FILE));

        if (getVersion().equals("6.1") || getVersion().equals("6.0")) {
            setPublicId(WEBLOGIC_DD_PUBLICID_61);
            setSystemId(WEBLOGIC_DD_SYSTEMID_61);
            setDtdURL(getClass().getResource(WEBLOGIC_DTD_FILE_NAME_61));
        }
        else {
            setPublicId(WEBLOGIC_DD_PUBLICID_70);
            setSystemId(WEBLOGIC_DD_SYSTEMID_70);
            setDtdURL(getClass().getResource(WEBLOGIC_DTD_FILE_NAME_70));
        }

        startProcess();

        if (atLeastOneCmpEntityBeanExists()) {
            if (DEFAULT_PERSISTENCE.equals(getPersistence())) {
                setDestinationFile(WEBLOGIC_CMP_DD_FILE_NAME);
                setTemplateURL(getClass().getResource(WEBLOGIC_CMP_DEFAULT_TEMPLATE_FILE));
                if (getContext().getConfigParam("EjbSpec").equals("1.1")) {
                    if (getVersion().equals("6.1") || getVersion().equals("6.0")) {
                        setPublicId(WEBLOGIC_CMP11_PUBLICID_61);
                        setSystemId(WEBLOGIC_CMP11_SYSTEMID_61);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP11_DTD_FILE_NAME_61));
                    }
                    else {
                        setPublicId(WEBLOGIC_CMP11_PUBLICID_70);
                        setSystemId(WEBLOGIC_CMP11_SYSTEMID_70);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP11_DTD_FILE_NAME_70));
                    }
                }
                else if (getContext().getConfigParam("EjbSpec").equals("2.0")) {
                    if (getVersion().equals("6.1") || getVersion().equals("6.0")) {
                        setPublicId(WEBLOGIC_CMP20_PUBLICID_61);
                        setSystemId(WEBLOGIC_CMP20_SYSTEMID_61);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP20_DTD_FILE_NAME_61));
                    }
                    else {
                        setPublicId(WEBLOGIC_CMP20_PUBLICID_70);
                        setSystemId(WEBLOGIC_CMP20_SYSTEMID_70);
                        setDtdURL(getClass().getResource(WEBLOGIC_CMP20_DTD_FILE_NAME_70));
                    }
                }
                else {
                    throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.UNSUPPORTED_EJB_SPEC, new String[]{getContext().getConfigParam("EjbSpec").toString()}));
                }
            }
            else {
                LogUtil.getLog(getClass(), "execute").warn(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.NON_WEBLOGIC_PERSISTENCE, new String[]{getPersistence()}));
            }

            startProcess();
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        if (getDestinationFile().equals(WEBLOGIC_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{WEBLOGIC_DD_FILE_NAME}));
        }
        else if (getDestinationFile().equals(WEBLOGIC_CMP_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{WEBLOGIC_CMP_DD_FILE_NAME}));
        }
    }

    /**
     * Legal values of the validate-db-schema-with in weblogic-rdbms20-persistence-600.dtd
     *
     * @created   17. juni 2002
     */
    public static class ValidateDbSchemaWithTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String META_DATA = "MetaData";

        public final static String TABLE_QUERY = "TableQuery";

        /**
         * Gets the Values attribute of the ValidateDbSchemaWithTypes object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                META_DATA, TABLE_QUERY
                });
        }
    }

    /**
     * Legal values of the database-type in weblogic-rdbms20-persistence-700.dtd
     *
     * @created   02 april 2003
     */
    public static class DatabaseTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String DB2 = "DB2";
        public final static String INFORMIX = "INFORMIX";
        public final static String ORACLE = "ORACLE";
        public final static String SQL_SERVER = "SQL_SERVER";
        public final static String SYBASE = "SYBASE";
        public final static String POINTBASE = "POINTBASE";

        /**
         * Gets the Values attribute of the DatabaseTypes object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                DB2, INFORMIX, ORACLE, SQL_SERVER, SYBASE, POINTBASE
                });
        }
    }

    /**
     * @created   17. juni 2002
     */
    public static class Version extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_6_1 = "6.1";
        // Have to add support for x.x.x version numbers in ConfigTagsHandler
        //public final static String VERSION_6_1_2 = "6.1.2";
        // NOT FULLY SUPPORTED YET
        public final static String VERSION_7_0 = "7.0";

        /**
         * Gets the Values attribute of the EjbSpecVersion object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                VERSION_6_1, VERSION_7_0
                });
        }
    }
}
