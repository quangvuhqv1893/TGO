/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

/**
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   May 31, 2002
 * @version   $Revision: 1.3 $
 */
public final class XDocletTemplateMessages
{
    /**
     * @msg.bundle   msg="Error in template file: \" sign expected but something different found, line={0} of template
     *      file: {1}"
     */
    public final static String TEMPLATE_QUOTE_EXPECTED = "TEMPLATE_QUOTE_EXPECTED";

    /**
     * @msg.bundle   msg="Error in template file: = sign expected but something different found, line={0} of template
     *      file: {1}"
     */
    public final static String TEMPLATE_EQUALS_EXPECTED = "TEMPLATE_EQUALS_EXPECTED";

    /**
     * @msg.bundle   msg="Template file {0} not found."
     */
    public final static String TEMPLATE_NOT_FOUND = "TEMPLATE_NOT_FOUND";

    /**
     * @msg.bundle   msg="An error occured while writing output to file {0}"
     */
    public final static String TEMPLATE_ERROR_WRITING_OUTPUT = "TEMPLATE_ERROR_WRITING_OUTPUT";

    /**
     * @msg.bundle   msg="Could not find tag handler for prefix: {0}"
     */
    public final static String TEMPLATE_NO_TAGHANDLER = "TEMPLATE_NO_TAGHANDLER";

    /**
     * @msg.bundle   msg="Unable to create instance of tag handler class: {0} because {1}"
     */
    public final static String TEMPLATE_ILLEGALACCESSEXCEPTION = "TEMPLATE_ILLEGALACCESSEXCEPTION";

    /**
     * @msg.bundle   msg="Unable to create instance of tag handler class: {0} because {1}"
     */
    public final static String TEMPLATE_INSTANTIATIONEXCEPTION = "TEMPLATE_INSTANTIATIONEXCEPTION";

    /**
     * @msg.bundle   msg="Unable to load tag handler class: {0} because {1}"
     */
    public final static String TEMPLATE_CLASSNOTFOUNDEXCEPTION = "TEMPLATE_CLASSNOTFOUNDEXCEPTION";

    /**
     * @msg.bundle   msg="Unable to load tag handler class: {0} because {1}"
     */
    public final static String TEMPLATE_NOCLASSDEFFOUNDERROR = "TEMPLATE_NOCLASSDEFFOUNDERROR";

    /**
     * @msg.bundle   msg="Couldn''t load tag mapping file from jar."
     */
    public final static String TEMPLATE_COULDNT_LOAD_MAPPINGS = "TEMPLATE_COULDNT_LOAD_MAPPINGS";

    /**
     * @msg.bundle   msg="Could not find method {0} in class {1} ({2})"
     */
    public final static String TEMPLATE_NO_SUCH_METHOD = "TEMPLATE_NO_SUCH_METHOD";

    /**
     * @msg.bundle   msg="Invoking method in class {0} failed: {1}, line={2} of template file: {3}, exception: {4}"
     */
    public final static String TEMPLATE_INVOKE_METHOD_FAILED = "TEMPLATE_INVOKE_METHOD_FAILED";

    /**
     * @msg.bundle   msg="Error in template file: corresponding {0} not found, line={1} of template file: {2}"
     */
    public final static String TEMPLATE_CORRESPONDING_TAG_MISSING = "TEMPLATE_CORRESPONDING_TAG_MISSING";

    /**
     * @msg.bundle   msg="Error in template file: corresponding {0} not found, line={1} of template file: {2}"
     */
    public final static String TEMPLATE_CLOSE_TAG_MISSING = "TEMPLATE_CLOSE_TAG_MISSING";

    /**
     * @msg.bundle   msg="Error in template file: > sign expected after / sign but something different found, line={0}
     *      of template file: {1}"
     */
    public final static String TEMPLATE_GT_EXPECTED = "TEMPLATE_GT_EXPECTED";

    /**
     * @msg.bundle   msg="Unable to read properties file due to IOException: {0}"
     */
    public final static String TEMPLATE_IOEXCEPTION = "TEMPLATE_IOEXCEPTION";

    /**
     * @msg.bundle   msg="''{0}'' parameter missing. Specify both ''destinationFile'' and ''templateFile'' configuration
     *      parameters please."
     */
    public final static String TEMPLATE_PARAMETER_MISSING = "TEMPLATE_PARAMETER_MISSING";

    /**
     * @msg.bundle   msg="Error in template file: syntax error, line={0} of template file: {1} affecting template: {2}"
     */
    public final static String TEMPLATE_SYNTAX_ERROR = "TEMPLATE_SYNTAX_ERROR";

}
