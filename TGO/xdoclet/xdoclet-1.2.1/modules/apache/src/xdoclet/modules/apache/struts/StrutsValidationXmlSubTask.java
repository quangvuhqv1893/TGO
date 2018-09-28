/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.struts;

import xdoclet.XDocletException;
import xdoclet.XmlSubTask;

/**
 * Generates Struts Validator validation.xml deployment descriptor.
 *
 * @author               Erik Hatcher (ehatcher@apache.org)
 * @created              August 23, 2002
 * @ant.element          display-name="validation.xml" name="strutsvalidationxml"
 *      parent="xdoclet.modules.web.WebDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="validation-global.xml" relates-to="validation.xml" description="An XML unparsed entity
 *      containing the global elements for the validation descriptor."
 */
public class StrutsValidationXmlSubTask extends XmlSubTask
{
    private final static String DTD_FILE_NAME_11 = "resources/validation_1_1.dtd";
    private final static String VALIDATION_PUBLICID_11 = "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.0//EN";
    private final static String VALIDATION_SYSTEMID_11 = "http://jakarta.apache.org/commons/dtds/validator_1_0.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/validation_xml.xdt";
    private static String GENERATED_FILE_NAME = "validation.xml";

    /**
     * Describe what the StrutsValidationXmlSubTask constructor does
     */
    public StrutsValidationXmlSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setPublicId(VALIDATION_PUBLICID_11);
        setSystemId(VALIDATION_SYSTEMID_11);
        setDtdURL(getClass().getResource(DTD_FILE_NAME_11));
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     * @todo                        is this method even needed here?
     */
    public void execute() throws XDocletException
    {
        startProcess();
    }
}
