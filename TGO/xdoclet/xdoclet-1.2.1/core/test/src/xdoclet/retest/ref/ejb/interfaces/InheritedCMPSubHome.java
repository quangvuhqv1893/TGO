/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Home interface for inherited/cmp/InheritedCMPSub. Lookup using {1}
 * @xdoclet-generated at 14-mars-02 20:35:07
 */
public interface InheritedCMPSubHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/inherited/cmp/InheritedCMPSub";
   public static final String JNDI_NAME="inherited/cmp/InheritedCMPSub";

   public InheritedCMPSub findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.InheritedCMPSubPK pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;
   public java.util.Collection findByClassic()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
