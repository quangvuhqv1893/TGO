/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Home interface for inherited/cmp/InheritedCMPSuper. Lookup using {1}
 * @xdoclet-generated at 14-mars-02 20:35:07
 */
public interface InheritedCMPSuperHome
   extends xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubHome
{
   public static final String COMP_NAME="java:comp/env/ejb/inherited/cmp/InheritedCMPSuper";
   public static final String JNDI_NAME="inherited/cmp/InheritedCMPSuper";

   public InheritedCMPSuper findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.InheritedCMPSuperPK pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
