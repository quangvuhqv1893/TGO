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
 * Generates a skeleton {0}-service.xml file for JBoss mbean configuration. This can help you see what you can set in an
 * mbean, you can just fill in your values and deploy. Currently there is a limitation that only managed attributes with
 * getters show up in the results, however there is a comment for those with only a setter. It treats read-only managed
 * attributes as if they can be written.
 *
 * @author        <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created       April 4, 2002
 * @ant.element   display-name="JBoss XML Doc" name="jbossxmldoc" parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version       $Revision: 1.5 $
 */
public class JBossXmlDocSubTask extends XmlSubTask
{
    private final static String DD_FILE_NAME = "{0}-doc.xml";

    private final static String DD_PUBLICID = "-//OASIS//DTD DocBook XML V4.1.2//EN";

    private final static String DD_SYSTEMID = "http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd";

    //private final static String DTD_FILE_NAME = "";

    private static String DEFAULT_TEMPLATE_FILE = "resources/jbossmx-xml-doc.xdt";

    /**
     * Describe what the JBossXmlDocSubTask constructor does
     */
    public JBossXmlDocSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setHavingClassTag("jmx:mbean");
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
    }
}
