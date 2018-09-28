/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.solarmetric.jdo;

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
 * @created       10 October 2002
 * @version       $Revision: 1.2 $
 * @ant.element   display-name="Solarmetric Kodo support" name="kodo" parent="xdoclet.modules.jdo.JdoDocletTask"
 */
public class KodoSubTask extends VendorExtensionsSubTask
{

    private final static Log LOG = LogFactory.getLog(KodoSubTask.class);

    private String  version = KodoVersionTypes.VERSION_2_3;

    /**
     * Gets the Version attribute of the KodoSubTask object
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
        return "kodo";
    }

    public String getVendorDescription()
    {
        return "KODO by SolarMetric";
    }

    /**
     * The version of Kodo. Supported versions are 2.3.
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is "2.3".
     */
    public void setVersion(KodoVersionTypes version)
    {
        this.version = version.getValue();
    }

    /**
     * @return
     * @exception XDocletException
     * @todo                        lock-column is likely to be standardizable
     */
    protected Collection getClassExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentClass().getDoc();

        if (doc.hasTag(SQL_TABLE_TAG)) {
            String sqlName = doc.getTagAttributeValue(SQL_TABLE_TAG, TABLE_NAME_ATTR);

            extensions.add(new VendorExtension(getVendorName(), "table", sqlName));
            if (doc.hasTag("kodo.table")) {
                XTag tag = doc.getTag("kodo.table");
                String pkColumn = tag.getAttributeValue("pk-column");
                String lockColumn = tag.getAttributeValue("lock-column");
                String classColumn = tag.getAttributeValue("class-column");

                if (pkColumn != null) {
                    extensions.add(new VendorExtension(getVendorName(), "pk-column", pkColumn));

                    String identity = doc.getTagAttributeValue("jdo.persistence-capable", "identity-type");

                    if (identity == null || "datastore".equals(identity)) {
                        LOG.warn(Translator.getString(XDocletModulesSolarmetricMessages.class,
                            XDocletModulesSolarmetricMessages.NOT_SUPPORTED_PK_COLUMN_AND_DATASTORE_IDENTITY));
                    }
                }
                if (lockColumn != null) {
                    extensions.add(new VendorExtension(getVendorName(), "lock-column", lockColumn));
                }
                if (classColumn != null) {
                    extensions.add(new VendorExtension(getVendorName(), "class-column", classColumn));
                }
            }
        }
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
        String collectionType = doc.getTagAttributeValue("jdo.field", "collection-type");

        if (doc.hasTag(SQL_RELATION_TAG)) {
            if (collectionType == null) {
                String style = doc.getTagAttributeValue(SQL_RELATION_TAG, STYLE_ATTR);

                if (STYLE_FOREIGN_KEY_VALUE.equals(style)) {
                    Collection tags = doc.getTags(SQL_FIELD_TAG);

                    for (TagIterator i = XCollections.tagIterator(tags); i.hasNext(); ) {
                        XTag tag = i.next();
                        String columnName = tag.getAttributeValue(COLUMN_NAME_ATTR);
                        String relatedField = tag.getAttributeValue(RELATED_FIELD_ATTR);

                        extensions.add(new VendorExtension(getVendorName(), relatedField + "-data-column", columnName));
                    }
                }
                else {
                    LOG.warn(Translator.getString(XDocletModulesJdoMessages.class,
                        XDocletModulesJdoMessages.NOT_SUPPORTED_TAG_ATTRIBUTE_VALUE,
                        new String[]{SQL_RELATION_TAG, STYLE_ATTR, style, getVendorDescription()}));
                }
            }
            else if ("collection".equals(collectionType) || "array".equals(collectionType)) {
                String style = doc.getTagAttributeValue(SQL_RELATION_TAG, STYLE_ATTR);

                if (STYLE_FOREIGN_KEY_VALUE.equals(style)) {
                    String relatedField = doc.getTagAttributeValue(SQL_RELATION_TAG, RELATED_FIELD_ATTR);

                    extensions.add(new VendorExtension(getVendorName(), "inverse", relatedField));
                }
                else if (STYLE_RELATION_TABLE_VALUE.equals(style)) {
                    String embeddedClassName = doc.getTagAttributeValue("jdo.field", "element-type");
                    XClass embeddedClass = getXJavaDoc().getXClass(embeddedClassName);
                    String relatedCollection = doc.getTagAttributeValue(SQL_RELATION_TAG, RELATED_FIELD_ATTR);
                    XDoc relatedDoc = embeddedClass.getField(relatedCollection).getDoc();

                    extensions.add(new VendorExtension(getVendorName(), "inverse", relatedCollection));

                    String relationTable = doc.getTagAttributeValue(SQL_RELATION_TAG, TABLE_NAME_ATTR);

                    extensions.add(new VendorExtension(getVendorName(), "table", relationTable));

                    Collection tags = doc.getTags(SQL_FIELD_TAG);

                    for (TagIterator i = XCollections.tagIterator(tags); i.hasNext(); ) {
                        XTag tag = i.next();
                        String columnName = tag.getAttributeValue(COLUMN_NAME_ATTR);
                        String relatedField = tag.getAttributeValue(RELATED_FIELD_ATTR);

                        extensions.add(new VendorExtension(getVendorName(), relatedField + "-ref-column", columnName));
                    }

                    Collection targetTags = relatedDoc.getTags(SQL_FIELD_TAG);

                    for (TagIterator i = XCollections.tagIterator(targetTags); i.hasNext(); ) {
                        XTag tag = i.next();
                        String columnName = tag.getAttributeValue(COLUMN_NAME_ATTR);
                        String relatedField = tag.getAttributeValue(RELATED_FIELD_ATTR);

                        extensions.add(new VendorExtension(getVendorName(), relatedField + "-data-column", columnName));
                    }
                }
                else {
                    LOG.warn(Translator.getString(XDocletModulesJdoMessages.class,
                        XDocletModulesJdoMessages.NOT_SUPPORTED_TAG_ATTRIBUTE_VALUE,
                        new String[]{SQL_RELATION_TAG, STYLE_ATTR, style, getVendorDescription()}));
                }
            }
            else {
                LOG.warn(Translator.getString(XDocletModulesJdoMessages.class,
                    XDocletModulesJdoMessages.NOT_SUPPORTED_TAG_ATTRIBUTE_VALUE,
                    new String[]{"jdo.field", "collection-type", collectionType, getVendorDescription()}));
            }

        }
        else if (doc.hasTag(SQL_FIELD_TAG)) {
            String tableName = doc.getTagAttributeValue(SQL_FIELD_TAG, TABLE_NAME_ATTR);
            String columnName = doc.getTagAttributeValue(SQL_FIELD_TAG, COLUMN_NAME_ATTR);

            if ("true".equals(doc.getTagAttributeValue("jdo.field", "primary-key"))) {
                XDoc classDoc = getCurrentClass().getDoc();
                String identity = classDoc.getTagAttributeValue("jdo.persistence-capable", "identity-type");

                if (identity == null || "datastore".equals(identity)) {
                    LOG.warn(Translator.getString(XDocletModulesSolarmetricMessages.class,
                        XDocletModulesSolarmetricMessages.NOT_SUPPORTED_PK_FIELD_AND_DATASTORE_IDENTITY));
                }
            }
            if (tableName != null) {
                extensions.add(new VendorExtension(getVendorName(), "table", tableName));
            }
            extensions.add(new VendorExtension(getVendorName(), "data-column", columnName));

            if (doc.hasTag("kodo.field")) {
                XTag tag = doc.getTag("kodo.field");
                String blob = tag.getAttributeValue("blob");
                String columnLength = tag.getAttributeValue("column-length");
                String columnIndex = tag.getAttributeValue("column-index");
                String ordered = tag.getAttributeValue("ordered");
                String orderColumn = tag.getAttributeValue("order-column");
                String readOnly = tag.getAttributeValue("read-only");

                if (blob != null) {
                    extensions.add(new VendorExtension(getVendorName(), "blob", blob));
                }
                if (columnLength != null) {
                    extensions.add(new VendorExtension(getVendorName(), "column-length", columnLength));
                }
                if (columnIndex != null) {
                    extensions.add(new VendorExtension(getVendorName(), "column-index", columnIndex));
                }
                if (ordered != null) {
                    extensions.add(new VendorExtension(getVendorName(), "ordered", ordered));
                }
                if (orderColumn != null) {
                    extensions.add(new VendorExtension(getVendorName(), "order-column", orderColumn));
                }
                if (readOnly != null) {
                    extensions.add(new VendorExtension(getVendorName(), "read-only", readOnly));
                }
            }

        }

        return extensions;
    }

    protected Collection getCollectionExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

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
     * @todo                        Kodo actually supports the maps, need to implement it here
     */
    protected Collection getMapExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();

        LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_YET_SUPPORTED,
            new String[]{getVendorDescription()}));

        return extensions;
    }

    /**
     * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
     * @created   10 October 2002
     */
    public static class KodoVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_2_3 = "2.3";
        public final static String VERSION_2_3_1 = "2.3.1";
        public final static String VERSION_2_3_2 = "2.3.2";
        public final static String VERSION_2_3_3 = "2.3.3";

        /**
         * Gets the Values attribute of the KodoVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return new String[]{VERSION_2_3, VERSION_2_3_1, VERSION_2_3_2, VERSION_2_3_3};
        }
    }

}
