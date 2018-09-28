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
 * @ant.element   display-name="Apache AXIS" name="axisundeploy" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.1 $
 */
public class AxisUndeploySubTask extends XmlSubTask
{
    private final static String AXIS_SCHEMA = "http://xml.apache.org/axis/wsdd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/axis-undeploy_wsdd.xdt";

    private static String GENERATED_FILE_NAME = "undeploy-{0}.xml";

    /**
     * Describe what the AxisUndeploySubTask constructor does
     */
    public AxisUndeploySubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        System.out.println("Undeploy TEMPLATE URL:  " + getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setSchema(AXIS_SCHEMA);
        setHavingClassTag("axis:service");
        setValidateXML(false);
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

        System.out.println(Translator.getString(XDocletModulesApacheAxisEjbMessages.class, XDocletModulesApacheAxisEjbMessages.GENERATING_UD,
            new String[]{getCurrentClass().getQualifiedName(), service_urn, dest_file_name}));
    }
}
