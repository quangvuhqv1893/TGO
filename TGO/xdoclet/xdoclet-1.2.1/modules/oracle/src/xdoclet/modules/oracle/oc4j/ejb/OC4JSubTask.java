/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.oracle.oc4j.ejb;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * Generates OC4J specific deployment descriptor (orion-ejb-jar.xml).
 *
 * @author               <a href="mailto:elango@users.sourceforge.net">Elango</a>
 * @created              April 6, 2003
 * @ant.element          display-name="OC4J" name="oc4j" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.1 $
 * @xdoclet.merge-file   file="oc4j-{0}.xml" relates-to="orion-ejb-jar.xml" description="An XML document containing the
 *      session-deployment, entity-deployment or message-driven-deployment element for a bean, instead of generating it
 *      from tags."
 * @xdoclet.merge-file   file="oc4j-{0}-attributes.xml" relates-to="orion-ejb-jar.xml" description="A text file
 *      containing any attributes for a bean's session-deployment, entity-deployment or message-driven-deployment
 *      element not specified using oc4j.bean tag parameters."
 * @xdoclet.merge-file   file="oc4j-{0}-settings.xml" relates-to="orion-ejb-jar.xml" description="An XML unparsed entity
 *      containing the (env-entry-mapping*, ejb-ref-mapping*, resource-ref-mapping*, resource-env-ref-mapping*) elements
 *      for a bean."
 * @xdoclet.merge-file   file="oc4j-assembly-descriptor.xml" relates-to="orion-ejb-jar.xml" description="An XML document
 *      containing the assembly-descriptor element for orion-ejb-jar.xml."
 */
public class OC4JSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String OC4J_DD_FILE_NAME = "orion-ejb-jar.xml";

    private final static String OC4J_DD_PUBLICID = "-//Evermind//DTD Enterprise JavaBeans 1.1 runtime//EN";

    private final static String OC4J_DD_SYSTEMID = "http://xmlns.oracle.com/ias/dtds/orion-ejb-jar.dtd";

    private final static String OC4J_DTD_FILE_NAME = "resources/orion-ejb-jar.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/oc4j.xdt";

    private String  deploymentVersion = OC4JVersionTypes.VERSION_9_0_3;

    /**
     * Default Constructor.
     */
    public OC4JSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(OC4J_DD_FILE_NAME);
        setPublicId(OC4J_DD_PUBLICID);
        setSystemId(OC4J_DD_SYSTEMID);
        setDtdURL(getClass().getResource(OC4J_DTD_FILE_NAME));
    }

    /**
     * Gets the Version attribute of the OC4JSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return this.deploymentVersion;
    }

    /**
     * Sets the Version attribute of the OC4JSubTask object
     *
     * @param deploymentVersion  The new Version value
     */
    public void setVersion(OC4JVersionTypes deploymentVersion)
    {
        this.deploymentVersion = deploymentVersion.getValue();
    }

    /**
     * Output the processing file name.
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {

        if (getDestinationFile().equals(OC4J_DD_FILE_NAME)) {
            System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{OC4J_DD_FILE_NAME}));
        }

    }

    /**
     * @author    <a href="mailto:itselango@yahoo.com">Elango</a>
     * @created   April 6, 2003
     */
    public static class OC4JVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_9_0_3 = "9.0.3.0.0";
        public final static String VERSION_9_0_4 = "9.0.4.0.0";

        /**
         * Gets the Values attribute of the OC4JVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_9_0_3, VERSION_9_0_4});
        }
    }
}
