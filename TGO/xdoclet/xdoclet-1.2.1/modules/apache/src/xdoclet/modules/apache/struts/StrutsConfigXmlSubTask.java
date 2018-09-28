/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.struts;

import java.io.File;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XDocletTagSupport;

import xdoclet.XmlSubTask;
import xdoclet.modules.apache.struts.ejb.XDocletModulesApacheStrutsEjbMessages;
import xdoclet.util.Translator;

/**
 * Generates struts-config.xml deployment descriptor.
 *
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              September 2, 2001
 * @ant.element          display-name="struts-config.xml" name="strutsconfigxml"
 *      parent="xdoclet.modules.web.WebDocletTask"
 * @ant.task             ignore="true"
 * @version              $Revision: 1.10 $
 * @xdoclet.merge-file   file="struts-data-sources.xml" relates-to="struts-config.xml" description="An XML document
 *      containing the optional data-sources element."
 * @xdoclet.merge-file   file="struts-forms.xml" relates-to="struts-config.xml" description="An XML unparsed entity
 *      containing form-bean elements, for additional non-XDoclet forms."
 * @xdoclet.merge-file   file="global-exceptions.xml" relates-to="struts-config.xml" description="An XML document
 *      containing the optional global-exceptions element."
 * @xdoclet.merge-file   file="global-forwards.xml" relates-to="struts-config.xml" description="An XML document
 *      containing the optional global-forwards element."
 * @xdoclet.merge-file   file="struts-actions.xml" relates-to="struts-config.xml" description="An XML unparsed entity
 *      containing action elements, for additional non-XDoclet actions."
 * @xdoclet.merge-file   file="actions.xml" relates-to="struts-config.xml" description="Deprecated (renamed to
 *      struts-actions.xml). Still used, but only for backwards compatibility."
 * @xdoclet.merge-file   file="struts-controller.xml" relates-to="struts-config.xml" description="An XML document
 *      containing the optional controller element."
 * @xdoclet.merge-file   file="struts-message-resources.xml" relates-to="struts-config.xml" description="An XML unparsed
 *      entity containing any message-resources elements."
 * @xdoclet.merge-file   file="struts-plugins.xml" relates-to="struts-config.xml" description="An XML unparsed entity
 *      containing any plug-in elements."
 */
public class StrutsConfigXmlSubTask extends XmlSubTask
{
    private final static String STRUTS_PUBLICID_10 = "-//Apache Software Foundation//DTD Struts Configuration 1.0//EN";

    private final static String STRUTS_SYSTEMID_10 = "http://jakarta.apache.org/struts/dtds/struts-config_1_0.dtd";

    private final static String DTD_FILE_NAME_10 = "resources/struts-config_1_0.dtd";

    private final static String STRUTS_PUBLICID_11 = "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN";

    private final static String STRUTS_SYSTEMID_11 = "http://jakarta.apache.org/struts/dtds/struts-config_1_1.dtd";

    private final static String DTD_FILE_NAME_11 = "resources/struts-config_1_1.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/struts_config_xml.xdt";

    private static String GENERATED_FILE_NAME = "struts-config.xml";

    /**
     * Defaults to Struts 1.0.
     */
    private String  strutsVersion = StrutsVersion.STRUTS_1_0;

    /**
     * Sets the controller that the struts application should use. Valid for version 1.1
     */
    private String  controller = "";

    /**
     * Describe what the StrutsConfigXmlSubTask constructor does
     */
    public StrutsConfigXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }

    public String getController()
    {
        return controller;
    }

    /**
     * Gets the Version attribute of the StrutsConfigXmlSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return strutsVersion;
    }

    /**
     * Sets the struts version to use. Legal values are "1.0" and "1.1".
     *
     * @param version
     * @ant.not-required   No. Default is "1.0".
     */
    public void setVersion(StrutsVersion version)
    {
        strutsVersion = version.getValue();
    }

    /**
     * Sets fully qualified class to use when instantiating ActionMapping objects.
     *
     * @param controller
     */
    public void setController(String controller)
    {
        this.controller = controller;
    }

    /**
     * Generate struts-config.xml
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        if (strutsVersion.equals(StrutsVersion.STRUTS_1_0) && (controller.equals("") == false)) {
            throw new XDocletException(Translator.getString(XDocletModulesApacheStrutsEjbMessages.class, XDocletModulesApacheStrutsEjbMessages.BAD_STRUTS_VERSION, new String[]{controller, "1.1", "controller"}));
        }

        if (strutsVersion.equals(StrutsVersion.STRUTS_1_0)) {
            setPublicId(STRUTS_PUBLICID_10);
            setSystemId(STRUTS_SYSTEMID_10);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_10));
        }
        else {
            setPublicId(STRUTS_PUBLICID_11);
            setSystemId(STRUTS_SYSTEMID_11);
            setDtdURL(getClass().getResource(DTD_FILE_NAME_11));
        }
        startProcess();
    }

    /**
     * @created   17. juni 2002
     */
    public static class StrutsVersion extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String STRUTS_1_0 = "1.0";

        public final static String STRUTS_1_1 = "1.1";

        /**
         * Gets the Values attribute of the StrutsVersion object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                STRUTS_1_0, STRUTS_1_1
                });
        }
    }
}
