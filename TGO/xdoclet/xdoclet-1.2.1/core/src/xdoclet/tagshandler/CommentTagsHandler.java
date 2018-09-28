/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import xjavadoc.XJavaDoc;

import xdoclet.XDocletTagSupport;

/**
 * @author               Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created              Feb 12, 2002
 * @xdoclet.taghandler   namespace="Comment"
 * @version              $Revision: 1.5 $
 */
public class CommentTagsHandler extends XDocletTagSupport
{
    /**
     * This tag simply output nothing. It is used to include comments in template files
     *
     * @param template
     * @doc.tag         type="block"
     */
    public void comment(String template)
    {
    }

}
