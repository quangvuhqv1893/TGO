/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Home interface for simple/SimpleCMP. Lookup using {1}
 * @xdoclet-generated at 05-mars-02 22:51:01
 */
public interface SimpleCMPHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/simple/SimpleCMP";
   public static final String JNDI_NAME="simple/SimpleCMP";

   public SimpleCMP findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.SimpleCMPPK pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByClassic()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByBoth()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByRemote()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
