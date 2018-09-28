/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.macromedia.jrun.web;

import java.io.File;

import xdoclet.XmlSubTask;

/**
 * Generates jrun-web.xml deployment descriptor.
 *
 * @author               Dan Schaffer (dschaffer@macromedia.com)
 * @created              February 7, 2002
 * @ant.element          display-name="JRun" name="jrunwebxml" parent="xdoclet.modules.web.WebDocletTask"
 * @version              $Revision: 1.7 $
 * @xdoclet.merge-file   file="session-config.xml" relates-to="jrun-web.xml" description="An XML document containing the
 *      optional session-config element."
 * @xdoclet.merge-file   file="virtual-mapping.xml" relates-to="jrun-web.xml" description="An XML unparsed entity
 *      containing the virtual-mapping elements."
 */
public class JRunWebXmlSubTask extends XmlSubTask
{
    private final static String JRUN_WEB_PUBLICID = "-//Macromedia, Inc.//DTD jrun-web 4.0//EN";

    private final static String JRUN_WEB_SYSTEMID = "http://www.macromedia.com/dtds/jrun-web.dtd";

    private final static String JRUN_WEB_DTD_FILE_NAME = "resources/jrun-web.dtd";

    /**
     * The default template file - jrun_web_xml.j.
     */
    private static String DEFAULT_TEMPLATE_FILE = "resources/jrun_web_xml.xdt";

    /**
     * The generated file name - jrun-web.xml.
     */
    private static String GENERATED_FILE_NAME = "jrun-web.xml";

    /**
     * The Security Domain, defaults to "" because it is not included if not set in the build.xml.
     */
    protected String contextRoot = "";

    protected String reload = "";

    protected String compile = "";

    protected String loadSystemClassesFirst = "";

    /**
     * Describe what the JRunWebXmlSubTask constructor does
     */
    public JRunWebXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setPublicId(JRUN_WEB_PUBLICID);
        setSystemId(JRUN_WEB_SYSTEMID);
        setDtdURL(getClass().getResource(JRUN_WEB_DTD_FILE_NAME));
    }

    /**
     * Return the Context Root of the web application. This is where the webApp will be installed in the browser (e.g.
     * http://localhost:8100/contextRoot
     *
     * @return   The Context Root
     */
    public String getContextRoot()
    {
        return contextRoot;
    }

    /**
     * Return the Reload setting. Determines whether to automatically reload servlets, helper classes, jsp helper
     * classes.
     *
     * @return   reload setting either true or false
     */
    public String getReload()
    {
        return reload;
    }

    /**
     * Return the Compile setting. Determines whether to automatically compile servlets
     *
     * @return   compile setting either true or false
     */
    public String getCompile()
    {
        return compile;
    }

    /**
     * Return the Load-System-Classes-First setting. Determines whether to load system classpath first or app server
     * classpath.
     *
     * @return   loadSystemClassesFirst setting
     */
    public String getLoadSystemClassesFirst()
    {
        return loadSystemClassesFirst;
    }

    /**
     * Set the Context Root..
     *
     * @param contextRoot  the new context root
     */
    public void setContextRoot(String contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * Set the Reload setting to either true or false.
     *
     * @param reload  new reload value
     */
    public void setReload(String reload)
    {
        if (reload.equalsIgnoreCase("true")) {
            this.reload = "true";
        }
        else {
            this.reload = "false";
        }
    }

    /**
     * Set the Compile setting to either true or false.
     *
     * @param compile  new compile value
     */
    public void setCompile(String compile)
    {
        if (compile.equalsIgnoreCase("true")) {
            this.compile = "true";
        }
        else {
            this.compile = "false";
        }
    }

    /**
     * Set the LoadSystemClassesFirst setting to either true or false.
     *
     * @param loadSystemClassesFirst  new setting
     */
    public void setLoadSystemClassesFirst(String loadSystemClassesFirst)
    {
        if (loadSystemClassesFirst.equalsIgnoreCase("true")) {
            this.loadSystemClassesFirst = "true";
        }
        else {
            this.loadSystemClassesFirst = "false";
        }
    }
}
