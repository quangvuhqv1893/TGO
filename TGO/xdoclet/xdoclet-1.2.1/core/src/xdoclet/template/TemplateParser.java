/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;

import xdoclet.util.FileManager;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Subclass of Template Engine that do not generate anything but only parse the document. The TagHandlers have a
 * callback entry to this method to set in it anything they want to. This class was introduced for parsing .j files and
 * return a list of merge files needed for the generation. The timestamp checking can then verify all files involved in
 * a generation and bypass the generation if -nothing has changed-.
 *
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   December 27, 2001
 * @version   $Revision: 1.12 $
 */
public class TemplateParser extends TemplateEngine
{
    private static TemplateParser instance = new TemplateParser();
    private List    mergeFiles;

    /**
     * Initialize the Template Engine. Reads the XDoclet properties file, and loads any XTag handler classes specified.
     */
    protected TemplateParser()
    {
        super();
        mergeFiles = new ArrayList();
        output = null;
    }

    /**
     * Gets the ParserInstance attribute of the TemplateParser class
     *
     * @return   The ParserInstance value
     */
    public static TemplateParser getParserInstance()
    {
        return instance;
    }

    public TemplateTagHandler getTagHandlerFor(String prefix) throws TemplateException
    {
        return TemplateEngine.getEngineInstance().getTagHandlerFor(prefix);
    }

    /**
     * Return the list of merge files involved in the generation.
     *
     * @return   an array of File
     */
    public String[] getMergeFiles()
    {
        return (String[]) mergeFiles.toArray(new String[mergeFiles.size()]);
    }

    /**
     * A utility method used for generating the dest_file based on template_file template file.
     *
     * @exception TemplateException  Description of Exception
     */
    public void start() throws TemplateException
    {
        Log log = LogUtil.getLog(TemplateParser.class, "start");

        String content = FileManager.getURLContent(getTemplateURL());

        if (content != null) {
            log.debug("content.length()=" + content.length());

            setCurrentLineNum(0);
            generate(content);
        }
        else {
            String msg = Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_NOT_FOUND, new String[]{getTemplateURL().toString()});

            log.error(msg);
            throw new TemplateException(msg);
        }
    }

    /**
     * In this class, this method does not -generate- anything but only parse the files. Callback to this class can be
     * made by specific TagHandlers during the process.
     *
     * @param template               Description of Parameter
     * @exception TemplateException  Description of Exception
     */
    public void generate(final String template) throws TemplateException
    {
        int index = 0;
        int previousIndex = 0;

        while (true) {
            // Look for the next tag that we haven't yet handled.
            index = template.indexOf(XDOCLET_HEAD, previousIndex);

            if (index == -1) {
                break;
            }
            else {
                previousIndex = handleTag(index, template);
            }
        }

    }

    /**
     * Callback by the MergeTagsHandler to give the parser the list of merge files involved.
     *
     * @param file  one merge file involved
     */
    public void addMergeFile(String file)
    {
        mergeFiles.add(file);
    }

    /**
     * Callback by the MergeTagsHandler to know if a merge file has already been taken into account.
     *
     * @param file
     * @return
     */
    public boolean hasMergeFile(String file)
    {
        return mergeFiles.contains(file);
    }

    /**
     * Describe what the method does
     *
     * @param cmd                    Describe what the parameter does
     * @param attributes             Describe what the parameter does
     * @param template               Describe what the parameter does
     * @param i                      Describe what the parameter does
     * @exception TemplateException  Describe the exception
     */
    protected void invokeContentMethod(String cmd, Properties attributes, String template, int i) throws TemplateException
    {
        // We do not need anything when parsing (e.g. isGenerationNeeded run)
    }

    /**
     * Describe what the method does
     *
     * @param m                              Describe what the parameter does
     * @param cmdImplProvider                Describe what the parameter does
     * @param params1                        Describe what the parameter does
     * @return                               Describe the return value
     * @exception InvocationTargetException  Describe the exception
     * @exception IllegalAccessException     Describe the exception
     * @exception TemplateException          Describe the exception
     */
    protected Object invoke(Method m, Object cmdImplProvider, Object[] params1)
         throws InvocationTargetException, IllegalAccessException, TemplateException
    {
        Log log = LogUtil.getLog(TemplateParser.class, "invoke");

        if (log.isDebugEnabled()) {
            log.debug(m.getName() + params1[0]);
        }

        // The trick to have a quick parsing is to only take care of merging.
        // The rest is not important and the tag handlers are bypassed by simply
        // calling generate()
        if (!m.getName().equals("merge")) {
            generate((String) params1[0]);
            return null;
        }
        else {
            return m.invoke(cmdImplProvider, params1);
        }
    }
}
