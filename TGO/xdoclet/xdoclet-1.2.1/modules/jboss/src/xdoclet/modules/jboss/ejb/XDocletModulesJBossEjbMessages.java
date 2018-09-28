/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.ejb;

/**
 * @author    <a href="mailto:mnewcomb@tacintel.com">Michael Newcomb</a>
 * @created   July 12, 2002
 * @version   $Revision: 1.3 $
 */
public final class XDocletModulesJBossEjbMessages
{
    /**
     * @msg.bundle   msg="JBoss {0} is not supported."
     */
    public final static String UNSUPPORTED_JBOSS_VERSION = "UNSUPPORTED_JBOSS_VERSION";

    /**
     * @msg.bundle   msg="JBoss {0} does not support EJB Spec. {1}."
     */
    public final static String UNSUPPORTED_EJB_VERSION_FOR_JBOSS_VERSION = "UNSUPPORTED_EJB_VERSION_FOR_JBOSS_VERSION";

    /**
     * @msg.bundle   msg="Datasource and datasourcemapping (aka. typemapping) must be specified together, you have only
     *      specified {0}."
     */
    public final static String DATASOURCE_DATASOURCEMAPPING_PARAMETER_MISSING = "DATASOURCE_DATASOURCEMAPPING_PARAMETER_MISSING";
}
