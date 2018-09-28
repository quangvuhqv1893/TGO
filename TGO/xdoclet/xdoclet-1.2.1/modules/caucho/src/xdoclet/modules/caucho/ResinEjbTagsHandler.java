/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.caucho;

import xjavadoc.XMethod;

import xdoclet.XDocletException;
import xdoclet.modules.ejb.dd.RelationTagsHandler;
import xdoclet.util.DocletUtil;

/**
 * Template tags handler used by resin-ejb.j and resin-relationships.j to add resin cmp-specific configuration
 * (resin.ejb) to the standard deployment descriptor.
 *
 * @author               Yoritaka Sakakura (yori@teardrop.org)
 * @created              June 5, 2002
 * @see                  <a href="http://caucho.com/products/resin-ejb/ejb-ref/resin-ejb-config.xtp">Resin CMP
 *      Configuration</a>
 * @xdoclet.taghandler   namespace="ResinEjb"
 */
public class ResinEjbTagsHandler extends RelationTagsHandler
{
    private final static String RELATION_TAG = "resin-ejb:relation";

    /**
     * Evaluates the body if the left side of the relationship is many and the order-by parameter of the
     * resinejb:relation method-level tag is defined.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasLeftOrderBy(String template)
         throws XDocletException
    {
        if (leftOrderBy() != null)
            generate(template);
    }

    /**
     * Evaluates the body if the left side of the relationship is single and the sql-column parameter of the
     * resinejb:relation method-level tag is defined.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasLeftSqlColumn(String template)
         throws XDocletException
    {
        if (leftSqlColumn() != null)
            generate(template);
    }

    /**
     * Evaluates the body if the right side of the relationship is many and the order-by parameter of the
     * resinejb:relation method-level tag is defined.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasRightOrderBy(String template)
         throws XDocletException
    {
        if (rightOrderBy() != null)
            generate(template);
    }

    /**
     * Evaluates the body if the right side of the relationship is single and the sql-column parameter of the
     * resinejb:relation method-level tag is defined.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasRightSqlColumn(String template)
         throws XDocletException
    {
        if (rightSqlColumn() != null)
            generate(template);
    }

    /**
     * Evaluates the body if either side of the current relation is many and the sql-table parameter of the
     * resinejb:relation method-level tag is defined.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasSqlTable(String template)
         throws XDocletException
    {
        if (sqlTable() != null)
            generate(template);
    }

    /**
     * Returns the order-by for the left side of the current relation, if applicable.
     *
     * @return
     * @exception XDocletException
     * @see                         #ifHasLeftOrderBy(String)
     * @doc.tag                     type="content"
     */
    public String leftOrderBy()
         throws XDocletException
    {
        if (!currentRelation.isLeftMany())
            return null;
        else
            return getMethodTagValue(RELATION_TAG, "order-by", true);
    }

    /**
     * Returns the sql-column for the left side of the current relation, if applicable.
     *
     * @return
     * @exception XDocletException
     * @see                         #ifHasLeftSqlColumn(String)
     * @doc.tag                     type="content"
     */
    public String leftSqlColumn()
         throws XDocletException
    {
        return getMethodTagValue(RELATION_TAG, "sql-column", true);
    }

    /**
     * Returns the order-by for the right side of the current relation, if applicable.
     *
     * @return
     * @exception XDocletException
     * @see                         #ifHasRightOrderBy(String)
     * @doc.tag                     type="content"
     */
    public String rightOrderBy()
         throws XDocletException
    {
        if (!currentRelation.isRightMany())
            return null;
        else
            return getMethodTagValue(RELATION_TAG, "order-by", false);
    }

    /**
     * Returns the sql-column for the right side of the current relation, if applicable.
     *
     * @return
     * @exception XDocletException
     * @see                         #ifHasRightSqlColumn(String)
     * @doc.tag                     type="content"
     */
    public String rightSqlColumn()
         throws XDocletException
    {
        String rightValue = getMethodTagValue(RELATION_TAG, "sql-column", false);

        if (rightValue != null)
            return rightValue;
        else
            return getMethodTagValue(RELATION_TAG, "target-sql-column", true);
    }

    /**
     * Returns the signature of the current method in a form suitable for the
     * /resinejb/enterprise-beans/entity/method/signature element.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String signatureFromMethod()
         throws XDocletException
    {
        XMethod method = getCurrentMethod();

        if (method == null)
            throw L.error(L.NO_CURRENT_METHOD);
        else
            return method.getNameWithSignature(false);
    }

    /**
     * Returns the sql-table of the current relationship, if any.
     *
     * @return
     * @exception XDocletException
     * @see                         #ifHasSqlTable(String)
     * @doc.tag                     type="content"
     */
    public String sqlTable()
         throws XDocletException
    {
        String sqlTable = null;

        if (currentRelation.isLeftMany())
            sqlTable = getMethodTagValue(RELATION_TAG, "sql-table", true);
        if (sqlTable == null && currentRelation.isRightMany())
            sqlTable = getMethodTagValue(RELATION_TAG, "sql-table", false);

        return sqlTable;
    }


    private String getMethodTagValue(String tagName, String paramName, boolean left)
         throws XDocletException
    {
        XMethod method = left ? currentRelation.getLeftMethod() : currentRelation.getRightMethod();

        if (method == null)
            return null;
        else
            return getTagValue(FOR_METHOD, method.getDoc(), tagName, paramName, null, null, false, false);
    }
}
