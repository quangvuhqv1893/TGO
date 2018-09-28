/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jmx;

import java.io.File;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.util.Translator;

/**
 * Generates mlet description for JMX MBean.
 *
 * @author               Jerome Bernard (jerome.bernard@xtremejava.com)
 * @created              February 6, 2002
 * @ant.element          display-name="MLet" name="mlet" parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version              $Revision: 1.7 $
 * @xdoclet.merge-file   file="mlet-entry-{0}.mlet" relates-to="mbeans.mlet" description="An XML document containing the
 *      MLET entry for a class, instead of generating from a jmx.mlet-entry tag."
 */
public class MLetSubTask extends TemplateSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/mlet.xdt";

    private static String GENERATED_FILE_NAME = "mbeans.mlet";

    /**
     * Describe what the MLetSubTask constructor does
     */
    public MLetSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setHavingClassTag("jmx:mlet-entry");
    }
}
