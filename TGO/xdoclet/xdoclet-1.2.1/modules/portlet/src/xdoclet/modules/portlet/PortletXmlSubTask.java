/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.portlet;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates portlet.xml deployment descriptor, per JSR-168.
 *
 * @author               Craig Walls (xdoclet@habuma.com)
 * @created              August 18, 2003
 * @ant.element          display-name="portlet.xml" name="portletxml" parent="xdoclet.modules.portlet.PortletDocletTask"
 * @xdoclet.merge-file   file="custom-portlet-modes.xml" relates-to="portlet.xml" description="An XML unparsed entity
 *      containing custom-portlet-mode entities for a portlet application deployed to a JSR-168 compliant portlet
 *      container.
 * @xdoclet.merge-file   file="portlet-custom-window-states.xml" relates-to="portlet.xml" description="An XML unparsed
 *      entity containing custom-window-state entities for portlet application deployed to a JSR-168 compliant portlet
 *      container.
 * @xdoclet.merge-file   file="portlet-user-attributes.xml" relates-to="portlet.xml" description="An XML unparsed entity
 *      containing user-attribute entities for portlet application deployed to a JSR-168 compliant portlet container.
 * @xdoclet.merge-file   file="portlet-security.xml" relates-to="portlet.xml" description="An XML unparsed entity
 *      containing security-constraint entities for portlet application deployed to a JSR-168 compliant portlet
 *      container.
 */
public class PortletXmlSubTask extends XmlSubTask
{
    private static String DEFAULT_PORTLET_XML_SCHEMA = "http://java.sun.com/xml/portlet.xsd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/portlet_xml.xdt";

    private static String GENERATED_FILE_NAME = "portlet.xml";

    public PortletXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setSchema(DEFAULT_PORTLET_XML_SCHEMA);
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        startProcess();
    }

    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{getDestinationFile()}));
    }
}
