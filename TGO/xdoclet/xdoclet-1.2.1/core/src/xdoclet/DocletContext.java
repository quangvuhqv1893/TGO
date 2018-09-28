/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   June 19, 2001
 * @version   $Revision: 1.17 $
 */
public class DocletContext
{
    private static DocletContext singleInstance = null;
    private String  destDir;
    private String  mergeDir;
    private String  excludedTags;
    private SubTask[] subTasks;
    private Hashtable properties;
    private HashMap configs;
    private boolean force;
    private boolean verbose;
    private String  addedTags;
    private Map     unmodifiableProperties;
    private SubTask activeSubTask;

    /**
     * Describe what the DocletContext constructor does
     *
     * @param destDir       Describe what the parameter does
     * @param mergeDir      Describe what the parameter does
     * @param excludedTags  Describe what the parameter does
     * @param subTasks      Describe what the parameter does
     * @param properties    Describe what the parameter does
     * @param configs       Describe what the parameter does
     * @param force         Describe what the parameter does
     * @param addedTags
     * @param verbose
     */
    public DocletContext(
        String destDir,
        String mergeDir,
        String excludedTags,
        SubTask[] subTasks,
        Hashtable properties,
        HashMap configs,
        boolean force,
        boolean verbose,
        String addedTags
        )
    {
        this.destDir = destDir;
        this.mergeDir = mergeDir;
        this.excludedTags = excludedTags;
        this.subTasks = subTasks;
        this.properties = properties;
        this.configs = configs;
        this.force = force;
        this.verbose = verbose;
        this.addedTags = addedTags;
    }

    /**
     * Gets the Instance attribute of the DocletContext class
     *
     * @return   The Instance value
     */
    public static DocletContext getInstance()
    {
        return singleInstance;
    }

    /**
     * Sets the SingleInstance attribute of the DocletContext class
     *
     * @param singleInstance  The new SingleInstance value
     */
    public static void setSingleInstance(DocletContext singleInstance)
    {
        DocletContext.singleInstance = singleInstance;
    }

    /**
     * Gets the ActiveSubTask attribute of the DocletContext object
     *
     * @return   The ActiveSubTask value
     */
    public SubTask getActiveSubTask()
    {
        return activeSubTask;
    }

    /**
     * Gets the DestDir attribute of the DocletContext object
     *
     * @return   The DestDir value
     */
    public String getDestDir()
    {
        return destDir;
    }

    /**
     * Gets the MergeDir attribute of the DocletContext object
     *
     * @return   The MergeDir value
     */
    public String getMergeDir()
    {
        return mergeDir;
    }

    /**
     * Gets the Force attribute of the DocletContext object.
     *
     * @return   The Force value
     */
    public boolean isForce()
    {
        return force;
    }

    /**
     * Gets the Verbose attribute of the DocletContext object.
     *
     * @return   The Verbose value
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    public String getAddedTags()
    {
        return addedTags;
    }

    /**
     * Gets the ExcludedTags attribute of the DocletContext object
     *
     * @return   The ExcludedTags value
     */
    public String getExcludedTags()
    {
        if (excludedTags == null) {
            return "";
        }
        else {
            return excludedTags;
        }
    }

    /**
     * Gets the SubTasks attribute of the DocletContext object
     *
     * @return   The SubTasks value
     */
    public SubTask[] getSubTasks()
    {
        return subTasks;
    }

    /**
     * Gets the Property attribute of the DocletContext object
     *
     * @param name  Describe what the parameter does
     * @return      The Property value
     */
    public String getProperty(String name)
    {
        return (String) properties.get(name);
    }

    /**
     * Gets the Properties attribute of the DocletContext object
     *
     * @return   The Properties value
     */
    public Map getProperties()
    {
        // create one unmodifiableProperties instance and serve the world.
        // It's a safe bet because there's no DocletContext.setProperty method
        if (unmodifiableProperties == null) {
            unmodifiableProperties = Collections.unmodifiableMap(properties);
        }

        return unmodifiableProperties;
    }

    /**
     * Gets the ConfigParam attribute of the DocletContext object
     *
     * @param name  Describe what the parameter does
     * @return      The ConfigParam value
     */
    public Object getConfigParam(String name)
    {
        return configs.get(name.toLowerCase());
    }

    /**
     * Gets the SubTaskDefined attribute of the DocletContext object
     *
     * @param subtaskName  Describe what the parameter does
     * @return             The SubTaskDefined value
     */
    public boolean isSubTaskDefined(String subtaskName)
    {
        return getSubTaskBy(subtaskName) != null;
    }

    /**
     * Gets the SubTaskBy attribute of the DocletContext object
     *
     * @param subtaskName  Describe what the parameter does
     * @return             The SubTaskBy value
     */
    public SubTask getSubTaskBy(String subtaskName)
    {
        for (int i = 0; i < subTasks.length; i++) {
            if (subTasks[i] != null && subTasks[i].getSubTaskName().toLowerCase().equals(subtaskName.toLowerCase())) {
                return subTasks[i];
            }
        }

        return null;
    }

    /**
     * Sets the ActiveSubTask attribute of the DocletContext object
     *
     * @param activeSubTask  The new ActiveSubTask value
     */
    public void setActiveSubTask(SubTask activeSubTask)
    {
        this.activeSubTask = activeSubTask;
    }
}
