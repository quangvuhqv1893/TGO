/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc;

import java.io.File;

import java.util.List;
import xdoclet.ConfigParameter;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;

/**
 * This tag handler is kinda special. It loops over the module directories in the modules directory and is only used to
 * generate links to XDoclet tag docs. Pretty dirty, but it gets the job done.
 *
 * @author               Aslak Hellesoy
 * @created              13. juni 2002
 * @xdoclet.taghandler   namespace="Module"
 * @version              $Revision: 1.3 $
 */
public class ModuleTagsHandler extends XDocletTagSupport
{
    private String  currentModule;

    private static boolean isModule(File file)
    {
        File module_build_xml = new File(file, "build.xml");

        return (!file.getName().equalsIgnoreCase("build")) && (!file.getName().equalsIgnoreCase("cvs")) && module_build_xml.exists();
    }

    public String moduleName()
    {
        return currentModule;
    }

    /**
     * Iterates over all modules
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void forAllModules(String template) throws XDocletException
    {
        List cps = getDocletContext().getActiveSubTask().getConfigParams();
        ConfigParameter cp = (ConfigParameter) cps.get(0);
        File moduleParentDir = new File(cp.getValue());
        File[] moduleDirs = moduleParentDir.listFiles();

        for (int i = 0; i < moduleDirs.length; i++) {
            if (isModule(moduleDirs[i])) {
                currentModule = moduleDirs[i].getName();
                generate(template);
            }
        }
    }
}
