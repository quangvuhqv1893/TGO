/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

/**
 * @author    Aslak Hellesøy
 * @created   17. juli 2002
 * @version   $Revision: 1.4 $
 */
public final class LoaderMessages
{
    /**
     * @msg.bundle   msg="Registering modules..."
     */
    public final static String REGISTERING_MODULES = "REGISTERING_MODULES";

    /**
     * @msg.bundle   msg="There is an entry on XDoclet's classpath that doesn''t exist: {0}. Ignoring it"
     */
    public final static String NONEXISTANT_CLASSPATH_ENTRY = "NONEXISTANT_CLASSPATH_ENTRY";

    /**
     * @msg.bundle   msg="initClasspath must be called first!"
     */
    public final static String INIT_CLASSPATH_NOT_CALLED = "INIT_CLASSPATH_NOT_CALLED";

    /**
     * @msg.bundle   msg="Registered {0} modules."
     */
    public final static String DONE_REGISTERING_MODULES = "DONE_REGISTERING_MODULES";

    /**
     * @msg.bundle   msg="Error during module loading: {0}"
     */
    public final static String LOAD_MODULE_ERROR = "LOAD_MODULE_ERROR";

    /**
     * @msg.bundle   msg="Skipping {0} (no META-INF/xdoclet.xml in here)."
     */
    public final static String SKIP_NO_XDOCLET_XML = "SKIP_NO_XDOCLET_XML";

    /**
     * @msg.bundle   msg="META-INF/xdoclet.xml is bad in {0}."
     */
    public final static String BAD_XDOCLET_XML = "BAD_XDOCLET_XML";

    /**
     * @msg.bundle   msg="META-INF/xdoclet.xml exists in {0}. Parsing it."
     */
    public final static String PARSING_XDOCLET_XML = "PARSING_XDOCLET_XML";

    /**
     * @msg.bundle   msg="Invalid zip/jar file: {0}"
     */
    public final static String INVALID_ZIP_FILE = "INVALID_ZIP_FILE";
}
