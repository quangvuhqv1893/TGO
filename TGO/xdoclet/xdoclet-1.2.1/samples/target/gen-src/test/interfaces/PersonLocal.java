/*
 * Generated by XDoclet - Do not edit!
 */
package test.interfaces;

/**
 * Local interface for Person.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public interface PersonLocal
   extends javax.ejb.EJBLocalObject
{

   public void setId( java.lang.String id ) ;

   /**
    * Name of the person.
    */
   public java.lang.String getName(  ) ;

   /**
    * FirstName of the person.
    */
   public java.lang.String getFirstName(  ) ;

   public void setFirstName( java.lang.String firstName ) ;

   /**
    * phone of the person.
    */
   public java.lang.String getPhone(  ) ;

   public void setPhone( java.lang.String phone ) ;

   /**
    * fax of the person.
    */
   public java.lang.String getFax(  ) ;

   public void setFax( java.lang.String fax ) ;

   public void talkTo(  ) ;

   /**
    * The creation-date of the entity. This field is purely to track when this entity was created, and should be set in ejbCreate (<code>setCreationDate(new Date());</code>. It is not included in the value object. <p>We use the qualified name here because XDoclet doesn't copy imports from base classes into the generated interfaces.</p>
    */
   public java.util.Date getCreationDate(  ) ;

   public void setCreationDate( java.util.Date creationDate ) ;

}
