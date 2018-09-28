/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.jmx;

import java.io.File;
import xdoclet.XDocletException;

import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates a {servicefile}-service.xml file for JBoss mbean configuration. This can help you see what you can set in
 * an mbean, you can just fill in your values and deploy. Currently there is a limitation that only managed attributes
 * with getters show up in the results, however there is a comment for those with only a setter. It treats read-only
 * managed attributes as if they can be written.
 *
 * @author               <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created              March 3, 2002
 * @ant.element          display-name="JBoss XML Service" name="jbossxmlservicetemplate"
 *      parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version              $Revision: 1.7 $
 * @xdoclet.merge-file   file="jboss-service.ent" relates-to="{servicefile}-service.xml" description="An XML unparsed
 *      entity containing jboss deployment descriptions for mbeans you wish to include in the {servicefile}-service.xml
 *      which aren't processed by XDoclet. It can also include classpath and global depends elements. servicefile is
 *      specified in the ant subtask"
 */
public class JBossXmlServiceTemplateSubTask extends XmlSubTask
{
    private final static String DD_FILE_NAME = "-service.xml";

    private static String DEFAULT_TEMPLATE_FILE = "resources/jboss-service-template.xdt";

    private String  servicefile;

    /**
     * Describe what the JBossXmlServiceTemplateSubTask constructor does
     */
    public JBossXmlServiceTemplateSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setHavingClassTag("jmx:mbean");
    }

    /**
     * Get the Servicefile value.
     *
     * @return   the Servicefile value.
     */
    public String getServicefile()
    {
        return servicefile;
    }

    /**
     * Set the Servicefile value.
     *
     * @param servicefile
     */
    public void setServicefile(String servicefile)
    {
        this.servicefile = servicefile;
    }


    public void execute() throws XDocletException
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(servicefile + "-service.xml");
        setHavingClassTag("jmx:mbean");
        startProcess();

    }
}
