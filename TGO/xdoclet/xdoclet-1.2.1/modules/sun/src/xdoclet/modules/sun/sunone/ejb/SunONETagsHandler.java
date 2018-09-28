/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.sun.sunone.ejb;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.Translator;

/**
 * @author               <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created              23 February, 2003
 * @xdoclet.taghandler   namespace="SunONE"
 * @version              $Revision: 1.1 $
 */
public class SunONETagsHandler extends AbstractProgramElementTagsHandler
{

    // initialise the secure random instance
    private final static SecureRandom seeder = new SecureRandom();

    /**
     * Cached per JVM server IP.
     */
    private static String hexServerIP = null;
    /**
     * For use in extracting method names.
     */
    protected MethodTagsHandler handler = new MethodTagsHandler();

    /**
     * Collection of attributes. XXX: Does this need to be synchronized?
     */
    protected Map   attributes = Collections.synchronizedMap(new HashMap());

    /**
     * For looping through indexed tags.
     */
    protected int   index = 0;

    /**
     * A 32 byte GUID generator (Globally Unique ID). I couldn't find any documentation for how Sun's own utilities
     * generate them, so I've copied the method used in the generated Util classes. Hopefully that's good enough.
     *
     * @return                      The name of current EJB bean.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public static String generateGUID() throws XDocletException
    {
        return generateGUID(getCurrentClass());
    }

    /**
     * A 32 byte GUID generator (Globally Unique ID). I couldn't find any documentation for how Sun's own utilities
     * generate them, so I've copied the method used in the generated Util classes. Hopefully that's good enough.
     *
     * @param o
     * @return                      The name of current EJB bean.
     * @exception XDocletException
     */
    public static String generateGUID(Object o) throws XDocletException
    {
        StringBuffer tmpBuffer = new StringBuffer(16);

        if (hexServerIP == null) {
            InetAddress localInetAddress = null;

            try {
                // get the inet address
                localInetAddress = InetAddress.getLocalHost();
            }
            catch (java.net.UnknownHostException uhe) {
                System.err.println("Could not get the local IP address using InetAddress.getLocalHost()!");
                // todo: find better way to get around this...
                uhe.printStackTrace();
                return null;
            }

            byte serverIP[] = localInetAddress.getAddress();

            hexServerIP = hexFormat(getInt(serverIP), 8);
        }

        String hashcode = hexFormat(System.identityHashCode(o), 8);

        tmpBuffer.append(hexServerIP);
        tmpBuffer.append(hashcode);

        long timeNow = System.currentTimeMillis();
        int timeLow = (int) timeNow & 0xFFFFFFFF;
        int node = seeder.nextInt();

        StringBuffer guid = new StringBuffer(32);

        guid.append(hexFormat(timeLow, 8));
        guid.append(tmpBuffer.toString());
        guid.append(hexFormat(node, 8));
        return "{" + guid.substring(0, 8) + "-" + guid.substring(8, 12) + "-"
            + guid.substring(12, 16) + "-" + guid.substring(16, 20) + "-"
            + guid.substring(20, 32) + "}";
    }

    private static int getInt(byte bytes[])
    {
        int i = 0;
        int j = 24;

        for (int k = 0; j >= 0; k++) {
            int l = bytes[k] & 0xff;

            i += l << j;
            j -= 8;
        }
        return i;
    }

    private static String hexFormat(int i, int j)
    {
        String s = Integer.toHexString(i);

        return padHex(s, j) + s;
    }

    private static String padHex(String s, int i)
    {
        StringBuffer tmpBuffer = new StringBuffer();

        if (s.length() < i) {
            for (int j = 0; j < i - s.length(); j++) {
                tmpBuffer.append('0');
            }
        }
        return tmpBuffer.toString();
    }

}
