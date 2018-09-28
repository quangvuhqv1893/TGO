/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.web;

import xdoclet.XDocletException;

import xdoclet.XmlSubTask;
import xdoclet.modules.jboss.ejb.JBossSubTask;

/**
 * Generates jboss-web.xml deployment descriptor.
 *
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              August 9, 2001
 * @modified             Jon Barnett (jbarnett@pobox.com)
 * @ant.element          display-name="JBoss" name="jbosswebxml" parent="xdoclet.modules.web.WebDocletTask"
 * @xdoclet.merge-file   file="jbossweb-resource-env-ref.xml" relates-to="jboss-web.xml" description="An XML document
 *      containing the optional resource-env-ref elements."
 * @xdoclet.merge-file   file="jbossweb-resource-ref.xml" relates-to="jboss-web.xml" description="An XML document
 *      containing the optional resource-ref elements."
 * @xdoclet.merge-file   file="jbossweb-ejb-ref.xml" relates-to="jboss-web.xml" description="An XML document containing
 *      the optional ejb-ref elements."
 * @xdoclet.merge-file   file="jbossweb-ejb-local-ref.xml" relates-to="jboss-web.xml" description="An XML document
 *      containing the optional ejb-local-ref elements."
 * @version              $Revision: 1.10 $
 */
public class JBossWebXmlSubTask extends XmlSubTask
{
    private final static String JBOSS_WEB_PUBLICID_3_2 = "-//JBoss//DTD Web Application 2.3//EN";
    private final static String JBOSS_WEB_PUBLICID_3_0 = "-//JBoss//DTD Web Application 2.3//EN";
    private final static String JBOSS_WEB_PUBLICID_2_4 = "-//JBoss//DTD Web Application 2.2//EN";

    private final static String JBOSS_WEB_SYSTEMID_3_2 = "http://www.jboss.org/j2ee/dtd/jboss-web_3_2.dtd";
    private final static String JBOSS_WEB_SYSTEMID_3_0 = "http://www.jboss.org/j2ee/dtd/jboss-web_3_0.dtd";
    private final static String JBOSS_WEB_SYSTEMID_2_4 = "http://www.jboss.org/j2ee/dtd/jboss-web.dtd";

    private final static String JBOSS_WEB_DTD_FILE_NAME_3_2 = "resources/jboss-web_3_2.dtd";
    private final static String JBOSS_WEB_DTD_FILE_NAME_3_0 = "resources/jboss-web_3_0.dtd";
    private final static String JBOSS_WEB_DTD_FILE_NAME_2_4 = "resources/jboss-web.dtd";

    /**
     * The default template file - jboss_web_xml.j.
     */
    private static String DEFAULT_TEMPLATE_FILE = "resources/jboss_web_xml.xdt";

    /**
     * The generated file name - jboss-web.xml.
     */
    private static String GENERATED_FILE_NAME = "jboss-web.xml";

    /**
     * The Security Domain, defaults to "" because it is not included if not set in the build.xml.
     */
    protected String securityDomain = "";

    /**
     * The Context Root, defaults to "" because it is not included if not set in the build.xml.
     */
    protected String contextRoot = "";

    /**
     * The Virtual Host, defaults to "" because it is not included if not set in the build.xml.
     */
    protected String virtualHost = "";

    /**
     * The JBoss version to target. Possible subversion are the values present in JBossSubTask.JBossVersionTypes
     */
    protected String version = JBossSubTask.JBossVersionTypes.VERSION_3_2;

    /**
     * Describe what the JBossWebXmlSubTask constructor does
     */
    public JBossWebXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
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
     * Return the Context Root.
     *
     * @return   The Context Root value
     */
    public String getContextroot()
    {
        return contextRoot;
    }

    /**
     * Return the Virtual Host.
     *
     * @return   The Virtual Host value
     */
    public String getVirtualhost()
    {
        return virtualHost;
    }

    /**
     * Get the target JBoss version
     *
     * @return   the version
     */
    public String getVersion()
    {
        return version;
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
     * Set the Context Root.
     *
     * @param contextRoot  The new Context Root value
     */
    public void setContextroot(String contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * Set the Virtual Host.
     *
     * @param virtualHost  The new Virtual Host value
     */
    public void setVirtualhost(String virtualHost)
    {
        this.virtualHost = virtualHost;
    }

    /**
     * Set the target JBoss version
     *
     * @param version  the version
     */
    public void setVersion(JBossSubTask.JBossVersionTypes version)
    {
        this.version = version.getValue();
    }

    public void execute() throws XDocletException
    {
        if (getVersion().equals(JBossSubTask.JBossVersionTypes.VERSION_2_4)) {
            setPublicId(JBOSS_WEB_PUBLICID_2_4);
            setSystemId(JBOSS_WEB_SYSTEMID_2_4);
            setDtdURL(getClass().getResource(JBOSS_WEB_DTD_FILE_NAME_2_4));
        }
        else if (getVersion().equals(JBossSubTask.JBossVersionTypes.VERSION_2_4)) {
            setPublicId(JBOSS_WEB_PUBLICID_3_0);
            setSystemId(JBOSS_WEB_SYSTEMID_3_0);
            setDtdURL(getClass().getResource(JBOSS_WEB_DTD_FILE_NAME_3_0));
        }
        else {
            setPublicId(JBOSS_WEB_PUBLICID_3_2);
            setSystemId(JBOSS_WEB_SYSTEMID_3_2);
            setDtdURL(getClass().getResource(JBOSS_WEB_DTD_FILE_NAME_3_2));
        }

        super.execute();
    }
}

