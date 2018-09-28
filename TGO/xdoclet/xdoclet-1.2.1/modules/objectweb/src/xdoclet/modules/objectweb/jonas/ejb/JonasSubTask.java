/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.objectweb.jonas.ejb;

import org.apache.tools.ant.types.EnumeratedAttribute;

import xdoclet.XDocletException;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;

/**
 * Generates the deployment descriptor for JOnAS.
 *
 * @author               Richard Chuo (forthy@mac.com)
 * @author               Mathieu Peltier (mathieu.peltier@inrialpes.fr)
 * @author               Philippe Charaux (philippe.charaux@bull.net)
 * @created              11 juillet 2002
 * @ant.element          display-name="JOnAS" name="jonas" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.6 $
 * @xdoclet.merge-file   file="jonas-{0}.xml" relates-to="jonas-ejb-jar.xml" description="An XML document containing the
 *      jonas-session, jonas-entity or jonas-message-driven element for a bean, according to its type."
 */
public class JonasSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    /**
     * Default JOnAS deployment descriptor file name.
     */
    private final static String DEFAULT_JONAS_DD_FILE_NAME = "jonas-ejb-jar.xml";

    /**
     * Default template file.
     */
    private final static String DEFAULT_TEMPLATE_FILE = "resources/jonas.xdt";

    /**
     * JOnAS 2.3 deployment descriptor public ID.
     */
    private final static String JONAS_DD_PUBLICID_2_3
         = "-//ObjectWeb//DTD JOnAS 2.3//EN";

    /**
     * JOnAS 2.3 deployment descriptor system ID.
     */
    private final static String JONAS_DD_SYSTEMID_2_3
         = "@JONAS_ROOT@/xml/jonas-ejb-jar.dtd";

    /**
     * Location of local copy of JOnAS 2.3 DTD.
     */
    private final static String JONAS_DTD_FILE_NAME_2_3
         = "resources/jonas-ejb-jar_2_3.dtd";

    /**
     * JOnAS 2.4 deployment descriptor public ID.
     */
    private final static String JONAS_DD_PUBLICID_2_4
         = "-//ObjectWeb//DTD JOnAS 2.4//EN";

    /**
     * JOnAS 2.4 deployment descriptor system ID.
     */
    private final static String JONAS_DD_SYSTEMID_2_4
         = "http://www.objectweb.org/jonas/dtds/jonas-ejb-jar_2_4.dtd";

    /**
     * Location of local copy of JOnAS 2.4 DTD.
     */
    private final static String JONAS_DTD_FILE_NAME_2_4
         = "resources/jonas-ejb-jar_2_4.dtd";

    /**
     * JOnAS 2.5 deployment descriptor public ID.
     */
    private final static String JONAS_DD_PUBLICID_2_5
         = "-//ObjectWeb//DTD JOnAS 2.5//EN";

    /**
     * JOnAS 2.5 deployment descriptor system ID.
     */
    private final static String JONAS_DD_SYSTEMID_2_5
         = "http://www.objectweb.org/jonas/dtds/jonas-ejb-jar_2_5.dtd";

    /**
     * Location of local copy of JOnAS 2.5 DTD.
     */
    private final static String JONAS_DTD_FILE_NAME_2_5
         = "resources/jonas-ejb-jar_2_5.dtd";

    /**
     * JOnAS 3.0 deployment descriptor public ID.
     */
    private final static String JONAS_DD_PUBLICID_3_0
         = "-//ObjectWeb//DTD JOnAS 3.0//EN";

    /**
     * JOnAS 3.0 deployment descriptor system ID.
     */
    private final static String JONAS_DD_SYSTEMID_3_0
         = "http://www.objectweb.org/jonas/dtds/jonas-ejb-jar_3_0.dtd";

    /**
     * Location of local copy of JOnAS 3.0 DTD.
     */
    private final static String JONAS_DTD_FILE_NAME_3_0
         = "resources/jonas-ejb-jar_3_0.dtd";

    /**
     * JOnAS version to generate files for.
     */
    private String  version = JonasVersionTypes.DEFAULT_VERSION;

    /**
     * Constructor.
     */
    public JonasSubTask()
    {
        super();
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DEFAULT_JONAS_DD_FILE_NAME);
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
     * Sets the version of JOnAS. Supported versions are: 2.3, 2.4, 2.5, 2.6, 3.0
     *
     * @param version      The new version value. Supported versions are: 2.3, 2.4, 2.5, 2.6, 3.0
     * @ant.not-required   No, default is "2.6".
     */
    public void setVersion(JonasVersionTypes version)
    {
        this.version = version.getValue();
    }

    public void execute() throws XDocletException
    {

        if (getVersion().equals(JonasVersionTypes.VERSION_3_0)) {
            if (getPublicId() == null)
                setPublicId(JONAS_DD_PUBLICID_3_0);
            if (getSystemId() == null)
                setSystemId(JONAS_DD_SYSTEMID_3_0);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(JONAS_DTD_FILE_NAME_3_0));
        }
        else if (getVersion().equals(JonasVersionTypes.VERSION_2_5)
            || getVersion().equals(JonasVersionTypes.VERSION_2_6)) {
            // nothing changed between 2.5 & 2.6 for EJBs, so use 2.5 DTD for both
            if (getPublicId() == null)
                setPublicId(JONAS_DD_PUBLICID_2_5);
            if (getSystemId() == null)
                setSystemId(JONAS_DD_SYSTEMID_2_5);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(JONAS_DTD_FILE_NAME_2_5));
        }
        else if (getVersion().equals(JonasVersionTypes.VERSION_2_4)) {
            if (getPublicId() == null)
                setPublicId(JONAS_DD_PUBLICID_2_4);
            if (getSystemId() == null)
                setSystemId(JONAS_DD_SYSTEMID_2_4);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(JONAS_DTD_FILE_NAME_2_4));
        }
        else if (getVersion().equals(JonasVersionTypes.VERSION_2_3)) {
            if (getPublicId() == null)
                setPublicId(JONAS_DD_PUBLICID_2_3);
            if (getSystemId() == null)
                setSystemId(JONAS_DD_SYSTEMID_2_3);
            if (getDtdURL() == null)
                setDtdURL(getClass().getResource(JONAS_DTD_FILE_NAME_2_3));
        }
        startProcess();
    }

    // --------------------------------------------------------------------------

    /**
     * JonasVersionTypes class.
     *
     * @author    Mathieu Peltier.
     * @created   June, 24 2002
     */
    public static class JonasVersionTypes extends EnumeratedAttribute
    {
        public final static String VERSION_2_3 = "2.3";
        public final static String VERSION_2_4 = "2.4";
        public final static String VERSION_2_5 = "2.5";
        public final static String VERSION_2_6 = "2.6";
        public final static String VERSION_3_0 = "3.0";
        public final static String DEFAULT_VERSION = VERSION_2_6;

        /**
         * Gets the possible values.
         *
         * @return   The possible values.
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_2_3, VERSION_2_4, VERSION_2_5, VERSION_2_6, VERSION_3_0});
        }
    }

}
