/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.bea.wls.ejb;

/**
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   May 31, 2002
 * @version   $Revision: 1.5 $
 */
public final class XDocletModulesBeaWlsEjbMessages
{
    /**
     * @msg.bundle   msg="You should only specify weblogic:relation join-table-name=\"blabla\" in one of the methods
     *      defining the relationship."
     */
    public final static String JOIN_TABLE_NAME_ONLY_ONE_SIDE = "JOIN_TABLE_NAME_ONLY_ONE_SIDE";

    /**
     * @msg.bundle   msg="The relation {0} seems to be many-to-many (m:n). You should therefore specify
     *      weblogic:relation join-table-name=\"blabla\"."
     */
    public final static String JOIN_TABLE_NAME_NEEDED = "JOIN_TABLE_NAME_NEEDED";

    /**
     * @msg.bundle   msg="Using a different persistence engine than weblogic's own: {0}"
     */
    public final static String NON_WEBLOGIC_PERSISTENCE = "NON_WEBLOGIC_PERSISTENCE";

    /**
     * @msg.bundle   msg="Can''t guess what is the related pk column when there is more than one weblogic.column-map"
     */
    public final static String CANT_GUESS_PK_FIELD = "CANT_GUESS_PK_FIELD";

    /**
     * @msg.bundle   msg="There {0} bean has a compound key. Please specify pk-field"
     */
    public final static String MORE_THAN_ONE_PK_FIELD = "MORE_THAN_ONE_PK_FIELD";

    /**
     * @msg.bundle   msg="There {0} bean doesn''t specify ejb.persistence column-name"
     */
    public final static String NO_PK_FIELD = "NO_PK_FIELD";

}
