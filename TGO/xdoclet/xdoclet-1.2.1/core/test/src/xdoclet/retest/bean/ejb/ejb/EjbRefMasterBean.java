package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;

/**
 * @ejb:bean
 *      type="Stateless"
 *      name="ejbref/EjbRefMaster"
 *      view-type="both"
 *
 * @ejb:ejb-ref
 *      ejb-name="ejbref/EjbRefClient"
 *      ref-name="ejbref/EjbRefClient-refname"
 * @ejb:ejb-external-ref
 *      ejb-name="ejbref/EjbRefClient"
 *      ref-name="ejbref/EjbRefClient-ext-refname"
 *      type="Entity"
 *      home="xdoclet.retest.bean.ejb.interfaces.EjbRefClientHome"
 *      remote="xdoclet.retest.bean.ejb.interfaces.EjbRefClient"
 *
 * @jboss:ejb-ref-jndi
 *      ref-name="ejbref/EjbRefClient-refname"
 *      jndi-name="other/EjbRefClient-jboss-refname"
 *
 */
public abstract class EjbRefMasterBean
        implements SessionBean
{
}
