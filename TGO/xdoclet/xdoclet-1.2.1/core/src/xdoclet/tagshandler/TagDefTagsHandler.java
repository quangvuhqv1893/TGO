/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.Properties;

import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XDocletTagSupport;
import xdoclet.template.TemplateException;
import xdoclet.template.TemplateTagHandler;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * The implementation of TagDef template tag. It's mainly designed for use by end users that want to define template
 * tags of their own but don't want to touch xdoclet's tag mapping file and modify the jar file.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Nov 11, 2001
 * @xdoclet.taghandler   namespace="TagDef"
 * @version              $Revision: 1.7 $
 */
public class TagDefTagsHandler extends XDocletTagSupport
{
    /**
     * Defines a template tag handler for a template tag to TemplateEngine.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="namespace" optional="false" description="The template namespace name, 'Merge'
     *      for example if we were to define template namespace 'Merge' this way."
     * @doc.param                   name="handler" optional="false" description="The template tag handler full qualified
     *      class name. It's the class that implements tags of namespace. It should be a public class, with a no
     *      argument public constructor, and should extend xdoclet.XDocletTagSupport."
     */
    public String tagDef(Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(TagDefTagsHandler.class, "tagDef");

        String namespace = attributes.getProperty("namespace");
        String handlerFullClassName = attributes.getProperty("handler");

        if (log.isDebugEnabled()) {
            log.debug("namespace=" + namespace);
            log.debug("handler=" + handlerFullClassName);
        }

        if (namespace == null) {
            mandatoryTemplateTagParamNotFound("tagDef", "namespace");
        }

        if (handlerFullClassName == null) {
            mandatoryTemplateTagParamNotFound("tagDef", "handler");
        }

        Class handlerClass = null;

        try {
            handlerClass = getClass().getClassLoader().loadClass(handlerFullClassName);

            Object handlerInstance = handlerClass.newInstance();

            getEngine().setTagHandlerFor(namespace, (TemplateTagHandler) handlerInstance);
        }
        catch (ClassNotFoundException e) {
            String msg = Translator.getString(XDocletMessages.class, XDocletMessages.CLASS_NOT_FOUND,
                new String[]{handlerFullClassName});

            log.error(msg, e);
            throw new XDocletException(e, msg);
        }
        catch (InstantiationException e) {
            String msg = Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.TAGDEF_INSTANTIATION_EXCEPTION,
                new String[]{handlerClass.toString()});

            log.error(msg, e);
            throw new XDocletException(e, msg);
        }
        catch (IllegalAccessException e) {
            String msg = Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.TAGDEF_ILLEGALACCESS_EXCEPTION,
                new String[]{handlerClass.toString()});

            log.error(msg, e);
            throw new XDocletException(e, msg);
        }
        catch (TemplateException e) {
            String msg = Translator.getString(XDocletMessages.class, XDocletTagshandlerMessages.TAGDEF_COULDNT_DEF_HANDLER,
                new String[]{handlerFullClassName, namespace});

            log.error(msg, e);
            throw new XDocletException(e, msg);
        }

        return "";
    }
}
