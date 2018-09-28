/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import xjavadoc.XClass;
import xjavadoc.XJavaDoc;

import xdoclet.loader.ModuleFinder;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.template.TemplateParser;
import xdoclet.util.LogUtil;

/**
 * Verify if the generation is needed for Java files and Xml files based templates.
 *
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   March 30, 2002
 * @version   $Revision: 1.19 $
 */
public class GenerationManager
{
    private final static File newestJar = ModuleFinder.getNewestFileOnClassPath();

    private static Map parserDb = null;

    private final TemplateSubTask subTask;

    private boolean guessGenerationNeeded = true;

    private XJavaDoc _xJavaDoc;

    /**
     * Describe what the GenerationManager constructor does
     *
     * @param subTask   Describe what the parameter does
     * @param xJavaDoc
     */
    public GenerationManager(XJavaDoc xJavaDoc, TemplateSubTask subTask)
    {
        if (xJavaDoc == null) {
            throw new IllegalArgumentException("xJavaDoc can't be null");
        }
        _xJavaDoc = xJavaDoc;
        this.subTask = subTask;
    }

    /**
     * Return (and construct) the template database. It is a map between a <code>String</code> representing the template
     * file and an array of <String> representing the merge files that are part of the generation.
     *
     * @return   the <code>Map</code>
     */
    private static Map getParserDb()
    {
        if (parserDb == null) {
            parserDb = new HashMap();
        }
        return parserDb;
    }

    /**
     * During parsing we build the Template database. We store it on file.
     *
     * @param templateURL  the template file
     * @param files        the merge files involved in the generation
     */
    private static void updateParserDb(URL templateURL, String[] files)
    {
        // Merge existing list with new list
        String[] mergeFiles = (String[]) getParserDb().get(new File(templateURL.getFile()).getName());
        List complete = new ArrayList(Arrays.asList(files));

        if (mergeFiles != null) {
            for (int j = 0; j < mergeFiles.length; j++) {
                String file = mergeFiles[j];

                if (!complete.contains(file)) {
                    complete.add(file);
                }
            }
        }
        getParserDb().put(new File(templateURL.getFile()).getName(), complete.toArray(new String[complete.size()]));
    }

    /**
     * Gets the GuessGenerationNeeded attribute of the GenerationManager object
     *
     * @return   The GuessGenerationNeeded value
     */
    public boolean isGuessGenerationNeeded()
    {
        return guessGenerationNeeded;
    }

    /**
     * Test if a Java source mmust be generated or not depending of timestamp of elements involved.
     *
     * @param clazz                 the Class from wich we generate
     * @param file                  the File that will be generated
     * @param withTemplate
     * @return                      true if generation is needed
     * @exception XDocletException
     */
    public boolean isGenerationNeeded(XClass clazz, File file, boolean withTemplate)
         throws XDocletException
    {
        Log log = LogUtil.getLog(GenerationManager.class, "generation");

        if (subTask.getContext().isForce()) {
            log.debug("Force generation enabled");
            return true;
        }

        if (isGuessGenerationNeeded() == false) {
            log.debug("guessGenerationNeeded enabled");
            return true;
        }

        // 1. Check whether a file on classpath is newer than the destination file
        if (isClasspathNewerThanFile(file))
            return true;

        // 2. Check whether the class (or any superclass) is newer than the destination file
        if (isClassHierarchyNewerThanFile(clazz, file))
            return true;

        // 3. Check whether the template file or any merge files are newer than the destination file
        if (isTemplateNewerThanFile(withTemplate, file))
            return true;

        return false;
    }

    /**
     * Verify if the generation of a file to generate is needed because either the Template used to generate the file
     * have a later timestamp, or because ALL the Java sources imported in this task have a sooner timestamp. This is
     * used to test if xml files generation is needed.
     *
     * @param file                  The file to check
     * @return                      true if the generation is needed
     * @exception XDocletException
     */
    public boolean isGenerationNeeded(File file)
         throws XDocletException
    {
        Log log = LogUtil.getLog(GenerationManager.class, "generation");

        log.debug("Generation need check for " + file.getName());

        if (subTask.getContext().isForce()) {
            log.debug("Force generation enabled");
            return true;
        }

        if (isGuessGenerationNeeded() == false) {
            log.debug("guessGenerationNeeded enabled");
            return true;
        }

        // 1. Check on Jar timestamp
        if (isClasspathNewerThanFile(file) == true)
            return true;

        // 2. Check the timestamp of template file and merge files
        if (isGenerationNeeded(file, subTask.getTemplateURL())) {
            return true;
        }

        log.debug("Generation need check for " + file.getName());

        // 3. Check Timestamp of all java sources in sourcepath

        for (Iterator i = _xJavaDoc.getSourceClasses().iterator(); i.hasNext(); ) {
            if (isGenerationNeeded((XClass) i.next(), file, false)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the GuessGenerationNeeded attribute of the GenerationManager object.
     *
     * @param guessGenerationNeeded  The new GuessGenerationNeeded value
     */
    public void setGuessGenerationNeeded(boolean guessGenerationNeeded)
    {
        this.guessGenerationNeeded = guessGenerationNeeded;
    }

    private boolean isClassHierarchyNewerThanFile(XClass clazz, File file)
    {
        Log log = LogUtil.getLog(GenerationManager.class, "generation");

        while (clazz != null) {
            if (clazz.getQualifiedName().equals("java.lang.Object")) {
                return false;
            }
            if (file.lastModified() < clazz.lastModified()) {
                if (log.isDebugEnabled()) {
                    log.debug("Generation needed for '" + file.getAbsolutePath() + "' because " + clazz.getQualifiedName() + " is newer (it's in the class hierarchy)");
                }
                return true;
            }
            clazz = clazz.getSuperclass();
        }

        return false;
    }

    private boolean isTemplateNewerThanFile(boolean withTemplate, File file) throws XDocletException
    {
        Log log = LogUtil.getLog(GenerationManager.class, "generation");

        log.debug("Checking template. withTemplate=" + withTemplate);

        if (withTemplate) {
            if (isGenerationNeeded(file, subTask.getTemplateURL())) {
                if (log.isDebugEnabled()) {
                    log.debug("Generation needed for '" + file.getAbsolutePath() + "' because template file is newer.");
                }

                return true;
            }
        }

        return false;
    }

    private boolean isClasspathNewerThanFile(File file)
    {
        Log log = LogUtil.getLog(GenerationManager.class, "generation");

        if (file.lastModified() < newestJar.lastModified()) {
            if (log.isDebugEnabled()) {
                log.debug("Generation needed for '" + file.getAbsolutePath() + "' because " + newestJar.getName() + " is newer.");
            }
            return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("No files on classpath are newer than '" + file.getAbsolutePath() + "'");
        }

        return false;
    }

    /**
     * Verify if the generation of a file is needed because either the template file has a sooner timestamp, or because
     * one of the merge files have a sooner timestamp
     *
     * @param file                  The file to generate
     * @param templateURL           the Template file to use
     * @return                      true if generation is needed.
     * @exception XDocletException
     */
    private boolean isGenerationNeeded(File file, URL templateURL)
         throws XDocletException
    {
        Log log = LogUtil.getLog(GenerationManager.class, "xml");

        if (log.isDebugEnabled()) {
            log.debug("Generation need check for " + file.getAbsolutePath());
        }

        // 1. Check Timestamp of Template file
        File templateFile = new File(subTask.getTemplateURL().getFile());

        if (templateFile.exists() && file.lastModified() < templateFile.lastModified()) {
            if (log.isDebugEnabled()) {
                log.debug("Generation needed for '" + file.getAbsolutePath() + "' because of timestamp of " + subTask.getTemplateURL());
            }
            return true;
        }
        if (log.isDebugEnabled()) {
            log.debug("Reject file '" + file.getAbsolutePath() + "' because of timestamp of " + subTask.getTemplateURL());
        }

        // 2. Check timestamp of Merge files found inside Template
        String[] files;

        if (getParserDb().get(templateFile) == null) {
            TemplateEngine the_engine = subTask.getEngine();
            TemplateParser the_parser = TemplateParser.getParserInstance();

            subTask.setEngine(the_parser);

            // Why is setOutput called here? We're only checking _IF_ we're going to generate! (Aslak)
            the_parser.setOutput(file);
            the_parser.setTemplateURL(templateURL);

            try {
                the_parser.start();
            }
            catch (TemplateException e) {
                throw new XDocletException(e, e.toString());
            }

            files = the_parser.getMergeFiles();
            if (files != null) {
                updateParserDb(templateURL, files);
            }

            //restore
            subTask.setEngine(the_engine);
        }
        else {
            files = (String[]) getParserDb().get(new File(templateURL.getFile()).getName());
            for (int i = 0; i < files.length; i++) {
                if (log.isDebugEnabled()) {
                    log.debug(templateURL.getFile() + " : " + files[i]);
                }
            }
        }

        log.debug("Number of Merge files involved = " + files.length);

        for (int i = 0; i < files.length; i++) {
            String mergeFilePattern = files[i];
            List mergeFiles = new ArrayList();

            if (mergeFilePattern.indexOf("{0}") != -1) {

                for (Iterator j = _xJavaDoc.getSourceClasses().iterator(); j.hasNext(); ) {
                    XClass aClass = (XClass) j.next();
                    String ejbName = MessageFormat.format(mergeFilePattern, new Object[]{AbstractProgramElementTagsHandler.getClassNameFor(aClass)});
                    String mergeFileName = PackageTagsHandler.packageNameAsPathFor(aClass.getContainingPackage()) + File.separator + ejbName;

                    if (subTask.getMergeDir() != null)
                        mergeFiles.add(new File(subTask.getMergeDir(), mergeFileName));
                }
            }
            else {
                if (subTask.getMergeDir() != null)
                    mergeFiles.add(new File(subTask.getMergeDir(), mergeFilePattern));
            }
            for (Iterator iterator = mergeFiles.iterator(); iterator.hasNext(); ) {
                File mergeFile = (File) iterator.next();

                log.debug("Generation check for '" + file.getAbsolutePath() + "' because of " + mergeFile.getName());

                if (mergeFile.exists()) {
                    if (file.lastModified() < mergeFile.lastModified()) {
                        log.debug("Generation needed for '" + file.getAbsolutePath() + "' because of timestamp of " + mergeFile.getName());
                        return true;
                    }
                    log.debug("Reject file '" + file.getAbsolutePath() + "' because of timestamp of " + mergeFile.getName());
                }
            }
        }
        return false;
    }
}
