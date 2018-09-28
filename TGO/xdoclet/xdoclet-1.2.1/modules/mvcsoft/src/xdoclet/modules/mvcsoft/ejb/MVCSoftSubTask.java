/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.mvcsoft.ejb;

import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;

/**
 * Generates MVCSoft's xml.
 *
 * @author        Daniel OConnor (docodan@mvcsoft.com)
 * @created       November 1, 2001
 * @ant.element   display-name="MVCSoft" name="mvcsoft" parent="xdoclet.modules.ejb.EjbDocletTask"
 */
public class MVCSoftSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/mvcsoft.xdt";

    private static String DD_FILE_NAME = "mvcsoft-pm.xml";

    private String  deploymentVersion = MVCSoftVersionTypes.VERSION_1_0_0;

    private String  connectionJndiName = "false";

    private String  loggingType = "false";

    private String  lightweightFactoryName = "false";

    /**
     * Describe what the MVCSoftSubTask constructor does
     */
    public MVCSoftSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setValidateXML(false);
    }

    /**
     * Gets the DeploymentVersion attribute of the MVCSoftSubTask object
     *
     * @return   The DeploymentVersion value
     */
    public String getDeploymentVersion()
    {
        return this.deploymentVersion;
    }

    /**
     * Gets the Connectionjndiname attribute of the MVCSoftSubTask object
     *
     * @return   The Connectionjndiname value
     */
    public String getConnectionjndiname()
    {
        return connectionJndiName;
    }

    /**
     * Gets the Loggingtype attribute of the MVCSoftSubTask object
     *
     * @return   The Loggingtype value
     */
    public String getLoggingtype()
    {
        return loggingType;
    }

    /**
     * Gets the lightweightfactoryname attribute of the MVCSoftSubTask object
     *
     * @return   The Lightweightfactoryname value
     */
    public String getLightweightfactoryname()
    {
        return lightweightFactoryName;
    }

    /**
     * Sets the DeploymentVersion attribute of the MVCSoftSubTask object
     *
     * @param deploymentVersion  The new DeploymentVersion value
     */
    public void setDeploymentVersion(MVCSoftVersionTypes deploymentVersion)
    {
        this.deploymentVersion = deploymentVersion.getValue();
    }

    /**
     * Sets the Connectionjndiname attribute of the MVCSoftSubTask object
     *
     * @param jndiName     The new Connectionjndiname value
     * @ant.not-required   No, default is "java:DefaultDS"
     */
    public void setConnectionjndiname(String jndiName)
    {
        connectionJndiName = jndiName;
    }

    /**
     * Sets the Loggingtype attribute of the MVCSoftSubTask object
     *
     * @param loggingType  The new Loggingtype value
     * @ant.not-required   No, default is "None"
     */
    public void setLoggingtype(String loggingType)
    {
        this.loggingType = loggingType;
    }

    /**
     * Sets the lightweightfactoryname attribute of the MVCSoftSubTask object
     *
     * @param lightweightFactoryName
     * @ant.not-required              No, default is no value
     */
    public void setLightweightfactoryname(String lightweightFactoryName)
    {
        this.lightweightFactoryName = lightweightFactoryName;
    }

    /**
     * @author    Daniel OConnor (docodan@mvcsoft.com)
     * @created   November 1, 2001
     */
    public static class MVCSoftVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_1_0_0 = "1.0.0";
        public final static String VERSION_1_1 = "1.1";

        /**
         * Gets the Values attribute of the MVCSoftVersionTypes object.
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_1_0_0, VERSION_1_1});
        }
    }

}
