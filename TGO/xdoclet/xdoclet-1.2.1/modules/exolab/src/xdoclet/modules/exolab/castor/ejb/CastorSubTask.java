/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.exolab.castor.ejb;

import java.io.File;
import xdoclet.XDocletException;

import xdoclet.XmlSubTask;

/**
 * Generates mapping.xml deployment descriptor.
 *
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              October 7, 2001
 * @ant.element          display-name="Castor Mapping" name="castormapping" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.6 $
 * @xdoclet.merge-file   file="key-generator.xml" relates-to="mapping.xml" description="An XML unparsed entity
 *      containing the key-generator element(s) for the mapping file."
 */
public class CastorSubTask extends XmlSubTask
{
    private final static String MAPPING_DD_PUBLICID = "-//EXOLAB/Castor Mapping DTD Version 1.0//EN";

    private final static String MAPPING_DD_SYSTEMID = "http://castor.exolab.org/mapping.dtd";

    private static String DEFAULT_TEMPLATE_FILE = "resources/mapping_xml.xdt";

    private static String GENERATED_FILE_NAME = "mapping.xml";

    /**
     * Describe what the CastorSubTask constructor does
     */
    public CastorSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setPublicId(MAPPING_DD_PUBLICID);
        setSystemId(MAPPING_DD_SYSTEMID);
    }
}
