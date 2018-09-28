/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.orion.ejb;

import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;

/**
 * Generates Orion's orion-ejb-jar.xml.
 *
 * @author               Ara Abrahamian (ara_e_w@yahoo.com)
 * @created              May 15, 2001
 * @ant.element          display-name="Orion" name="orion" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="orion-{0}.xml" relates-to="orion-ejb-jar.xml" description="An XML document containing the
 *      session-deployment, entity-deployment or message-driven-deployment element for a bean, instead of generating it
 *      from tags."
 * @xdoclet.merge-file   file="orion-{0}-attributes.xml" relates-to="orion-ejb-jar.xml" description="A text file
 *      containing the attributes for a bean's session-deployment, entity-deployment or message-driven-deployment
 *      element, instead of specifying them with orion.bean tag parameters."
 * @xdoclet.merge-file   file="orion-{0}-settings.xml" relates-to="orion-ejb-jar.xml" description="An XML unparsed
 *      entity containing the (env-entry-mapping*, ejb-ref-mapping*) elements for a bean."
 * @xdoclet.merge-file   file="orion-assembly-descriptor.xml" relates-to="orion-ejb-jar.xml" description="An XML
 *      document containing the assembly-descriptor element for orion-ejb-jar.xml."
 */
public class OrionSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String ORION_DD_FILE_NAME = "orion-ejb-jar.xml";

    private final static String ORION_DD_PUBLICID = "-//Evermind//DTD Enterprise JavaBeans 1.1 runtime//EN";

    private final static String ORION_DD_SYSTEMID = "http://www.orionserver.com/dtds/orion-ejb-jar.dtd";

    private final static String ORION_DTD_FILE_NAME = "resources/orion-ejb-jar.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/orion.xdt";

    private String  version = OrionVersionTypes.VERSION_1_6_0;

    /**
     * Describe what the OrionSubTask constructor does
     */
    public OrionSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(ORION_DD_FILE_NAME);

        setPublicId(ORION_DD_PUBLICID);
        setSystemId(ORION_DD_SYSTEMID);
        setDtdURL(getClass().getResource(ORION_DTD_FILE_NAME));
    }

    /**
     * Gets the DeploymentVersion attribute of the OrionSubTask object
     *
     * @return   The DeploymentVersion value
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Sets the DeploymentVersion attribute of the OrionSubTask object
     *
     * @param version  The new DeploymentVersion value
     */
    public void setVersion(OrionVersionTypes version)
    {
        this.version = version.getValue();
    }

    /**
     * @author    Aslak Hellesøy
     * @created   November 22, 2001
     */
    public static class OrionVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_1_5_0 = "1.5.0";
        public final static String VERSION_1_5_1 = "1.5.1";
        public final static String VERSION_1_5_2 = "1.5.2";
        public final static String VERSION_1_5_3 = "1.5.3";
        public final static String VERSION_1_5_4 = "1.5.4";
        public final static String VERSION_1_6_0 = "1.6.0";
        public final static String VERSION_2_0 = "2.0";

        /**
         * Gets the Values attribute of the OrionVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_1_5_0, VERSION_1_5_1, VERSION_1_5_2, VERSION_1_5_3, VERSION_1_5_4, VERSION_1_6_0, VERSION_2_0});
        }
    }
}
