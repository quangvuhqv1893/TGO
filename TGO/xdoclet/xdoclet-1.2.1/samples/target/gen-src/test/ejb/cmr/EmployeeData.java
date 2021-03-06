/*
 * Generated by XDoclet - Do not edit!
 */
package test.ejb.cmr;

/**
 * Data object for Employee.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public class EmployeeData
   extends java.lang.Object
   implements java.io.Serializable
{
   private java.lang.Integer id;
   private java.lang.String name;

  /* begin value object */
   private test.ejb.cmr.EmployeeValue EmployeeValue = null;

   public test.ejb.cmr.EmployeeValue getEmployeeValue()
   {
	  if( EmployeeValue == null )
	  {
          EmployeeValue = new test.ejb.cmr.EmployeeValue();
	  }
      try
         {
            EmployeeValue.setId( getId() );
            EmployeeValue.setName( getName() );
                   }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return EmployeeValue;
   }

   public void setEmployeeValue( test.ejb.cmr.EmployeeValue valueHolder )
   {

	  try
	  {
		 setName( valueHolder.getName() );
	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

  /* end value object */

   public EmployeeData()
   {
   }

   public EmployeeData( java.lang.Integer id,java.lang.String name )
   {
      setId(id);
      setName(name);
   }

   public EmployeeData( EmployeeData otherData )
   {
      setId(otherData.getId());
      setName(otherData.getName());

   }

   public java.lang.Integer getPrimaryKey() {
     return  getId();
   }

   public java.lang.Integer getId()
   {
      return this.id;
   }
   public void setId( java.lang.Integer id )
   {
      this.id = id;
   }

   public java.lang.String getName()
   {
      return this.name;
   }
   public void setName( java.lang.String name )
   {
      this.name = name;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("id=" + getId() + " " + "name=" + getName());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof EmployeeData )
      {
         EmployeeData lTest = (EmployeeData) pOther;
         boolean lEquals = true;

         if( this.id == null )
         {
            lEquals = lEquals && ( lTest.id == null );
         }
         else
         {
            lEquals = lEquals && this.id.equals( lTest.id );
         }
         if( this.name == null )
         {
            lEquals = lEquals && ( lTest.name == null );
         }
         else
         {
            lEquals = lEquals && this.name.equals( lTest.name );
         }

         return lEquals;
      }
      else
      {
         return false;
      }
   }

   public int hashCode()
   {
      int result = 17;

      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      return result;
   }

}
