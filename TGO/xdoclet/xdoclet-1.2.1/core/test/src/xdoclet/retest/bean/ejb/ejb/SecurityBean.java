/*
 * Created by IntelliJ IDEA.
 * User: Vincent
 * Date: Apr 9, 2002
 * Time: 12:05:35 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.EntityBean;

/**
 * @ejb:bean
 *      name="ejbsec/Security"
 *      type="CMP"
 *      view-type="both"
 *      cmp-version="2.x"
 *
 * @ejb:permission role-name="Hey,Ho"
 */
public abstract class SecurityBean
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
     * @ejb:permission role-name="a"
     */
    public void roleA(){}

    /**
     * @ejb:interface-method
     * @ejb:permission role-name="a,b"
     */
    public void roleAAndB(){}

}
