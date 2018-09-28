/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.objectweb.jonas.web;

import org.apache.tools.ant.types.EnumeratedAttribute;

import xdoclet.XDocletException;
import xdoclet.XmlSubTask;

/**
 * Generates the web application deployment descriptor for JOnAS.
 *
 * @author        <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created       03 October 2002
 * @ant.element   display-name="JOnAS" name="jonaswebxml" parent="xdoclet.modules.web.WebDocletTask"
 * @version       $Revision: 1.1 $
 */
public class JonasWebSubTask extends XmlSubTask
{
    /**
     * Default JOnAS deployment descriptor file name.
     */
    private final static String DEFAULT_JONAS_DD_FILE_NAME = "jonas-web.xml";

    /**
     * Default template file.
     */
    private final static String DEFAULT_TEMPLATE_FILE = "resources/jonas-web_xml.xdt";

    /**
     * Location of local copy of JOnAS 2.6 DTD.
     */
    private final static String JONAS_DTD_FILE_NAME_2_6
         = "resources/jonas-web-app_2_6.dtd";

    /**
     * JOnAS 2.6 deployment descriptor system ID.
     */
    private final static String JONAS_DD_SYSTEMID_2_6
         = "http://www.objectweb.org/jonas/dtds/jonas-web-app_2_6.dtd";

    /**
     * JOnAS 2.6 deployment descriptor public ID.
     */
    private final static String JONAS_DD_PUBLICID_2_6
         = "-//ObjectWeb//DTD JOnAS Web App 2.6//EN";

    /**
     * JOnAS version to generate files for.
     */
    private String  version = JonasVersionTypes.DEFAULT_VERSION;

    /**
     * The host element specifies the name of host used for deploy the web application.
     */
    private String  host = null;

    /**
     * The context-root element specifies the context root for the web application.
     */
    private String  contextRoot = null;

    /**
     * Constructor.
     */
    public JonasWebSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DEFAULT_JONAS_DD_FILE_NAME);
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
     * Return the Host.
     *
     * @return   The Host value
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Gets the {@link #version} attribute.
     *
     * @return   The version value.
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * The context-root element specifies the context root for the web application.
     *
     * @param contextRoot  The new Context Root value
     * @ant.not-required
     */
    public void setContextroot(String contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * The host element specifies the name of host used for deploy the web application.
     *
     * @param host         The new Host value
     * @ant.not-required
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * Sets the version of JOnAS. Supported versions are: 2.6.
     *
     * @param version      The new version value. Supported versions are: 2.6.
     * @ant.not-required   No, default is "2.6".
     */
    public void setVersion(JonasVersionTypes version)
    {
        this.version = version.getValue();
    }

    public void execute() throws XDocletException
    {
        if (getVersion().equals(JonasVersionTypes.VERSION_2_6)) {
            if (getPublicId() == null)
                setPublicId(JONAS_DD_PUBLICID_2_6);
            if (getSystemId() == null)
                setSystemId(JONAS_DD_SYSTEMID_2_6);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(JONAS_DTD_FILE_NAME_2_6));
        }
        startProcess();
    }

    // --------------------------------------------------------------------------

    /**
     * JonasVersionTypes class.
     *
     * @author    <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
     * @created   03 October 2002
     */
    public static class JonasVersionTypes extends EnumeratedAttribute
    {
        public final static String VERSION_2_6 = "2.6";
        public final static String DEFAULT_VERSION = VERSION_2_6;

        /**
         * Gets the possible values.
         *
         * @return   The possible values.
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_2_6});
        }
    }

}
