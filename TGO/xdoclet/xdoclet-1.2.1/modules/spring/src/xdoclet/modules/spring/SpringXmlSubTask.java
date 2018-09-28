/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.spring;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates XML file to wire beans in the Spring framework.
 *
 * @author               Craig Walls (xdoclet@habuma.com)
 * @created              March 5, 2004
 * @ant.element          display-name="spring.xml" name="springxml" parent="xdoclet.modules.spring.SpringDocletTask"
 * @xdoclet.merge-file   file="spring-beans.xml" relates-to="spring.xml" description="An XML unparsed entity containing
 *      bean declarations to be included in a generated Spring bean XML file."
 */
public class SpringXmlSubTask extends XmlSubTask
{
    private final static String SPRING_PUBLIC_ID = "-//SPRING//DTD BEAN//EN";

    private final static String SPRING_SYSTEM_ID = "http://www.springframework.org/dtd/spring-beans.dtd";

    private final static String SPRING_DTD = "resources/spring-beans.dtd";

    private final static String DEFAULT_TEMPLATE_FILE = "resources/spring_xml.xdt";

    private final static String GENERATED_FILE_NAME = "spring.xml";

    private String  defaultAutowire = "no";
    private String  defaultLazyInit = "false";
    private String  defaultDependencyCheck = "none";

    public SpringXmlSubTask()
    {
        setPublicId(SPRING_PUBLIC_ID);
        setSystemId(SPRING_SYSTEM_ID);
        setDtdURL(getClass().getResource(SPRING_DTD));
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }

    public String getDefaultAutowire()
    {
        return defaultAutowire;
    }

    public String getDefaultDependencyCheck()
    {
        return defaultDependencyCheck;
    }

    public String getDefaultLazyInit()
    {
        return defaultLazyInit;
    }

    /**
     * Sets the name of the generated bean XML file. Defaults to "spring.xml".
     *
     * @param destinationFile
     */
    public void setDestinationFile(String destinationFile)
    {
        super.setDestinationFile(destinationFile);
    }

    /**
     * Sets the default autowiring mode to apply to all beans in the generated file. Each bean can override this default
     * by setting the autowire attribute of \@spring.bean. Defaults to "no".
     *
     * @param autowire
     */
    public void setDefaultAutowire(String autowire)
    {
        defaultAutowire = autowire;
    }

    /**
     * Sets the default dependency checking mode for all beans in the generated file. Each bean can override this
     * default setting the dependency-check attribute of \@spring.bean. Defaults to "none".
     *
     * @param dependencyCheck
     */
    public void setDefaultDependencyCheck(String dependencyCheck)
    {
        defaultDependencyCheck = dependencyCheck;
    }

    /**
     * Sets the default lazy initialization mode for all beans in the generated file. Each bean can override this
     * default by setting the lazy-init attribute of \@spring.bean. Defaults to "false".
     *
     * @param lazyInit
     */
    public void setDefaultLazyInit(String lazyInit)
    {
        defaultLazyInit = lazyInit;
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        startProcess();
    }

    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{getDestinationFile()}));
    }
}
