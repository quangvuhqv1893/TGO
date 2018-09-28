/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.webwork;

import java.io.File;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
 * Generates HTML file containing description of defined WebWork actions.
 *
 * @author        Rickard Oberg (rickard@xpedio.com)
 * @created       September 4, 2001
 * @ant.element   display-name="WebWork Action Docs" name="webworkactiondocs" parent="xdoclet.modules.web.WebDocletTask"
 * @version       $Revision: 1.4 $
 */
public class WebWorkActionDocsSubTask extends TemplateSubTask
{

    private final static String DEFAULT_TEMPLATE_FILE = "resources/webwork_actions.xdt";

    private final static String GENERATED_FILE_NAME = "actions.html";

    String          javadocDir = null;

    /**
     * Describe what the WebWorkActionDocsSubTask constructor does
     */
    public WebWorkActionDocsSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }

    /**
     * Gets the JavadocDir attribute of the WebWorkActionDocsSubTask object
     *
     * @return   The JavadocDir value
     */
    public String getJavadocDir()
    {
        return javadocDir;
    }

    /**
     * Sets the JavadocDir attribute of the WebWorkActionDocsSubTask object
     *
     * @param javadocDir  The new JavadocDir value
     */
    public void setJavadocDir(String javadocDir)
    {
        this.javadocDir = javadocDir;
    }
}
