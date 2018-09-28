/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import xdoclet.template.TemplateException;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Oct 14, 2001
 * @version   $Revision: 1.7 $
 */
public class XDocletException extends TemplateException
{
    /**
     * Describe what the XDocletException constructor does
     *
     * @param msg  Describe what the parameter does
     */
    public XDocletException(String msg)
    {
        this(null, msg);
    }

    /**
     * Describe what the XDocletException constructor does
     *
     * @param nestedException  Describe what the parameter does
     * @param msg              Describe what the parameter does
     */
    public XDocletException(Exception nestedException, String msg)
    {
        super(nestedException, msg);
    }

    /**
     * Gets the PrintStackTrace attribute of the XDocletException object
     *
     * @return   The PrintStackTrace value
     */
    public String getPrintStackTrace()
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            super.printStackTrace(writer);
            baos.close();
            writer.close();

            return new String(baos.toByteArray());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

