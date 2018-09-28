/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.caucho;

import xdoclet.modules.web.WebXmlSubTask;

/**
 * Subtask for generation of web.xml with resin extensions.
 *
 * @author               Yoritaka Sakakura (yori@teardrop.org)
 * @created              June 5, 2002
 * @see                  <a href="http://caucho.com/products/resin/ref/app-config.xtp">Web Application Configuration</a>
 * @ant.element          name="resin-web-xml" parent="xdoclet.modules.web.WebDocletTask" display-name="Resin"
 * @xdoclet.merge-file   file="web-settings.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. If present, used instead of generating from config parameters."
 * @xdoclet.merge-file   file="filters.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="filter-mappings.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="servlets.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="servlet-mappings.xml" relates-to="resin-web.xml" description="The standard merge file
 *      from WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the
 *      useStandardMergeFiles config parameter is true."
 * @xdoclet.merge-file   file="mime-mappings.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="taglibs.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="web-resource-env-refs.xml" relates-to="resin-web.xml" description="The standard merge
 *      file from WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the
 *      useStandardMergeFiles config parameter is true."
 * @xdoclet.merge-file   file="ejb-resourcerefs.xml" relates-to="resin-web.xml" description="The standard merge file
 *      from WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the
 *      useStandardMergeFiles config parameter is true."
 * @xdoclet.merge-file   file="web-security.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="web-sec-roles.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="web-env-entries.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="web-ejbrefs.xml" relates-to="resin-web.xml" description="The standard merge file from
 *      WebDocletTask's deploymentdescriptor subtask. Also included in Resin's descriptor if the useStandardMergeFiles
 *      config parameter is true."
 * @xdoclet.merge-file   file="resinweb-custom.xml" relates-to="resin-web.xml" description="Optional top-level merge
 *      path resinweb-custom.xml for resin-only configuration."
 * @xdoclet.merge-file   file="web-sec-rolerefs-{0}.xml" relates-to="resin-web.xml" description="An XML unparsed entity
 *      containing security-role-ref elements for a bean, to use instead of generating from web.security-role-ref tags."
 * @xdoclet.merge-file   file="web-ejbrefs-{0}.xml" relates-to="resin-web.xml" description="An XML unparsed entity
 *      containing ejb-ref elements for a bean, to use instead of generating from web.ejb-ref tags."
 * @xdoclet.merge-file   file="web-env-entries-{0}.xml" relates-to="resin-web.xml" description="An XML unparsed entity
 *      containing env-entry elements for a bean, to use instead of generating from web.env-entry tags."
 * @xdoclet.merge-file   file="ejb-resourcerefs-{0}.xml" relates-to="resin-web.xml" description="An XML unparsed entity
 *      containing resource-ref elements for a bean, to use instead of generating from web.resource-ref tags."
 * @xdoclet.merge-file   file="resin-jndi-link-{0}.xml" relates-to="resin-web.xml" description="An XML unparsed entity
 *      containing jndi-link elements for a bean, to use instead of generating from resin.jndi-link tags."
 */
public class ResinWebXmlSubTask extends WebXmlSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/resin-web-xml.xdt";
    private static String DEST_FILE_NAME = "resin-web.xml";

    private String  appDir = "";
    private String  classUpdateInterval = "";
    private String  configUpdateInterval = "";
    private String  directoryServlet = "";
    private String  encoding = "";
    private String  id = "";
    private String  lazyInit = "";
    private String  secure = "";
    private String  tempDir = "";
    private String  urlRegexp = "";
    private String  workDir = "";

    private String  generateSourceComments = "true";
    private String  searchForConfigElements = "true";
    private String  useStandardMergeFiles = "true";


    public ResinWebXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DEST_FILE_NAME);
        setPublicId(null);
        setSystemId(null);
        setSchema(null);
        setDtdURL(null);
        setValidateXML(false);
    }


    /**
     * web-app/app-dir
     *
     * @return
     */
    public String getAppDir()
    {
        return appDir;
    }

    /**
     * web-app/character-encoding
     *
     * @return
     */
    public String getCharEncoding()
    {
        return encoding;
    }

    /**
     * web-app/class-update-interval
     *
     * @return
     */
    public String getClassUpdateInterval()
    {
        return classUpdateInterval;
    }

    /**
     * web-app/config-update-interval
     *
     * @return
     */
    public String getConfigUpdateInterval()
    {
        return configUpdateInterval;
    }

    /**
     * web-app/directory-servlet
     *
     * @return
     */
    public String getDirectoryServlet()
    {
        return directoryServlet;
    }

    /**
     * Whether or not to include an xml comment for each xdoclet-generated element pointing back to the originating
     * class/method; default true.
     *
     * @return
     */
    public String getGenerateSourceComments()
    {
        return generateSourceComments;
    }

    /**
     * web-app/id
     *
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * web-app/lazy-init
     *
     * @return
     */
    public String getLazyInit()
    {
        return lazyInit;
    }

    /**
     * Whether or not iterate over all classes in search for configuration other than filters, filter-mappings,
     * listeners, servlets, and servlet-mappings; default true.
     *
     * @return
     */
    public String getSearchForConfigElements()
    {
        return searchForConfigElements;
    }

    /**
     * web-app/secure
     *
     * @return
     */
    public String getSecure()
    {
        return secure;
    }

    /**
     * web-app/temp-dir
     *
     * @return
     */
    public String getTempDir()
    {
        return tempDir;
    }

    /**
     * web-app/url-regexp
     *
     * @return
     */
    public String getUrlRegexp()
    {
        return urlRegexp;
    }

    /**
     * Whether or not to use the top-level merge points defined in the standard web_xml.j; default true.
     *
     * @return
     */
    public String getUseStandardMergeFiles()
    {
        return useStandardMergeFiles;
    }

    /**
     * web-app/work-dir
     *
     * @return
     */
    public String getWorkDir()
    {
        return workDir;
    }

    public void setAppDir(String appDir)
    {
        this.appDir = appDir;
    }

    public void setCharEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setClassUpdateInterval(String classUpdateInterval)
    {
        this.classUpdateInterval = classUpdateInterval;
    }

    public void setConfigUpdateInterval(String configUpdateInterval)
    {
        this.configUpdateInterval = configUpdateInterval;
    }

    public void setDirectoryServlet(String directoryServlet)
    {
        this.directoryServlet = directoryServlet;
    }

    public void setGenerateSourceComments(String generate)
    {
        this.generateSourceComments = generate;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setLazyInit(String lazyInit)
    {
        this.lazyInit = lazyInit;
    }

    public void setSearchForConfigElements(String searchForConfigElements)
    {
        this.searchForConfigElements = searchForConfigElements;
    }

    public void setSecure(String secure)
    {
        this.secure = secure;
    }

    public void setTempDir(String tempDir)
    {
        this.tempDir = tempDir;
    }

    public void setUrlRegexp(String regexp)
    {
        this.urlRegexp = regexp;
    }

    public void setUseStandardMergeFiles(String useStandardMergeFiles)
    {
        this.useStandardMergeFiles = useStandardMergeFiles;
    }

    public void setWorkDir(String workDir)
    {
        this.workDir = workDir;
    }
}
