/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.webwork;

import java.io.*;

import java.net.*;
import java.util.*;
import org.apache.commons.logging.Log;
import xjavadoc.*;

import xdoclet.*;

/**
 * Generates views.properties.
 *
 * @author               Rickard Oberg (rickard@xpedio.com)
 * @created              September 4, 2001
 * @ant.element          display-name="WebWork views.properties" name="webworkconfigproperties"
 *      parent="xdoclet.modules.web.WebDocletTask"
 * @version              $Revision: 1.5 $
 * @xdoclet.merge-file   file="views.properties" relates-to="views.properties" description="Properties file containing
 *      forwards definitions."
 */
public class WebWorkConfigPropertiesSubTask extends TemplateSubTask
{
    protected static String DEFAULT_TEMPLATE_FILE = "resources/webwork_views.xdt";

    protected static String GENERATED_FILE_NAME = "views.properties";

    /**
     * Describe what the WebWorkConfigPropertiesSubTask constructor does
     */
    public WebWorkConfigPropertiesSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        addOfType("webwork.action.Action");
    }

}
