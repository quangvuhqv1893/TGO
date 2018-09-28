/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.util;

import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;

/**
 * Utility class for doing i18n translations.
 *
 * @author    <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created   Jan 18, 2002
 * @version   $Revision: 1.11 $
 */
public final class Translator
{
    /**
     * All methods/fields are static, so prevent creating instances.
     */
    private Translator()
    {
    }

    /**
     * Gets the String attribute of the Translator class.
     *
     * @param clazz        Messages class containing resource key constants, from which the bundle was generated.
     * @param resourceKey  The resource key to look up in the bundle.
     * @return             The localized string
     */
    public static java.lang.String getString(Class clazz, String resourceKey)
    {
        return getString(clazz, resourceKey, null);
    }

    /**
     * Gets the String attribute of the Translator class.
     *
     * @param clazz        Messages class containing resource key constants, from which the bundle was generated.
     * @param resourceKey  The resource key to look up in the bundle.
     * @param arguments    A string array of the arguments to be substituted for any placeholders ({0}, {1} etc.) in the
     *      resource value string.
     * @return             The localized string
     */
    public static java.lang.String getString(Class clazz, String resourceKey, String[] arguments)
    {
        return getString(clazz.getName() + "Messages", resourceKey, arguments);
    }

    /**
     * Gets the String attribute of the Translator class.
     *
     * @param bundleKey    Bundle name, without the tailing ".resources.Messages".
     * @param resourceKey  The resource key to look up in the bundle.
     * @return             The localized string
     */
    public static java.lang.String getString(String bundleKey, String resourceKey)
    {
        return getString(bundleKey + "Messages", resourceKey, null);
    }

    /**
     * Gets the String attribute of the Translator class.
     *
     * @param bundleKey    Full bundle name, including the tailing ".resources.Messages".
     * @param resourceKey  The resource key to look up in the bundle.
     * @param arguments    A string array of the arguments to be substituted for any placeholders ({0}, {1} etc.) in the
     *      resource value string.
     * @return             The localized string
     */
    public static java.lang.String getString(String bundleKey, String resourceKey, String[] arguments)
    {
        Log log = LogUtil.getLog(Translator.class, "getString");

        if (log.isDebugEnabled()) {
            log.debug("bundleKey=" + bundleKey);
            log.debug("resourceKey=" + resourceKey);

            if (arguments == null) {
                log.debug("no arguments");
            }
            else {
                log.debug("#arguments=" + arguments.length);
                for (int i = 0; i < arguments.length; i++) {
                    log.debug("arg #" + i + '=' + arguments[i]);
                }
            }
        }

        try {
            ResourceBundle bundle = getBundle(bundleKey);
            String resource = bundle.getString(resourceKey.toLowerCase());

            log.debug("resource=" + resource);

            String msg = MessageFormat.format(resource, arguments);

            log.debug("return message: " + msg);

            return msg;
        }
        catch (MissingResourceException e) {
            // No i18n for this one, or we risk an infinite recursion...
            //This message is not very interesting unless you are an
            //xdoclet developer who is currently working on
            //translations, in which case you can set the log level for
            //this class to debug.
            log.debug("XDoclet MISSING RESOURCE: Can't locate resource '" + resourceKey + "' for bundle '" + bundleKey + "'.");
            return resourceKey + " arguments: " + arguments;
        }
    }

    private static String getPackageNameOfClass(Class clazz)
    {
        String class_name = clazz.getName();

        return class_name.substring(0, class_name.lastIndexOf('.'));
    }

    /**
     * Gets the Bundle attribute of the Translator class.
     *
     * @param bundleKey                     The key of the Bundle to return
     * @return                              The Bundle value
     * @exception MissingResourceException  Describe the exception
     */
    private static ResourceBundle getBundle(java.lang.String bundleKey) throws MissingResourceException
    {
        Log log = LogUtil.getLog(Translator.class, "getBundle");

        if (log.isDebugEnabled()) {
            log.debug("bundleKey=" + bundleKey);
        }

        try {
            return ResourceBundle.getBundle(bundleKey);
        }
        catch (MissingResourceException e) {
            // only caught in order to have the chance to log it
            log.debug("bundle not found");
            throw e;
        }
    }
}
