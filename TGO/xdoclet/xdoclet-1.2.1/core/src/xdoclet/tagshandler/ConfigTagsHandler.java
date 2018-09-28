/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;

import xdoclet.ConfigParamIntrospector;
import xdoclet.SubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.util.LogUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Config"
 * @version              $Revision: 1.8 $
 */
public class ConfigTagsHandler extends XDocletTagSupport
{
    /**
     * The current config parameter name. forAllConfigParameters sets the value while looping over values of the config
     * parameter. Note that this is only used for config parameters of type java.util.ArrayList. Other tags working with
     * config parameters use current config parameter name to lookup the value of that parameter. For each config
     * parameter there exists a corresponding getter method in the subtask, so all public getter methods can be used as
     * config parameters.
     *
     * @see   #currentConfigParamIndex
     * @see   #getConfigParameter(java.lang.String)
     * @see   #forAllConfigParameters(java.lang.String,java.util.Properties)
     */
    private static String currentConfigParam = null;

    /**
     * The current config parameter index. forAllConfigParameters sets the value while looping over values of the config
     * parameter.
     *
     * @see   #currentConfigParam
     * @see   #getConfigParameter(java.lang.String)
     * @see   #forAllConfigParameters(java.lang.String,java.util.Properties)
     */
    private static int currentConfigParamIndex = -1;

    /**
     * Gets the CurrentConfigParamIndex attribute of the ConfigTagsHandler class
     *
     * @return   The CurrentConfigParamIndex value
     */
    public static int getCurrentConfigParamIndex()
    {
        return currentConfigParamIndex;
    }

    /**
     * Returns the value for the specified configuration parameter. Null if the config parameter not found.
     *
     * @param paramName             Description of Parameter
     * @return                      The ConfigParameter value
     * @exception XDocletException  Description of Exception
     * @todo                        When searching for param part of a subtask.element.param config param we rely on
     *      bare reflection mechanism, so if we had getMyParam and we specified myparam for example (case mismatch) it
     *      fails (in Class.getMethod). We should provide a case-insensitive solution.
     */
    public Object getConfigParameter(String paramName) throws XDocletException
    {
        Log log = LogUtil.getLog(ConfigTagsHandler.class, "getConfigParameter");

        paramName = Introspector.decapitalize(paramName);

        SubTask subtask = getDocletContext().getActiveSubTask();

        if (log.isDebugEnabled()) {
            log.debug("subtask=" + subtask.getClass());
            log.debug("currentConfigParamIndex=" + currentConfigParamIndex);
            log.debug("currentConfigParam=" + currentConfigParam);
            log.debug("paramName=" + paramName);
        }

        String configName = paramName;
        Object configValue = null;
        int index = 0;

        index = configName.indexOf('.');

        if (index != -1) {
            // so it's not in global namespace or activesubtask.param

            int index2 = paramName.indexOf('.', index + 1);

            // has one dot. subtaskname.param or activesubtask.element.param format
            if (configValue == null && index2 == -1) {
                // 1. is in subtaskname.param format
                configName = paramName;
                configValue = getDocletContext().getConfigParam(configName);

                // 2. is in activesubtask.element.param format
                if (configValue == null) {
                    String elemname = paramName.substring(0, index);
                    String paramname = paramName.substring(index + 1);

                    configName = subtask.getSubTaskName() + '.' + elemname;
                    configValue = getDocletContext().getConfigParam(configName);

                    // ok we have activesubtask.element value, use reflection to get
                    // the param inside the element
                    if (configValue != null) {
                        // we're in a forAllConfigParams loop for a ArrayList-based config parameter
                        if (currentConfigParamIndex != -1) {
                            log.debug("In a forAllConfigParams loop for an ArrayList-based config parameter.");
                            // param_value = element at currentConfigParamIndex index of the ArrayList
                            configValue = ((java.util.ArrayList) configValue).get(currentConfigParamIndex);
                        }

                        Method getterMethod = ConfigParamIntrospector.findGetterMethod(configValue, paramname);

                        if (getterMethod == null) {
                            configValue = null;
                        }

                        try {
                            configValue = getterMethod.invoke(configValue, null);
                        }
                        catch (Exception e) {
                            log.debug("not found", e);
                        }
                    }
                }
            }
            else {
                // has more than one dot. subtaskname.elem.param format

                // lookup subtaskname.elem
                int lastDotIndex = paramName.lastIndexOf('.');
                String paramname = paramName.substring(lastDotIndex + 1);

                configName = paramName.substring(0, lastDotIndex);
                configValue = getDocletContext().getConfigParam(configName);

                if (configValue != null) {
                    // we're in a forAllConfigParams loop for a ArrayList-based config parameter
                    if (currentConfigParamIndex != -1) {
                        log.debug("In a forAllConfigParams loop for an ArrayList-based config parameter.");

                        // param_value = element at currentConfigParamIndex index of the ArrayList
                        configValue = ((java.util.ArrayList) configValue).get(currentConfigParamIndex);
                    }

                    Method getterMethod = ConfigParamIntrospector.findGetterMethod(configValue, paramname);

                    if (getterMethod == null) {
                        configValue = null;
                    }

                    try {
                        configValue = getterMethod.invoke(configValue, null);
                    }
                    catch (Exception e) {
                        log.debug("not found", e);
                    }
                }
            }
        }
        else {
            // is either in global namespace or activesubtask.param

            // 1. search active subtask
            configName = subtask.getSubTaskName() + '.' + paramName;
            configValue = getDocletContext().getConfigParam(configName);

            // 2. search DocletTask global namespace
            if (configValue == null) {
                configName = paramName;
                configValue = getDocletContext().getConfigParam(paramName);
            }
        }

        // not found at all
        if (configValue == null) {
            return null;
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Config param found:" + paramName);
            }
        }

        // NULL is used instead of null because you can't put a null in a hashtable
        if (configValue.equals(ConfigParamIntrospector.NULL)) {
            configValue = null;
        }

        log.debug("configValue=" + configValue);

        return configValue;
    }

    /**
     * Evaluates the body if config parameter specified is not null.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     */
    public void ifHasConfigParam(String template, Properties attributes) throws XDocletException
    {
        if (!configParameterValue(attributes).equals("")) {
            generate(template);
        }
    }

    /**
     * Returns the values of a configuration parameter with the name paramName.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     */
    public String configParameterValue(Properties attributes) throws XDocletException
    {
        String paramName = attributes.getProperty("paramName");
        Object configParam = getConfigParameter(paramName);

        // if empty collection, treat as no value
        if (configParam instanceof Collection && ((Collection) configParam).isEmpty()) {
            return "";
        }
        // or if zero length array treat as no value
        else if (configParam instanceof Object[] && ((Object[]) configParam).length == 0) {
            return "";
        }
        // otherwise get the value
        else if (configParam != null) {
            return configParam.toString();
        }
        // if nothing there, return empty string
        else {
            return "";
        }
    }

    /**
     * Evaluate the body for all configuration parameters with the name paramName. It's basically used for
     * java.util.ArrayList-based parameter types, and the body is evaluated for all items of the ArrayList.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     */
    public void forAllConfigParameters(String template, Properties attributes) throws XDocletException
    {
        String paramName = attributes.getProperty("paramName");
        ArrayList configParams = (java.util.ArrayList) getConfigParameter(paramName);

        for (int i = 0; i < configParams.size(); i++) {
            currentConfigParam = paramName;
            currentConfigParamIndex = i;

            generate(template);
        }

        currentConfigParam = null;
        currentConfigParamIndex = -1;
    }

    /**
     * Evaluate the body if the value of the configuration parameter is greater or equal to value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifConfigParamGreaterOrEquals(String template, Properties attributes) throws XDocletException
    {
        if (ifConfigParamGreaterOrEquals_Impl(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body if the value of the configuration parameter is not greater or equal to value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifConfigParamNotGreaterOrEquals(String template, Properties attributes) throws XDocletException
    {
        if (!ifConfigParamGreaterOrEquals_Impl(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body if the value of the configuration parameter equals value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifConfigParamEquals(String template, Properties attributes) throws XDocletException
    {
        if (ifConfigParamEquals_Impl(attributes)) {
            generate(template);
        }
    }

    /**
     * Evaluate the body if the value of the configuration parameter doesn't equal value.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The config parameter name, it's a
     *      parameter settable from within build file."
     * @doc.param                   name="value" optional="false" description="The desired value."
     */
    public void ifConfigParamNotEquals(String template, Properties attributes) throws XDocletException
    {
        if (!ifConfigParamEquals_Impl(attributes)) {
            generate(template);
        }
    }

    /**
     * The implementation of ifConfigParamGreaterOrEquals and ifConfigParamNotGreaterOrEquals tags. Currently the value
     * can only be of a float type like "2.0".
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         #ifConfigParamGreaterOrEquals(java.lang.String,java.util.Properties)
     * @see                         #ifConfigParamNotGreaterOrEquals(java.lang.String,java.util.Properties)
     */
    protected boolean ifConfigParamGreaterOrEquals_Impl(Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(ConfigTagsHandler.class, "ifConfigParamGreaterOrEquals_Impl");

        String paramName = attributes.getProperty("paramName");
        String paramValue = attributes.getProperty("value");

        Object configParamValue = getConfigParameter(paramName);

        boolean greaterOrEquals = false;

        if (configParamValue != null) {
            greaterOrEquals = configParamValue.equals(paramValue);
            if (!greaterOrEquals) {
                StringTokenizer configParamTokenizer = new StringTokenizer(configParamValue.toString(), ".");
                StringTokenizer paramTokenizer = new StringTokenizer(paramValue, ".");

                boolean greater = false;
                boolean less = false;

                while (!greater && !less && (configParamTokenizer.hasMoreTokens() ||
                    paramTokenizer.hasMoreTokens())) {
                    int i = configParamTokenizer.hasMoreTokens() ? Integer.parseInt(configParamTokenizer.nextToken()) : 0;
                    int j = paramTokenizer.hasMoreTokens() ? Integer.parseInt(paramTokenizer.nextToken()) : 0;

                    greater = i > j;
                    less = i < j;
                }

                greaterOrEquals = greater || !less;
            }
        }

        return greaterOrEquals;
    }

    /**
     * The implementation of ifConfigParamEquals and ifConfigParamEquals tags. Currently the value can only be of a
     * float type like "2.0".
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @see                         #ifConfigParamEquals(java.lang.String,java.util.Properties)
     * @see                         #ifConfigParamNotEquals(java.lang.String,java.util.Properties)
     */
    protected boolean ifConfigParamEquals_Impl(Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(ConfigTagsHandler.class, "ifConfigParamEquals_Impl");

        String paramName = attributes.getProperty("paramName");
        String paramValue = attributes.getProperty("value");

        Object configParamValue = getConfigParameter(paramName);

        if (log.isDebugEnabled())
            log.debug("paramName='" + paramName +
                "',paramValue='" + paramValue +
                "',configParamValue='" + configParamValue + '\'');

        if (configParamValue == null)
            return false;

        if (configParamValue instanceof Boolean)
            return configParamValue.equals(Boolean.valueOf(paramValue));
        if (configParamValue instanceof Integer)
            return configParamValue.equals(Integer.valueOf(paramValue));

        // Catch-all for Strings and any other types we don't (yet) check for.
        // Any non-strings that get here should be added to the cases above, or
        // the comparison may not be accurate e.g. "1.000" != "1.0000"
        if (!(configParamValue instanceof String))
            log.warn(configParamValue.getClass() + " needs adding to configParamValue types");
        return configParamValue.toString().equals(paramValue);
    }

    /**
     * Say we had <XDtConfig:forAllConfigParameters paramName="tagLibs">, and now we do a
     * <XDtConfig:configParameterValue paramName="tagLibs.uri"/>. What this method does is
     *
     * @param paramName
     * @return           true if it is the same
     */
    private boolean isSubConfigParamInSameConfigParam(String paramName)
    {
        return paramName.equals(currentConfigParam);
    }
}
