/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache;

import java.util.Properties;

import xdoclet.XDocletException;

/**
 * Common interface for the subtemplate engines
 *
 * @author    zluspai
 * @created   July 16, 2003
 */
public interface SubTemplateEngine
{

    /**
     * Set a variable of the engine namespace
     *
     * @param name   The name of the variable
     * @param value  The value of the varialbe
     */
    public void setVariable(String name, Object value);

    /**
     * Get a value of a variable
     *
     * @param name
     * @return
     */
    public Object getVariable(String name);

    /**
     * Clear all variables
     */
    public void clearVariables();

    /**
     * Generate template results
     *
     * @param template                   The template to parse
     * @param attributes
     * @return                           The results
     * @exception XDocletException
     * @throws xdoclet.XDocletException  If any problems
     */
    public String generate(String template, Properties attributes) throws XDocletException;

}

