/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Primary key for inherited/cmp/InheritedCMPSub.
 * @xdoclet-generated at 14-mars-02 20:35:08
 */
public class InheritedCMPSubPK
   extends Object
   implements java.io.Serializable
{
   transient private int _hashCode = 0;
   transient private String value = null;

   public InheritedCMPSubPK()
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
      if( !(obj instanceof InheritedCMPSubPK) )
         return false;

      InheritedCMPSubPK pk = (InheritedCMPSubPK)obj;
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
