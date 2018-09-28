/*
 * $Id: SimpleSLLocal.java,v 1.1 2002/02/25 17:41:17 vharcq Exp $
 */
package xdoclet.retest.ref.ejb.interfaces;

public interface SimpleSLLocal
        extends javax.ejb.EJBLocalObject
{
    public void doSomething(  ) ;

    public void doSomething( int a ) ;

    public void doSomething( int[] a ) ;

    public void doSomething( java.lang.String a ) ;

    public void doSomething( java.lang.String[] a ) ;

    public int[] iadoSomething(  ) ;

    public int[] iadoSomething( int a ) ;

    public int[] iadoSomething( int[] a ) ;

    public int[] iadoSomething( java.lang.String a ) ;

    public int[] iadoSomething( java.lang.String[] a ) ;

    public int idoSomething(  ) ;

    public int idoSomething( int a ) ;

    public int idoSomething( int[] a ) ;

    public int idoSomething( java.lang.String a ) ;

    public int idoSomething( java.lang.String[] a ) ;

    public java.lang.String[] sadoSomething(  ) ;

    public java.lang.String[] sadoSomething( int[] a ) ;

    public java.lang.String[] sadoSomething( java.lang.String a ) ;

    public java.lang.String[] sadoSomething( java.lang.String[] a ) ;

    public java.lang.String sdoSomething(  ) ;

    public java.lang.String sdoSomething( int a ) ;

    public java.lang.String sdoSomething( int[] a ) ;

    public java.lang.String sdoSomething( java.lang.String a ) ;

    public java.lang.String sdoSomething( java.lang.String[] a ) ;

    public java.lang.String[] siadoSomething( int a ) ;

}
