/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author    <a href="mailto:pathos@pandora.be">Mathias Bogaert</a>
 * @created   4 mei 2002
 * @version   $Revision: 1.4 $
 */
public final class LogUtil
{
    /**
     * Returns an instance of Jakarta Commons Log object. For now simply return the classname + name as a log.
     *
     * @param clazz  Class (will use clazz.getName() to obtain the class name)
     * @param name   Method name
     * @return       a logger for the specified class and method
     */
    public static Log getLog(Class clazz, String name)
    {
        return LogFactory.getLog(new StringBuffer(clazz.getName()).append('.').append(name).toString());
    }
}
