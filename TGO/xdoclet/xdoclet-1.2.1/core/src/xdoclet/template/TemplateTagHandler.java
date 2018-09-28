/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

import xjavadoc.XJavaDoc;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Oct 14, 2001
 * @version   $Revision: 1.12 $
 */
public abstract class TemplateTagHandler
{
    private static XJavaDoc _xJavaDoc;

    public static void setXJavaDoc(XJavaDoc xJavaDoc)
    {
        if (xJavaDoc == null) {
            throw new IllegalStateException("xJavaDoc can't be null");
        }
        _xJavaDoc = xJavaDoc;
    }

    protected static XJavaDoc getXJavaDoc()
    {
        return _xJavaDoc;
    }

}
