/*
 * $Id: IFTagSLBean.java,v 1.1 2002/03/02 12:16:44 vharcq Exp $
 */
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;

/**
 * @ejb:bean
 *      type="Stateless"
 *      name="simple/IFTagSL"
 *      view-type="both"
 * @ejb:interface
 *      extends="xdoclet.retest.bean.ejb.base.BaseEJBObject, javax.ejb.EJBObject"
 *      local-extends="xdoclet.retest.bean.ejb.base.BaseEJBLocalObject, javax.ejb.EJBLocalObject"
 * @ejb:home
 *      extends="xdoclet.retest.bean.ejb.base.BaseEJBHome, javax.ejb.EJBHome"
 *      local-extends="xdoclet.retest.bean.ejb.base.BaseEJBLocalHome, javax.ejb.EJBLocalHome"
 */
public abstract class IFTagSLBean
        extends SimpleSLBean
        implements SessionBean
{
}
