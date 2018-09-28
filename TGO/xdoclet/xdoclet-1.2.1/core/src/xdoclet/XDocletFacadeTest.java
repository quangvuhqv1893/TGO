/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.FileSet;

import junit.framework.TestCase;
import xdoclet.DocletTask;
import xdoclet.SubTask;
import xdoclet.XDocletFacade;
//import xdoclet.modules.externalizer.ExternalizerSubTask;

/*
 * @author          Ara Abrahamian (ara_e_w@yahoo.com)
 * @created         Aug 25, 2002
 * @version         $Revision: 1.3 $
 */
/**
 * @created   August 25, 2002
 */
public class XDocletFacadeTest extends TestCase
{
    public XDocletFacadeTest(String name)
    {
        super(name);
    }

    public void testProcess()
    {
        /*
         * XDocletFacade xdoclet = new XDocletFacade();
         * DocletTask task = new DocletTask();
         * task.setDestDir(new File("c:/temp"));
         * FileSet fileset = new FileSet();
         * fileset.setDir(new File("D:\\Projects\\xdoclet-all\\xdoclet\\core\\src"));
         * fileset.setIncludes("xdoclet/XDocletMessages.java");
         * task.addFileset(fileset);
         * /I test against externalizer because it's in xdoclet module
         * ExternalizerSubTask subtask = new ExternalizerSubTask();
         * subtask.setTagName("msg.bundle");
         * subtask.setKeyParamName("id");
         * subtask.setValueParamName("msg");
         * xdoclet.process(task, new SubTask[]{subtask});
         */
    }
}
