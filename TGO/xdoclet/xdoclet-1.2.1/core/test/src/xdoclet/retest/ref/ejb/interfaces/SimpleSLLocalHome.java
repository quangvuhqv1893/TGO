/*
 * $Id: SimpleSLLocalHome.java,v 1.1 2002/02/25 17:41:17 vharcq Exp $
 */
package xdoclet.retest.ref.ejb.interfaces;

public interface SimpleSLLocalHome
        extends javax.ejb.EJBLocalHome
{
    public static final String COMP_NAME="java:comp/env/ejb/simple/SimpleSLLocal";
    public static final String JNDI_NAME="simple/SimpleSLLocal";

    public xdoclet.retest.ref.ejb.interfaces.SimpleSLLocal create() throws javax.ejb.CreateException;

}
