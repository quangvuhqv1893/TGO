/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.jmx;

/**
 * Backwards Compatibility layer
 *
 * @author       Aslak Hellesøy
 * @created      2. september 2002
 * @deprecated
 */
public class JMXDocletTask extends xdoclet.modules.jmx.JMXDocletTask
{
    public JMXDocletTask()
    {
        System.out.println("The xdoclet.jmx.JMXDocletTask is deprecated and will be removed in a future version of XDoclet. Please update your taskdef to use xdoclet.modules.jmx.JMXDocletTask instead.");
    }
}
