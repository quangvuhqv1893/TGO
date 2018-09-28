/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.externalizer;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.template.TemplateException;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
/**
 * Creates translator classes for convenient access to externalized resource bundles
 *
 * @author               Konstantin Pribluda(kpriblouda@yahoo.com)
 * @created              October 5, 2002
 * @ant.element          display-name="Properties Translator" name="propertiestranslator" parent="xdoclet.DocletTask"
 * @xdoclet.merge-file   file="translator-custom.xdt" relates-to="{0}Translator.java" description="Custom Java code to
 *      be included in generated Translator classes."
 * @version              $Revision: 1.4 $
 */
public class PropertiesTranslatorSubTask extends TemplateSubTask
{
    public final static String GENERATED_FILE_NAME = "{0}Translator.java";
    private static String DEFAULT_TEMPLATE_FILE = "resources/translator.xdt";


    /**
     * constructor for properties generator subtask
     */
    public PropertiesTranslatorSubTask()
    {
        // accept only files with message bundle attribute set
        setHavingClassTag("msg.bundle");
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);

    }


    /**
     * compute file name for generated translator class bundle
     *
     * @param clazz
     * @return
     * @exception XDocletException
     */
    protected String getGeneratedFileFileName(XClass clazz) throws XDocletException
    {
        String result = MessageFormat.format(getDestinationFile(), new Object[]{clazz.getQualifiedName().replace('.', '/')});

        return result;
    }

}
