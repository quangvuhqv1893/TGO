/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

/**
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   May 31, 2002
 * @version   $Revision: 1.2 $
 */
public final class XDocletTagshandlerMessages
{
    /**
     * @msg.bundle   msg="{0} can only be called when the current field is not null."
     */
    public final static String ONLY_CALL_FIELD_NOT_NULL = "ONLY_CALL_FIELD_NOT_NULL";

    /**
     * @msg.bundle   msg="{0} can only be called when the current constructor is not null."
     */
    public final static String ONLY_CALL_CONSTRUCTOR_NOT_NULL = "ONLY_CALL_CONSTRUCTOR_NOT_NULL";

    /**
     * @msg.bundle   msg="{0} can only be called when the current method is not null."
     */
    public final static String ONLY_CALL_METHOD_NOT_NULL = "ONLY_CALL_METHOD_NOT_NULL";
    /**
     * @msg.bundle   msg="Could not define tag handler class ''{0}'' for template tag ''{1}''."
     */
    public final static String TAGDEF_COULDNT_DEF_HANDLER = "TAGDEF_COULDNT_DEF_HANDLER";
    /**
     * @msg.bundle   msg="Could not instantiate class ''{0}'', not accessible. Template tag handler classes should have
     *      a public no argument constructor."
     */
    public final static String TAGDEF_ILLEGALACCESS_EXCEPTION = "TAGDEF_ILLEGALACCESS_EXCEPTION";
    /**
     * @msg.bundle   msg="Could not instantiate class ''{0}''. Template tag handler classes should have a public no
     *      argument constructor."
     */
    public final static String TAGDEF_INSTANTIATION_EXCEPTION = "TAGDEF_INSTANTIATION_EXCEPTION";
    /**
     * @msg.bundle   msg="{0} parameter not specified for id element. Ignoring id element."
     */
    public final static String ID_PARAM_MISSING = "ID_PARAM_MISSING";
    /**
     * @msg.bundle   msg="{0} attribute must be set!"
     */
    public final static String ATTRIBUTE_NOT_SET_ERROR = "ATTRIBUTE_NOT_SET_ERROR";
}
