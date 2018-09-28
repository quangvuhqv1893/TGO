/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

/**
 * @author    Aslak Hellesøy
 * @created   7. april 2002
 */
public class TagHandlerDefinition
{
    public final String namespace;
    public final String className;

    /**
     * Describe what the TagHandlerDefinition constructor does
     *
     * @param namespace  Describe what the parameter does
     * @param className  Describe what the parameter does
     */
    public TagHandlerDefinition(String namespace, String className)
    {
        this.namespace = namespace;
        this.className = className;
    }
}
