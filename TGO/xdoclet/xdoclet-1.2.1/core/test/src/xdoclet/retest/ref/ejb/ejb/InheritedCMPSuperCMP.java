/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.ejb;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * CMP layer for inherited/cmp/InheritedCMPSuper.
 * @xdoclet-generated at 14-mars-02 20:40:53
 */
public abstract class InheritedCMPSuperCMP
   extends xdoclet.retest.bean.ejb.ejb.InheritedCMPSuperBean
   implements EntityBean
{
   private xdoclet.retest.bean.ejb.interfaces.InheritedCMPSuperData dataHolder;

   public xdoclet.retest.bean.ejb.interfaces.InheritedCMPSuperData getData()
   {
      if( dataHolder == null )
      {
         try
         {
            dataHolder = new xdoclet.retest.bean.ejb.interfaces.InheritedCMPSuperData();

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }
      }

      return dataHolder;
   }

   public void ejbLoad()
   {
      dataHolder = null;

   }

   public void ejbStore()
   {
   }

   public void ejbActivate()
   {
   }

   public void ejbPassivate()
   {
      dataHolder = null;

   }

   public void setEntityContext(javax.ejb.EntityContext ctx)
   {
   }

   public void unsetEntityContext()
   {
   }

   public void ejbRemove()
   {
      dataHolder = null;

   }

 /* Value Objects BEGIN */

/* This class has NO primkey-field */

/* Value Objects END */

}
