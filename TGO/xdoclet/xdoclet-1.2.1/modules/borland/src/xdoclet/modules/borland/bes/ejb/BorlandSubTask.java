/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.borland.bes.ejb;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * @author               Michal Maczka
 * @created              December 5, 2002
 * @ant.element          display-name="Borland" name="borland" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @xdoclet.merge-file   file="ejb-borland-datasources.xml" relates-to="ejb-borland.xml" description="An XML unparsed
 *      entity containing the datasource elements."
 * @xdoclet.merge-file   file="ejb-borland-authorization-domain.xml" relates-to="ejb-borland.xml" description="An XML
 *      document containing the optional authorization-domain element."
 * @xdoclet.merge-file   file="ejb-borland-ejb-refs-{0}.xml" relates-to="ejb-borland.xml" description="An XML unparsed
 *      entity containing the ejb-ref elements for a bean, to be used instead of generating from bes.ejb-ref tags."
 * @xdoclet.merge-file   file="ejb-borland-ejb-local-refs-{0}.xml" relates-to="ejb-borland.xml" description="An XML
 *      unparsed entity containing the ejb-local-ref elements for a bean, to be used instead of generating from
 *      bes.ejb-local-ref tags."
 * @xdoclet.merge-file   file="ejb-borland-resource-refs-{0}.xml" relates-to="ejb-borland.xml" description="An XML
 *      unparsed entity containing the resource-ref elements for a bean, to be used instead of generating from
 *      bes.resource-ref tags."
 * @xdoclet.merge-file   file="ejb-borland-resource-env-refs-{0}.xml" relates-to="ejb-borland.xml" description="An XML
 *      unparsed entity containing the resource-env-ref elements for a bean, to be used instead of generating from
 *      bes.resource-env-ref tags."
 * @xdoclet.merge-file   file="ejb-borland-properties-{0}.xml" relates-to="ejb-borland.xml" description="An XML unparsed
 *      entity containing the property elements for a bean, to be used instead of generating from bes.property tags."
 */
public class BorlandSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String BORLAND_DD_FILE_NAME = "ejb-borland.xml";

    private final static String BORLAND_DD_PUBLICID =
        "-//Borland Software Corporation//DTD Enterprise JavaBeans 2.0//EN";

    private final static String BORLAND_DD_SYSTEMID =
        "http://www.borland.com/devsupport/appserver/dtds/ejb-jar_2_0-borland.dtd";

    private final static String BORLAND_DTD_FILE_NAME =
        "resources/ejb-borland_2_0.dtd";

    private static String DEFAULT_TEMPLATE_FILE =
        "resources/ejb-borland_2_0.xdt";

    private String  _version = "5.1";

    private String  _datasource = "NO DATA SOURCE!";

    private String  _datasourceMapping = "NO DATASOURCE MAPPING!";

    private boolean _createTables = false;

    /**
     * Gets the Version attribute of the BorlandSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return _version;
    }

    /**
     * Gets the Createtables attribute of the BorlandSubTask object
     *
     * @return   The Createtables value
     */
    public String getCreatetables()
    {
        return _createTables ? "True" : "False";
    }

    /**
     * Gets the Createtables attribute of the BorlandSubTask object
     *
     * @return   The Createtables value
     */
    public String getDatasource()
    {
        return _datasource;
    }


    public String getDatasourceMapping()
    {
        return _datasourceMapping;
    }

    /**
     * Sets the Version attribute of the BorlandSubTask object
     *
     * @param version  The new Version value
     */
    public void setVersion(String version)
    {
        _version = version;
    }

    /**
     * Sets the Createtables attribute of the BorlandSubTask object
     *
     * @param flag  The new Createtables value
     */
    public void setCreatetables(boolean flag)
    {
        _createTables = flag;
    }

    /**
     * Sets the Datasource attribute of the JBossSubTask object
     *
     * @param datasource
     */
    public void setDatasource(String datasource)
    {
        _datasource = datasource;
    }

    /**
     * @param datasourceMapping
     */
    public void setDatasourceMapping(String datasourceMapping)
    {
        _datasourceMapping = datasourceMapping;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        //
        //super.validateOptions();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void execute() throws XDocletException
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(BORLAND_DD_FILE_NAME);
        setPublicId(BORLAND_DD_PUBLICID);
        setSystemId(BORLAND_DD_SYSTEMID);
        setDtdURL(getClass().getResource(BORLAND_DTD_FILE_NAME));
        startProcess();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        if (getDestinationFile().equals(BORLAND_DD_FILE_NAME)) {
            System.out.println(
                Translator.getString(
                XDocletMessages.class,
                XDocletMessages.GENERATING_SOMETHING,
                new String[]{BORLAND_DD_FILE_NAME}));
        }
    }

    private boolean hasDatasource()
    {
        return getDatasource() != null && getDatasource().trim().length() > 0;
    }

    private boolean hasDatasourceMapping()
    {
        return getDatasourceMapping() != null
            && getDatasourceMapping().trim().length() > 0;
    }

}
