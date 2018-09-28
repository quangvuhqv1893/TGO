/*
 * $Id: InheritedCMPSuperBean.java,v 1.3 2002/04/02 06:08:20 vharcq Exp $
 */
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.EntityBean;

/**
 * @ejb:bean
 *      name="inherited/cmp/InheritedCMPSuper"
 *      type="CMP"
 *      view-type="both"
 *      cmp-version="2.x"
 *
 * @ejb:home
 *      extends="xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubHome"
 *      local-extends="xdoclet.retest.bean.ejb.interfaces.InheritedCMPSubLocalHome"
 *      todo="THIS SHOULD NOT NEEDED IN FACT ! ! ! see bug 516890  "
 *
 */
public abstract class InheritedCMPSuperBean
        extends InheritedCMPSubBean
        implements EntityBean
{
}
