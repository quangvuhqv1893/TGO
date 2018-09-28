/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Oct 14, 2001
 * @version   $Revision: 1.5 $
 */
public class TemplateException extends Exception
{
    private Exception nestedException = null;

    /**
     * Describe what the TemplateException constructor does
     *
     * @param msg  Describe what the parameter does
     */
    public TemplateException(String msg)
    {
        this(null, msg);
    }

    /**
     * Describe what the TemplateException constructor does
     *
     * @param nestedException  Describe what the parameter does
     * @param msg              Describe what the parameter does
     */
    public TemplateException(Exception nestedException, String msg)
    {
        super(msg);
        this.nestedException = nestedException;
    }

    /**
     * Gets the NestedException attribute of the TemplateException object
     *
     * @return   The NestedException value
     */
    public Exception getNestedException()
    {
        return nestedException;
    }
}
