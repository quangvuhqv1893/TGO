/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.bsf;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.Properties;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.ObjectRegistry;
import org.apache.commons.logging.Log;
import xdoclet.XDocletException;

import xdoclet.modules.apache.SubTemplateEngine;
import xdoclet.util.LogUtil;

/**
 * Template engine uses a BSF script to generate output
 *
 * @author    zluspai
 * @created   July 16, 2003
 */
public class BsfSubTemplateEngine implements SubTemplateEngine
{
    /*
     * The scripts share the same namespaces, and should use the
     * bsf.lookupBean() to access the objects. The generated script should be written to the 'out' PrintStream. An example
     * to use it: <pre>
     * <XDtTemplateEngines:BSFGenerator scriptengine="javascript" >
     * out =  bsf.lookupBean ("out");
     * out.println ("Hello World from BSF script!");
     * print out the methods of the current class
     * currentClass = bsf.lookupBean ("currentClass");
     * out.println ("Current class:"+currentClass.getName());
     * out.println ("Methods are:");
     * imethods = currentClass.getMethods().iterator();
     * while (imethods.hasNext()) {
     * method = imethods.next();
     * out.println ("	"+method.getName());
     * }
     * </XDtTemplateEngines:BSFGenerator> </pre>
     */
    private BSFManager bsfManager;


    public BsfSubTemplateEngine()
    {
        Log log = LogUtil.getLog(BsfEngineTagHandler.class, "ctor");

        try {
            log.debug("Creating BSFManager()");
            bsfManager = new BSFManager();
        }
        catch (Exception ex) {
            log.error("Exception when creating BSFManager", ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#getVariable(java.lang.String)
     */
    public Object getVariable(String name)
    {
        ObjectRegistry objreg = bsfManager.getObjectRegistry();

        return objreg.lookup(name);
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#setVariable(java.lang.String, java.lang.Object)
     */
    public void setVariable(String name, Object value)
    {
        // can't store null value in BSF registry
        if (value != null) {
            ObjectRegistry objreg = bsfManager.getObjectRegistry();

            objreg.register(name, value);
        }
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#clearVariables()
     */
    public void clearVariables()
    {
        bsfManager.setObjectRegistry(new ObjectRegistry());
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#generate(java.lang.String, java.util.Properties)
     */
    public String generate(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(BsfEngineTagHandler.class, "generate");

        log.debug("generate() called with attributes:" + attributes);

        String scriptingEngine = attributes.getProperty("scriptengine");

        if (scriptingEngine == null) {
            throw new XDocletException("Missing attribute 'scriptengine' specifying BSF script language");
        }

        // create 'out' object
        ByteArrayOutputStream bbuf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bbuf);

        try {
            BSFEngine bsfEngine = bsfManager.loadScriptingEngine(scriptingEngine);

            bsfManager.getObjectRegistry().register("out", out);
            bsfEngine.exec("", 0, 0, template);

        }
        catch (BSFException e) {
            throw new XDocletException(e, "Exception when running scriptengine='" + scriptingEngine + "'");
        }
        out.flush();
        return bbuf.toString();
    }

}
