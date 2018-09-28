/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.webwork;

import xdoclet.XDocletTagSupport;
import xdoclet.tagshandler.ClassTagsHandler;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="WebWork"
 * @version              $Revision: 1.4 $
 */
public class WebWorkTagsHandler extends XDocletTagSupport
{
    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String javaDocHtmlFile()
    {
        // Create path to HTML file in JavaDoc pointing to the action class

        // Possibly prefix with a base directory
        WebWorkActionDocsSubTask webworkSubtask = (WebWorkActionDocsSubTask) getDocletContext().getActiveSubTask();
        String javadocDir = webworkSubtask.getJavadocDir();

        return (javadocDir == null ? "" : javadocDir + "/") + getCurrentClass().getQualifiedName().replace('.', '/') + ".html";
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String commandName()
    {
        String name = getCurrentMethod().getName().substring(2);

        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }
}
