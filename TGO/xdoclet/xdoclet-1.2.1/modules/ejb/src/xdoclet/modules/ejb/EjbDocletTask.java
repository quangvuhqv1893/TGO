/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb;

import org.apache.tools.ant.BuildException;

import xdoclet.DocletTask;
import xdoclet.util.Translator;

/**
 * This task executes various EJB-specific sub-tasks. Make sure to include the jar file containing Sun's javax.ejb.*
 * classes on the taskdef's classpath.
 *
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       April 30, 2001
 * @ant.element   name="ejbdoclet" display-name="EJB Task"
 * @version       $Revision: 1.11 $
 */
public class EjbDocletTask extends DocletTask
{
    /**
     * Defaults to EJB 2.0.
     */
    private String  ejbspec = EjbSpecVersion.EJB_2_0;

    /**
     * List of suffix strings used when calculating a default bean name from a class.
     */
    private String  ejbClassNameSuffix = "Bean,EJB,Ejb";

    /**
     * Gets the EjbSpec attribute of the EjbDocletTask object
     *
     * @return   The EjbSpec value
     */
    public String getEjbSpec()
    {
        return ejbspec;
    }

    /**
     * Gets the EjbClassNameSuffix attribute of the EjbDocletTask object
     *
     * @return   The EjbClassNameSuffix value
     */
    public String getEjbClassNameSuffix()
    {
        return ejbClassNameSuffix;
    }

    /**
     * The version of EJB spec ejbdoclet should adhere, currently "1.1" and "2.0" defined. If, for example, "2.0"
     * specified, then ejbdoclet will generate EJB 2.0-compatible ejb-jar.xml file.
     *
     * @param ejbspec
     * @ant.not-required   No. Default is "2.0"
     */
    public void setEjbSpec(EjbSpecVersion ejbspec)
    {
        this.ejbspec = ejbspec.getValue();
    }

    /**
     * A comma-separated list of endings which should be removed from the bean class name to generate a bean's name if
     * no name parameter is specified in the <a href="../../../../tags/ejb-tags.html#@ejb.bean">ejb.bean</a> tag.
     *
     * @param ejbClassNameSuffix
     * @ant.not-required          No, Default is "Bean,EJB,Ejb"
     */
    public void setEjbClassNameSuffix(String ejbClassNameSuffix)
    {
        this.ejbClassNameSuffix = ejbClassNameSuffix;
    }


    protected void validateOptions() throws BuildException
    {
        super.validateOptions();
        try {
            checkClass("javax.ejb.EntityBean");
        }
        catch (BuildException ex) {
            throw new BuildException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.MISSING_J2EE_CLASSES), ex);
        }
    }

    /**
     * @author    Ara Abrahamian (ara_e@email.com)
     * @created   July 19, 2001
     */
    public static class EjbSpecVersion extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String EJB_1_1 = "1.1";

        public final static String EJB_2_0 = "2.0";

        /**
         * Gets the Values attribute of the EjbSpecVersion object
         *
         * @return   The Values value
         */
        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{
                EJB_1_1, EJB_2_0
                });
        }
    }

}
