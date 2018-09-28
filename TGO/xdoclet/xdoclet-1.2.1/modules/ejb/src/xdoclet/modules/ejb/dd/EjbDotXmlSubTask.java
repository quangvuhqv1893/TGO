/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dd;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;

import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="ejb-jar.xml" name="deploymentdescriptor"
 *      parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.13 $
 * @xdoclet.merge-file   file="session-beans.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing session elements for beans you wish to include which aren't processed by XDoclet."
 * @xdoclet.merge-file   file="entity-beans.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity containing
 *      entity elements for beans you wish to include which aren't processed by XDoclet."
 * @xdoclet.merge-file   file="message-driven-beans.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing message-driven elements for beans you wish to include which aren't processed by XDoclet."
 * @xdoclet.merge-file   file="ejb-env-entries-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing env-entry elements for a bean, to use instead of generating them from ejb.env-entry tags."
 * @xdoclet.merge-file   file="ejb-ejbrefs-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing (ejb-ref*, ejb-local-ref*) elements for a bean, to use instead of generating them from ejb.ejb-ref
 *      and ejb.ejb-external-ref tags."
 * @xdoclet.merge-file   file="ejb-sec-rolerefs-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing security-role-ref elements for a bean, to use instead of generating them from ejb.security-role-ref
 *      tags."
 * @xdoclet.merge-file   file="ejb-resourcerefs-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing resource-ref elements for a bean, to use instead of generating them from ejb.resource-ref tags."
 * @xdoclet.merge-file   file="ejb-resource-env-refs-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed
 *      entity containing resource-env-ref elements for a bean, to use instead of generating them from
 *      ejb.resource-env-ref tags."
 * @xdoclet.merge-file   file="ejb-finders-{0}.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing query elements for a bean, for additional finder and select methods not defined in the bean class or
 *      its tags."
 * @xdoclet.merge-file   file="assembly-descriptor.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing &quot;additional assembly descriptor info&quot;."
 * @xdoclet.merge-file   file="ejb-security-roles.xml" relates-to="ejb-jar.xml" description="An XML unparsed entity
 *      containing security-role elements, to use instead of generating them from role-name parameters on
 *      ejb.permission, ejb.finder and ejb.pk tags."
 * @xdoclet.merge-file   file="relationships.xml" relates-to="ejb-jar.xml" description="AN XML unparsed entity
 *      containing ejb-relationship elements to add additional relationships that were not generated through Xdoclet."
 */
public class EjbDotXmlSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String DEFAULT_TEMPLATE_FILE = "resources/ejb-jar_xml.xdt";

    private final static String DD_FILE_NAME = "ejb-jar.xml";

    private final static String DD_PUBLICID_11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";

    private final static String DD_SYSTEMID_11 = "http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd";

    private final static String DTD_FILE_NAME_11 = "resources/ejb11-jar.dtd";

    private final static String DD_PUBLICID_20 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";

    private final static String DD_SYSTEMID_20 = "http://java.sun.com/dtd/ejb-jar_2_0.dtd";

    private final static String DTD_FILE_NAME_20 = "resources/ejb20-jar.dtd";

    protected String description;

    protected String displayname;

    protected String smallicon = "";

    protected String largeicon = "";

    protected String clientjar = "false";

    public EjbDotXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DD_FILE_NAME);
        setDescription(Translator.getString(XDocletMessages.class, XDocletMessages.NO_DESCRIPTION));
        setDisplayname(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATED_BY_XDOCLET));
    }

    /**
     * Gets the Smallicon attribute of the EjbDotXmlSubTask object
     *
     * @return   The Smallicon value
     */
    public String getSmallicon()
    {
        return smallicon;
    }

    /**
     * Gets the Largeicon attribute of the EjbDotXmlSubTask object
     *
     * @return   The Largeicon value
     */
    public String getLargeicon()
    {
        return largeicon;
    }


    /**
     * Gets the Displayname attribute of the EjbDotXmlSubTask object
     *
     * @return   The Displayname value
     */
    public String getDisplayname()
    {
        return displayname;
    }

    /**
     * Gets the Description attribute of the EjbDotXmlSubTask object
     *
     * @return   The Description value
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the Clientjar attribute of the EjbDotXmlSubTask object
     *
     * @return   The Clientjar value
     */
    public String getClientjar()
    {
        return clientjar;
    }

    /**
     * Sets the Smallicon attribute of the EjbDotXmlSubTask object
     *
     * @param smallicon  The new Smallicon value
     */
    public void setSmallicon(String smallicon)
    {
        this.smallicon = smallicon;
    }

    /**
     * Sets the Largeicon attribute of the EjbDotXmlSubTask object
     *
     * @param largeicon  The new Largeicon value
     */
    public void setLargeicon(String largeicon)
    {
        this.largeicon = largeicon;
    }

    /**
     * Sets the Displayname attribute of the EjbDotXmlSubTask object
     *
     * @param displayname  The new Displayname value
     */
    public void setDisplayname(String displayname)
    {
        this.displayname = displayname;
    }

    /**
     * Sets the Description attribute of the EjbDotXmlSubTask object
     *
     * @param description  The new Description value
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets the Clientjar attribute of the EjbDotXmlSubTask object
     *
     * @param clientjar  The new Clientjar value
     */
    public void setClientjar(String clientjar)
    {
        this.clientjar = clientjar;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getDestinationFile() == null || getDestinationFile().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"destinationFile"}));
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        if (getContext().getConfigParam("EjbSpec").equals("1.1")) {
            setPublicId(DD_PUBLICID_11);
            setSystemId(DD_SYSTEMID_11);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_11));
        }
        else {
            setPublicId(DD_PUBLICID_20);
            setSystemId(DD_SYSTEMID_20);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_20));
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
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_DD, new String[]{getDestinationFile()}));
    }
}
