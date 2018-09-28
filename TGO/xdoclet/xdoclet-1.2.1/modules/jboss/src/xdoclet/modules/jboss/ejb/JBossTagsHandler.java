/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;
import xjavadoc.*;
import xdoclet.XDocletException;
import xdoclet.tagshandler.ClassTagsHandler;

/**
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @author               Julien Viet (julien_viet@yahoo.fr)
 * @created              October 18, 2001
 * @xdoclet.taghandler   namespace="JBoss"
 * @version              $Revision: 1.10 $
 */
public class JBossTagsHandler extends ClassTagsHandler
{
    /**
     * Evaluates the body if at least one of the classes has an jboss:dvc tag, otherwise not.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     */
    public void ifHasDVC(String template) throws XDocletException
    {
        boolean hasDVC = false;
        Collection classes = getXJavaDoc().getSourceClasses();

        for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
            XClass clazz = i.next();
            XTag dvc = clazz.getDoc().getTag("jboss:dvc");

            if (dvc != null) {
                hasDVC = true;
                break;
            }
        }

        // Looks for a jbosscmp-jdbc-dvc.xml merge file
        if (!hasDVC) {
            File mergeFile = new File(getDocletContext().getActiveSubTask().getMergeDir(), "jbosscmp-jdbc-dvc.xml");

            if (mergeFile.exists())
                hasDVC = true;
        }

        if (hasDVC) {
            generate(template);
        }
    }

    public void ifMethodTagMatchesClassTag(String template, Properties attributes) throws XDocletException
    {
        String paramName = attributes.getProperty("paramName");
        String classLoadGroup = getCurrentClassTag().getAttributeValue(paramName);
        String methodLoadGroup = getCurrentMethodTag().getAttributeValue(paramName);

        if (classLoadGroup.equals(methodLoadGroup)) {
            generate(template);
        }
    }

}
