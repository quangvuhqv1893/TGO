/*
 * Generated by XDoclet - Do not edit!
 */
package test.interfaces;

/**
 * Local home interface for Stateful.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public interface StatefulLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/StatefulLocal";
   public static final String JNDI_NAME="StatefulLocal";

   public test.interfaces.StatefulLocal createWithParam(java.lang.String x)
      throws javax.ejb.CreateException;

   public test.interfaces.StatefulLocal create(java.lang.String x)
      throws javax.ejb.CreateException;

}
