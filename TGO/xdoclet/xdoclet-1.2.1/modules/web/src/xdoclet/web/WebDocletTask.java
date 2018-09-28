/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.web;

/**
 * Backwards Compatibility layer
 *
 * @author       Aslak Hellesøy
 * @created      2. september 2002
 * @deprecated
 */
public class WebDocletTask extends xdoclet.modules.web.WebDocletTask
{
    public WebDocletTask()
    {
        System.out.println("The xdoclet.web.WebDocletTask is deprecated and will be removed in a future version of XDoclet. Please update your taskdef to use xdoclet.modules.web.WebDocletTask instead.");
    }
}
