/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import xdoclet.util.LogUtil;

/**
 * Used by DocletTask. Creates and returns a HashMap of config params for a DocletTask or SubTask.
 *
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Jan 19, 2002
 * @version   $Revision: 1.8 $
 */
public final class ConfigParamIntrospector
{
    public final static Object NULL = new NullObject();

    /**
     * Describe what the method does
     *
     * @param name  Describe what the parameter does
     * @return      Describe the return value
     */
    public static String capitalize(String name)
    {
        if (name == null || name.trim().length() == 0) {
            return name;
        }

        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
            Character.isLowerCase(name.charAt(0))) {
            return name;
        }

        char chars[] = name.toCharArray();

        chars[0] = Character.toUpperCase(chars[0]);

        return new String(chars);
    }

    /**
     * Describe what the method does
     *
     * @param javabean             Describe what the parameter does
     * @param capitalPropertyName  Describe what the parameter does
     * @return                     Describe the return value
     */
    public static Method findGetterMethod(Object javabean, String capitalPropertyName)
    {
        Log log = LogUtil.getLog(ConfigParamIntrospector.class, "findGetterMethod");

        Method getterMethod = null;

        capitalPropertyName = capitalize(capitalPropertyName);

        try {
            getterMethod = javabean.getClass().getMethod("get" + capitalPropertyName, null);
        }
        catch (NoSuchMethodException e) {
            if (log.isDebugEnabled()) {
                log.error("Method get" + capitalPropertyName + " not found.");
            }
        }

        // try isBlabla if boolean
        if (getterMethod == null) {
            // && args[0].getClass().equals( boolean.class ) )

            try {
                getterMethod = javabean.getClass().getMethod("is" + capitalPropertyName, null);
            }
            catch (NoSuchMethodException e) {
                if (log.isDebugEnabled()) {
                    log.error("Method is" + capitalPropertyName + " not found.");
                }
            }
        }

        return getterMethod;
    }

    /**
     * Describe what the method does
     *
     * @param task     Describe what the parameter does
     * @param configs  Describe what the parameter does
     */
    static void fillConfigParamsFor(DocletTask task, HashMap configs)
    {
        fillConfigParamsHashMapUsingReflectionFor(task, configs, "");
    }

    /**
     * Describe what the method does
     *
     * @param subtask  Describe what the parameter does
     * @param configs  Describe what the parameter does
     */
    static void fillConfigParamsFor(SubTask subtask, HashMap configs)
    {
        fillConfigParamsHashMapUsingReflectionFor(subtask, configs, subtask.getSubTaskName() + '.');
    }

    /**
     * Extract the name of a property from a method name - subtracting a given prefix.
     *
     * @param methodName  The method name
     * @param prefix      The prefix
     * @return            The property name
     */
    private static String getPropertyName(String methodName, String prefix)
    {
        int start = prefix.length();

        return Introspector.decapitalize(methodName.substring(start));
    }

    /**
     * Uses reflection to find javabean properties of an object (should have both setter and getter and fills the
     * hashtable with key="name of the property" and value="the value of that property".
     *
     * @param javabean
     * @param configs
     * @param propertyPrefix
     */
    private static void fillConfigParamsHashMapUsingReflectionFor(Object javabean, HashMap configs, String propertyPrefix)
    {
        Log log = LogUtil.getLog(ConfigParamIntrospector.class, "fillConfigParamsHashMapUsingReflectionFor");

        if (log.isDebugEnabled()) {
            log.debug("javabean=" + javabean);
            log.debug("javabean.getClass()=" + javabean.getClass());
            log.debug("configs.size()=" + configs.size());
        }

        try {
            Method[] methods = javabean.getClass().getMethods();

            for (int i = 0; i < methods.length; i++) {
                final Method method = methods[i];
                final String name = method.getName();
                Class returnType = method.getReturnType();
                Class[] args = method.getParameterTypes();

                // if is a setter method
                if (name.startsWith("set") &&
                    Modifier.isPublic(method.getModifiers()) &&
                    Void.TYPE.equals(returnType) &&
                    args.length == 1) {

                    String propertyName = getPropertyName(name, "set");
                    String capitalPropertyName = capitalize(propertyName);
                    Method getterMethod = null;

                    if (log.isDebugEnabled()) {
                        log.debug("name=" + name);
                        log.debug("propertyName=" + propertyName);
                        log.debug("capitalPropertyName=" + capitalPropertyName);
                    }

                    getterMethod = findGetterMethod(javabean, capitalPropertyName);

                    // discard this property if getter not found
                    if (getterMethod == null) {
                        if (log.isDebugEnabled()) {
                            log.error("Getter method not found.");
                        }
                        continue;
                    }

                    // get its value
                    Object propertyValue = null;

                    try {
                        propertyValue = getterMethod.invoke(javabean, null);
                    }
                    catch (IllegalAccessException e) {
                        if (log.isDebugEnabled()) {
                            log.error("IllegalAccessException", e);
                        }
                        continue;
                    }
                    catch (IllegalArgumentException e) {
                        if (log.isDebugEnabled()) {
                            log.error("IllegalArgumentException", e);
                        }
                        continue;
                    }
                    catch (InvocationTargetException e) {
                        if (log.isDebugEnabled()) {
                            log.error("InvocationTargetException", e);
                        }
                        continue;
                    }

                    if (propertyValue == null) {
                        // hashtable doesn't accept null values
                        propertyValue = NULL;
                    }

                    // for now only accept serializable properties, later when xjavadocs
                    // is ready we can remove this limitation
                    if (!(propertyValue instanceof Serializable)) {
                        continue;
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("putting propertyName=" + (propertyPrefix + propertyName));
                        log.debug("putting propertyValue=" + propertyValue);
                    }

                    configs.put((propertyPrefix + propertyName).toLowerCase(), propertyValue);
                }
            }
        }
        catch (SecurityException e) {
            log.error("A SecurityException exception!!", e);
        }

        log.debug("configs.size()=" + configs.size());
    }

    /**
     * @author    Aslak Hellesøy
     * @created   January 20, 2002
     */
    private final static class NullObject implements Serializable
    {
        /**
         * Simply equals if is of type NullObject (NullObject treated like a singleton).
         *
         * @param obj
         * @return     true if argument is a NullObject
         */
        public boolean equals(Object obj)
        {
            return obj instanceof NullObject;
        }
    }
}
