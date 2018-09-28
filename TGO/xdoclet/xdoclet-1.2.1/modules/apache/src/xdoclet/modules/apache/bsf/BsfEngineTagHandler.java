/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.bsf;

import java.util.Properties;

import org.apache.commons.logging.Log;
import xdoclet.XDocletException;
import xdoclet.modules.apache.ScriptEngineTagHandler;
import xdoclet.modules.apache.SubTemplateEngine;

import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.util.LogUtil;

/**
 * This is a tag handler able to execute BSF engine on a block!
 *
 * @author               zluspai
 * @created              July 16, 2003
 * @xdoclet.taghandler   namespace="Bsf"
 */
public class BsfEngineTagHandler extends ScriptEngineTagHandler
{
    private BsfSubTemplateEngine bsfEngine;

    /**
     * Get a value of a BSF variable
     *
     * @param attributes
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String getVariable(Properties attributes) throws XDocletException
    {
        return getSubTemplateVariable(getBsfEngine(), attributes);
    }

    /**
     * Evaluates the body block with the BSF script If the silent="yes" attribute is set then the Generator will not
     * produce any output, but the template will run. If the disable="yes" attribute is set then the Velocity template
     * will not run at all.
     *
     * @param template               The body of the block tag
     * @param attributes             The attributes of the template tag
     * @exception TemplateException
     * @doc.tag                      type="block"
     */
    public void generator(String template, Properties attributes) throws TemplateException
    {
        Log log = LogUtil.getLog(BsfEngineTagHandler.class, "generator");

        log.debug("generator() called.");
        generate(getBsfEngine(), template, attributes);
    }

    /**
     * Clear all BSF variables
     *
     * @doc.tag                  type="content"
     * @throws XDocletException
     */
    public void clearVariables() throws XDocletException
    {
        getBsfEngine().clearVariables();
    }

    private BsfSubTemplateEngine getBsfEngine()
    {
        if (bsfEngine == null) {
            bsfEngine = new BsfSubTemplateEngine();
        }
        return bsfEngine;
    }
}

