/*
 * Generated by XDoclet - Do not edit!
 */
package test.interfaces;

/**
 * Value object for Customer.
 *
 * @xdoclet-generated at 28-04-04
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version 1.2.1
 */
public class CustomerNormalValue
   extends java.lang.Object
   implements java.io.Serializable 
{
   private float credit;
   private boolean creditHasBeenSet = false;
   private java.lang.String[][] array;
   private boolean arrayHasBeenSet = false;
   private byte[] image;
   private boolean imageHasBeenSet = false;
   private float tax;
   private boolean taxHasBeenSet = false;
   private java.lang.String id;
   private boolean idHasBeenSet = false;
   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private java.lang.String firstName;
   private boolean firstNameHasBeenSet = false;
   private java.util.Collection AccountViews = new java.util.ArrayList();
   private java.util.Collection ShippingAddressValues = new java.util.ArrayList();

   private test.interfaces.CustomerPK pk;

   private int _version = 0;

   public CustomerNormalValue()
   {
	  pk = new test.interfaces.CustomerPK();
   }

   public CustomerNormalValue( float credit,java.lang.String[][] array,byte[] image,float tax,java.lang.String id,java.lang.String name,java.lang.String firstName )
   {
	  this.credit = credit;
	  creditHasBeenSet = true;
	  this.array = array;
	  arrayHasBeenSet = true;
	  this.image = image;
	  imageHasBeenSet = true;
	  this.tax = tax;
	  taxHasBeenSet = true;
	  this.id = id;
	  idHasBeenSet = true;
	  this.name = name;
	  nameHasBeenSet = true;
	  this.firstName = firstName;
	  firstNameHasBeenSet = true;
	  pk = new test.interfaces.CustomerPK(this.getId());
   }

   //TODO Cloneable is better than this !
   public CustomerNormalValue( CustomerNormalValue otherValue )
   {
	  this.credit = otherValue.credit;
	  creditHasBeenSet = true;
	  this.array = otherValue.array;
	  arrayHasBeenSet = true;
	  this.image = otherValue.image;
	  imageHasBeenSet = true;
	  this.tax = otherValue.tax;
	  taxHasBeenSet = true;
	  this.id = otherValue.id;
	  idHasBeenSet = true;
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.firstName = otherValue.firstName;
	  firstNameHasBeenSet = true;
	// TODO Clone is better no ?
	  this.AccountViews = otherValue.AccountViews;
	// TODO Clone is better no ?
	  this.ShippingAddressValues = otherValue.ShippingAddressValues;

	  pk = new test.interfaces.CustomerPK(this.getId());
   }

   public test.interfaces.CustomerPK getPrimaryKey()
   {
	  return pk;
   }

   public void setPrimaryKey( test.interfaces.CustomerPK pk )
   {
      // it's also nice to update PK object - just in case
      // somebody would ask for it later...
      this.pk = pk;
	  setId( pk.id );
   }

   public float getCredit()
   {
	  return this.credit;
   }

   public void setCredit( float credit )
   {
	  this.credit = credit;
	  creditHasBeenSet = true;

   }

   public boolean creditHasBeenSet(){
	  return creditHasBeenSet;
   }
   public java.lang.String[][] getArray()
   {
	  return this.array;
   }

   public byte[] getImage()
   {
	  return this.image;
   }

   public float getTax()
   {
	  return this.tax;
   }

   public java.lang.String getId()
   {
	  return this.id;
   }

   public void setId( java.lang.String id )
   {
	  this.id = id;
	  idHasBeenSet = true;

		 pk.setId(id);
   }

   public boolean idHasBeenSet(){
	  return idHasBeenSet;
   }
   public java.lang.String getName()
   {
	  return this.name;
   }

   public void setName( java.lang.String name )
   {
	  this.name = name;
	  nameHasBeenSet = true;

   }

   public boolean nameHasBeenSet(){
	  return nameHasBeenSet;
   }
   public java.lang.String getFirstName()
   {
	  return this.firstName;
   }

   public void setFirstName( java.lang.String firstName )
   {
	  this.firstName = firstName;
	  firstNameHasBeenSet = true;

   }

   public boolean firstNameHasBeenSet(){
	  return firstNameHasBeenSet;
   }

   protected java.util.Collection addedAccountViews = new java.util.ArrayList();
   protected java.util.Collection onceAddedAccountViews = new java.util.ArrayList();
   protected java.util.Collection removedAccountViews = new java.util.ArrayList();
   protected java.util.Collection updatedAccountViews = new java.util.ArrayList();

   public java.util.Collection getAddedAccountViews() { return addedAccountViews; }
   public java.util.Collection getOnceAddedAccountViews() { return onceAddedAccountViews; }
   public java.util.Collection getRemovedAccountViews() { return removedAccountViews; }
   public java.util.Collection getUpdatedAccountViews() { return updatedAccountViews; }

   public void setAddedAccountViews(java.util.Collection addedAccountViews)
   {
      this.addedAccountViews.clear();
      this.addedAccountViews.addAll(addedAccountViews);
   }

   public void setOnceAddedAccountViews(java.util.Collection onceAddedAccountViews)
   {
      this.onceAddedAccountViews.clear();
      this.onceAddedAccountViews.addAll(onceAddedAccountViews);
   }

   public void setRemovedAccountViews(java.util.Collection removedAccountViews)
   {
      this.removedAccountViews.clear();
      this.removedAccountViews.addAll(removedAccountViews);
   }

   public void setUpdatedAccountViews(java.util.Collection updatedAccountViews)
   {
      this.updatedAccountViews.clear();
      this.updatedAccountViews.addAll(updatedAccountViews);
   }

   public test.interfaces.AccountValue[] getAccountViews()
   {
	  return (test.interfaces.AccountValue[])this.AccountViews.toArray(new test.interfaces.AccountValue[AccountViews.size()]);
   }

   public void setAccountViews(test.interfaces.AccountValue[] AccountViews)
   {
      this.AccountViews.clear();
      for (int i=0; i < AccountViews.length; i++)
      	this.AccountViews.add(AccountViews[i]);
   }

   public void clearAccountViews()
   {
	  this.AccountViews.clear();
   }

   public void addAccountView(test.interfaces.AccountValue added)
   {
	  this.AccountViews.add(added);

      if (this.removedAccountViews.contains(added))
      {
        this.removedAccountViews.remove(added);
        if (this.onceAddedAccountViews.contains(added))
        {
          if (! this.addedAccountViews.contains(added))
            this.addedAccountViews.add(added);
        }
        else if (! this.updatedAccountViews.contains(added))
        {
            this.updatedAccountViews.add(added);
        }
      }
      else
      {
        if (! this.onceAddedAccountViews.contains(added))
          this.onceAddedAccountViews.add(added);
        if (! this.addedAccountViews.contains(added))
          this.addedAccountViews.add(added);
      }
   }

   public void removeAccountView(test.interfaces.AccountValue removed)
   {
	  this.AccountViews.remove(removed);

      if (this.addedAccountViews.contains(removed))
        this.addedAccountViews.remove(removed);
      else if (! this.removedAccountViews.contains(removed))
        this.removedAccountViews.add(removed);

	  if (this.updatedAccountViews.contains(removed))
		 this.updatedAccountViews.remove(removed);
   }

   public void updateAccountView(test.interfaces.AccountValue updated)
   {
	  if ( !this.updatedAccountViews.contains(updated) && !this.addedAccountViews.contains(updated))
		 this.updatedAccountViews.add(updated);
      if (this.removedAccountViews.contains(updated))
         this.removedAccountViews.remove(updated);
   }

   public void cleanAccountView(){
	  this.addedAccountViews = new java.util.ArrayList();
      this.onceAddedAccountViews = new java.util.ArrayList();
	  this.removedAccountViews = new java.util.ArrayList();
	  this.updatedAccountViews = new java.util.ArrayList();
   }

   public void copyAccountViewsFrom(test.interfaces.CustomerNormalValue from)
   {
	  // TODO Clone the List ????
	  this.AccountViews = from.AccountViews;
   }
   protected java.util.Collection addedShippingAddressValues = new java.util.ArrayList();
   protected java.util.Collection onceAddedShippingAddressValues = new java.util.ArrayList();
   protected java.util.Collection removedShippingAddressValues = new java.util.ArrayList();
   protected java.util.Collection updatedShippingAddressValues = new java.util.ArrayList();

   public java.util.Collection getAddedShippingAddressValues() { return addedShippingAddressValues; }
   public java.util.Collection getOnceAddedShippingAddressValues() { return onceAddedShippingAddressValues; }
   public java.util.Collection getRemovedShippingAddressValues() { return removedShippingAddressValues; }
   public java.util.Collection getUpdatedShippingAddressValues() { return updatedShippingAddressValues; }

   public void setAddedShippingAddressValues(java.util.Collection addedShippingAddressValues)
   {
      this.addedShippingAddressValues.clear();
      this.addedShippingAddressValues.addAll(addedShippingAddressValues);
   }

   public void setOnceAddedShippingAddressValues(java.util.Collection onceAddedShippingAddressValues)
   {
      this.onceAddedShippingAddressValues.clear();
      this.onceAddedShippingAddressValues.addAll(onceAddedShippingAddressValues);
   }

   public void setRemovedShippingAddressValues(java.util.Collection removedShippingAddressValues)
   {
      this.removedShippingAddressValues.clear();
      this.removedShippingAddressValues.addAll(removedShippingAddressValues);
   }

   public void setUpdatedShippingAddressValues(java.util.Collection updatedShippingAddressValues)
   {
      this.updatedShippingAddressValues.clear();
      this.updatedShippingAddressValues.addAll(updatedShippingAddressValues);
   }

   public test.interfaces.AddressValue[] getShippingAddressValues()
   {
	  return (test.interfaces.AddressValue[])this.ShippingAddressValues.toArray(new test.interfaces.AddressValue[ShippingAddressValues.size()]);
   }

   public void setShippingAddressValues(test.interfaces.AddressValue[] ShippingAddressValues)
   {
      this.ShippingAddressValues.clear();
      for (int i=0; i < ShippingAddressValues.length; i++)
      	this.ShippingAddressValues.add(ShippingAddressValues[i]);
   }

   public void clearShippingAddressValues()
   {
	  this.ShippingAddressValues.clear();
   }

   public void addShippingAddressValue(test.interfaces.AddressValue added)
   {
	  this.ShippingAddressValues.add(added);

      if (this.removedShippingAddressValues.contains(added))
      {
        this.removedShippingAddressValues.remove(added);
        if (this.onceAddedShippingAddressValues.contains(added))
        {
          if (! this.addedShippingAddressValues.contains(added))
            this.addedShippingAddressValues.add(added);
        }
        else if (! this.updatedShippingAddressValues.contains(added))
        {
            this.updatedShippingAddressValues.add(added);
        }
      }
      else
      {
        if (! this.onceAddedShippingAddressValues.contains(added))
          this.onceAddedShippingAddressValues.add(added);
        if (! this.addedShippingAddressValues.contains(added))
          this.addedShippingAddressValues.add(added);
      }
   }

   public void removeShippingAddressValue(test.interfaces.AddressValue removed)
   {
	  this.ShippingAddressValues.remove(removed);

      if (this.addedShippingAddressValues.contains(removed))
        this.addedShippingAddressValues.remove(removed);
      else if (! this.removedShippingAddressValues.contains(removed))
        this.removedShippingAddressValues.add(removed);

	  if (this.updatedShippingAddressValues.contains(removed))
		 this.updatedShippingAddressValues.remove(removed);
   }

   public void updateShippingAddressValue(test.interfaces.AddressValue updated)
   {
	  if ( !this.updatedShippingAddressValues.contains(updated) && !this.addedShippingAddressValues.contains(updated))
		 this.updatedShippingAddressValues.add(updated);
      if (this.removedShippingAddressValues.contains(updated))
         this.removedShippingAddressValues.remove(updated);
   }

   public void cleanShippingAddressValue(){
	  this.addedShippingAddressValues = new java.util.ArrayList();
      this.onceAddedShippingAddressValues = new java.util.ArrayList();
	  this.removedShippingAddressValues = new java.util.ArrayList();
	  this.updatedShippingAddressValues = new java.util.ArrayList();
   }

   public void copyShippingAddressValuesFrom(test.interfaces.CustomerNormalValue from)
   {
	  // TODO Clone the List ????
	  this.ShippingAddressValues = from.ShippingAddressValues;
   }

   public int getVersion()
   {
	  return _version;
   }
   public void setVersion(int version)
   {
	  this._version = version;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("credit=" + getCredit() + " " + "array=" + getArray() + " " + "image=" + getImage() + " " + "tax=" + getTax() + " " + "id=" + getId() + " " + "name=" + getName() + " " + "firstName=" + getFirstName());
	  str.append(",version=");
	  str.append(_version);
	  str.append('}');

	  return(str.toString());
   }

   /**
    * A Value Object has an identity if the attributes making its Primary Key have all been set. An object without identity is never equal to any other object.
    *
    * @return true if this instance has an identity.
    */
   protected boolean hasIdentity()
   {
	  boolean ret = true;
	  ret = ret && idHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof CustomerNormalValue)
	  {
		 CustomerNormalValue that = (CustomerNormalValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 if( this.id == null )
		 {
			lEquals = lEquals && ( that.id == null );
		 }
		 else
		 {
			lEquals = lEquals && this.id.equals( that.id );
		 }

		 lEquals = lEquals && isIdentical(that);

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public boolean isIdentical(Object other)
   {
	  if (other instanceof CustomerNormalValue)
	  {
		 CustomerNormalValue that = (CustomerNormalValue) other;
		 boolean lEquals = true;
		 lEquals = lEquals && this.credit == that.credit;
		 if( this.array == null )
		 {
			lEquals = lEquals && ( that.array == null );
		 }
		 else
		 {
			lEquals = lEquals && this.array.equals( that.array );
		 }
		 lEquals = lEquals && this.image == that.image;
		 lEquals = lEquals && this.tax == that.tax;
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
		 }
		 if( this.firstName == null )
		 {
			lEquals = lEquals && ( that.firstName == null );
		 }
		 else
		 {
			lEquals = lEquals && this.firstName.equals( that.firstName );
		 }
		 if( this.getAccountViews() == null )
		 {
			lEquals = lEquals && ( that.getAccountViews() == null );
		 }
		 else
		 {
			lEquals = lEquals && java.util.Arrays.equals(this.getAccountViews() , that.getAccountViews()) ;
		 }
		 if( this.getShippingAddressValues() == null )
		 {
			lEquals = lEquals && ( that.getShippingAddressValues() == null );
		 }
		 else
		 {
			lEquals = lEquals && java.util.Arrays.equals(this.getShippingAddressValues() , that.getShippingAddressValues()) ;
		 }

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + Float.floatToIntBits(credit);

      result = 37*result + ((this.array != null) ? this.array.hashCode() : 0);

      if (image != null) {
        for (int i=0; i<image.length; i++)
        {
          long l = image[i];
          result = 37*result + (int)(l^(l>>>32));
        }
      }

      result = 37*result + Float.floatToIntBits(tax);

      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      result = 37*result + ((this.firstName != null) ? this.firstName.hashCode() : 0);

	  result = 37*result + ((this.getAccountViews() != null) ? this.getAccountViews().hashCode() : 0);
	  result = 37*result + ((this.getShippingAddressValues() != null) ? this.getShippingAddressValues().hashCode() : 0);
	  return result;
   }

}