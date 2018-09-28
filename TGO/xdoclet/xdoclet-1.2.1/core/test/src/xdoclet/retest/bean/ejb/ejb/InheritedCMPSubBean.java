package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.EntityBean;

/**
 * @ejb:bean
 *      name="inherited/cmp/InheritedCMPSub"
 *      type="CMP"
 *      view-type="both"
 *      cmp-version="2.x"
 *
 * @ejb:finder
 *      signature="java.util.Collection findByClassic()"
 */
public abstract class InheritedCMPSubBean
        implements EntityBean
{
}
