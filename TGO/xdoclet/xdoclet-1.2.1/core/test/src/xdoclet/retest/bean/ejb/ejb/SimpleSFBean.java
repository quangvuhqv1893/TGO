/*
* $Id: SimpleSFBean.java,v 1.4 2002/04/12 22:17:54 vharcq Exp $
*/
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;

/**
 * @ejb:bean type="Stateful" name="simple/SimpleSF" view-type="both"
 */
public abstract class SimpleSFBean implements SessionBean
{
    /**
     * @ejb:interface-method
     */
    public void doSomething(){}

    /**
     * @ejb:interface-method
     */
    public int idoSomething(){
        return 9;
    }

    /**
     * @ejb:interface-method
     */
    public int[] iadoSomething(){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String sdoSomething(){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String[] sadoSomething(){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public void doSomething(int a){}

    /**
     * @ejb:interface-method
     */
    public int idoSomething(int a){
        return a;
    }

    /**
     * @ejb:interface-method
     */
    public int[] iadoSomething(int a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String sdoSomething(int a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String[] siadoSomething(int a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public void doSomething(int[] a){}

    /**
     * @ejb:interface-method
     */
    public int idoSomething(int[] a){
        return 9;
    }

    /**
     * @ejb:interface-method
     */
    public int[] iadoSomething(int[] a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String sdoSomething(int[] a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String[] sadoSomething(int[] a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public void doSomething(String a){}

    /**
     * @ejb:interface-method
     */
    public int idoSomething(String a){
        return 9;
    }

    /**
     * @ejb:interface-method
     */
    public int[] iadoSomething(String a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String sdoSomething(String a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String[] sadoSomething(String a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public void doSomething(String[] a){}

    /**
     * @ejb:interface-method
     */
    public int idoSomething(String[] a){
        return 9;
    }

    /**
     * @ejb:interface-method
     */
    public int[] iadoSomething(String[] a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String sdoSomething(String[] a){
        return null;
    }

    /**
     * @ejb:interface-method
     */
    public String[] sadoSomething(String[] a){
        return null;
    }

    /* --------------------------------------------------------------------- */
    /* HOME METHODS                                                          */
    /* --------------------------------------------------------------------- */

    /**
     * @ejb:create-method
     */
    public void ejbCreate() throws CreateException
    {
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreate(int a) throws CreateException
    {
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreate(int[] a) throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreate(String a) throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreate(String[] a) throws CreateException,IllegalAccessException,IllegalArgumentException{
    }


    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla() throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla(int a) throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla(int[] a) throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla(String a) throws CreateException{
    }

    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla(String[] a) throws CreateException{
    }

    /**
     * @ejb:create-method view-type="local"
     */
    public void ejbCreateLocal(String[] a) throws CreateException{
    }

}
