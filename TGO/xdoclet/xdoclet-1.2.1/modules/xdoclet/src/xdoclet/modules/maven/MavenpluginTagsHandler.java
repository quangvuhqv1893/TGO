/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.maven;

import java.util.Iterator;
import java.util.List;
import xjavadoc.XClass;
import xjavadoc.XParameter;

import xdoclet.XDocletException;
import xdoclet.modules.doc.AntdocSubTask;
import xdoclet.modules.doc.AntdocTagsHandler;
import xdoclet.tagshandler.TypeTagsHandler;

/**
 * This tag handler is used to generate xdoclet Maven plugin.
 *
 * @author               Ara Abrahamian
 * @created              21 September 2002
 * @xdoclet.taghandler   namespace="Mavenplugin"
 * @version              $Revision: 1.3 $
 */
public class MavenpluginTagsHandler extends AntdocTagsHandler
{
    private List    tasks;

    private static boolean isNestedElementParameter(XParameter parameter)
    {
        return TypeTagsHandler.isPrimitiveType(parameter.getType().getQualifiedName()) == false &&
            parameter.getType().getQualifiedName().startsWith("xjavadoc") == false &&
            parameter.getType().getQualifiedName().startsWith("java") == false;
    }

    public List getTasks()
    {
        return tasks;
    }

    public void setTasks(List tasks)
    {
        this.tasks = tasks;
    }

    public void forAllTasks(String template) throws XDocletException
    {
        XClass old_cur_class = getCurrentClass();

        for (Iterator i = getTasks().iterator(); i.hasNext(); ) {
            docElement = (AntdocSubTask.Element) i.next();

            setCurrentClass(docElement.getXClass());

            generate(template);
        }

        setCurrentClass(old_cur_class);
    }

    public void ifIsASubTask(String template) throws XDocletException
    {
        ifIsASubTask_Impl(template, true);
    }

    public void ifIsNotASubTask(String template) throws XDocletException
    {
        ifIsASubTask_Impl(template, false);
    }

    public void ifIsAFileSet(String template) throws XDocletException
    {
        ifIsAFileSet_Impl(template, true);
    }

    public void ifIsNotAFileSet(String template) throws XDocletException
    {
        ifIsAFileSet_Impl(template, false);
    }

    public void ifIsAConfigParam(String template) throws XDocletException
    {
        if (subElement.getXClass().isA("xdoclet.ConfigParameter"))
            generate(template);
    }

    public void ifIsANestedElement(String template) throws XDocletException
    {
        ifIsANestedElement_Impl(template, true);
    }

    public String nestedElementName() throws XDocletException
    {
        return getCurrentMethod().getName().substring(3);
    }

    public String nestedElementType() throws XDocletException
    {
        for (Iterator i = getCurrentMethod().getParameters().iterator(); i.hasNext(); ) {
            XParameter parameter = (XParameter) i.next();

            return parameter.getType().getQualifiedName();
        }

        return "";
    }

    public void ifIsNotANestedElement(String template) throws XDocletException
    {
        ifIsANestedElement_Impl(template, false);
    }

    private void ifIsASubTask_Impl(String template, boolean condition) throws XDocletException
    {
        if (subElement.isDynamicSubElement() == condition)
            generate(template);
    }

    private void ifIsAFileSet_Impl(String template, boolean condition) throws XDocletException
    {
        if (subElement.getXClass().isA("org.apache.tools.ant.types.FileSet") == condition)
            generate(template);
    }

    private void ifIsANestedElement_Impl(String template, boolean condition) throws XDocletException
    {
        if ((getCurrentMethod().getName().startsWith("set") || getCurrentMethod().getName().startsWith("add")) && getCurrentMethod().getParameters().size() == 1) {

            for (Iterator i = getCurrentMethod().getParameters().iterator(); i.hasNext(); ) {
                XParameter parameter = (XParameter) i.next();

                if (isNestedElementParameter(parameter) == condition) {
                    generate(template);
                }
            }
        }
    }
}
