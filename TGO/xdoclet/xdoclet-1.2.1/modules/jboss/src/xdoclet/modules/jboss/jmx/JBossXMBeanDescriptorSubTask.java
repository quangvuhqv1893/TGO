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
 * Generates xml file for JBossMX.
 *
 * @author        Dmitri Colebatch (dim@bigpond.net.au)
 * @author        <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created       Februrary 17, 2002
 * @ant.element   display-name="JBoss" name="jbossxmbean" parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version       $Revision: 1.7 $
 */
public class JBossXMBeanDescriptorSubTask extends XmlSubTask
{
    //private static String GENERATED_FILE_NAME = "{0}.xml";

    private final static String DD_FILE_NAME = "{0}.xml";

    private final static String DD_PUBLICID = "-//JBoss//DTD JBOSS XMBEAN 1.0//EN";

    private final static String DD_SYSTEMID = "http://www.jboss.org/j2ee/dtd/jboss_xmbean_1_0.dtd";

    private final static String DTD_FILE_NAME = "resources/jboss_xmbean_1_0.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/jbossmx-xml-descriptor.xdt";

    /**
     * Describe what the JBossXMBeanDescriptorSubTask constructor does
     */
    public JBossXMBeanDescriptorSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setHavingClassTag("jboss:xmbean");
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
        setDtdURL(getClass().getResource(DTD_FILE_NAME));

    }

}
