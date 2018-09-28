/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.webwork;

import xdoclet.XDocletException;

import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates the actions.xml file.
 *
 * @author        Craig Walls, Frank Febbraro (frank@phase2technology.com)
 * @created       April 7, 2003
 * @ant.element   name="webworkactionsxml" display-name="Generates WebWork actions.xml"
 *      parent="xdoclet.modules.web.WebDocletTask"
 * @version       $Revision: 1.1 $
 */
public class WebWorkActionsXmlSubTask extends XmlSubTask
{
    private final static String DD_FILE_NAME = "resources/actions.dtd";

    private final static String DEFAULT_TEMPLATE_FILE = "resources/webwork_actions_xml.xdt";

    private final static String GENERATED_FILE_NAME = "actions.xml";

    /**
     * This sets up the defaults, they can be overriden from within the specific tag.
     */
    public WebWorkActionsXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        addOfType("webwork.action.Action");
    }

    /**
     * Set up the DTD incase the generated XMl will be validated.
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        // For validation purposes
        setDtdURL(getClass().getResource(DD_FILE_NAME));

        startProcess();
    }

    /**
     * Prints out a descriptive message while processing.
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesWebWorkMessages.class, XDocletModulesWebWorkMessages.GENERATING_ACTIONS_XML, new String[]{getDestinationFile()}));
    }
}
