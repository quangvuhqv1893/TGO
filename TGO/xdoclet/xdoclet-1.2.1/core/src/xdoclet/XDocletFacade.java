/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.File;

import org.apache.tools.ant.Project;

/*
 * @author          Ara Abrahamian (ara_e_w@yahoo.com)
 * @created         Augest 22, 2002
 * @version         $Revision: 1.3 $
 */
/**
 * @created   August 25, 2002
 */
public class XDocletFacade
{
    private Project dummyProject;

    public XDocletFacade()
    {
        dummyProject = new Project();

        dummyProject.setName("DummyProject");
    }

    public File getBaseDir()
    {
        return dummyProject.getBaseDir();
    }

    public void setBaseDir(File base_dir)
    {
        dummyProject.setBaseDir(base_dir);
    }

    public void process(DocletTask task, SubTask[] subtasks)
    {
        task.setProject(dummyProject);
        task.setTaskName(task.getClass().getName());

        for (int i = 0; i < subtasks.length; i++) {
            SubTask subtask = subtasks[i];

            task.addSubTask(subtask);
        }

        task.execute();
    }
}
