/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.maven;

import java.beans.Introspector;

import java.util.*;

import xjavadoc.*;
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

import xdoclet.modules.doc.AntdocSubTask;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;

/**
 * Generates xdoclet Maven plugin.
 *
 * @author        Ara Abrahamian
 * @created       21 September 2002
 * @version       $Revision: 1.2 $
 * @ant.element   display-name="Maven plugin" name="mavenplugin" parent="xdoclet.DocletTask"
 */
public class MavenpluginSubTask extends AntdocSubTask
{
    private static String MAVENPLUGIN_TEMPLATE_FILE = "resources/mavenplugin.xdt";

    public String getDestinationFile()
    {
        //return "{0}.xml";
        return "plugin.jelly";
    }

    public void init(XJavaDoc xJavaDoc) throws XDocletException
    {
        super.init(xJavaDoc);

        setTemplateURL(getClass().getResource(MAVENPLUGIN_TEMPLATE_FILE));

        setDestinationFile("plugin.jelly");
    }

    protected void startProcessForAll() throws XDocletException
    {
        MavenpluginTagsHandler mavenPluginTagsHandler = null;

        try {
            mavenPluginTagsHandler = (MavenpluginTagsHandler) TemplateEngine.getEngineInstance().getTagHandlerFor("Mavenplugin");
        }
        catch (TemplateException e) {
            throw new XDocletException(e.getMessage());
        }

        mavenPluginTagsHandler.setTasks(getTasks());

        super.startProcessForAll();
    }

    private List getTasks()
    {
        List tasks = new ArrayList();

        List to_iter = Arrays.asList(classToAntElementMap.values().toArray());

        for (Iterator i = to_iter.iterator(); i.hasNext(); ) {
            Element element = (Element) i.next();

            if (element.getXClass().isA("xdoclet.DocletTask"))
                tasks.add(element);
        }

        return tasks;
    }
}
