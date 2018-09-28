/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.ejb;

/**
 * Backwards Compatibility layer
 *
 * @author       Aslak Hellesøy
 * @created      2. september 2002
 * @deprecated
 */
public class EjbDocletTask extends xdoclet.modules.ejb.EjbDocletTask
{
    public EjbDocletTask()
    {
        System.out.println("The xdoclet.ejb.EjbDocletTask is deprecated and will be removed in a future version of XDoclet. Please update your taskdef to use xdoclet.modules.ejb.EjbDocletTask instead.");
    }
}
