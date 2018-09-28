/*
 * $Id: CreateMethodSLBean.java,v 1.1 2002/03/02 12:16:44 vharcq Exp $
 */
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;

/**
 * @ejb:bean type="Stateless" name="simple/CreateMethodSL" view-type="both"
 */
public abstract class CreateMethodSLBean
        extends SimpleSLBean
        implements SessionBean
{
    /* --------------------------------------------------------------------- */
    /* HOME METHODS                                                          */
    /* --------------------------------------------------------------------- */

    /**
     * @ejb:create-method
     */
    public void ejbCreateBlaBla() throws CreateException{
    }

}
