/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.ant.modulesbuilder;

import java.io.File;
import java.util.Enumeration;

import java.util.Vector;

/**
 * Holds information related to a module.
 *
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   Jun 9, 2002
 * @version   $Revision: 1.1 $
 */
final class Module
{
    private Vector  dependencies = new Vector();
    private String  name;
    private boolean executed;
    private File    baseDir;

    /**
     * Returns an enumeration of the dependencies of this target.
     *
     * @return   an enumeration of the dependencies of this target
     */
    public Enumeration getDependencies()
    {
        return dependencies.elements();
    }

    /**
     * Returns the name of this module.
     *
     * @return   the name of this module, or <code>null</code> if the name has not been set yet.
     */
    public String getName()
    {
        return name;
    }

    public boolean isExecuted()
    {
        return executed;
    }

    public File getBaseDir()
    {
        return baseDir;
    }

    /**
     * Sets the name of this module.
     *
     * @param name  The name of this module. Should not be <code>null</code>.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public void setExecuted(boolean executed)
    {
        this.executed = executed;
    }

    public void setBaseDir(File baseDir)
    {
        this.baseDir = baseDir;
    }

    /**
     * Adds a dependency to this target.
     *
     * @param dependency  The name of a target this target is dependent on. Must not be <code>null</code>.
     */
    public void addDependency(String dependency)
    {
        dependencies.addElement(dependency);
    }
}
