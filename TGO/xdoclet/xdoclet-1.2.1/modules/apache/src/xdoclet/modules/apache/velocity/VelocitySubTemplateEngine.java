/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.velocity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import xdoclet.XDocletException;
import xdoclet.modules.apache.SubTemplateEngine;

/**
 * Velocity subtemplate engine
 *
 * @author    zluspai
 * @created   July 16, 2003
 */
class VelocitySubTemplateEngine implements SubTemplateEngine
{

    // context where the Velocity variables stored
    private VelocityContext context = new VelocityContext();

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#getVariable(java.lang.String)
     */
    public Object getVariable(String name)
    {
        return context.get(name);
    }


    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#setVariable(java.lang.String, java.lang.Object)
     */
    public void setVariable(String name, Object value)
    {
        context.put(name, value);
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#clearVariables()
     */
    public void clearVariables()
    {
        context = new VelocityContext();
    }

    /*
     * (non-Javadoc)
     * @see xdoclet.templateutil.SubTemplateEngine#generate(java.lang.String, java.util.Properties)
     */
    public String generate(String template, Properties attributes) throws XDocletException
    {
        //		write out the current template as a temp file
//        String templateFile = "TEMP_" + System.currentTimeMillis() + ".vm";
        //		String templateFile = "c:\\temp\\"+System.currentTimeMillis()+".vm";
//        File f = new File(templateFile);

//        f.deleteOnExit();
        try {
//            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
//
//            os.write(template.getBytes());
//            os.flush();
//            os.close();

            /*
             * setup
             */
            Velocity.init();

            //init("velocity.properties");

            /*
             * Make a context object and populate with the data.  This
             * is where the Velocity engine gets the data to resolve the
             * references (ex. $list) in the template
             */
            //		VelocityContext context = new VelocityContext();

            /*
             * get the Template object.  This is the parsed version of your
             * template input file.  Note that getTemplate() can throw
             * ResourceNotFoundException : if it doesn't find the template
             * ParseErrorException : if there is something wrong with the VTL
             * Exception : if something else goes wrong (this is generally
             * indicative of as serious problem...)
             */
            /*
             * Template velocityTemplate = null;
             * try {
             * velocityTemplate = Velocity.
             * /.getTemplate(templateFile);
             * }
             * catch (ResourceNotFoundException rnfe) {
             * throw new XDocletException(rnfe, "Cannot load temporary template file:" + templateFile);
             * }
             * catch (ParseErrorException pee) {
             * throw new XDocletException(pee, "Syntax error in template!");
             * }
             */
            /*
             * Now have the template engine process your template using the
             * data placed into the context.  Think of it as a  'merge'
             * of the template and the data to produce the output stream.
             */
            StringWriter writer = new StringWriter();

//            if (velocityTemplate != null) {
            Velocity.evaluate(context, writer, "generate", template);
//                velocityTemplate.merge(context, writer);
//            }

            /*
             * flush and cleanup
             */
            writer.flush();
            writer.close();

            return writer.getBuffer().toString();
        }
        catch (Exception e) {
            throw new XDocletException(e, "Exception when executing Velocity template!");
        }
//        finally {
//            f.delete();
//        }
    }
}

