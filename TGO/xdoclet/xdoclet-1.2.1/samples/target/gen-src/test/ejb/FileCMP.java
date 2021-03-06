/*
 * Generated by XDoclet - Do not edit!
 */
package test.ejb;

/**
 * CMP layer for File.
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public abstract class FileCMP
   extends test.ejb.FileBean
   implements javax.ejb.EntityBean
{

   public test.interfaces.FileData getData()
   {
      test.interfaces.FileData dataHolder = null;
      try
      {
         dataHolder = new test.interfaces.FileData();

         dataHolder.setId( getId() );
         dataHolder.setContent( getContent() );
         dataHolder.setContentType( getContentType() );
         dataHolder.setCreationDate( getCreationDate() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   /**
    * Generated ejbPostCreate for corresponding ejbCreate method.
    *
    * @see #ejbCreate(java.lang.String id,java.lang.String contentType,byte[] content)
    */
   public void ejbPostCreate(java.lang.String id,java.lang.String contentType,byte[] content)
   {
   }

   public void ejbLoad() 
   {
      super.ejbLoad();
   }

   public void ejbStore() 
   {
         super.ejbStore();
   }

   public void ejbActivate() 
   {
      super.ejbActivate();
   }

   public void ejbPassivate() 
   {
      super.ejbPassivate();

   }

   public void setEntityContext(javax.ejb.EntityContext ctx) 
   {
      super.setEntityContext(ctx);
   }

   public void unsetEntityContext() 
   {
      super.unsetEntityContext();
   }

   public void ejbRemove() throws javax.ejb.RemoveException
   {
      super.ejbRemove();

   }

 /* Value Objects BEGIN */

/* Value Objects END */

   public abstract java.lang.String getId() ;

   public abstract void setId( java.lang.String id ) ;

   public abstract byte[] getContent() ;

   public abstract void setContent( byte[] content ) ;

   public abstract java.lang.String getContentType() ;

   public abstract void setContentType( java.lang.String contentType ) ;

   public abstract java.util.Date getCreationDate() ;

   public abstract void setCreationDate( java.util.Date creationDate ) ;

}
