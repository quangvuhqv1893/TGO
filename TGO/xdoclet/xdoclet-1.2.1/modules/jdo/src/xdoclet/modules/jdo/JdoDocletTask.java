/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jdo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tools.ant.BuildException;

import xdoclet.DocletTask;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;

/**
 * This task that executes various JDO-specific sub-tasks.
 *
 * @author        Ludovic Claude (ludovicc@users.sourceforge.net)
 * @created       June 11, 20012
 * @version       $Revision: 1.2 $
 * @ant.element   name="jdodoclet" display-name="JDO Task"
 */
public class JdoDocletTask extends DocletTask
{
    /**
     * Defaults to JDO 1.0.
     */
    private String  jdospec = JdoSpecVersion.JDO_1_0;

    /**
     * Gets the JdoSpec attribute of the JdoDocletTask object
     *
     * @return   The JdoSpec value
     */
    public String getJdoSpec()
    {
        return jdospec;
    }

    /**
     * @param jdospec
     */
    public void setJdoSpec(JdoSpecVersion jdospec)
    {
        this.jdospec = jdospec.getValue();
    }

    protected void validateOptions() throws BuildException
    {
        super.validateOptions();
    }

    /**
     * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
     * @created   June 11, 20012
     */
    public static class JdoSpecVersion extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String JDO_1_0 = "1.0";

        /**
         * Gets the Values attribute of the JdoSpecVersion object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                JDO_1_0
                });
        }
    }

}
