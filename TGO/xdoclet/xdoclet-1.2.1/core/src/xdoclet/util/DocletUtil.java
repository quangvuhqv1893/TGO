/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.util;

import java.util.StringTokenizer;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @author    <a href="aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created   July 14, 2001
 * @version   $Revision: 1.19 $
 */
public final class DocletUtil
{

    /**
     * Return an array of String from a String containing delimited values. For example "a,b,c" will return a String[3]
     * {"a","b","c"}.
     *
     * @param delimited  the String value to tokenize
     * @param delimiter  the delimiter ("," or ";" for example)
     * @return           an array of String delimited value
     */
    public static String[] tokenizeDelimitedToArray(String delimited, String delimiter)
    {
        StringTokenizer st = new StringTokenizer(delimited, delimiter);
        String[] ret = new String[st.countTokens()];
        int i = 0;

        while (st.hasMoreTokens()) {
            ret[i++] = st.nextToken();
        }
        return ret;
    }
}
