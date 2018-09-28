package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.EntityBean;

/**
 * @ejb:bean
 *      name="ejbtrans/Transaction"
 *      type="CMP"
 *      view-type="both"
 *      cmp-version="2.x"
 *
 * @ejb:transaction type="NotSupported"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByNotSpecified()"
 * @ejb:finder
 *      signature="java.util.Collection findByNotSupported()"
 *      transaction-type="NotSupported"
 * @ejb:finder
 *      signature="java.util.Collection findBySupports()"
 *      transaction-type="Supports"
 * @ejb:finder
 *      signature="java.util.Collection findByRequired()"
 *      transaction-type="Required"
 * @ejb:finder
 *      signature="java.util.Collection findByRequiresNew()"
 *      transaction-type="RequiresNew"
 * @ejb:finder
 *      signature="java.util.Collection findByMandatory()"
 *      transaction-type="Mandatory"
 * @ejb:finder
 *      signature="java.util.Collection findByNever()"
 *      transaction-type="Never"
 * @ejb:finder
 *      signature="java.util.Collection findByLocalNotSupported()"
 *      transaction-type="NotSupported"
 *      method-intf="LocalHome"
 * @ejb:finder
 *      signature="java.util.Collection findByHomeSupports()"
 *      transaction-type="Supports"
 *      method-intf="Home"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByLocalNotSpecified()"
 *      view-type="local"
 * @ejb:finder
 *      signature="java.util.Collection findByLocalNotSupported()"
 *      transaction-type="NotSupported"
 *      view-type="local"
 * @ejb:finder
 *      signature="java.util.Collection findByLocalLocalNotSupported()"
 *      transaction-type="NotSupported"
 *      method-intf="LocalHome"
 *      view-type="local"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByRemoteNotSpecified()"
 *      view-type="remote"
 * @ejb:finder
 *      signature="java.util.Collection findByRemoteNotSupported()"
 *      transaction-type="NotSupported"
 *      view-type="remote"
 * @ejb:finder
 *      signature="java.util.Collection findByHomeRemoteBySupports()"
 *      transaction-type="Supports"
 *      method-intf="Home"
 *      view-type="remote"
 *
 *
 * @ejb:finder
 *      signature="java.util.Collection findByBothNotSpecified()"
 *      view-type="both"
 * @ejb:finder
 *      signature="java.util.Collection findByBothNotSupported()"
 *      transaction-type="NotSupported"
 *      view-type="both"
 * @ejb:finder
 *      signature="java.util.Collection findByBothSupports()"
 *      transaction-type="Supports"
 *      view-type="both"
 * @ejb:finder
 *      signature="java.util.Collection findByBoth()"
 *      transaction-type="NotSupported"
 *      method-intf="LocalHome"
 *      view-type="both"
 * @ejb:finder
 *      signature="java.util.Collection findByBoth()"
 *      transaction-type="Supports"
 *      method-intf="Home"
 *      view-type="both"
 *
 */
public abstract class TransactionBean
        implements EntityBean
{

    /**
     * @ejb:persistent-field
     * @ejb:pk-field
     */
    public abstract String getId();

    /**
     * @ejb:persistent-field
     */
    public abstract String getName();

    /**
     * @ejb:interface-method
     * @ejb:transaction type="NotSupported" description="BlaBla"
     */
    public void createNotSupported(String p1, int p2,byte[] p3){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="NotSupported"
     */
    public void methodNotSupported(String p1, int p2,byte[] p3){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="Supports"
     */
    public void methodSupports(){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="Required"
     */
    public void methodRequired(String p1){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="RequiresNew"
     */
    public void methodRequiresNew(){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="Mandatory"
     */
    public void methodMandatory(){}

    /**
     * @ejb:interface-method
     * @ejb:transaction type="Never"
     */
    public void methodNever(){}

    /**
     * @ejb:interface-method
     */
    public void methodNotSpecified(){}

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb:transaction type="NotSupported"
     */
    public void methodNotSupported(String p1, int p2){}

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public void methodLocalNotSupported(String p1, int p2){}

    /**
     * @ejb:interface-method view-type="both"
     * @ejb:transaction type="NotSupported"
     */
    public void methodBothNotSupported(String p1, int p2){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="NotSupported"
     */
    public void ejbHomeMethodNotSupported(String p1, int p2,byte[] p3){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="Supports"
     */
    public void ejbHomeMethodSupports(){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="Required"
     */
    public void ejbHomeMethodRequired(String p1){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="RequiresNew"
     */
    public void ejbHomeMethodRequiresNew(){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="Mandatory"
     */
    public void ejbHomeMethodMandatory(){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="Never"
     */
    public void ejbHomeMethodNever(){}

    /**
     * @ejb:home-method
     */
    public void ejbHomeMethodNotSpecified(){}

    /**
     * @ejb:home-method view-type="remote"
     * @ejb:transaction type="NotSupported"
     */
    public void ejbHomeMethodNotSupported(String p1, int p2){}

    /**
     * @ejb:home-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public void ejbHomeLocalMethodNotSupported(String p1, int p2){}

    /**
     * @ejb:home-method view-type="both"
     * @ejb:transaction type="NotSupported"
     */
    public void ejbHomeBothMethodNotSupported(String p1, int p2){}

    /**
     * @ejb:create-method
     * @ejb:transaction type="NotSupported"
     */
    public void ejbCreateMethodNotSupported(String p1, int p2,byte[] p3){}

    /**
     * @ejb:create-method
     * @ejb:transaction type="Supports"
     */
    public void ejbCreateMethodSupports(){}

    /**
     * @ejb:create-method
     * @ejb:transaction type="Required"
     */
    public void ejbCreateMethodRequired(String p1){}

    /**
     * @ejb:home-method
     * @ejb:transaction type="RequiresNew"
     */
    public void ejbCreateMethodRequiresNew(){}

    /**
     * @ejb:create-method
     * @ejb:transaction type="Mandatory"
     */
    public void ejbCreateMethodMandatory(){}

    /**
     * @ejb:create-method
     * @ejb:transaction type="Never"
     */
    public void ejbCreateMethodNever(){}

    /**
     * @ejb:create-method
     */
    public void ejbCreateMethodNotSpecified(){}

    /**
     * @ejb:create-method view-type="remote"
     * @ejb:transaction type="Supports"
     */
    public void ejbCreateMethodSupports(Object[] [] o){}

    /**
     * @ejb:create-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void ejbCreateLocalMethodSupports(Object[] [] o){}

    /**
     * @ejb:create-method view-type="both"
     * @ejb:transaction type="Supports"
     */
    public void ejbCreateBothMethodSupports(Object[] [] o){}

}
