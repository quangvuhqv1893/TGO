/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.ejb;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * CMP layer for inherited/cmp/InheritedCMPSub.
 * @xdoclet-generated at 14-mars-02 20:35:08
 */
public abstract class InheritedCMPSubCMP
   extends xdoclet.retest.bean.ejb.ejb.InheritedCMPSubBean
   implements EntityBean
{
   private xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubData dataHolder;

   public xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubData getData()
   {
      if( dataHolder == null )
      {
         try
         {
            dataHolder = new xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubData();

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
