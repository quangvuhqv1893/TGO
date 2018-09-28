/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;

import xdoclet.template.TemplateException;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * This class serves as an entry-point for starting XDoclet.
 *
 * @author    Ara Abrahamian (ara_e@email.com)
 * @author    Aslak Hellesoy
 * @created   April 30, 2001
 */
public class XDocletMain
{
    /**
     * Starts XDoclet
     *
     * @param xJavaDoc
     * @exception XDocletException
     */
    protected void start(XJavaDoc xJavaDoc) throws XDocletException
    {
        Log log = LogUtil.getLog(XDocletMain.class, "start");

        try {
            log.debug("Context successfully loaded.");

            SubTask[] subtasks = DocletContext.getInstance().getSubTasks();

            for (int i = 0; i < subtasks.length; i++) {
                if (subtasks[i] != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("SubTask " + subtasks[i].getSubTaskName() + " initialized.");
                    }

                    subtasks[i].init(xJavaDoc);
                    DocletContext.getInstance().setActiveSubTask(subtasks[i]);

                    log.info(Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_TASKNAME, new String[]{"<" + subtasks[i].getSubTaskName() + "/>"}));
                    subtasks[i].execute();
                }
            }
        }
        catch (XDocletException e) {
            log.error(Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
            log.error("<<" + e.getMessage() + ">>");

            if (e.getNestedException() != null) {
                e.getNestedException().printStackTrace();
            }

            if (e.getNestedException() instanceof TemplateException) {
                TemplateException te = (TemplateException) e.getNestedException();

                if (log.isDebugEnabled()) {
                    log.error("Template Exception = " + te);
                    log.error("Nested Exception = " + te.getNestedException());
                }
            }
            throw e;
        }
    }
}
