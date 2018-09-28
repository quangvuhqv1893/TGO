/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.spring;

import org.apache.tools.ant.BuildException;

import xdoclet.DocletTask;

/**
 * This task executes various sub-tasks specific to the Spring framework.
 *
 * @author        Craig Walls (xdoclet@habuma.com)
 * @created       November 9, 2003
 * @ant.element   name="springdoclet" display-name="Spring Task"
 */
public class SpringDocletTask extends DocletTask
{
    protected void validateOptions() throws BuildException
    {
        super.validateOptions();
    }
}
