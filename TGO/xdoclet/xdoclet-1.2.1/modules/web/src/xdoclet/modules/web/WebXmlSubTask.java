/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.web;

import java.util.ArrayList;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates web.xml deployment descriptor.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              June 19, 2001
 * @ant.element          display-name="web.xml" name="deploymentdescriptor" parent="xdoclet.modules.web.WebDocletTask"
 * @version              $Revision: 1.10 $
 * @xdoclet.merge-file   file="web-settings.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      (icon?, display-name?, description?, distributable?, context-param*) elements, to be used instead of generating
 *      them from config parameters."
 * @xdoclet.merge-file   file="filters.xml" relates-to="web.xml" description="An XML unparsed entity containing the
 *      filter elements for any additional filters not processed by XDoclet."
 * @xdoclet.merge-file   file="filter-mappings.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      the filter-mapping elements for any additional filters not processed by XDoclet."
 * @xdoclet.merge-file   file="listeners.xml" relates-to="web.xml" description="An XML unparsed entity containing the
 *      listener elements for any additional listeners not processed by XDoclet."
 * @xdoclet.merge-file   file="servlets.xml" relates-to="web.xml" description="An XML unparsed entity containing the
 *      servlet elements for any additional servlets not processed by XDoclet."
 * @xdoclet.merge-file   file="servlet-mappings.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      the servlet-mapping elements for any additional servlets not processed by XDoclet."
 * @xdoclet.merge-file   file="web-sec-rolerefs-{0}.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing any security-role-ref elements for a servlet, to use instead of generating from web.security-role-ref
 *      tags."
 * @xdoclet.merge-file   file="mime-mappings.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      the mime-mapping elements for the web application."
 * @xdoclet.merge-file   file="welcomefiles.xml" relates-to="web.xml" description="XML document containing a
 *      welcome-file-list element, used instead of welcomeFiles config parameters."
 * @xdoclet.merge-file   file="error-pages.xml" relates-to="web.xml" description="An XML unparsed entity containing the
 *      error-page elements for the web application."
 * @xdoclet.merge-file   file="taglibs.xml" relates-to="web.xml" description="An XML unparsed entity containing taglib
 *      elements, for tag libraries not defined in tagLibs config parameters."
 * @xdoclet.merge-file   file="web-resource-env-refs.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing resource-env-ref elements for any resources not specified by web.resource-env-ref tags."
 * @xdoclet.merge-file   file="ejb-resourcerefs.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      resource-ref entities for any resources not specified in web.resource-ref tags."
 * @xdoclet.merge-file   file="ejb-resourcerefs-{0}.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing resource-ref entities for any resources for a class not specified in web.resource-ref tags."
 * @xdoclet.merge-file   file="web-security.xml" relates-to="web.xml" description="An XML unparsed entity containing the
 *      (security-constraint*, login-config?) elements for the web application."
 * @xdoclet.merge-file   file="web-sec-roles.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      security-role entities for any roles not specified in web.security-role tags."
 * @xdoclet.merge-file   file="web-env-entries.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      env-entry entities for any entries not specified in web.env-entry tags."
 * @xdoclet.merge-file   file="web-env-entries-{0}.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing env-entry entities for any entries for a class not specified in web.env-entry tags."
 * @xdoclet.merge-file   file="web-ejbrefs.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      ejb-ref entities for any EJB references not specified in web.ejb-ref tags."
 * @xdoclet.merge-file   file="web-ejbrefs-{0}.xml" relates-to="web.xml" description="An XML unparsed entity containing
 *      ejb-ref entities for any EJB references for a class not specified in web.ejb-ref tags."
 * @xdoclet.merge-file   file="web-ejbrefs-local.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing ejb-local-ref entities for any EJB local references not specified in web.ejb-local-ref tags."
 * @xdoclet.merge-file   file="web-ejbrefs-local-{0}.xml" relates-to="web.xml" description="An XML unparsed entity
 *      containing ejb-local-ref entities for any EJB local references for a class not specified in web.ejb-local-ref
 *      tags."
 */
public class WebXmlSubTask extends XmlSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/web_xml.xdt";

    private static String GENERATED_FILE_NAME = "web.xml";

    private static String WEBXML_PUBLICID_2_3 = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

    private static String WEBXML_PUBLICID_2_2 = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";

    private static String WEBXML_SYSTEMID_2_3 = "http://java.sun.com/dtd/web-app_2_3.dtd";

    private static String WEBXML_SYSTEMID_2_2 = "http://java.sun.com/j2ee/dtds/web-app_2_2.dtd";

    private static String WEBXML_DTD_FILE_NAME_2_3 = "resources/web-jar-23.dtd";

    private static String WEBXML_DTD_FILE_NAME_2_2 = "resources/web-jar-22.dtd";

    protected String servletSpec = ServletVersionTypes.VERSION_2_3;

    protected String smallIcon = "";

    protected String largeIcon = "";

    protected String displayName = "";

    protected String description = "";

    protected boolean distributable = true;

    protected ArrayList contextParams = new ArrayList();

    protected Integer sessionTimeout = null;

    // container default

    protected ArrayList welcomeFiles = new ArrayList();

    protected ArrayList tagLibs = new ArrayList();

    /**
     * Describe what the WebXmlSubTask constructor does
     */
    public WebXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }

    /**
     * Gets the Servletspec attribute of the WebXmlSubTask object
     *
     * @return   The Servletspec value
     */
    public String getServletspec()
    {
        return servletSpec;
    }

    /**
     * Gets the ContextParams attribute of the WebXmlSubTask object
     *
     * @return   The ContextParams value
     */
    public ArrayList getContextParams()
    {
        return contextParams;
    }

    /**
     * Gets the Smallicon attribute of the WebXmlSubTask object
     *
     * @return   The Smallicon value
     */
    public String getSmallicon()
    {
        return smallIcon;
    }

    /**
     * Gets the Largeicon attribute of the WebXmlSubTask object
     *
     * @return   The Largeicon value
     */
    public String getLargeicon()
    {
        return largeIcon;
    }

    /**
     * Gets the Displayname attribute of the WebXmlSubTask object
     *
     * @return   The Displayname value
     */
    public String getDisplayname()
    {
        return displayName;
    }

    /**
     * Gets the Description attribute of the WebXmlSubTask object
     *
     * @return   The Description value
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the Distributable attribute of the WebXmlSubTask object
     *
     * @return   The Distributable value
     */
    public boolean getDistributable()
    {
        return distributable;
    }

    /**
     * Gets the Sessiontimeout attribute of the WebXmlSubTask object
     *
     * @return   The Sessiontimeout value
     */
    public Integer getSessiontimeout()
    {
        return sessionTimeout;
    }

    /**
     * Gets the WelcomeFiles attribute of the WebXmlSubTask object
     *
     * @return   The WelcomeFiles value
     */
    public ArrayList getWelcomeFiles()
    {
        return welcomeFiles;
    }

    /**
     * Gets the TagLibs attribute of the WebXmlSubTask object
     *
     * @return   The TagLibs value
     */
    public ArrayList getTagLibs()
    {
        return tagLibs;
    }

    /**
     * Sets the Servletspec attribute of the WebXmlSubTask object
     *
     * @param servletSpec  The new Servletspec value
     */
    public void setServletspec(ServletVersionTypes servletSpec)
    {
        this.servletSpec = servletSpec.getValue();
    }

    /**
     * Sets the Smallicon attribute of the WebXmlSubTask object
     *
     * @param smallIcon  The new smallIcon value
     */
    public void setSmallicon(String smallIcon)
    {
        this.smallIcon = smallIcon;
    }

    /**
     * Sets the Largeicon attribute of the WebXmlSubTask object
     *
     * @param largeIcon  The new largeIcon value
     */
    public void setLargeicon(String largeIcon)
    {
        this.largeIcon = largeIcon;
    }

    /**
     * Sets the Displayname attribute of the WebXmlSubTask object
     *
     * @param displayName  The new Displayname value
     */
    public void setDisplayname(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Sets the Description attribute of the WebXmlSubTask object
     *
     * @param description  The new Description value
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets the Distributable attribute of the WebXmlSubTask object
     *
     * @param distributable  The new Distributable value
     */
    public void setDistributable(boolean distributable)
    {
        this.distributable = distributable;
    }

    /**
     * Sets the Sessiontimeout attribute of the WebXmlSubTask object
     *
     * @param session_timeout  The new Sessiontimeout value
     */
    public void setSessiontimeout(Integer session_timeout)
    {
        sessionTimeout = session_timeout;
    }

    /**
     * Sets the TagLibs attribute of the WebXmlSubTask object
     *
     * @param tagLibs  The new TagLibs value
     */
    public void setTagLibs(ArrayList tagLibs)
    {
        this.tagLibs = tagLibs;
    }

    /**
     * Sets the WelcomeFiles attribute of the WebXmlSubTask object
     *
     * @param welcomeFiles  The new WelcomeFiles value
     */
    public void setWelcomeFiles(ArrayList welcomeFiles)
    {
        this.welcomeFiles = welcomeFiles;
    }

    /**
     * Sets the ContextParams attribute of the WebXmlSubTask object
     *
     * @param contextParams  The new ContextParams value
     */
    public void setContextParams(ArrayList contextParams)
    {
        this.contextParams = contextParams;
    }

    /**
     * Describe the method
     *
     * @param cp  Describe the method parameter
     */
    public void addConfiguredContextparam(ContextParam cp)
    {
        contextParams.add(cp);
    }

    /**
     * Describe the method
     *
     * @param file  Describe the method parameter
     */
    public void addConfiguredWelcomefile(WelcomeFile file)
    {
        welcomeFiles.add(file);
    }

    /**
     * Describe the method
     *
     * @param taglib  Describe the method parameter
     */
    public void addConfiguredTaglib(TagLib taglib)
    {
        tagLibs.add(taglib);
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        if (getServletspec().equals("2.2")) {
            setPublicId(WEBXML_PUBLICID_2_2);
            setSystemId(WEBXML_SYSTEMID_2_2);
            setDtdURL(getClass().getResource(WEBXML_DTD_FILE_NAME_2_2));
        }
        else {
            setPublicId(WEBXML_PUBLICID_2_3);
            setSystemId(WEBXML_SYSTEMID_2_3);
            setDtdURL(getClass().getResource(WEBXML_DTD_FILE_NAME_2_3));
        }

        startProcess();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{getDestinationFile()}));
    }

    /**
     * @author    Aslak Hellesøy
     * @created   July 28, 2001
     */
    public static class ContextParam implements java.io.Serializable
    {
        private String paramName = null;

        private String paramValue = null;

        private String description = "";

        /**
         * Gets the Name attribute of the ContextParam object
         *
         * @return   The Name value
         */
        public String getName()
        {
            return paramName;
        }

        /**
         * Gets the Value attribute of the ContextParam object
         *
         * @return   The Value value
         */
        public String getValue()
        {
            return paramValue;
        }

        /**
         * Gets the Description attribute of the ContextParam object
         *
         * @return   The Description value
         */
        public String getDescription()
        {
            return description;
        }

        /**
         * Sets the Name attribute of the ContextParam object
         *
         * @param name  The new Name value
         */
        public void setName(String name)
        {
            paramName = name;
        }

        /**
         * Sets the Value attribute of the ContextParam object
         *
         * @param value  The new Value value
         */
        public void setValue(String value)
        {
            paramValue = value;
        }

        /**
         * Sets the Description attribute of the ContextParam object
         *
         * @param desc  The new Description value
         */
        public void setDescription(String desc)
        {
            description = desc;
        }
    }

    /**
     * @author    Aslak Hellesøy
     * @created   July 28, 2001
     */
    public static class TagLib implements java.io.Serializable
    {
        private String taglibUri = null;

        private String taglibLocation = null;

        /**
         * Gets the Uri attribute of the TagLib object
         *
         * @return   The Uri value
         */
        public String getUri()
        {
            return taglibUri;
        }


        /**
         * Gets the Location attribute of the TagLib object
         *
         * @return   The Location value
         */
        public String getLocation()
        {
            return taglibLocation;
        }

        /**
         * Sets the Uri attribute of the TagLib object
         *
         * @param uri  The new Uri value
         */
        public void setUri(String uri)
        {
            taglibUri = uri;
        }

        /**
         * Sets the Location attribute of the TagLib object
         *
         * @param location  The new Location value
         */
        public void setLocation(String location)
        {
            taglibLocation = location;
        }
    }

    /**
     * The welcomefile element contains file name to use as a default welcome file, such as index.html.
     *
     * @author    Aslak Hellesøy
     * @created   Sep 18, 2001
     */
    public static class WelcomeFile implements java.io.Serializable
    {
        private String file = null;

        /**
         * Gets the File attribute of the WelcomeFile object
         *
         * @return   The File value
         */
        public String getFile()
        {
            return file;
        }

        /**
         * The welcomefile element contains file name to use as a default welcome file, such as index.html.
         *
         * @param file  The new File value
         */
        public void setFile(String file)
        {
            this.file = file;
        }
    }

    /**
     * @author    Ara Abrahamian (ara_e@email.com)
     * @created   October 20, 2001
     */
    public static class ServletVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_2_2 = "2.2";
        public final static String VERSION_2_3 = "2.3";

        /**
         * Gets the Values attribute of the ServletVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_2_2, VERSION_2_3});
        }
    }
}
