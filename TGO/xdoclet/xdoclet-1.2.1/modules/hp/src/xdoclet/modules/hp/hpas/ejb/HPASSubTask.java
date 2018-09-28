/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.hp.hpas.ejb;

import xdoclet.XDocletException;
import xdoclet.XmlSubTask;
import xdoclet.modules.ejb.dd.AbstractEjbDeploymentDescriptorSubTask;
import xdoclet.util.Translator;

/**
 * Creates hp-ejb-jar.xml deployment descriptor for HPAS.
 *
 * @author        <a href="mailto:stevensa@users.sourceforge.net">Andrew Stevens</a>
 * @created       January 10, 2002
 * @ant.element   display-name="HP AS" name="hpas" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.7 $
 */
public class HPASSubTask extends AbstractEjbDeploymentDescriptorSubTask
{
    private final static String HPAS_DD_SCHEMA = "http://www.hp.bluestone.com/xml/schemas/hp-ejb-jar.xsd";

    private final static String DEFAULT_TEMPLATE_FILE = "resources/hp-ejb-jar_xml.xdt";

    private final static String DEFAULT_GENERATED_FILE = "hp-ejb-jar.xml";

    protected String sfsbPassivationRoot = "";

    protected String persistenceProduct = "VXML";

    protected String persistenceVersion = "";

    protected String persistenceClass = "com.hp.mwlabs.j2ee.containers.ejb.persistence.vxml.CMPPersistenceManager";

    protected String persistenceSuffix = "";

    public HPASSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(DEFAULT_GENERATED_FILE);
        setSchema(HPAS_DD_SCHEMA);
        setValidateXML(false);
    }

    /**
     * Gets the SfsbPassivationRoot attribute of the HPASSubTask object
     *
     * @return   The SfsbPassivationRoot value
     */
    public String getSfsbPassivationRoot()
    {
        return sfsbPassivationRoot;
    }

    /**
     * Gets the PersistenceProduct attribute of the HPASSubTask object
     *
     * @return   The PersistenceProduct value
     */
    public String getPersistenceProduct()
    {
        return persistenceProduct;
    }

    /**
     * Gets the PersistenceVersion attribute of the HPASSubTask object
     *
     * @return   The PersistenceVersion value
     */
    public String getPersistenceVersion()
    {
        return persistenceVersion;
    }

    /**
     * Gets the PersistenceClass attribute of the HPASSubTask object
     *
     * @return   The PersistenceClass value
     */
    public String getPersistenceClass()
    {
        return persistenceClass;
    }

    /**
     * Gets the PersistenceSuffix attribute of the HPASSubTask object
     *
     * @return   The PersistenceSuffix value
     */
    public String getPersistenceSuffix()
    {
        return persistenceSuffix;
    }

    /**
     * The path to a local directory indicating where the container should passivate Stateful Session Bean instances.
     *
     * @param sfsbPassivationRoot  The new SfsbPassivationRoot value
     * @ant.not-required           No, only if <code>hpas.bean passivation="true"</code> tag is used on a stateful
     *      session bean. Default is "".
     */
    public void setSfsbPassivationRoot(String sfsbPassivationRoot)
    {
        this.sfsbPassivationRoot = sfsbPassivationRoot;
    }

    /**
     * Only applies when using CMP. Specifies the name of a third party persistence manager product.
     *
     * @param persistenceProduct  The new PersistenceProduct value
     * @ant.not-required          No, default is "VXML".
     */
    public void setPersistenceProduct(String persistenceProduct)
    {
        this.persistenceProduct = persistenceProduct;
    }

    /**
     * Only applies when using CMP. Specifies the version of the third party product.
     *
     * @param persistenceVersion  The new PersistenceVersion value
     * @ant.not-required          No, default is "".
     */
    public void setPersistenceVersion(String persistenceVersion)
    {
        this.persistenceVersion = persistenceVersion;
    }

    /**
     * Only applies when using CMP. Specifies the class that implements the persistence manager interface.
     *
     * @param persistenceClass  The new PersistenceClass value
     * @ant.not-required        No, default is <code>com.hp.mwlabs.j2ee.containers.ejb.persistence.vxml.CMPPersistenceManager</code>
     *      .
     */
    public void setPersistenceClass(String persistenceClass)
    {
        this.persistenceClass = persistenceClass;
    }

    /**
     * Only applies when using CMP. Specifies a suffix to be appended to the abstract bean class name to obtain the
     * generated bean class name.
     *
     * @param persistenceSuffix  The new PersistenceSuffix value
     * @ant.not-required         No, default is "".
     */
    public void setPersistenceSuffix(String persistenceSuffix)
    {
        this.persistenceSuffix = persistenceSuffix;
    }
}
