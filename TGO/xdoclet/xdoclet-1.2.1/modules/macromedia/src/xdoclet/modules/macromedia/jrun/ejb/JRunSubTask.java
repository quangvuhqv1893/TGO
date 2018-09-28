/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.macromedia.jrun.ejb;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * @author               Aslak Hellesøy
 * @created              February 2, 2002
 * @ant.element          display-name="JRun" name="jrun" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @xdoclet.merge-file   file="ejb-container.xml" relates-to="jrun-ejb-jar.xml" description="An XML unparsed entity
 *      containing the contents of the ejb-container element, i.e. (entity-default-store-type?,
 *      session-default-store-type?, file-store-directory?, cmp20-store-manager?)"
 */
public class JRunSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String JRUN_DD_FILE_NAME = "jrun-ejb-jar.xml";

    private final static String JRUN_DD_PUBLICID = "-//Macromedia, Inc.//DTD jrun-ejb-jar 4.0//EN";

    private final static String JRUN_DD_SYSTEMID = "http://jrun.macromedia.com/dtds/jrun-ejb-jar.dtd";

    private final static String JRUN_DTD_FILE_NAME = "resources/jrun-ejb-jar.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/jrun-ejb-jar-xml.xdt";

    private String  _version = "4.0";

    private String  _dataSource = "DefaultDataSource";

    private boolean _createTables = false;

    /**
     * Gets the Source attribute of the JRunSubTask object
     *
     * @return   The Source value
     */
    public String getSource()
    {
        return _dataSource;
    }


    /**
     * Gets the Version attribute of the JRunSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return _version;
    }


    /**
     * Gets the Createtables attribute of the JRunSubTask object
     *
     * @return   The Createtables value
     */
    public String getCreatetables()
    {
        return _createTables ? "True" : "False";
    }


    /**
     * Sets the Source attribute of the JRunSubTask object
     *
     * @param new_data_source  The new Source value
     */
    public void setSource(String new_data_source)
    {
        _dataSource = new_data_source;
    }


    /**
     * Sets the Version attribute of the JRunSubTask object
     *
     * @param version  The new Version value
     */
    public void setVersion(String version)
    {
        _version = version;
    }


    /**
     * Sets the Createtables attribute of the JRunSubTask object
     *
     * @param flag  The new Createtables value
     */
    public void setCreatetables(boolean flag)
    {
        _createTables = flag;
    }


    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // JRun does not require a template url or a destination file
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
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(JRUN_DD_FILE_NAME);
        setPublicId(JRUN_DD_PUBLICID);
        setSystemId(JRUN_DD_SYSTEMID);
        setDtdURL(getClass().getResource(JRUN_DTD_FILE_NAME));

        startProcess();
    }


    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        if (getDestinationFile().equals(JRUN_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{JRUN_DD_FILE_NAME}));
        }
    }
}
