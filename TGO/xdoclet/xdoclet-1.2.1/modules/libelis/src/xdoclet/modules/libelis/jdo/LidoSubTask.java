/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.libelis.jdo;

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
 * @ant.element   display-name="Libelis LIDO support" name="lido" parent="xdoclet.modules.jdo.JdoDocletTask"
 */
public class LidoSubTask extends VendorExtensionsSubTask
{

    private final static Log LOG = LogFactory.getLog(LidoSubTask.class);

    private String  version = LidoVersionTypes.VERSION_1_3;

    /**
     * Gets the Version attribute of the LidoSubTask object
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
        return "libelis";
    }

    public String getVendorDescription()
    {
        return "LIDO by Libelis";
    }

    /**
     * The version of Lido. Supported versions are 1.3.
     *
     * @param version      The new Version value
     * @ant.not-required   No, default is "1.3".
     */
    public void setVersion(LidoVersionTypes version)
    {
        this.version = version.getValue();
    }

    protected Collection getClassExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentClass().getDoc();

        if (doc.hasTag(SQL_TABLE_TAG)) {
            String sqlName = doc.getTagAttributeValue(SQL_TABLE_TAG, TABLE_NAME_ATTR);

            extensions.add(new VendorExtension(getVendorName(), "sql-name", sqlName));
        }
        return extensions;
    }

    protected Collection getFieldExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

        if (doc.hasTag(SQL_RELATION_TAG)) {
            String style = doc.getTagAttributeValue(SQL_RELATION_TAG, STYLE_ATTR);

            if (STYLE_FOREIGN_KEY_VALUE.equals(style)) {
                if (doc.getTagAttributeValue("jdo.field", "collection-type") == null) {
                    Collection tags = doc.getTags(SQL_FIELD_TAG);
                    String sqlName = getSqlNames(XCollections.tagIterator(tags));

                    extensions.add(new VendorExtension(getVendorName(), "sql-name", sqlName));
                }
                // else ignore
            }
            else if (STYLE_RELATION_TABLE_VALUE.equals(style)) {
                String tableName = doc.getTagAttributeValue(SQL_RELATION_TAG, TABLE_NAME_ATTR);

                extensions.add(new VendorExtension(getVendorName(), "sql-name", tableName));
            }
            else {
                LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_SUPPORTED_TAG_ATTRIBUTE_VALUE,
                    new String[]{SQL_RELATION_TAG, STYLE_ATTR, style, getVendorDescription()}));
            }

        }
        else if (doc.hasTag(SQL_FIELD_TAG)) {
            // todo: check that the class is a primitive type or standard java-type
            //not sure I got the tag translations right...
            String tableName = doc.getTagAttributeValue(SQL_FIELD_TAG, TABLE_NAME_ATTR);
            String sqlName = doc.getTagAttributeValue(SQL_FIELD_TAG, COLUMN_NAME_ATTR);

            if (tableName != null &&
                !tableName.equals(doc.getTagAttributeValue(SQL_TABLE_TAG, TABLE_NAME_ATTR))) {
                LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_SUPPORTED_MULTIPLE_TABLE_MAPPING,
                    new String[]{getVendorDescription()}));
            }
            extensions.add(new VendorExtension(getVendorName(), "sql-name", sqlName));
        }
        return extensions;
    }

    protected Collection getCollectionExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();
        XDoc doc = getCurrentField().getDoc();

        if (doc.hasTag(SQL_RELATION_TAG)) {
            String style = doc.getTagAttributeValue(SQL_RELATION_TAG, STYLE_ATTR);

            if (STYLE_FOREIGN_KEY_VALUE.equals(style)) {
                String relatedField = doc.getTagAttributeValue(SQL_RELATION_TAG, RELATED_FIELD_ATTR);

                extensions.add(new VendorExtension(getVendorName(), "sql-reverse", "javaField:" + relatedField));
            }
            else if (STYLE_RELATION_TABLE_VALUE.equals(style)) {
                Collection tags = doc.getTags(SQL_FIELD_TAG);
                String sqlName = getSqlNames(XCollections.tagIterator(tags));

                extensions.add(new VendorExtension(getVendorName(), "sql-owner-name", sqlName));

                String embeddedClassName = doc.getTagAttributeValue("jdo.field", "element-type");
                XClass embeddedClass = getXJavaDoc().getXClass(embeddedClassName);
                String relatedCollection = doc.getTagAttributeValue(SQL_RELATION_TAG, RELATED_FIELD_ATTR);
                XDoc relatedDoc = embeddedClass.getField(relatedCollection).getDoc();
                Collection targetTags = relatedDoc.getTags(SQL_FIELD_TAG);
                String targetSqlName = getSqlNames(XCollections.tagIterator(targetTags));

                extensions.add(new VendorExtension(getVendorName(), "sql-value-name", targetSqlName));
            }
            else {
                LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_SUPPORTED_TAG_ATTRIBUTE_VALUE,
                    new String[]{SQL_RELATION_TAG, STYLE_ATTR, style, getVendorDescription()}));
            }

        }
        return extensions;
    }

    protected Collection getArrayExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();

        LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_SUPPORTED_ARRAY,
            new String[]{getVendorDescription()}));

        return extensions;
    }

    protected Collection getMapExtensions() throws XDocletException
    {
        Collection extensions = new ArrayList();

        LOG.warn(Translator.getString(XDocletModulesJdoMessages.class, XDocletModulesJdoMessages.NOT_SUPPORTED_MAP,
            new String[]{getVendorDescription()}));

        return extensions;
    }

    private String getSqlNames(TagIterator i)
    {
        StringBuffer sqlNames = new StringBuffer();

        while (i.hasNext()) {
            XTag tag = i.next();
            String columnName = tag.getAttributeValue(COLUMN_NAME_ATTR);
            String relatedField = tag.getAttributeValue(RELATED_FIELD_ATTR);

            if (sqlNames.length() > 0)
                sqlNames.append(", ");
            sqlNames.append(relatedField + ":" + columnName);
        }
        return sqlNames.toString();
    }

    /**
     * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
     * @created   10 October 2002
     */
    public static class LidoVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_1_3 = "1.3";

        /**
         * Gets the Values attribute of the LidoVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return new String[]{VERSION_1_3};
        }
    }

}
