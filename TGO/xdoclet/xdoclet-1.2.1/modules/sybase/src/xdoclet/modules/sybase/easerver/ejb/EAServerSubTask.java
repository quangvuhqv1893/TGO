/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.sybase.easerver.ejb;

import org.apache.commons.logging.Log;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates configuration files for EJB jars in EAServer 4.1+
 *
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              August 15, 2002
 * @ant.element          display-name="EAServer" name="easerver" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.5 $
 * @xdoclet.merge-file   file="easerver-ejb-macros.ent" relates-to="sybase-easerver-config.xml" description="An XML
 *      unparsed entity containing macro elements (e.g. defining property values)."
 * @xdoclet.merge-file   file="easerver-{0}.ent" relates-to="sybase-easerver-config.xml" description="An XML unparsed
 *      entity containing configure elements for a bean."
 * @xdoclet.merge-file   file="easerver-ejb-config.ent" relates-to="sybase-easerver-config.xml" description="An XML
 *      unparsed entity containing additional configure elements e.g. for beans not processed by XDoclet."
 * @xdoclet.merge-file   file="easerver-ejb-custom.xdt" relates-to="sybase-easerver-config.xml" description="Insertion
 *      point for custom template code which generates additional macro and configure elements."
 */
public class EAServerSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    /**
     * The default template file - sybase-easerver-config_xml.xdt
     */
    private final static String DEFAULT_TEMPLATE_FILE = "resources/sybase-easerver-config_xml.xdt";

    /**
     * The generated file name - sybase-easerver-config.xml
     */
    private final static String GENERATED_FILE_NAME = "sybase-easerver-config.xml";

    /**
     * Public ID of the Jaguar-specific DD's DTD.
     */
    private final static String JAGUAR_DD_PUBLICID = "-//Sybase, Inc.//DTD EAServer configuration 1.0//EN";

    /**
     * System ID of the Jaguar-specific DD's DTD.
     */
    private final static String JAGUAR_DD_SYSTEMID = "http://www.sybase.com/dtds/easerver/sybase-easerver-config_1_0.dtd";

    /**
     * Path to the local copy of the Jaguar-specific DD's DTD.
     */
    private final static String JAGUAR_DD_DTD_FILE_NAME = "resources/sybase-easerver-config_1_0.dtd";

    /**
     * Name of the Jaguar Package in the Repository that the EJBs will be deployed to.
     */
    private String  packageName = null;

    /**
     * Description of the Jaguar Package in the Repository.
     */
    private String  packageDescription = null;

    /**
     * The Jaguar version.
     */
    private String  version = EAServerVersionTypes.VERSION_4_1;

    public EAServerSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setPublicId(JAGUAR_DD_PUBLICID);
        setSystemId(JAGUAR_DD_SYSTEMID);
        setDtdURL(getClass().getResource(JAGUAR_DD_DTD_FILE_NAME));
        setValidateXML(true);
    }

    /**
     * Return the Jaguar version.
     *
     * @return   The Jaguar version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Return the Jaguar Package name.
     *
     * @return   The PackageName value
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * Return the Jaguar Package description.
     *
     * @return   The PackageDescription value
     */
    public String getPackageDescription()
    {
        return packageDescription;
    }

    /**
     * Set the Jaguar version.
     *
     * @param version  The new Version value
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Set the Jaguar Package name.
     *
     * @param packageName  The new Security Domain value
     * @ant.required       Yes
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * Set the Jaguar Package description.
     *
     * @param packageDescription  The new PackageDescription value
     */
    public void setPackageDescription(String packageDescription)
    {
        this.packageDescription = packageDescription;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        Log log = LogUtil.getLog(EAServerSubTask.class, "validateOptions");

        log.debug("packageName = " + getPackageName());
        if (getPackageName() == null || getPackageName().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"packageName"}));
        }
    }

    // override execute method?

    /**
     * @created   23 August 2002
     */
    public static class EAServerVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_4_1 = "4.1";

        /**
         * Gets the Values attribute of the EAServerVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_4_1});
        }
    }

}
