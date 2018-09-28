/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Primary key for inherited/cmp/InheritedCMPSuper.
 * @xdoclet-generated at 14-mars-02 20:35:08
 */
public class InheritedCMPSuperPK
   extends Object
   implements java.io.Serializable
{
   transient private int _hashCode = 0;
   transient private String value = null;

   public InheritedCMPSuperPK()
   {
   }

   public int hashCode()
   {
      if( _hashCode == 0 )
      {
      }

      return _hashCode;
   }

   public boolean equals(Object obj)
   {
      if( !(obj instanceof InheritedCMPSuperPK) )
         return false;

      InheritedCMPSuperPK pk = (InheritedCMPSuperPK)obj;
      boolean eq = true;

      if( obj == null )
      {
         eq = false;
      }
      else
      {
      }

      return eq;
   }

   public String toString()
   {
      if( value == null )
      {
         value = "[.";
         value += "]";
      }

      return value;
   }
}
