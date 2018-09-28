/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jmx;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.util.Translator;

/**
 * Generates MBean interface for JMX MBean.
 *
 * @author               Rickard Oberg (rickard@xpedio.com)
 * @author               <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created              September 4, 2001
 * @ant.element          display-name="MBean Interface" name="mbeaninterface" parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="mbean-custom.xdt" relates-to="MBean files" description="Custom template/code to be
 *      included in the generated MBean classes."
 */
public class MBeanInterfaceSubTask extends TemplateSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/mbean.xdt";

    private static String GENERATED_FILE_NAME = "{0}MBean.java";

    /**
     * Describe what the MBeanInterfaceSubTask constructor does
     */
    public MBeanInterfaceSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setHavingClassTag("jmx:mbean");
    }
}
