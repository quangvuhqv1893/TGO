/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.solarmetric.jdo;

/**
 * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
 * @created   11 October 2002
 * @version   $Revision: 1.2 $
 */
public final class XDocletModulesSolarmetricMessages
{

    /**
     * @msg.bundle   msg="Kodo doesn't support the use of primary key fields with datastore identity. Use application
     *      identity or use a hidden int column as primary key (not visible in the class)"
     */
    public final static String NOT_SUPPORTED_PK_FIELD_AND_DATASTORE_IDENTITY = "NOT_SUPPORTED_PK_AND_DATASTORE";

    /**
     * @msg.bundle   msg="Kodo doesn't support the use of tag kodo.table with attribute pk-column with datastore
     *      identity."
     */
    public final static String NOT_SUPPORTED_PK_COLUMN_AND_DATASTORE_IDENTITY = "NOT_SUPPORTED_PK_AND_DATASTORE";

}
