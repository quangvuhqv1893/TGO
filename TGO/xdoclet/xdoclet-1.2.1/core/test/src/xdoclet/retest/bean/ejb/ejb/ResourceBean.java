package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;

/**
 * @ejb:bean
 *      type="Stateless"
 *      name="ejbres/Resource"
 *      view-type="both"
 *
 * @ejb:resource-ref
 *      res-name="jdbc/DBPool"
 *      res-type="javax.sql.DataSource"
 *      res-auth="Container"
 * @jboss:resource-ref
 *      res-ref-name="jdbc/DBPool"
 *      resource-name="MyDataSourceManager"
 *
 * @ejb:resource-ref
 *      res-name="jdbc/DBPool2"
 *      res-type="javax.sql.DataSource"
 *      res-auth="Application"
 *      description="JDBC Pool ..."
 *      res-sharing-scope="Shareable"
 *
 * @ejb:resource-env-ref
 *      name="jms/StockQueue"
 *      type="javax.jms.Queue"
 *
 * @ejb:resource-ref
 *      res-name="mail/MyMail"
 *      res-type="javax.mail.Session"
 *      res-auth="Container"
 * @jboss:resource-manager
 *      res-man-name="mail/MyMail"
 *      res-man-jndi-name="Mail"
 *
 */
public abstract class ResourceBean
        implements SessionBean
{
}
