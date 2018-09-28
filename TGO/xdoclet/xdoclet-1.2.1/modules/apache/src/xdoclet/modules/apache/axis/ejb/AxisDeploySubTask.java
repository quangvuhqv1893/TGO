/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.axis.ejb;

import xdoclet.XDocletException;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * @author        Craig Walls (xdoclet@habuma.com)
 * @created       Mar 23, 2003
 * @ant.element   display-name="Apache AXIS" name="axisdeploy" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.1 $
 */
public class AxisDeploySubTask extends XmlSubTask
{
    private final static String AXIS_SCHEMA = "http://xml.apache.org/axis/wsdd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/axis-deploy_wsdd.xdt";

    private static String GENERATED_FILE_NAME = "deploy-{0}.xml";

    private String  contextFactoryName = "";

    private String  contextProviderUrl = "";

    /**
     * Describe what the AxisDeploySubTask constructor does
     */
    public AxisDeploySubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        System.out.println("Deploy TEMPLATE URL:  " + getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setSchema(AXIS_SCHEMA);
        setHavingClassTag("axis:service");
        setValidateXML(false);
    }

    /**
     * Gets the contextFactoryName
     *
     * @return
     */
    public String getContextFactoryName()
    {
        return contextFactoryName;
    }

    /**
     * Gets the contextProviderUrl
     *
     * @return
     */
    public String getContextProviderUrl()
    {
        return contextProviderUrl;
    }

    /**
     * Sets the contextFactoryName.
     *
     * @param string  The contextFactoryName
     */
    public void setContextFactoryName(String string)
    {
        contextFactoryName = string;
    }

    /**
     * Sets the contextProviderUrl.
     *
     * @param string  The contextProviderUrl
     */
    public void setContextProviderUrl(String string)
    {
        contextProviderUrl = string;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     * @msg.bundle                  id="parameter_missing_or_empty" msg="parameter is missing or empty."
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        String service_urn = getCurrentClass().getDoc().getTag("axis:service").getValue();
        String dest_file_name = getDestinationFile();

        System.out.println(Translator.getString(XDocletModulesApacheAxisEjbMessages.class, XDocletModulesApacheAxisEjbMessages.GENERATING_DD,
            new String[]{getCurrentClass().getQualifiedName(), service_urn, dest_file_name}));
    }

}
