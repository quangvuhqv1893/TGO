/*
 * Generated by XDoclet - Do not edit!
 */
package test.ejb.cmr;

/**
 * Data object for LanguageCode.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public class LanguageCodeData
   extends java.lang.Object
   implements java.io.Serializable
{
   private java.lang.Integer languageCodeId;
   private java.lang.String name;

  /* begin value object */

  /* end value object */

   public LanguageCodeData()
   {
   }

   public LanguageCodeData( java.lang.Integer languageCodeId,java.lang.String name )
   {
      setLanguageCodeId(languageCodeId);
      setName(name);
   }

   public LanguageCodeData( LanguageCodeData otherData )
   {
      setLanguageCodeId(otherData.getLanguageCodeId());
      setName(otherData.getName());

   }

   public java.lang.Integer getPrimaryKey() {
     return  getLanguageCodeId();
   }

   public java.lang.Integer getLanguageCodeId()
   {
      return this.languageCodeId;
   }
   public void setLanguageCodeId( java.lang.Integer languageCodeId )
   {
      this.languageCodeId = languageCodeId;
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

      str.append("languageCodeId=" + getLanguageCodeId() + " " + "name=" + getName());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof LanguageCodeData )
      {
         LanguageCodeData lTest = (LanguageCodeData) pOther;
         boolean lEquals = true;

         if( this.languageCodeId == null )
         {
            lEquals = lEquals && ( lTest.languageCodeId == null );
         }
         else
         {
            lEquals = lEquals && this.languageCodeId.equals( lTest.languageCodeId );
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

      result = 37*result + ((this.languageCodeId != null) ? this.languageCodeId.hashCode() : 0);

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      return result;
   }

}
