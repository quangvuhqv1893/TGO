/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

import xjavadoc.XClass;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.template.TemplateTagHandler;
import xdoclet.util.Translator;

/**
 * Extracts doc.blabla tags from xdoclet.* sources and generates an html file describing the tags and their parameters.
 *
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       June 19, 2001
 * @ant.element   display-name="Tag documentation" name="documenttags" parent="xdoclet.modules.doc.DocumentDocletTask"
 * @todo          use DocletTask as parent instead. should be enough.
 * @version       $Revision: 1.10 $
 */
public class DocumentTagsSubTask extends TemplateSubTask
{
    private static String INDEX_TEMPLATE_FILE = "resources/index.xdt";

    private static String GENERATED_INDEX_FILE_NAME = "index.html";

    private static String NAMESPACES_TEMPLATE_FILE = "resources/namespaces.xdt";

    private static String GENERATED_NAMESPACES_FILE_NAME = "namespaces.html";

    private static String TAGS_MAIN_TEMPLATE_FILE = "resources/tags_main.xdt";

    private static String GENERATED_TAGS_MAIN_FILE_NAME = "tags.html";

    private static String TAGS_TOC_MAIN_TEMPLATE_FILE = "resources/tags_toc_main.xdt";

    private static String GENERATED_TAGS_TOC_MAIN_FILE_NAME = "tags_toc.html";

    private static String TAGS_TOC_TEMPLATE_FILE = "resources/tags_toc.xdt";

    private static String GENERATED_TAGS_TOC_FILE_NAME = "{0}_toc.html";

    private static String TAGS_TEMPLATE_FILE = "resources/tags.xdt";

    private static String GENERATED_TAGS_FILE_NAME = "{0}.html";

    private String  currentNamespace;

    /**
     * Gets the CurrentNamespace attribute of the DocumentTagsSubTask object
     *
     * @return   The CurrentNamespace value
     */
    public String getCurrentNamespace()
    {
        return currentNamespace;
    }


    /**
     * Called to validate configuration parameters - really noop here
     *
     * @exception XDocletException  thrown on failure
     */
    public void validateOptions() throws XDocletException
    {
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void execute() throws XDocletException
    {

        setTemplateURL(getClass().getResource(INDEX_TEMPLATE_FILE));
        setDestinationFile(GENERATED_INDEX_FILE_NAME);
        startProcess();

        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.GENERATING_NAMESPACES));
        setTemplateURL(getClass().getResource(NAMESPACES_TEMPLATE_FILE));
        setDestinationFile(GENERATED_NAMESPACES_FILE_NAME);
        startProcess();

        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.GENERATING_TAGS_MAIN));
        setTemplateURL(getClass().getResource(TAGS_MAIN_TEMPLATE_FILE));
        setDestinationFile(GENERATED_TAGS_MAIN_FILE_NAME);
        startProcess();

        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.GENERATING_TAGS_TOC_MAIN));
        setTemplateURL(getClass().getResource(TAGS_TOC_MAIN_TEMPLATE_FILE));
        setDestinationFile(GENERATED_TAGS_TOC_MAIN_FILE_NAME);
        startProcess();

        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.GENERATING_TAGS_MAIN));
        setTemplateURL(getClass().getResource(TAGS_TOC_TEMPLATE_FILE));
        setDestinationFile(GENERATED_TAGS_TOC_FILE_NAME);
        addOfType("xdoclet.template.TemplateTagHandler");
        //setExtentValue( "superclass" );
        startProcess();

        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.GENERATING_TAGS_MAIN));
        setTemplateURL(getClass().getResource(TAGS_TEMPLATE_FILE));
        setDestinationFile(GENERATED_TAGS_FILE_NAME);
        addOfType("xdoclet.template.TemplateTagHandler");
        //setExtentValue( "superclass" );
        startProcess();
    }

    /**
     * Gets the GeneratedFileName attribute of the DocumentTagsSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException  Describe the exception
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        String destinationFile = MessageFormat.format(getDestinationFile(), new Object[]{currentNamespace});

        return new File(destinationFile).toString();
    }

    /**
     * Processed template for clazz and generates output file for clazz.
     *
     * @param clazz                 Description of Parameter
     * @exception XDocletException  Description of Exception
     */
    protected void generateForClass(XClass clazz) throws XDocletException
    {
        setCurrentNamespace(clazz);

        super.generateForClass(clazz);
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
    }

    /**
     * Sets the CurrentNamespace attribute of the DocumentTagsSubTask object
     *
     * @param clazz                 The new CurrentNamespace value
     * @exception XDocletException  Describe the exception
     */
    private void setCurrentNamespace(XClass clazz) throws XDocletException
    {
        for (Iterator namespaces = TemplateEngine.getEngineInstance().getNamespaces().iterator(); namespaces.hasNext(); ) {
            String namespace = (String) namespaces.next();

            try {
                TemplateTagHandler handler = TemplateEngine.getEngineInstance().getTagHandlerFor(namespace);

                if (handler.getClass().getName().equals(clazz.getQualifiedName())) {
                    currentNamespace = namespace;
                    break;
                }
            }
            catch (TemplateException e) {
                throw new XDocletException(e, "Error getting tag handler for " + namespace);
            }
        }
        if (currentNamespace == null) {
            // throw new XDocletException("No namespace found for class " + clazz.qualifiedName());
        }
    }
}
