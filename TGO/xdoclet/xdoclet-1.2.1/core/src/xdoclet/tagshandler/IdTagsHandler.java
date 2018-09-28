/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XDocletTagSupport;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Id"
 * @version              $Revision: 1.8 $
 */
public class IdTagsHandler extends XDocletTagSupport
{
    private static Map prefixHash = new HashMap();

    /**
     * Resets the hashtable which backs the prefixId tag.
     */
    public static void reset()
    {
        prefixHash.clear();
    }

    /**
     * @param paramNames            comma-separated of parameter names, first params are in higher priority.
     * @param tagName
     * @return                      Description of the Returned Value
     * @exception XDocletException
     */
    private static String getIdByTagValues(String tagName, String paramNames) throws XDocletException
    {
        Log log = LogUtil.getLog(IdTagsHandler.class, "getIdByTagValues");

        if (tagName == null) {
            log.error(Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.ID_PARAM_MISSING, new String[]{"tagName"}));
            return "";
        }

        if (paramNames == null) {
            log.error(Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.ID_PARAM_MISSING, new String[]{"paramNames"}));
            return "";
        }

        StringTokenizer st = new StringTokenizer(paramNames, ",", false);

        while (st.hasMoreTokens()) {
            String paramValue = getTagValue(
                FOR_CLASS,
                tagName,
                st.nextToken(),
                null,
                null,
                true,
                false
                );

            if (paramValue != null) {
                return paramValue.replace('/', '_');
            }
        }

        return tagName;
    }

    /**
     * Generates an id attribute based on the given prefix. This is used for generating id attribute for XML elements.
     *
     * @param attributes            The attributes of the template tag
     * @return                      An id in the form of &lt;prefix&gt;_&lt;num&gt;
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="prefix" optional="false" description="The tag from which the value of the id
     *      is calculated."
     */
    public String prefixedId(Properties attributes) throws XDocletException
    {
        String useIdsParamName = getDocletContext().getActiveSubTask().getSubTaskName() + ".useIds";
        boolean useIds = ((Boolean) getDocletContext().getConfigParam(useIdsParamName)).booleanValue();

        if (useIds == false)
            return "";

        String prefixName = attributes.getProperty("prefix");
        String wrapInIdEqualsStr = attributes.getProperty("wrapInIdEquals");
        boolean wrapInIdEquals = TypeConversionUtil.stringToBoolean(wrapInIdEqualsStr, true);

        if (prefixName == null)
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.ATTRIBUTE_NOT_SET_ERROR, new String[]{"prefix"}));

        StringBuffer sbuf = new StringBuffer("");

        if (wrapInIdEquals)
            sbuf.append("id=\"");

        if (prefixHash.containsKey(prefixName)) {
            Integer val = (Integer) prefixHash.get(prefixName);
            Integer valPlusOne = new Integer((val.intValue()) + 1);

            prefixHash.put(prefixName, valPlusOne);
            sbuf.append(prefixName);
            sbuf.append('_');
            sbuf.append(valPlusOne);
        }
        else {
            prefixHash.put(prefixName, new Integer(1));
            sbuf.append(prefixName);
            sbuf.append("_1");
        }

        if (wrapInIdEquals) {
            sbuf.append('"');
        }

        return sbuf.toString();
    }

    /**
     * Generates an id attribute based on the given tag values. This is used for generating id attribute for XML
     * elements.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="tagName" optional="false" description="The tag from which the value of the id
     *      is calculated."
     * @doc.param                   name="paramNames" optional="false" description="Comma separated list of parameter
     *      names. The list is ordered, preferred param is before another param which is less important. If the param
     *      exists, its value is taken and used as the id value."
     */
    public String id(Properties attributes) throws XDocletException
    {
        String tagName = attributes.getProperty("tagName");
        String paramNames = attributes.getProperty("paramNames");

        return getIdByTagValues(tagName, paramNames);
    }
}
