/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.web;

import xdoclet.XDocletException;
import xdoclet.tagshandler.ClassTagsHandler;

/**
 * @author               Marcus Brito (pazu@animegaiden.com.br)
 * @created              September 08, 2002
 * @modified             Jon Barnett (jbarnett@pobox.com)
 * @xdoclet.taghandler   namespace="JBossWeb"
 * @version              $Revision: 1.3 $
 */
public class JBossWebTagsHandler extends ClassTagsHandler
{
    /**
     * Iterates over all \@jboss.resource-ref tags.
     *
     * @param template           The body of the block tag
     * @throws XDocletException  if something goes wrong
     */
    public void forAllResourceRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "jboss.resource-ref", "res-ref-name");
    }

    /**
     * Iterates over all \@jboss.ejb-ref-jndi tags.
     *
     * @param template           The body of the block tag
     * @throws XDocletException  if something goes wrong
     */
    public void forAllEjbRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "jboss.ejb-ref-jndi", "ref-name");
    }

    /**
     * Iterates over all \@jboss.ejb-local-ref tags.
     *
     * @param template           The body of the block tag
     * @throws XDocletException  if something goes wrong
     */
    public void forAllLocalRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "jboss.ejb-local-ref", "ref-name");
    }

    public void forAllResourceEnvRefs(String template) throws XDocletException
    {
        ClassTagsHandler.forAllDistinctClassTags(getEngine(), template, "jboss:resource-env-ref", "resource-env-ref-name");
    }
}
