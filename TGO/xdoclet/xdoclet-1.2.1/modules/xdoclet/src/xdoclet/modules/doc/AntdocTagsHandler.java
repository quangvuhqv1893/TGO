/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc;

import java.util.*;
import xjavadoc.XClass;
import xjavadoc.XTag;

import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;

/**
 * This tag handler is used to generate Ant documentation
 *
 * @author               Aslak Hellesoy
 * @created              13. juni 2002
 * @xdoclet.taghandler   namespace="Antdoc"
 * @version              $Revision: 1.8 $
 */
public class AntdocTagsHandler extends XDocletTagSupport
{
    /**
     * The element being documented
     */
    protected AntdocSubTask.Element docElement;
    /**
     * Current parent or child element
     */
    protected AntdocSubTask.SubElement subElement;

    public void setDocElement(AntdocSubTask.Element antElement)
    {
        docElement = antElement;
        setCurrentClass(antElement.getXClass());
    }

    /**
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String subElementName() throws XDocletException
    {
        return subElement.getName();
    }

    /**
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String elementName() throws XDocletException
    {
        return docElement.getName();
    }

    /**
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifHasSubElements(String template) throws XDocletException
    {
        if (docElement.getSubElements() != null && docElement.getSubElements().size() > 0) {
            generate(template);
        }
    }

    /**
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllSubElements(String template) throws XDocletException
    {
        XClass old_cur_class = getCurrentClass();

        for (Iterator i = docElement.getSubElements().iterator(); i.hasNext(); ) {
            subElement = (AntdocSubTask.SubElement) i.next();

            setCurrentClass(subElement.getXClass());

            generate(template);
        }

        setCurrentClass(old_cur_class);
    }

    /**
     * @return
     * @doc.tag   type="content"
     */
    public String subElementDescription()
    {
        return subElement.getDescription();
    }

    /**
     * @return
     * @doc.tag   type="content"
     */
    public String required()
    {
        // default value
        String result = null;
        XTag required = getCurrentMethod().getDoc().getTag("ant.required");

        if (required != null) {
            result = required.getValue().trim();
            if (result.equals("")) {
                result = "Yes.";
            }
        }
        else {
            XTag not_required = getCurrentMethod().getDoc().getTag("ant.not-required");

            if (not_required != null) {
                result = not_required.getValue().trim();
                if (result.equals("")) {
                    result = "No.";
                }
            }
            else
                result = "No.";
        }

        return result;
    }

    /**
     * Links to the root folder. Only required to generate links to CSS.
     *
     * @return
     * @todo      refactor this. It's copied more or less from InfoTagsHandler
     * @doc.tag   type="content"
     */
    public String rootlink()
    {
        return getRootlinkFor(getCurrentClass());
    }

    /**
     * @return
     * @doc.tag   type="content"
     */
    public String subElementLink()
    {
        XClass subElementClass = subElement.getSubject().getXClass();

        // see if there is a link from config params
        AntdocSubTask subtask = ((AntdocSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(AntdocSubTask.class)));
        String link = (String) subtask.getConfigParamsAsMap().get(subElementClass.getQualifiedName());

        if (link == null) {
            // we only replace . with / in package. We want to maintain dots in inner class names
            link = getRootlinkFor(docElement.getXClass()) + subElementClass.getContainingPackage().getName().replace('.', '/') + '/' + subElementClass.getName() + ".html";
        }
        return link;
    }

    private String getRootlinkFor(XClass clazz)
    {
        StringTokenizer st = new StringTokenizer(clazz.getQualifiedName(), ".");
        int n = st.countTokens() - 1;
        StringBuffer sbuf = new StringBuffer();

        for (int i = 0; i < n; i++) {
            sbuf.append("../");
        }

        return sbuf.toString();
    }

}
