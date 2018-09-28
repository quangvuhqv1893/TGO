/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.Properties;
import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.util.DocletUtil;
import xdoclet.util.Translator;

/**
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              Jan 24, 2002
 * @xdoclet.taghandler   namespace="I18n"
 * @version              $Revision: 1.8 $
 */
public class TranslatorTagsHandler extends XDocletTagSupport
{
    /**
     * Returns a localized text string.
     *
     * @param attributes            The attributes of the template tag
     * @return                      The localized string
     * @exception XDocletException  Description of Exception
     * @see                         xdoclet.util.Translator#getString(String,String,String[])
     * @doc.tag                     type="content"
     * @doc.param                   name="bundle" optional="true" description="The base name of the resource bundle to
     *      use e.g. xdoclet.modules.ejb (corresponding to
     *      modules/ejb/src/xdoclet/modules/ejb/resources/Messages.properties) etc. It defaults to xdoclet"
     * @doc.param                   name="resource" optional="false" description="The resource key to look up in the
     *      bundle."
     * @doc.param                   name="arguments" optional="true" description="An optional list of arguments to be
     *      substituted for any placeholders ({0}, {1} etc.) in the resource value string."
     * @doc.param                   name="delimiter" optional="true" description="The arguments parameter is delimited
     *      by the string specified in the delimiter parameter (default is a comma)."
     */
    public String getString(Properties attributes) throws XDocletException
    {
        String bundleKey = attributes.getProperty("bundle");
        String resourceKey = attributes.getProperty("resource");
        String argumentsStr = attributes.getProperty("arguments");
        String delimiter = attributes.getProperty("delimiter");

        String[] arguments = null;

        if (argumentsStr != null) {
            if (delimiter == null) {
                delimiter = PARAMETER_DELIMITER;
            }

            arguments = DocletUtil.tokenizeDelimitedToArray(argumentsStr, delimiter);
        }
        if (bundleKey == null) {
            bundleKey = "xdoclet.XDocletMessages";
        }

        return Translator.getString(bundleKey + "Messages", resourceKey, arguments);
    }

}
