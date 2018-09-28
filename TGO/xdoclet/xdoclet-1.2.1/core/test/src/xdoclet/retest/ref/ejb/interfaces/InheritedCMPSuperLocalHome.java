/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Local home interface for inherited/cmp/InheritedCMPSuper. Lookup using {1}
 * @xdoclet-generated at 14-mars-02 20:35:07
 */
public interface InheritedCMPSuperLocalHome
   extends xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/inherited/cmp/InheritedCMPSuperLocal";
   public static final String JNDI_NAME="inherited/cmp/InheritedCMPSuperLocal";

   public InheritedCMPSuperLocal findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.InheritedCMPSuperPK pk) throws javax.ejb.FinderException;

}
