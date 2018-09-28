/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.caucho;

import xdoclet.XmlSubTask;

/**
 * Subtask for generation of resin-ejb.
 *
 * @author               Yoritaka Sakakura (yori@teardrop.org)
 * @created              June 5, 2002
 * @see                  <a href="http://caucho.com/products/resin-ejb/ejb-ref/resin-ejb-config.xtp">Resin CMP
 *      Configuration</a>
 * @ant.element          name="resin-ejb-xml" display-name="Resin" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @xdoclet.merge-file   file="resin-query-functions.xml" relates-to="resin.ejb" description="An XML unparsed entity
 *      containing query-function elements to include in the descriptor."
 */
public class ResinEjbSubTask extends XmlSubTask
{
    private final static String DEFAULT_TEMPLATE_FILE = "resources/resin-ejb.xdt";
    private final static String DD_FILE_NAME = "resin.ejb";

    private String  cacheSize = "";
    private String  cacheTimeout = "";
    private String  generateSourceComments = "true";


    /**
     * Default generated filename is resin.cmp.
     */
    public ResinEjbSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setPublicId(null);
        setSystemId(null);
        setSchema(null);
        setDtdURL(null);
        setValidateXML(false);
    }


    /**
     * resin-ejb/cache-size; no default.
     *
     * @return
     */
    public String getCacheSize()
    {
        return cacheSize;
    }

    /**
     * resin-ejb/cache-timeout; no default.
     *
     * @return
     */
    public String getCacheTimeout()
    {
        return cacheTimeout;
    }

    /**
     * Whether or not include debug comments pointing to origin class/method for elements in the deployment descriptor.
     * Defaults to 'true'
     *
     * @return
     */
    public String getGenerateSourceComments()
    {
        return generateSourceComments;
    }

    public void setCacheSize(String cacheSize)
    {
        this.cacheSize = cacheSize;
    }

    public void setCacheTimeout(String cacheTimeout)
    {
        this.cacheTimeout = cacheTimeout;
    }

    public void setGenerateSourceComments(String generateSourceComments)
    {
        this.generateSourceComments = generateSourceComments;
    }
}
