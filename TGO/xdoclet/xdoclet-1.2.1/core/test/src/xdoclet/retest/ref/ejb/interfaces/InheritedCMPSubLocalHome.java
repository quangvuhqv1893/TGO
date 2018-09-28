/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Local home interface for inherited/cmp/InheritedCMPSub. Lookup using {1}
 * @xdoclet-generated at 14-mars-02 20:35:07
 */
public interface InheritedCMPSubLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/inherited/cmp/InheritedCMPSubLocal";
   public static final String JNDI_NAME="inherited/cmp/InheritedCMPSubLocal";

   public InheritedCMPSubLocal findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.InheritedCMPSubPK pk) throws javax.ejb.FinderException;
   public java.util.Collection findByClassic() throws javax.ejb.FinderException;

}
