/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.portlet;

import org.apache.tools.ant.BuildException;

import xdoclet.DocletTask;

/**
 * This task executes various portlet-specific sub-tasks. Make sure to include the jar file containing Sun's
 * javax.portlet.* classes on the taskdef's classpath.
 *
 * @author        Craig Walls (xdoclet@habuma.com)
 * @created       August 18, 2003
 * @ant.element   name="portletdoclet" display-name="Portlet Task"
 */
public class PortletDocletTask extends DocletTask
{
    protected void validateOptions() throws BuildException
    {
        super.validateOptions();
        checkClass("javax.portlet.Portlet");
    }
}
