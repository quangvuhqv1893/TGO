/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Local home interface for simple/SimpleCMP. Lookup using {1}
 * @xdoclet-generated at 05-mars-02 22:51:01
 */
public interface SimpleCMPLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/simple/SimpleCMPLocal";
   public static final String JNDI_NAME="simple/SimpleCMPLocal";

   public SimpleCMPLocal findByPrimaryKey(xdoclet.retest.ref.ejb.interfaces.SimpleCMPPK pk) throws javax.ejb.FinderException;

   public java.util.Collection findByClassic()
      throws javax.ejb.FinderException;

   public java.util.Collection findByBoth()
      throws javax.ejb.FinderException;

   public java.util.Collection findByLocal()
       throws javax.ejb.FinderException;

}
