/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.template.PrettyPrintWriter;
import xdoclet.template.TemplateParser;
import xdoclet.util.FileManager;
import xdoclet.util.LogUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Merge"
 * @version              $Revision: 1.12 $
 */
public class MergeTagsHandler extends XDocletTagSupport
{

    /**
     * Evaluates the body if the file exists specified by the "file" attribute.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="file" optional="false" description="The path to the file to be merged. The
     *      value of this parameter can have {0} in it, if so {0} is replaced with the current class name and system
     *      searches for the file in in mergeDir+packageName directory. {0} is for cases where you want to define and
     *      merge a file per each class."
     */
    public void ifMergeFileExists(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(MergeTagsHandler.class, "ifMergeFileExists");
        String merge_file_pattern = attributes.getProperty("file");

        if (log.isDebugEnabled()) {
            log.debug("Pattern = " + merge_file_pattern);
        }
        if (merge_file_pattern != null) {
            // .j is deprecated as the template file extension
            if (merge_file_pattern.endsWith(".j")) {
                log.warn("Deprecated template file extension used for merge file, .j should now be .xdt");
                merge_file_pattern = merge_file_pattern.substring(0, merge_file_pattern.length() - 2) + ".xdt";
            }

            String contents = getMergeFileContents(merge_file_pattern);

            if (contents != null) {
                if (log.isDebugEnabled())
                    log.debug("Merge File found");

                generate(template);

            }
        }
        else {
            log.error("<XDtMerge:ifMergeFileExists/> file parameter missing from template file.");
        }
    }

    /**
     * Merge contents of the file designated by the file parameter and evaluates the body if the file is not found. It
     * searches for the file in the directory specified by mergeDir configuration parameter.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  XDocletException if something goes wrong
     * @doc.tag                     type="block"
     * @doc.param                   name="file" optional="false" description="The path to the file to be merged. The
     *      value of this parameter can have {0} in it, if so {0} is replaced with the current class name and system
     *      searches for the file in in mergeDir+packageName directory. {0} is for cases where you want to define and
     *      merge a file per each class."
     * @doc.param                   name="generateMergedFile" values="true,false" description="If true then process the
     *      merged file also, otherwise only merge it and do not process it. True if the default."
     */
    public void merge(String template, Properties attributes) throws XDocletException
    {

        Log log = LogUtil.getLog(MergeTagsHandler.class, "merge");
        String merge_file_pattern = attributes.getProperty("file");

        if (log.isDebugEnabled()) {
            log.debug("Pattern = " + merge_file_pattern);
        }
        if (merge_file_pattern != null) {
            // .j is deprecated as the template file extension
            if (merge_file_pattern.endsWith(".j")) {
                log.warn("Deprecated template file extension used for merge file, .j should now be .xdt");
                merge_file_pattern = merge_file_pattern.substring(0, merge_file_pattern.length() - 2) + ".xdt";
            }

            String contents = getMergeFileContents(merge_file_pattern);

            if (contents != null) {
                if (log.isDebugEnabled())
                    log.debug("Merge File found");

                String generate_merged_file = attributes.getProperty("generateMergedFile");

                if (generate_merged_file != null && !generate_merged_file.equalsIgnoreCase("true") && !generate_merged_file.equalsIgnoreCase("yes")) {
                    getEngine().print(contents);
                }
                else {
                    generateUsingMergedFile(merge_file_pattern, contents);
                }
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("Merge File NOT found");
                }

                // use body of <XDtMerge:merge>
                generateUsingMergedFile(((TemplateSubTask) getDocletContext().getActiveSubTask()).getTemplateURL().toString(), template);
            }
        }
        else {
            log.error("<XDtMerge:merge/> file parameter missing from template file, ignoring merge command.");
            generate(template);
        }

    }

    /**
     * A utility method used for merging a file used by <XDtMerge:merge/>tag. If the mergeFilePattern parameter has a
     * {0} in it then the {0} is replaced with the sybolic class name of the current class, and the package structure of
     * the class is prefixed to it. If not search is done for the exact file with the name specified in
     * mergeFilePattern. Both of these two searches search for the file in root directory designated of mergeDir config
     * parameter. It uses xdoclet.util.FileManager to load the file. FileManager caches the content so that subsequent
     * tries to load the file are served from memory, without reloading the file again and again.
     *
     * @param mergeFilePattern  The exact file name or a string that has a {0} in it. {0} is substituted by symbolic
     *      class name of current class.
     * @return                  The content of the text file.
     * @see                     #merge(java.lang.String,java.util.Properties)
     * @see                     PackageTagsHandler#packageNameAsPathFor(xjavadoc.XPackage)
     * @see                     ClassTagsHandler#symbolicClassName()
     * @see                     xdoclet.util.FileManager
     */
    protected String getMergeFileContents(String mergeFilePattern)
    {
        Log log = LogUtil.getLog(MergeTagsHandler.class, "getMergeFileContents");

        String file = null;

        try {
            if (mergeFilePattern.indexOf("{0}") != -1) {
                if (getEngine() instanceof TemplateParser) {
                    ((TemplateParser) getEngine()).addMergeFile(mergeFilePattern);
                }
                else {

                    String ejb_name = MessageFormat.format(mergeFilePattern, new Object[]{AbstractProgramElementTagsHandler.getClassNameFor(getCurrentClass())});
                    String merge_file_name = PackageTagsHandler.packageNameAsPathWithoutSubstitutionFor(getCurrentClass().getContainingPackage()) + File.separator + ejb_name;

                    if (getDocletContext().getActiveSubTask().getMergeDir() != null) {
                        File mergeFile = new File(getDocletContext().getActiveSubTask().getMergeDir(), merge_file_name);

                        log.debug("Search for File " + mergeFile);

                        if (mergeFile.exists()) {
                            log.debug("Search for File OK");
                            file = FileManager.getURLContent(mergeFile.toURL());
                        }
                        else {
                            // Backwards Compatibility - check for templates still using .j file extensions
                            if (merge_file_name.endsWith(".xdt")) {
                                log.debug(".xdt mergefile not found, trying .j");
                                mergeFile = new File(getDocletContext().getActiveSubTask().getMergeDir(),
                                    merge_file_name.substring(0, merge_file_name.length() - 4) + ".j");
                                log.debug(".xdt mergefile not found, search for File " + mergeFile);

                                if (mergeFile.exists()) {
                                    log.debug("Search for File OK");
                                    file = FileManager.getURLContent(mergeFile.toURL());
                                }
                                else {
                                    log.debug("Search for File not OK");
                                }
                            }
                            else {
                                log.debug("Search for File not OK");
                            }
                        }
                    }
                }
            }
            else {
                if (getDocletContext().getActiveSubTask().getMergeDir() != null) {
                    File mergeFile = new File(getDocletContext().getActiveSubTask().getMergeDir(), mergeFilePattern);

                    if (getEngine() instanceof TemplateParser) {
                        TemplateParser parser = (TemplateParser) getEngine();

                        // This avoids infinite loop when a merge file merge itself
                        if (parser.hasMergeFile(mergeFilePattern)) {
                            return null;
                        }
                        else {
                            parser.addMergeFile(mergeFilePattern);
                        }
                    }

                    if (mergeFile.exists()) {
                        log.debug("Merge file found in " + getDocletContext().getActiveSubTask().getMergeDir());
                        file = FileManager.getURLContent(mergeFile.toURL());
                    }
                    else {
                        // Backwards Compatibility - check for templates still using .j file extensions
                        if (mergeFilePattern.endsWith(".xdt")) {
                            mergeFile = new File(getDocletContext().getActiveSubTask().getMergeDir(),
                                mergeFilePattern.substring(0, mergeFilePattern.length() - 4) + ".j");
                            log.debug(".xdt mergefile not found, trying " + mergeFile.getName());

                            if (mergeFile.exists()) {
                                log.debug("Merge file found in " + getDocletContext().getActiveSubTask().getMergeDir());
                                file = FileManager.getURLContent(mergeFile.toURL());
                            }
                            else {
                                log.debug("Merge file NOT found in " + getDocletContext().getActiveSubTask().getMergeDir());
                            }
                        }
                        else {
                            log.debug("Merge file NOT found in " + getDocletContext().getActiveSubTask().getMergeDir());
                        }
                    }
                }
            }
            if (file != null)
                return file;

            // was not found in mergedir, try the jar
            URL jarResource = getClass().getResource('/' + mergeFilePattern);

            if (jarResource != null) {
                log.debug("Merge file found in jar");

                if (getEngine() instanceof TemplateParser) {
                    TemplateParser parser = (TemplateParser) getEngine();

                    // This avoids infinite loop when a merge file merge itself
                    if (parser.hasMergeFile(mergeFilePattern)) {
                        return null;
                    }
                    else {
                        parser.addMergeFile(mergeFilePattern);
                    }
                }

                file = FileManager.getURLContent(jarResource);
            }
            else {
                // not found on file system or in jar.
                file = null;
            }
        }
        catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return file;
    }

    /**
     * Processes the file specified in merge_file_pattern that has the text content contents. It resets currentLineNum
     * to 0 upon calling generate() and restores it back to its previous value. It also sets and restores templateFile.
     *
     * @param mergeFile             The file to be merged
     * @param contents              Description of Parameter
     * @exception XDocletException  Description of Exception
     * @see                         xdoclet.template.TemplateEngine#setTemplateURL(java.net.URL)
     */
    protected void generateUsingMergedFile(String mergeFile, String contents) throws XDocletException
    {
        try {
            int lineNumber = getEngine().getCurrentLineNum();
            URL prevTemplateUrl = ((TemplateSubTask) getDocletContext().getActiveSubTask()).getTemplateURL();

            getEngine().setTemplateURL(new File(mergeFile).toURL());
            getEngine().setCurrentLineNum(0);

            generate(contents);

            getEngine().setTemplateURL(prevTemplateUrl);
            getEngine().setCurrentLineNum(lineNumber);
        }
        catch (MalformedURLException e) {
            throw new XDocletException(e.getMessage());
        }
    }

    /**
     * A utility method used for generating the dest_file based on template_file template file.
     *
     * @param dest_file             the path to the destination file prepended by value of the destDir configuration
     *      parameter.
     * @param templateFileName      the template file name
     * @exception XDocletException  Description of Exception
     */
    protected void generateFileUsingTemplate(String dest_file, String templateFileName) throws XDocletException
    {
        Log log = LogUtil.getLog(MergeTagsHandler.class, "generateFileUsingTemplate");

        getXJavaDoc().getSourceClasses();

        File file = new File(getDocletContext().getDestDir().toString(), dest_file);

        /*
         * File beanFile  = new File( destDir.toString(), javaFile( fullClassName() ) );
         * How to check modification timestamps???
         * if( file.exists() )
         * {
         * if( file.lastModified() > beanFile.lastModified() )
         * {
         * continue;
         * }
         * }
         */
        file.getParentFile().mkdirs();

        try {
            getEngine().setTemplateURL(new File(templateFileName).toURL());

            String content = FileManager.getURLContent(((TemplateSubTask) getDocletContext().getActiveSubTask()).getTemplateURL());

            if (content != null) {
                try {
                    PrettyPrintWriter out = new PrettyPrintWriter(new BufferedWriter(new FileWriter(file)));

                    getEngine().setWriter(out);
                    getEngine().setCurrentLineNum(0);
                    generate(content);
                    out.close();
                }
                catch (IOException ex) {
                    log.error("An error occured while writing output to file " + file, ex);
                }
            }
        }
        catch (MalformedURLException e) {
            throw new XDocletException(e.getMessage());
        }
    }
}
