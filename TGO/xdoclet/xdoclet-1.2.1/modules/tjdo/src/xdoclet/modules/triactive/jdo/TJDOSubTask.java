/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.triactive.jdo;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xjavadoc.TagIterator;
import xjavadoc.XClass;
import xjavadoc.XCollections;
import xjavadoc.XDoc;
import xjavadoc.XJavaDoc;
import xjavadoc.XTag;
import xdoclet.XDocletException;
import xdoclet.modules.jdo.VendorExtension;
import xdoclet.modules.jdo.VendorExtensionsSubTask;
import xdoclet.modules.jdo.XDocletModulesJdoMessages;
import xdoclet.util.Translator;

/**
 * @author        Ludovic Claude (ludovicc@users.sourceforge.net)
 * @author        <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @created       7 November 2002
 * @version       $Revision: 1.2 $
 * @ant.element   display-name="Triactive TJDO support" name="triactive" parent="xdoclet.modules.jdo.JdoDocletTask"
 */
public class TJDOSubTask extends VendorExtensionsSubTask
{

    private final static Log LOG = LogFactory.getLog(TJDOSubTask.class);

    private String  version = TJDOVersionTypes.VERSION_2_0;

    /**
     * Gets the Version attribute of the TJDOSubTask object
     *
     * @return   The Version value
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @return
     * @see      xdoclet.modules.jdo.VendorExtensionsSubTask#getVendorName()
     */
    public String getVendorName()
    {
        return "triactive";
    }

    public String getVendorDescription()
    {
        return "TJDO open source JDO implementation initially developed by Triactive";
    }

    /**
     * The version of TJDO. Supported versions are 2.0.
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is "2.0".
     */
    public void setVersion(TJDOVersionTypes version)
    {
        this.version = version.getValue();
    }

    /**
     * @return
     * @exception XDocletException
     */
    protected Collection getClassExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();

        return extensions;
    }

    /**
     * @return
     * @exception XDocletException
     * @todo                        column-length is likely to be standardizable
     */
    protected Collection getFieldExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

        if (doc.hasTag("tjdo.field")) {
            XTag tag = doc.getTag("tjdo.field");

            addSizeExtensions(extensions, tag, "column-", "");

            String collectionField = tag.getAttributeValue("collection-field");
            String mapField = tag.getAttributeValue("map-field");

            if (collectionField != null) {
                extensions.add(new VendorExtension(getVendorName(), "collection-field", collectionField));
            }
            if (mapField != null) {
                extensions.add(new VendorExtension(getVendorName(), "map-field", mapField));
            }
        }

        return extensions;
    }

    protected Collection getCollectionExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

        if (doc.hasTag("tjdo.field")) {
            XTag tag = doc.getTag("tjdo.field");
            String ownerField = tag.getAttributeValue("owner-field");

            if (ownerField != null) {
                extensions.add(new VendorExtension(getVendorName(), "owner-field", ownerField));
            }
            addSizeExtensions(extensions, tag, "element-", "");
        }

        return extensions;
    }

    protected Collection getArrayExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();

        return extensions;
    }

    /**
     * @return
     * @exception XDocletException
     */
    protected Collection getMapExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

        if (doc.hasTag("tjdo.field")) {
            XTag tag = doc.getTag("tjdo.field");
            String ownerField = tag.getAttributeValue("owner-field");
            String keyField = tag.getAttributeValue("key-field");

            if (ownerField != null) {
                extensions.add(new VendorExtension(getVendorName(), "owner-field", ownerField));
            }
            if (keyField != null) {
                extensions.add(new VendorExtension(getVendorName(), "key-field", keyField));
            }
            addSizeExtensions(extensions, tag, "key-", "key-");

            addSizeExtensions(extensions, tag, "value-", "value-");

        }

        return extensions;
    }

    protected void addSizeExtensions(Collection extensions, XTag tag, String sourcePrefix, String targetPrefix)
    {
        String length = tag.getAttributeValue(sourcePrefix + "length");
        String precision = tag.getAttributeValue(sourcePrefix + "precision");
        String scale = tag.getAttributeValue(sourcePrefix + "scale");

        if (length != null) {
            extensions.add(new VendorExtension(getVendorName(), targetPrefix + "length", length));
        }
        if (precision != null) {
            extensions.add(new VendorExtension(getVendorName(), targetPrefix + "precision", precision));
        }
        if (scale != null) {
            extensions.add(new VendorExtension(getVendorName(), targetPrefix + "scale", scale));
        }

    }

    /**
     * @created   November 7, 2002
     */
    public static class TJDOVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_2_0 = "2.0";

        /**
         * Gets the Values attribute of the TJDOVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return new String[]{VERSION_2_0};
        }
    }

}
