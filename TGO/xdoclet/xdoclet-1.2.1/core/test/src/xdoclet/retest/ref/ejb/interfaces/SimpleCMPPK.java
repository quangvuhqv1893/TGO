/*
 * Generated file - Do not edit!
 */
package xdoclet.retest.ref.ejb.interfaces;

import java.lang.*;
import javax.ejb.EntityBean;

/**
 * Primary key for simple/SimpleCMP.
 * @xdoclet-generated at 05-mars-02 22:51:01
 */
public class SimpleCMPPK
   extends Object
   implements java.io.Serializable
{
   transient private int _hashCode = 0;
   transient private String value = null;

   public SimpleCMPPK()
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
      if( !(obj instanceof SimpleCMPPK) )
         return false;

      SimpleCMPPK pk = (SimpleCMPPK)obj;
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
