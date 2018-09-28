/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.soap.ejb;

import java.io.File;

import org.apache.tools.ant.types.Path;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XmlSubTask;

import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Apache SOAP" name="apachesoap" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.9 $
 * @xdoclet.merge-file   file="soap-mappings-{0}.xml" relates-to="soap-dds-{0}.xml" description="An XML document
 *      containing the optional isd:mappings element."
 */
public class ApacheSoapSubTask extends XmlSubTask
{
    private final static String SOAP_SCHEMA = "http://xml.apache.org/xml-soap/deployment";

    private static String DEFAULT_TEMPLATE_FILE = "resources/apache-soap.xdt";

    private static String GENERATED_FILE_NAME = "soap-dds-{0}.xml";
    protected String statelessSessionEjbProvider = "org.apache.soap.providers.StatelessEJBProvider";

    protected String statefulSessionEjbProvider = "org.apache.soap.providers.StatefulEJBProvider";

    protected String entityEjbProvider = "org.apache.soap.providers.EntityEJBProvider";

    protected Path  providerClasspath;

    protected String contextProviderUrl = "";

    protected String contextFactoryName = "";

    /**
     * Describe what the ApacheSoapSubTask constructor does
     */
    public ApacheSoapSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setSchema(SOAP_SCHEMA);
        setHavingClassTag("soap:service");
        setValidateXML(false);

        //setPublicId( APACHE_SOAP_PUBLICID );
        //setDtdURL(getClass().getResource( APACHE_SOAP_DTD_FILE_NAME_2_3 ));
    }

    /**
     * Gets the StatelessSessionEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @return   The StatelessSessionEjbProvider value
     */
    public String getStatelessSessionEjbProvider()
    {
        return statelessSessionEjbProvider;
    }

    /**
     * Gets the StatefulSessionEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @return   The StatefulSessionEjbProvider value
     */
    public String getStatefulSessionEjbProvider()
    {
        return statefulSessionEjbProvider;
    }

    /**
     * Gets the EntityEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @return   The EntityEjbProvider value
     */
    public String getEntityEjbProvider()
    {
        return entityEjbProvider;
    }

    /**
     * Gets the ProviderClasspath attribute of the ApacheSoapSubTask object
     *
     * @return   The ProviderClasspath value
     */
    public Path getProviderClasspath()
    {
        return providerClasspath;
    }

    /**
     * Gets the ContextProviderUrl attribute of the ApacheSoapSubTask object
     *
     * @return   The ContextProviderUrl value
     */
    public String getContextProviderUrl()
    {
        return contextProviderUrl;
    }

    /**
     * Gets the ContextFactoryName attribute of the ApacheSoapSubTask object
     *
     * @return   The ContextFactoryName value
     */
    public String getContextFactoryName()
    {
        return contextFactoryName;
    }

    /**
     * Sets the StatelessSessionEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @param statelessSessionEjbProvider  The new StatelessSessionEjbProvider value
     */
    public void setStatelessSessionEjbProvider(String statelessSessionEjbProvider)
    {
        this.statelessSessionEjbProvider = statelessSessionEjbProvider;
    }

    /**
     * Sets the StatefulSessionEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @param statefulSessionEjbProvider  The new StatefulSessionEjbProvider value
     */
    public void setStatefulSessionEjbProvider(String statefulSessionEjbProvider)
    {
        this.statefulSessionEjbProvider = statefulSessionEjbProvider;
    }

    /**
     * Sets the EntityEjbProvider attribute of the ApacheSoapSubTask object
     *
     * @param entityEjbProvider  The new EntityEjbProvider value
     */
    public void setEntityEjbProvider(String entityEjbProvider)
    {
        this.entityEjbProvider = entityEjbProvider;
    }

    /**
     * Sets the Providerclasspath attribute of the ApacheSoapSubTask object
     *
     * @param providerClasspath  The new Providerclasspath value
     */
    public void setProviderclasspath(Path providerClasspath)
    {
        this.providerClasspath = providerClasspath;
    }

    /**
     * Sets the ContextProviderUrl attribute of the ApacheSoapSubTask object. The ContextProviderUrl is the URL
     * associated with the JNDI context provider used when looking up an EJB's home interface.
     *
     * @param contextProviderUrl  The new ContextProviderUrl value
     */
    public void setContextProviderUrl(String contextProviderUrl)
    {
        this.contextProviderUrl = contextProviderUrl;
    }

    /**
     * Sets the ContextFactoryName attribute of the ApacheSoapSubTask object. The ContextFactoryName is the name of the
     * JNDI context factory used when looking up an EJB's home interface.
     *
     * @param contextFactoryName  The new ContextFactoryName value
     */
    public void setContextFactoryName(String contextFactoryName)
    {
        this.contextFactoryName = contextFactoryName;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     * @msg.bundle                  id="parameter_missing_or_empty" msg="parameter is missing or empty."
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getStatelessSessionEjbProvider() == null || getStatelessSessionEjbProvider().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY,
                new String[]{"statelessSessionEjbProvider"}));
        }

        if (getStatefulSessionEjbProvider() == null || getStatefulSessionEjbProvider().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY,
                new String[]{"statefulSessionEjbProvider"}));
        }

        if (getEntityEjbProvider() == null || getEntityEjbProvider().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY,
                new String[]{"entityEjbProvider"}));
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        String service_urn = getCurrentClass().getDoc().getTag("soap:service").getValue();
        String dest_file_name = getDestinationFile();

        System.out.println(Translator.getString(XDocletModulesApacheSoapEjbMessages.class, XDocletModulesApacheSoapEjbMessages.GENERATING_DD,
            new String[]{getCurrentClass().getQualifiedName(), service_urn, dest_file_name}));
    }
}
