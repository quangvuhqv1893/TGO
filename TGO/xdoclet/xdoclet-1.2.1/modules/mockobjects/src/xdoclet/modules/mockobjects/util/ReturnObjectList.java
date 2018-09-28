/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.mockobjects.util;

import java.util.Vector;

import com.mockobjects.Verifiable;

import junit.framework.Assert;

/**
 * <p>
 *
 * Holds a list of objects which are used when returning objects from a method. </p> <p>
 *
 * For ever sucessive call to <code>next</code> the next object in the list will returned. When there are no more
 * objects in the list, either
 * <ul>
 *   <li> an assertion error will be thrown - if the last object should not be reused
 *   <li> the last object that was in the list will be returned - if the last object should be reused
 * </ul>
 * </p> <p>
 *
 * If the verify method is called and there are objects still in the list an assertion error will be thrown. </p>
 *
 * @author    Stig J&oslash;rgensen
 * @created   5. februar 2003
 */
public class ReturnObjectList implements Verifiable
{
    private final Vector myObjects = new Vector();
    private final String myName;
    private boolean myReuseLast = false;
    private Object  myLastObject;

    /**
     * Construct a new empty list
     *
     * @param aName  Label used to identify list
     */
    public ReturnObjectList(String aName)
    {
        myName = aName;
    }

    /**
     * Sets whether the last value in the list should be reused if there are no more entries in the list.
     *
     * @param reuseLast  whether the last value in the list should be reused if there are no more entries in the list.
     */
    public void setReuseLast(boolean reuseLast)
    {
        myReuseLast = reuseLast;
    }

    /**
     * Add an object to the end of the list.
     *
     * @param anObjectToReturn  object to be added to the list
     */
    public void add(Object anObjectToReturn)
    {
        myObjects.add(anObjectToReturn);

        if (null == myLastObject) {
            myLastObject = anObjectToReturn;
        }
    }

    /**
     * Add a boolean to the end of the list.
     *
     * @param aBooleanToReturn  boolean to be added to the list
     */
    public void add(boolean aBooleanToReturn)
    {
        myObjects.add(new Boolean(aBooleanToReturn));

        if (null == myLastObject) {
            myLastObject = new Boolean(aBooleanToReturn);
        }
    }

    /**
     * Add an integer to the end of the list.
     *
     * @param anIntegerToReturn  integer to be added to the list
     */
    public void add(int anIntegerToReturn)
    {
        myObjects.add(new Integer(anIntegerToReturn));

        if (null == myLastObject) {
            myLastObject = new Integer(anIntegerToReturn);
        }
    }

    /**
     * Returns whether there are more objects in the list or not. <p>
     *
     * If the user has specified that the last object should be reused, then this method will always return true. </p>
     *
     * @return   whether there are more objects in the list or not.
     */
    public boolean hasNext()
    {
        return (myReuseLast || (myObjects.size() > 0));
    }

    /**
     * Returns the next object from the list. Each object is returned in the order in which they where added. <p>
     *
     * If the user has spesified that the last object should be reused when there are no more entries, it will do this
     * and never fail. </p>
     *
     * @return   the next object from the list.
     */
    public Object next()
    {
        if (myReuseLast && (myObjects.size() == 0)) {
            return myLastObject;
        }
        else {
            Assert.assertTrue(myName + " has run out of objects.",
                myObjects.size() > 0);

            return myObjects.remove(0);
        }
    }

    /**
     * Clears the state.
     */
    public void clear()
    {
        myObjects.clear();
        myLastObject = null;
        myReuseLast = false;
    }

    /**
     * Verify that there are no objects left within the list.
     */
    public void verify()
    {
        Assert.assertEquals(myName + " has un-used objects.", 0,
            myObjects.size());
    }
}
