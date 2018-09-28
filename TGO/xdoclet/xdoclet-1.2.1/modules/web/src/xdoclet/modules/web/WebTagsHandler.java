/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.web;

import xdoclet.XDocletException;

import xdoclet.XDocletTagSupport;
import xdoclet.tagshandler.ClassTagsHandler;

/**
 * @author               Marcus Brito (pazu@animegaiden.com.br)
 * @created              Jun 28, 2002
 * @version              $Revision: 1.3 $
 * @xdoclet.taghandler   namespace="Web"
 */
public class WebTagsHandler extends XDocletTagSupport
{
    /**
     * Process the tag body for each web:ejb-ref tag in all source files. Please note that this tag already iterates
     * over all available sources; it should <em>not</em> be enclosed by a &lt;XDtClass:forAllClasses> tag or any other
     * that process classes. This tag does not process tags with duplicated name attributes. If such tags occurs, only
     * the first tag is processed, and further tags will only emit a warning message.
     *
     * @param template           The body of the block tag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllEjbRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:ejb-ref", "name");
    }

    /**
     * Process the tag body for each web:ejb-local-ref tag in all source files. Look at forAllEjbRefs for some notes
     * about the behavior of this tag.
     *
     * @param template           The body of the blocktag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllEjbLocalRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:ejb-local-ref", "name");
    }

    /**
     * Process the tag body for each web:resource-ref tag in all source files. Look at forAllEjbRefs for some notes
     * about the behavior of this tag.
     *
     * @param template           The body of the blocktag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllResourceRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:resource-ref", "name");
    }

    /**
     * Process the tag body for each web:resource-env-ref tag in all source files. Look at forAllEjbRefs for some notes
     * about the behavior of this tag.
     *
     * @param template           The body of the blocktag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllResourceEnvRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:resource-env-ref", "name");
    }

    /**
     * Process the tag body for each web:security-role tag in all source files. Look at forAllEjbRefs for some notes
     * about the behavior of this tag.
     *
     * @param template           The body of the blocktag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllSecurityRoles(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:security-role", "role-name");
    }

    /**
     * Process the tag body for each web:env-entry tag in all source files. Look at forAllEjbRefs for some notes about
     * the behavior of this tag.
     *
     * @param template           The body of the blocktag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="block"
     */
    public void forAllEnvEntries(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "web:env-entry", "name");
    }
}
