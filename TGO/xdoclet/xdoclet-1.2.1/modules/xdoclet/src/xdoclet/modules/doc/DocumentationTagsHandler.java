/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc;

import java.util.*;

import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.template.TemplateTagHandler;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Nov 30, 2001
 * @xdoclet.taghandler   namespace="Doc"
 * @version              $Revision: 1.5 $
 */
public class DocumentationTagsHandler extends XDocletTagSupport
{
    private String  currentNamespace;

    /**
     * Iterates over all template namespaces registered in /tagmappings.properties file and evaluates the body of the
     * tag for each namespace.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void forAllNamespaces(String template) throws XDocletException
    {
        for (Iterator iterator = getSortedNameSpaces().iterator(); iterator.hasNext(); ) {
            currentNamespace = (String) iterator.next();

            try {
                TemplateTagHandler th = TemplateEngine.getEngineInstance().getTagHandlerFor(currentNamespace);

                setCurrentClass(getXJavaDoc().getXClass(th.getClass().getName()));

                generate(template);
            }
            catch (TemplateException e) {
                throw new XDocletException(e, "Error getting tag handler");
            }
        }
    }

    /**
     * Returns current namespace name. Use inside forAllNamespaces only.
     *
     * @return                      Current namespace name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String namespace() throws XDocletException
    {
        return currentNamespace;
    }

    /**
     * Returns current namespace tags handler class name. Use inside forAllNamespaces only.
     *
     * @return                      Current namespace tags handler class name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String namespaceTagsHandlerClassName() throws XDocletException
    {
        try {
            return TemplateEngine.getEngineInstance().getTagHandlerFor(currentNamespace).getClass().getName();
        }
        catch (TemplateException e) {
            throw new XDocletException(e, "Error getting tag handler for " + currentNamespace);
        }
    }

    /**
     * Returns current namespace name from current class name.
     *
     * @return                      Current namespace tags handler class name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String namespaceFromClassName() throws XDocletException
    {
        for (Iterator iterator = getSortedNameSpaces().iterator(); iterator.hasNext(); ) {
            String tempNamespace = (String) iterator.next();

            try {
                if (TemplateEngine.getEngineInstance().getTagHandlerFor(tempNamespace).getClass().getName().equals(getCurrentClass().getQualifiedName())) {
                    return tempNamespace;
                }
            }
            catch (TemplateException e) {
                throw new XDocletException(e, "Error getting tag handler for" + tempNamespace);
            }
        }

        return null;
    }

    /**
     * Returns current namespace. Used for tags_toc.xdt.
     *
     * @return                      Current namespace tags handler class name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String currentNamespace() throws XDocletException
    {
        return getActiveDocumentTagsSubTask().getCurrentNamespace();
    }

    /**
     * Returns current namespace tags handler class name. Used for <namespace> .html files.
     *
     * @return                      Current namespace name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="namespace" optional="false" description="The namespace name we lookup the
     *      handler."
     */
    public String currentNamespaceTagsHandlerClassName() throws XDocletException
    {
        try {
            return TemplateEngine.getEngineInstance().getTagHandlerFor(getActiveDocumentTagsSubTask().getCurrentNamespace()).getClass().getName();
        }
        catch (TemplateException e) {
            throw new XDocletException(e, "Error getting tag handler for " + getActiveDocumentTagsSubTask().getCurrentNamespace());
        }
    }

    /**
     * Returns current namespace name. Use inside forAllNamespaces only.
     *
     * @return                      Current namespace name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="namespace" optional="false" description="The namespace name we lookup the
     *      handler."
     */
    public String currentNamespaceTagsHandlerClassNameAsDirStructure() throws XDocletException
    {
        return currentNamespaceTagsHandlerClassName().replace('.', '/');
    }

    private final List getSortedNameSpaces()
    {
        ArrayList result = new ArrayList(TemplateEngine.getEngineInstance().getNamespaces());

        Collections.sort(result);
        return result;
    }

    /**
     * Gets the ActiveDocumentTagsSubTask attribute of the DocumentationTagsHandler object.
     *
     * @return   The ActiveDocumentTagsSubTask value
     */
    private DocumentTagsSubTask getActiveDocumentTagsSubTask()
    {
        return (DocumentTagsSubTask) getDocletContext().getActiveSubTask();
    }
}
