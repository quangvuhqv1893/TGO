/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;

import xdoclet.util.LogUtil;

/**
 * <p>
 *
 * An abstract base class for all sub-tasks. Common code and the contract is defined here.</p> <p>
 *
 * Because of the way Ant is designed all setter methods automatically are settable config parameters. Note that by
 * default init() method inherits default setting from the containing task via DocletContext. Setter methods in sub-task
 * gives the user finer control over config parameters of the sub-task. </p>
 *
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   June 16, 2001
 * @version   $Revision: 1.74 $
 */
public abstract class SubTask extends DocletSupport implements Serializable
{
    private XJavaDoc _xJavaDoc;

    /**
     * Destination directory where generated files will be stored.
     */
    private File    destDir = null;

    /**
     * Merge directory where XDoclet searches for external files that are to be merged. Merge files internal to
     * xdoclet.jar are picked from xdoclet.jar's root instead of using mergeDir which is for external files. By default
     * it inherits its value from the shared DocletContext which is itself filled with values set by user for the task.
     */
    private File    mergeDir = null;

    private ArrayList configParams = new ArrayList();

    /**
     * You can explicitly give the task a distinct name. It's useful for <template/> where if you're going to use config
     * parameters you have to prefix the parameter with a unique subtask name, otherwise it's assumed to be a global
     * config parameter and it means that you can't use the same parameter name in two <template/> instances.
     * Overwriting it with this field explicitly gives you the chance to make the config param a local and solve the
     * above problem.
     */
    private String  subTaskName;

    /**
     * Gets the SubTaskName attribute of the SubTask object
     *
     * @return   The SubTaskName value
     */
    public final String getSubTaskName()
    {
        if (subTaskName != null) {
            return subTaskName;
        }

        return DocletTask.getSubTaskName(getClass());
    }

    /**
     * Gets the ConfigParams attribute of the SubTask object
     *
     * @return   The ConfigParams value
     */
    public List getConfigParams()
    {
        return configParams;
    }

    public Map getConfigParamsAsMap()
    {
        return DocletTask.getConfigParamsAsMap(getConfigParams());
    }


    /**
     * Gets the DestDir attribute of the SubTask object
     *
     * @return   The DestDir value
     */
    public File getDestDir()
    {
        return destDir;
    }

    /**
     * Gets the MergeDir attribute of the SubTask object
     *
     * @return   The MergeDir value
     */
    public File getMergeDir()
    {
        return mergeDir;
    }

    /**
     * Sets an optional name for the subtask that will be seen in XDoclet's debug messages.
     *
     * @param subTaskName
     */
    public void setSubTaskName(String subTaskName)
    {
        this.subTaskName = subTaskName;
    }

    /**
     * Sets the directory where the generated file will be written.
     *
     * @param destDir  The new DestDir value
     */
    public void setDestDir(File destDir)
    {
        this.destDir = destDir;
    }

    /**
     * Specifies the location of the merge directory. This is where XDoclet will look for merge files.
     *
     * @param mergeDir  The new MergeDir value
     */
    public void setMergeDir(File mergeDir)
    {
        this.mergeDir = mergeDir;
    }

    /**
     * Describe the method
     *
     * @param configParam  Describe the method parameter
     */
    public void addConfigParam(ConfigParameter configParam)
    {
        configParams.add(configParam);
    }

    /**
     * Describe what the method does
     *
     * @param src  Describe what the parameter does
     */
    public void copyAttributesFrom(TemplateSubTask src)
    {
        setDestDir(src.getDestDir());
        setMergeDir(src.getMergeDir());
        for (int i = 0; i < src.getConfigParams().size(); i++) {
            addConfigParam((ConfigParameter) src.getConfigParams().get(i));
        }
        setSubTaskName(src.getSubTaskName());
    }

    /**
     * Initializes SubTask. It inherits values of the config parameters if not explicitly defined for this sub-task.
     *
     * @param xJavaDoc
     * @exception XDocletException  Description of Exception
     * @see                         #execute()
     */
    public void init(XJavaDoc xJavaDoc) throws XDocletException
    {
        if (xJavaDoc == null) {
            throw new XDocletException("xJavaDoc can't be null");
        }
        _xJavaDoc = xJavaDoc;

        Log log = LogUtil.getLog(SubTask.class, "init");

        log.debug("mergeDir = " + mergeDir);

        if (destDir == null) {
            // not explicitly set by user, then inherit it from task
            log.debug("destDir inherited it from task");
            destDir = new File(getContext().getDestDir());
        }

        log.debug("destDir = " + destDir);

        if (mergeDir == null && getContext().getMergeDir() != null) {
            // not explicitly set by user, then inherit it from task
            log.debug("mergeDir inherited it from task");
            mergeDir = new File(getContext().getMergeDir());
        }

        log.debug("mergeDir = " + mergeDir);
    }

    /**
     * Called to start execution of the sub-task.
     *
     * @exception XDocletException  Description of Exception
     */
    public abstract void execute() throws XDocletException;

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
    }

    /**
     * A utility method that deleges the call to DocletContext.getSingleInstance().
     *
     * @return   the singleton context object
     */
    protected DocletContext getContext()
    {
        return DocletContext.getInstance();
    }

    protected XJavaDoc getXJavaDoc()
    {
        return _xJavaDoc;
    }
}
