/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.util;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Oct 15, 2001
 * @version   $Revision: 1.5 $
 */
public final class TypeConversionUtil
{
    /**
     * A utility method for converting a string to a boolean. "yes", "no", "true", "false", "1", "0", "on" and "off" are
     * valid values for a boolean string (ignoring case). If not one of then then the value of defaultValue parameter is
     * returned.
     *
     * @param defaultValue  Description of Parameter
     * @param in            The String to convert
     * @return              true or false
     */
    public static boolean stringToBoolean(String in, boolean defaultValue)
    {
        if (in == null || in.trim().length() == 0) {
            return defaultValue;
        }
        else {
            if (in.equalsIgnoreCase("on")) {
                return true;
            }
            if (in.equalsIgnoreCase("off")) {
                return false;
            }

            switch (in.charAt(0)) {
            case '1':
            case 't':
            case 'T':
            case 'y':
            case 'Y':
                return true;
            case '0':
            case 'f':
            case 'F':
            case 'n':
            case 'N':
                return false;
            default:
                return defaultValue;
            }
        }
    }
}
