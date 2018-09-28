/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.bea.wls.web;

import xdoclet.XDocletException;

import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates weblogic.xml deployment descriptor for Web apps.
 *
 * @author        <a href="aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created       October 9, 2001
 * @ant.element   display-name="WebLogic Server" name="weblogicwebxml" parent="xdoclet.modules.web.WebDocletTask"
 * @version       $Revision: 1.7 $
 */
public class WeblogicWebXmlSubTask extends XmlSubTask
{
    /**
     * The default template file - weblogic_web_xml.xdt.
     */
    private final static String DEFAULT_TEMPLATE_FILE = "resources/weblogic_web_xml.xdt";

    /**
     * The generated file name - weblogic.xml.
     */
    private final static String GENERATED_FILE_NAME = "weblogic.xml";

    private final static String WEBLOGIC_WEB_PUBLICID = "-//BEA Systems, Inc.//DTD Web Application 6.0//EN";

    private final static String WEBLOGIC_WEB_SYSTEMID = "http://www.bea.com/servers/wls600/dtd/weblogic-web-jar.dtd";

    private final static String WEBLOGIC_WEB_DTD_FILE_NAME = "resources/weblogic-web-jar.dtd";

    /**
     * The Security Domain, defaults to "" because it is not included if not set in the build.xml.
     */
    private String  securityDomain = "";

    /**
     * The description of the Web App.
     */
    private String  description;

    /**
     * The web app's context root (overrides any value in application.xml)
     */
    private String  contextRoot = null;

    /**
     * The WLS version.
     */
    private String  version = "6.1";

    /**
     * Describe what the WeblogicWebXmlSubTask constructor does
     *
     * @exception XDocletException  Describe the exception
     */
    public WeblogicWebXmlSubTask() throws XDocletException
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setPublicId(WEBLOGIC_WEB_PUBLICID);
        setSystemId(WEBLOGIC_WEB_SYSTEMID);
        setDtdURL(getClass().getResource(WEBLOGIC_WEB_DTD_FILE_NAME));
        setDescription(Translator.getString(XDocletModulesBeaWlsWebMessages.class, XDocletModulesBeaWlsWebMessages.GENERATED_ATTRIBUTE));
    }

    /**
     * Return the WLS version.
     *
     * @return   The WLS version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Return the Security Domain.
     *
     * @return   The Security Domain value
     */
    public String getSecuritydomain()
    {
        return securityDomain;
    }

    /**
     * Return the Description.
     *
     * @return   The Description value
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the Context Root.
     *
     * @return   The ContextRoot value
     */
    public String getContextRoot()
    {
        return contextRoot;
    }

    /**
     * Set the WLS version.
     *
     * @param version  The new Version value
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Set the Security Domain.
     *
     * @param securityDomain  The new Security Domain value
     */
    public void setSecuritydomain(String securityDomain)
    {
        this.securityDomain = securityDomain;
    }

    /**
     * Set the Description.
     *
     * @param description  The new Description value
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Set the Context Root.
     *
     * @param contextRoot  The new ContextRoot value
     */
    public void setContextRoot(String contextRoot)
    {
        this.contextRoot = contextRoot;
    }

}
