/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XDocletTagSupport;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * XML template support tags.
 *
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              Dec 20, 2001
 * @xdoclet.taghandler   namespace="Xml"
 * @version              $Revision: 1.6 $
 */
public class XmlTagsHandler extends XDocletTagSupport
{
    /**
     * Returns the DTD's public ID for an XML template.
     *
     * @return                      The public ID
     * @exception XDocletException  Description of Exception
     * @see                         xdoclet.XmlSubTask#getPublicId()
     * @doc.tag                     type="content"
     */
    public String publicId() throws XDocletException
    {
        try {
            XmlSubTask subTask = (XmlSubTask) getDocletContext().getActiveSubTask();

            return subTask.getPublicId();
        }
        catch (ClassCastException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.NO_XML_TAGS_ALLOWED));
        }
    }


    /**
     * Returns the DTD's system ID for an XML template.
     *
     * @return                      The system ID
     * @exception XDocletException  Description of Exception
     * @see                         xdoclet.XmlSubTask#getSystemId()
     * @doc.tag                     type="content"
     */
    public String systemId() throws XDocletException
    {
        try {
            XmlSubTask subTask = (XmlSubTask) getDocletContext().getActiveSubTask();

            return subTask.getSystemId();
        }
        catch (ClassCastException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.NO_XML_TAGS_ALLOWED));
        }
    }


    /**
     * Returns the schema for an XML template.
     *
     * @return                      The schema
     * @exception XDocletException  Description of Exception
     * @see                         xdoclet.XmlSubTask#getSchema()
     * @doc.tag                     type="content"
     */
    public String schema() throws XDocletException
    {
        try {
            XmlSubTask subTask = (XmlSubTask) getDocletContext().getActiveSubTask();

            return subTask.getSchema();
        }
        catch (ClassCastException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.NO_XML_TAGS_ALLOWED));
        }
    }
}
