/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.openejb.ejb;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
 * Creates openejb-jar.xml deployment descriptors for OpenEJB.
 *
 * @author        Brian Topping (topping@orb.org)
 * @created       January 23, 2004
 * @ant.element   display-name="OpenEJB" name="openejb" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.1 $
 */
public class OpenEJBSubTask extends TemplateSubTask
{
    private final static String DEFAULT_OPENEJB_XML_TEMPLATE_FILE = "resources/openejb-jar_xml.xdt";
    private final static String OPENEJB_XML_FILE_NAME = "openejb-jar.xml";
    private String  openEJBTemplateFile = DEFAULT_OPENEJB_XML_TEMPLATE_FILE;
    private String  OPENEJB_DTD_FILE_NAME = "resources/openejb-jar_1_1.dtd";

    public OpenEJBSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_OPENEJB_XML_TEMPLATE_FILE));
        setDestinationFile(OPENEJB_XML_FILE_NAME);
        setHavingClassTag("openejb");
    }

    public String getOpenEJBTemplateFile()
    {
        return openEJBTemplateFile;
    }

    public void setOpenEJBTemplateFile(String openEJBTemplateFile)
    {
        this.openEJBTemplateFile = openEJBTemplateFile;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // OpenEJB does not require a template url or a destination file
        super.validateOptions();
    }

}
