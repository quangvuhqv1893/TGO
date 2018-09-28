/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.velocity;

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
 * This is a tag handler able to execute Velocity template engine on a block!
 *
 * @author               zluspai
 * @created              July 16, 2003
 * @xdoclet.taghandler   namespace="Velocity"
 */
public class VelocityEngineTagHandler extends ScriptEngineTagHandler
{
    /*
     * @todo - put this section in the above docs, but needs care so the reformatter doesn't mangle it
     * <XDtTemplateEngines:generator>
     * currentClass is: ${currentClass.Name}
     * Methods are: #set ( $numMethods = 0 )
     * #foreach ($method in ${currentClass.methods})
     * $method.Name #set ( $numMethods = $numMethods+1 )
     * #end </XDtTemplateEngines:generator>
     * And you can get a variable from the last run Velocity context by using this XDoclet tag:
     * <XDtTemplateEngines:getVelocityVariable name="numMethods" />
     * Also if the result of the template contains a special <XDt></XDt> section that will be merged by the XDoclet
     * engine. For example:
     * <XDtTemplateEngines:generator>
     * Print out the methods using the XDoclet Templates:
     * <XDt> <XDtMethod:forAllMethods> <XDtMethod:methodName/> </XDtMethod:forAllMethods> </XDt>
     * </XDtTemplateEngines:generator>
     */
    private VelocitySubTemplateEngine velocityEngine;


    /**
     * Get a value of a velocity variable from the context <pre>
     * <XDtTemplateEngines:getVelocityVariable name="numMethods" default="0" />
     * </pre>
     *
     * @param attributes
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String getVariable(Properties attributes) throws XDocletException
    {
        return getSubTemplateVariable(getVelocityEngine(), attributes);
    }

    /**
     * Evaluates the body block with the Velocity template engine If the silent="yes" attribute is set then the
     * Generator will not produce any output, but the template will run. If the disable="yes" attribute is set then the
     * Velocity template will not run at all.
     *
     * @param template               The body of the block tag
     * @param attributes             The attributes of the template tag
     * @exception TemplateException
     * @doc.tag                      type="block"
     */
    public void generator(String template, Properties attributes) throws TemplateException
    {
        generate(getVelocityEngine(), template, attributes);
    }

    /**
     * Clear all velocity variables
     *
     * @doc.tag                  type="content"
     * @throws XDocletException
     */
    public void clearVariables() throws XDocletException
    {
        getVelocityEngine().clearVariables();
    }

    private VelocitySubTemplateEngine getVelocityEngine()
    {
        if (velocityEngine == null) {
            velocityEngine = new VelocitySubTemplateEngine();
        }
        return velocityEngine;
    }
}

