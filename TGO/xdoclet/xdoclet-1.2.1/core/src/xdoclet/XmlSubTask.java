/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.net.URL;

import org.apache.commons.logging.Log;

import xdoclet.tagshandler.IdTagsHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.XmlValidator;

/**
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       Oct 13, 2001
 * @ant.element   name="xml" parent="xdoclet.DocletTask" display-name="Standard Subtask for XML generation"
 * @version       $Revision: 1.21 $
 */
public class XmlSubTask extends TemplateSubTask
{
    private boolean useIds = false;

    private String  xmlEncoding = "UTF-8";

    /**
     * Flag that indicates whether validation of generated XML should occur.
     */
    private boolean validateXML = false;

    private String  publicId = null;

    private String  systemId = null;

    private URL     dtdURL = null;

    private String  schema = null;

    /**
     * Gets the UseIds attribute of the XmlSubTask object.
     *
     * @return   The UseIds value
     */
    public boolean getUseIds()
    {
        return useIds;
    }

    /**
     * Gets the Xmlencoding attribute of the XmlSubTask object.
     *
     * @return   The Xmlencoding value
     */
    public String getXmlencoding()
    {
        return xmlEncoding;
    }

    /**
     * Gets the DtdURL attribute of the XmlSubTask object.
     *
     * @return   The DtdURL value
     */
    public URL getDtdURL()
    {
        return dtdURL;
    }

    /**
     * Gets the PublicId attribute of the XmlSubTask object.
     *
     * @return   The PublicId value
     */
    public String getPublicId()
    {
        return publicId;
    }

    /**
     * Gets the SystemId attribute of the XmlSubTask object.
     *
     * @return   The SystemId value
     */
    public String getSystemId()
    {
        return systemId;
    }

    /**
     * Gets the Schema attribute of the XmlSubTask object.
     *
     * @return   The Schema value
     */
    public String getSchema()
    {
        return schema;
    }

    /**
     * Gets the ValidateXML attribute of the XmlSubTask object.
     *
     * @return   The ValidateXML value
     */
    public boolean isValidateXML()
    {
        return validateXML;
    }

    /**
     * If this attribute is set to true, XDoclet will generate id attributes in the XML document. Note that this is only
     * available in some subtasks.
     *
     * @param useIds       The new UseIds value
     * @ant.not-required   No. Default is "false"
     */
    public void setUseIds(boolean useIds)
    {
        this.useIds = useIds;
    }

    /**
     * The encoding of the produced xml file. If your XML file uses international characters, you might want to set this
     * to "ISO-8859-1".
     *
     * @param xmlEncoding  The new Xmlencoding value
     * @ant.not-required   No, default is "UTF-8"
     */
    public void setXmlencoding(String xmlEncoding)
    {
        this.xmlEncoding = xmlEncoding;
    }

    /**
     * The XML Schema to which the generated document should conform.
     *
     * @param schema  The new Schema value
     */
    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    /**
     * If this is set to true, the generated XML will be validated against its DTD or XML Schema.
     *
     * @param flag  The new Validatexml value
     */
    public void setValidateXML(boolean flag)
    {
        validateXML = flag;
    }

    /**
     * Describe what the method does
     *
     * @param templateSrc  Describe what the parameter does
     */
    public void copyAttributesFrom(TemplateSubTask templateSrc)
    {
        super.copyAttributesFrom(templateSrc);

        XmlSubTask src = (XmlSubTask) templateSrc;

        setValidateXML(src.isValidateXML());
        setPublicId(src.getPublicId());
        setSystemId(src.getSystemId());
        setDtdURL(src.getDtdURL());
        setSchema(src.getSchema());
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void startProcess() throws XDocletException
    {
        Log log = LogUtil.getLog(XmlSubTask.class, "startProcess");

        super.getXJavaDoc().setDocEncoding(xmlEncoding);

        if (log.isDebugEnabled()) {
            log.debug("isValidateXML()=" + isValidateXML());
            log.debug("getPublicId()=" + getPublicId());
            log.debug("getSystemId()=" + getSystemId());
            log.debug("getDtdURL()=" + getDtdURL());
            log.debug("getSchema()=" + getSchema());
        }

        if (shouldValidate()) {
            XmlValidator.getInstance().registerDTD(getPublicId(), getDtdURL());
        }

        // reset ids counter
        IdTagsHandler.reset();

        super.startProcess();
    }

    /**
     * Sets the DtdURL attribute of the XmlSubTask object.
     *
     * @param dtdURL  The new DtdURL value
     */
    protected void setDtdURL(URL dtdURL)
    {
        this.dtdURL = dtdURL;
    }

    /**
     * The PUBLIC ID of the DTD to which the generated document should conform.
     *
     * @param publicId  The new PublicId value
     */
    protected void setPublicId(String publicId)
    {
        this.publicId = publicId;
    }

    /**
     * The SYSTEM ID of the DTD to which the generated document should conform.
     *
     * @param systemId  The new SystemId value
     */
    protected void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineFinished() throws XDocletException
    {
        Log log = LogUtil.getLog(XmlSubTask.class, "engineFinished");

        log.debug("isValidateXML()=" + isValidateXML());
        if (shouldValidate()) {
            XmlValidator.getInstance().validate(getEngine().getOutput());
        }
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    private boolean shouldValidate()
    {
        return isValidateXML() && getPublicId() != null && getDtdURL() != null;
    }
}
