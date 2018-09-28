/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.ejb;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * CMP layer for simple/SimpleCMP.
 * @xdoclet-generated at 05-mars-02 22:51:01
 */
public abstract class SimpleCMPCMP
   extends xdoclet.retest.bean.ejb.ejb.SimpleCMPBean
   implements EntityBean
{
   private xdoclet.retest.bean.ejb.interfaces.SimpleCMPData dataHolder;

   public xdoclet.retest.bean.ejb.interfaces.SimpleCMPData getData()
   {
      if( dataHolder == null )
      {
         try
         {
            dataHolder = new xdoclet.retest.bean.ejb.interfaces.SimpleCMPData();

            dataHolder.setABoolean( isABoolean() );
            dataHolder.setAByte( getAByte() );
            dataHolder.setAShort( getAShort() );
            dataHolder.setAChar( getAChar() );
            dataHolder.setId( getId() );
            dataHolder.setAnInt( getAnInt() );
            dataHolder.setALong( getALong() );
            dataHolder.setAFloat( getAFloat() );
            dataHolder.setADouble( getADouble() );
            dataHolder.setAnObject( getAnObject() );
            dataHolder.setAnObjectArray( getAnObjectArray() );

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

/* Value Objects END */

   public abstract boolean isABoolean() ;

   public abstract void setABoolean( boolean aBoolean ) ;

   public abstract byte getAByte() ;

   public abstract void setAByte( byte aByte ) ;

   public abstract short getAShort() ;

   public abstract void setAShort( short aShort ) ;

   public abstract char getAChar() ;

   public abstract void setAChar( char aChar ) ;

   public abstract Integer getId() ;

   public abstract void setId( Integer id ) ;

   public abstract int getAnInt() ;

   public abstract void setAnInt( int anInt ) ;

   public abstract long getALong() ;

   public abstract void setALong( long aLong ) ;

   public abstract float getAFloat() ;

   public abstract void setAFloat( float aFloat ) ;

   public abstract double getADouble() ;

   public abstract void setADouble( double aDouble ) ;

   public abstract Object getAnObject() ;

   public abstract void setAnObject( Object anObject ) ;

   public abstract Object[] getAnObjectArray() ;

   public abstract void setAnObjectArray( Object[] anObjectArray ) ;

}
