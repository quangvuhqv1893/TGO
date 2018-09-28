package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;

/**
 * Simple Stateless Session Bean
 *
 * @ejb:bean type="Stateless" name="simple/SimpleSL" view-type="both"
 */
public abstract class SimpleSLBean
implements SessionBean
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

}
