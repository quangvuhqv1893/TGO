package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.EntityBean;

/**
 * @ejb:bean
 *      name="simple/SimpleCMP"
 *      type="CMP"
 *      view-type="both"
 *      cmp-version="2.x"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByClassic()"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByBoth()"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByLocal()"
 *      result-type-mapping="Local"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByRemote()"
 *      result-type-mapping="Remote"
 *
 */
public abstract class SimpleCMPBean
        implements EntityBean
{

    /**
     * Abstract cmp2 field get-set pair for field aBoolean
     * Get the value of aBoolean
     * @return value of aBoolean
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract boolean isABoolean();

    /**
     * Set the value of aBoolean
     * @param aBoolean  Value to assign to aBoolean
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setABoolean(boolean aBoolean);



    /**
     * Abstract cmp2 field get-set pair for field aByte
     * Get the value of aByte
     * @return value of aByte
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract byte getAByte();

    /**
     * Set the value of aByte
     * @param aByte  Value to assign to aByte
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAByte(byte aByte);



    /**
     * Abstract cmp2 field get-set pair for field aShort
     * Get the value of aShort
     * @return value of aShort
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract short getAShort();

    /**
     * Set the value of aShort
     * @param aShort  Value to assign to aShort
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAShort(short aShort);



    /**
     * Abstract cmp2 field get-set pair for field aChar
     * Get the value of aChar
     * @return value of aChar
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract char getAChar();

    /**
     * Set the value of aChar
     * @param aChar  Value to assign to aChar
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAChar(char aChar);



    /**
     * Abstract cmp2 field get-set pair for field id
     * Get the value of id
     * @return value of id
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract Integer getId();

    /**
     * Set the value of id
     * @param id  Value to assign to id
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setId(Integer id);



    /**
     * Abstract cmp2 field get-set pair for field anInt
     * Get the value of anInt
     * @return value of anInt
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract int getAnInt();

    /**
     * Set the value of anInt
     * @param anInt  Value to assign to anInt
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAnInt(int anInt);



    /**
     * Abstract cmp2 field get-set pair for field aLong
     * Get the value of aLong
     * @return value of aLong
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract long getALong();

    /**
     * Set the value of aLong
     * @param aLong  Value to assign to aLong
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setALong(long aLong);



    /**
     * Abstract cmp2 field get-set pair for field aFloat
     * Get the value of aFloat
     * @return value of aFloat
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract float getAFloat();

    /**
     * Set the value of aFloat
     * @param aFloat  Value to assign to aFloat
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAFloat(float aFloat);



    /**
     * Abstract cmp2 field get-set pair for field aDouble
     * Get the value of aDouble
     * @return value of aDouble
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract double getADouble();

    /**
     * Set the value of aDouble
     * @param aDouble  Value to assign to aDouble
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setADouble(double aDouble);



    /**
     * Abstract cmp2 field get-set pair for field anObject
     * Get the value of anObject
     * @return value of anObject
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract Object getAnObject();

    /**
     * Set the value of anObject
     * @param anObject  Value to assign to anObject
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAnObject(Object anObject);



    /**
     * Abstract cmp2 field get-set pair for field anObjectArray
     * Get the value of anObjectArray
     * @return value of anObjectArray
     *
     * @ejb:interface-method
     * @ejb:persistent-field
     */
    public abstract Object[] getAnObjectArray();

    /**
     * Set the value of anObjectArray
     * @param anObjectArray  Value to assign to anObjectArray
     *
     * @ejb:interface-method view-type="remote"
     */
    public abstract void setAnObjectArray(Object[] anObjectArray);

}
