/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache;

import java.util.Properties;
import org.apache.commons.logging.Log;
import xdoclet.XDocletException;

import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.util.LogUtil;

/**
 * @created   July 17, 2003
 */
public abstract class ScriptEngineTagHandler extends AbstractProgramElementTagsHandler
{
    // XDOCLET subtemplate section start tag inside the other template
    protected final String XDTSectionStart = "<XDt>";
    // subtemplate end tag
    protected final String XDTSectionEnd = "</XDt>";

    /**
     * Get a subtemplate variable
     *
     * @param subengine          The subtemplate engine
     * @param attributes         The attributes from XDOCLET tag
     * @return                   The value
     * @throws XDocletException
     */
    protected final String getSubTemplateVariable(SubTemplateEngine subengine, Properties attributes) throws XDocletException
    {
        String vname = attributes.getProperty("name");

        if (vname == null) {
            throw new XDocletException("Missing name property the name of the Velocity variable!");
        }

        Object value = subengine.getVariable(vname);

        if (value == null) {
            String defaultValue = attributes.getProperty("default", "");

            return defaultValue;
        }
        return value.toString();
    }

    /**
     * @param subengine                            The SubTemplateEngine used to generate
     * @param template                             The body of the block tag
     * @param attributes                           The attributes of the template tag
     * @exception TemplateException
     * @throws xdoclet.template.TemplateException
     */
    protected final void generate(SubTemplateEngine subengine, String template, Properties attributes) throws TemplateException
    {
        if ("yes".equalsIgnoreCase(attributes.getProperty("disable"))) {
            return;
        }

        fillVariables(subengine);

        String result = subengine.generate(template, attributes);
        Log log = LogUtil.getLog(ScriptEngineTagHandler.class, "generate");

        log.debug("Subengine generated results:" + result);

        StringBuffer output = new StringBuffer(result);

        // send back the XDOCLET engine the velocity output, but only when silent="true" is not set
        if (!"yes".equalsIgnoreCase(attributes.getProperty("silent"))) {
            TemplateEngine engine = getEngine();

            escapeResults(engine, output);
            engine.print(output.toString());
        }
    }


    /**
     * Fill the variables passed to the engines
     *
     * @param templateEngine
     * @exception XDocletException
     */
    protected final void fillVariables(SubTemplateEngine templateEngine) throws XDocletException
    {
        Log log = LogUtil.getLog(ScriptEngineTagHandler.class, "fillVariables");

        log.debug("fillVariables() called");
        try {
            templateEngine.setVariable("tagHandler", this);
            templateEngine.setVariable("currentPackage", getCurrentPackage());
            templateEngine.setVariable("currentClass", getCurrentClass());
            templateEngine.setVariable("currentMethod", getCurrentMethod());
            templateEngine.setVariable("currentConstructor", getCurrentConstructor());
            templateEngine.setVariable("currentField", getCurrentField());
            templateEngine.setVariable("currentClassTag", getCurrentClassTag());
            templateEngine.setVariable("currentFieldTag", getCurrentFieldTag());
            templateEngine.setVariable("currentMethodTag", getCurrentMethodTag());
        }
        catch (Exception ex) {
            log.error("Exception when setting variables", ex);
            throw new XDocletException(ex, "Exception when setting variables");
        }
    }

    /**
     * Escape and evaluate the <XDt></XDt> sections with XDOCLET template engine. This allows embedding XDOCLET sections
     * into Velocity sections
     *
     * @param engine                 The XDOCLET template engine
     * @param results                The results
     * @exception TemplateException
     */
    protected final void escapeResults(TemplateEngine engine, StringBuffer results) throws TemplateException
    {
        Log log = LogUtil.getLog(ScriptEngineTagHandler.class, "escapeResults");

        // will return when no more subtemplate found
        while (true) {
            // find the <XDt> sections
            int startidx = results.indexOf(XDTSectionStart);
            int endidx = results.indexOf(XDTSectionEnd);

            boolean templatefound = (startidx >= 0 && endidx >= 0);

            if (!templatefound)
                return;

            // extract the content of the <XDt> section
            String subXDtTemplate = results.substring(startidx + XDTSectionStart.length(), endidx);

            log.debug("subTemplate found:<" + subXDtTemplate + ">");

            // remove the <XDt> section
            results.delete(startidx, endidx + XDTSectionEnd.length());
            log.debug("Results after XDt section removed:<" + results.toString() + ">");

            // generate the subtemplate by calling the XDOCLET engine
            String subTemplateOutput = engine.outputOf(subXDtTemplate);

            log.debug("Generated subTempalte output:" + subTemplateOutput);

            // write back the result
            results.insert(startidx, subTemplateOutput);
        }
    }
}
