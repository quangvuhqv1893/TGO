/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ibm.websphere.ejb;

import xjavadoc.XClass;
import xdoclet.XDocletException;

import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;

/**
 * This is a JavaDoc doclet plugin that can be used to generate EJB-related files from just one or a set of EJB bean
 * source files that uses custom EJBDoclet JavaDoc tags. In addition to the files generated by EJBDoclet,
 * WebSphereDoclet will also generate WebSphere specific deployment XML files.
 *
 * @author        Minh Yie
 * @created       15 August 2001
 * @ant.element   display-name="WebSphere" name="websphere" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.6 $
 */
public class WebSphereSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private static String WEBSPHERE_DEFAULT_BND_TEMPLATE_FILE = "resources/ibm-ejb-jar-bnd_xmi.xdt";

    private static String WEBSPHERE_DD_BND_FILE_NAME = "ibm-ejb-jar-bnd.xmi";

    private static String WEBSPHERE_DEFAULT_EXT_TEMPLATE_FILE = "resources/ibm-ejb-jar-ext_xmi.xdt";

    private static String WEBSPHERE_DD_EXT_FILE_NAME = "ibm-ejb-jar-ext.xmi";

    private static String WEBSPHERE_SCHEMA_TEMPLATE_FILE = "resources/Schema_dbxmi.xdt";

    private static String WEBSPHERE_DD_SCHEMA_FILE_NAME = "Schema.dbxmi";

    public WebSphereSubTask()
    {
        setUseIds(true);
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // WebSphere does not require a template url or a destination file
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
        setTemplateURL(getClass().getResource(WEBSPHERE_DEFAULT_BND_TEMPLATE_FILE));
        setDestinationFile(WEBSPHERE_DD_BND_FILE_NAME);
        startProcess();

        setTemplateURL(getClass().getResource(WEBSPHERE_DEFAULT_EXT_TEMPLATE_FILE));
        setDestinationFile(WEBSPHERE_DD_EXT_FILE_NAME);
        startProcess();

        if (atLeastOneCmpEntityBeanExists()) {
            setTemplateURL(getClass().getResource(WEBSPHERE_SCHEMA_TEMPLATE_FILE));
            setDestinationFile(WEBSPHERE_DD_SCHEMA_FILE_NAME);
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
    }
}