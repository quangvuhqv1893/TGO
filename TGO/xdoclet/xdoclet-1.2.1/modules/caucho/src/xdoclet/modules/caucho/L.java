/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.caucho;

import xdoclet.XDocletException;
import xdoclet.util.Translator;

/**
 * overkill
 *
 * @author    Yoritaka Sakakura (yori@teardrop.org)
 * @created   June 6, 2002
 */
public final class L
{
    /**
     * @msg.bundle   msg="Generated from {0}."
     */
    public final static String GENERATED_FROM = "GENERATED_FROM";

    /**
     * @msg.bundle   msg="No current Javadoc tag."
     */
    public final static String NO_CURRENT_JAVADOC_TAG = "NO_CURRENT_JAVADOC_TAG";

    /**
     * @msg.bundle   msg="No current method."
     */
    public final static String NO_CURRENT_METHOD = "NO_CURRENT_METHOD";

    /**
     * @msg.bundle   msg="No current parameter."
     */
    public final static String NO_CURRENT_PARAMETER = "NO_CURRENT_PARAMETER";


    static XDocletException error(String key)
    {
        return error(key, null);
    }

    static XDocletException error(String key, String[] args)
    {
        return new XDocletException(l(key, args));
    }

    static String l(String key)
    {
        return l(key, null);
    }

    static String l(String key, String[] args)
    {
        return Translator.getString(L.class, key, args);
    }
}
