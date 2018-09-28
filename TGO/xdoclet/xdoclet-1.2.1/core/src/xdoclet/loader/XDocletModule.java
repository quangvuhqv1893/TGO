/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import xdoclet.util.LogUtil;

/**
 * This is an object view of the data in xdoclet.xml
 *
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Helles�y</a>
 * @created   7. april 2002
 * @version   $Revision: 1.11 $
 */
public class XDocletModule
{
    private ArrayList _tagHandlers = new ArrayList();
    private ArrayList _subTasks = new ArrayList();
    private URL     _xtagsDefinition = null;

    public XDocletModule()
    {
    }

    public List getTagHandlerDefinitions()
    {
        return _tagHandlers;
    }

    public List getSubTaskDefinitions()
    {
        return _subTasks;
    }

    public URL getXTagsDefinitionURL()
    {
        return _xtagsDefinition;
    }

    public void addTagHandler(String namespace, String clazz)
    {
        Log log = LogUtil.getLog(getClass(), "addTagHandler");

        try {
            log.debug("registering tag tandler " + clazz + " to namespace " + namespace + '.');

            TagHandlerDefinition thd = new TagHandlerDefinition(namespace, clazz);

            _tagHandlers.add(thd);
        }
        catch (Throwable t) {
            log.error("Couldn't register tag handler " + clazz + ':' + t.getMessage());
        }
    }

    public void addSubTask(String name, String implementationClass, String parentTaskClass)
    {
        if (!name.equals(name.toLowerCase())) {
            throw new IllegalStateException("Subtask names must be lowercase. Please modify the name of the subtask (" + name + ')');
        }
        _subTasks.add(new SubTaskDefinition(name, implementationClass, parentTaskClass));
    }

    void setXTagsDefinitionURL(URL xtagsDefinition)
    {
        _xtagsDefinition = xtagsDefinition;
    }
}
