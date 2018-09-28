/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.pramati.ejb;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * Generates Pramati deployment files
 *
 * @author        <a href="mailto:plightbo@hotmail.com">Patrick Lightbody</a>
 * @created       Feb 21, 2002
 * @ant.element   display-name="Pramati" name="pramati" parent="xdoclet.modules.ejb.EjbDocletTask"
 */
public class PramatiSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String PRAMATI_DEFAULT_TEMPLATE_FILE = "resources/pramati.xdt";

    private final static String PRAMATI_DD_FILE_NAME = "pramati-j2ee-server.xml";

    private final static String PRAMATI_OR_DEFAULT_TEMPLATE_FILE = "resources/pramati-or-map.xdt";

    private final static String PRAMATI_OR_DD_FILE_NAME = "pramati-or-map.xml";

    private String  _version = "3.0";

    private String  _dataSource = "";

    private String  _jarName = "";

    /**
     * Gets the Datasource attribute of the PramatiSubTask object
     *
     * @return   The Datasource value
     */
    public String getDatasource()
    {
        return _dataSource;
    }

    /**
     * Gets the Version attribute of the PramatiSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return _version;
    }

    /**
     * Gets the JarName attribute of the PramatiSubTask object
     *
     * @return   The JarName value
     */
    public String getJarName()
    {
        return _jarName;
    }

    /**
     * Sets the Datasource attribute of the PramatiSubTask object
     *
     * @param new_data_source  The new Datasource value
     */
    public void setDatasource(String new_data_source)
    {
        _dataSource = new_data_source;
    }

    /**
     * Sets the Version attribute of the PramatiSubTask object
     *
     * @param version  The new Version value
     */
    public void setVersion(String version)
    {
        _version = version;
    }

    /**
     * Sets the JarName attribute of the PramatiSubTask object
     *
     * @param jarName  The new JarName value
     */
    public void setJarName(String jarName)
    {
        this._jarName = jarName;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // Overridden, as the template url & destination file are set later in execute()
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
        setTemplateURL(getClass().getResource(PRAMATI_DEFAULT_TEMPLATE_FILE));
        setDestinationFile(PRAMATI_DD_FILE_NAME);

        startProcess();

        if (atLeastOneCmpEntityBeanExists()) {
            setTemplateURL(getClass().getResource(PRAMATI_OR_DEFAULT_TEMPLATE_FILE));
            setDestinationFile(PRAMATI_OR_DD_FILE_NAME);
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
        if (getDestinationFile().equals(PRAMATI_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{PRAMATI_DD_FILE_NAME}));
        }
        else if (getDestinationFile().equals(PRAMATI_OR_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{PRAMATI_OR_DD_FILE_NAME}));
        }
    }
}
