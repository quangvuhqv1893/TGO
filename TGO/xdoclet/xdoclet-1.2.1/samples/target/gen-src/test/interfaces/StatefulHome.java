/*
 * Generated by XDoclet - Do not edit!
 */
package test.interfaces;

/**
 * Home interface for Stateful.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public interface StatefulHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Stateful";
   public static final String JNDI_NAME="Stateful";

   public test.interfaces.Stateful createWithParam(java.lang.String x)
      throws javax.ejb.CreateException,java.rmi.RemoteException;

   public test.interfaces.Stateful create(java.lang.String x)
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
