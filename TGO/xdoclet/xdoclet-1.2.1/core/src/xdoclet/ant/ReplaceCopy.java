/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.ant;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import xdoclet.loader.ModuleFinder;

import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.util.Translator;

/**
 * Extension of Ant's Copy task that uses XDoclet's template engine to copy instead of Ant's plain copy. It will scan
 * each file for occurrences of &lt;XDtAnt:property name="some.ant.property"/&gt; and replace them with the associated
 * Ant property value. <p>
 *
 * This is similar to using the standard &lt;copy&gt; Ant built-in task with a nested &lt;filterset&gt; element, but
 * this task uses a pull mechanism (ant properties are pulled from the ant environment by the copied files) instead of a
 * push mechanism (where Ant pushes explicit values into the copied files).</p>
 *
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created   5. januar 2002
 * @version   $Revision: 1.14 $
 * @todo      Write docs!!!!!!!!!!!!!!!!!
 */
public class ReplaceCopy extends Copy
{
    public ReplaceCopy()
    {
        ModuleFinder.initClasspath(getClass());
    }

    /**
     * Overridden doFileOperations() as Ant suggests. It would be less copy-paste if fileUtils.copyFile in the
     * superclass' method used getFileUtils().copyFile instead. Then we could just override getFileUtils() and return a
     * FileUtils subclass which used XDoclet template engine. Until Ant fixes this, we'll do inheritance by copy/paste
     * :-(
     */
    protected void doFileOperations()
    {
        Hashtable properties = project.getProperties();
        AntPropertyTagsHandler antPropertyTagsHandler = new AntPropertyTagsHandler(properties);
        TemplateEngine engine = null;

        try {
            engine = TemplateEngine.getEngineInstance();
            engine.setTagHandlerFor("Ant", antPropertyTagsHandler);
        }
        catch (TemplateException e) {
            throw new BuildException(Translator.getString(XDocletAntMessages.class, XDocletAntMessages.ERROR_CREATING_TEMPLATEENGINE));
        }

        if (fileCopyMap.size() > 0) {
            log("Copying " + fileCopyMap.size() +
                " file" + (fileCopyMap.size() == 1 ? "" : "s") +
                " to " + destDir.getAbsolutePath());

            Enumeration e = fileCopyMap.keys();

            while (e.hasMoreElements()) {
                String fromFile = (String) e.nextElement();
                String toFile = (String) fileCopyMap.get(fromFile);

                if (fromFile.equals(toFile)) {
                    log("Skipping self-copy of " + fromFile, verbosity);
                    continue;
                }

                try {
                    log("Copying " + fromFile + " to " + toFile, verbosity);

                    FilterSetCollection executionFilters = new FilterSetCollection();

                    if (filtering) {
                        executionFilters.addFilterSet(project.getGlobalFilterSet());
                    }
                    for (Enumeration filterEnum = getFilterSets().elements(); filterEnum.hasMoreElements(); ) {
                        executionFilters.addFilterSet((FilterSet) filterEnum.nextElement());
                    }

//                    fileUtils.copyFile(fromFile, toFile, executionFilters,
//                                       forceOverwrite, preserveLastModified);
                    replace(fromFile, toFile, engine);

                }
                catch (TemplateException ioe) {
                    String msg = Translator.getString(XDocletAntMessages.class, XDocletAntMessages.FAILED_TO_COPY, new String[]{fromFile, toFile, ioe.getMessage()});

                    throw new BuildException(msg, ioe, location);
                }
            }
        }

        if (includeEmpty) {
            Enumeration e = dirCopyMap.elements();
            int count = 0;

            while (e.hasMoreElements()) {
                File d = new File((String) e.nextElement());

                if (!d.exists()) {
                    if (!d.mkdirs()) {
                        log("Unable to create directory " + d.getAbsolutePath(), Project.MSG_ERR);
                    }
                    else {
                        count++;
                    }
                }
            }

            if (count > 0) {
                log("Copied " + count +
                    " empty director" +
                    (count == 1 ? "y" : "ies") +
                    " to " + destDir.getAbsolutePath());
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @param fromFile               Describe what the parameter does
     * @param toFile                 Describe what the parameter does
     * @param engine                 Describe what the parameter does
     * @exception TemplateException  Describe the exception
     */
    private void replace(String fromFile, String toFile, TemplateEngine engine) throws TemplateException
    {
        try {
            engine.setTemplateURL(new File(fromFile).toURL());
            engine.setOutput(new File(toFile));
            engine.start();
        }
        catch (MalformedURLException e) {
            throw new TemplateException(e.getMessage());
        }
    }
}
