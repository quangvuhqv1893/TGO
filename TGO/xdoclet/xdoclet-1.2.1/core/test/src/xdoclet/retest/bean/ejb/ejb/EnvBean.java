/*
 * $Id: EnvBean.java,v 1.1 2002/03/10 18:06:00 vharcq Exp $
 */
package xdoclet.retest.bean.ejb.ejb;

import javax.ejb.SessionBean;

/**
 * @ejb:bean
 *      type="Stateless"
 *      name="env/Env"
 *      view-type="both"
 *
 * @ejb:env-entry
 *      name="StringEnv"
 *      type="java.lang.String"
 *      value="Bla bla bla"
 * @ejb:env-entry
 *      name="CharacterEnv"
 *      type="java.lang.Character"
 *      value="X"
 * @ejb:env-entry
 *      name="IntegerEnv"
 *      type="java.lang.Integer"
 *      value="32"
 * @ejb:env-entry
 *      name="BooleanEnv"
 *      type="java.lang.Boolean"
 *      value="true"
 * @ejb:env-entry
 *      name="DoubleEnv"
 *      type="java.lang.Double"
 *      value="32.56"
 * @ejb:env-entry
 *      name="ByteEnv"
 *      type="java.lang.Byte"
 *      value="10110101"
 * @ejb:env-entry
 *      name="ShortEnv"
 *      type="java.lang.Short"
 *      value="23"
 * @ejb:env-entry
 *      name="LongEnv"
 *      type="java.lang.Long"
 *      value="12355454.123"
 * @ejb:env-entry
 *      name="FloatEnv"
 *      type="java.lang.Float"
 *      value="78525.12"

 */
public abstract class EnvBean
        implements SessionBean
{
}
