/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jdo;


import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import xjavadoc.*;
import xdoclet.GenerationManager;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.XmlSubTask;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.template.TemplateException;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates the XML metadata for the JDO classes.
 *
 * @author               Ludovic Claude (ludovicc@users.sourceforge.net)
 * @author               David Jencks (d_jencks@users.sourceforge.net)
 * @created              June 11, 20012
 * @version              $Revision: 1.9 $
 * @ant.element          display-name="JDO XML MetaData" name="jdometadata" parent="xdoclet.modules.jdo.JdoDocletTask"
 * @xdoclet.merge-file   file="vendor-extensions.xml" relates-to="generated .jdo files" description="An XML unparsed
 *      entity containing any additional vendor extensions i.e. top-level extension elements."
 */
public class JdoXmlMetadataSubTask extends XmlSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/jdo_xml.xdt";

    private static String PACKAGE_GENERATED_FILE_NAME = "{0}.jdo";
    private static String CLASS_GENERATED_FILE_NAME = "{0}.jdo";

    private static String JDOXML_PUBLICID_1_0 = "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 1.0//EN";

    private static String JDOXML_SYSTEMID_1_0 = "http://java.sun.com/dtd/jdo_1_0.dtd";

    private static String JDOXML_DTD_FILE_NAME_1_0 = "resources/jdo-10.dtd";

    private String  jdoSpec = "1.0";
    private String  generation = "class";
    private boolean forceGenerationPerPackage;
    private String  project = "metadata";


    /**
     * Describe what the WebXmlSubTask constructor does
     */
    public JdoXmlMetadataSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
    }

    /**
     * Gets the Jdospec attribute of the JdoXmlSubTask object
     *
     * @return   The Jdospec value
     */
    public String getJdospec()
    {
        return jdoSpec;
    }

    public String getGeneration()
    {
        return generation;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject(String project)
    {
        this.project = project;
    }

    public void setGeneration(GenerationOptionTypes value)
    {
        generation = value.getValue();
    }

    /**
     * Sets the Jdospec attribute of the JdoXmlSubTask object
     *
     * @param jdoSpec  The new Jdospec value
     */
    public void setJdospec(JdoDocletTask.JdoSpecVersion jdoSpec)
    {
        this.jdoSpec = jdoSpec.getValue();
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        // JDO does not require a template url or a destination file
        //
        // super.validateOptions();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        setPublicId(JDOXML_PUBLICID_1_0);
        setSystemId(JDOXML_SYSTEMID_1_0);
        setDtdURL(getClass().getResource(JDOXML_DTD_FILE_NAME_1_0));

        if (isGenerationPerClass())
            setDestinationFile(CLASS_GENERATED_FILE_NAME);
        else if (isGenerationPerPackage())
            setDestinationFile(PACKAGE_GENERATED_FILE_NAME);
        else
            setDestinationFile(getProject() + ".jdo");

        startProcess();
    }

    public void startProcess() throws XDocletException
    {
        Log log = LogUtil.getLog(JdoXmlMetadataSubTask.class, "startProcess");

        if (log.isDebugEnabled()) {
            log.debug("destDir.toString()=" + getDestDir());
            log.debug("getTemplateURL()=" + getTemplateURL());
            log.debug("getDestinationfile()=" + getDestinationFile());
            log.debug("getOfType()=" + getOfType());
            log.debug("getExtent()=" + getExtent());
            log.debug("getHavingClassTag()=" + getHavingClassTag());
        }

        if (isGenerationPerClass()) {
            startProcessPerClass();
        }
        else if (isGenerationPerPackage()) {
            startProcessPerPackage();
        }
        else {
            startProcessForAll();
        }
    }

    protected boolean isForceGenerationPerPackage()
    {
        return forceGenerationPerPackage;
    }

    /**
     * @return
     * @todo     make isGenerationPerClass() in TemplateSubTask protected
     */
    protected boolean isGenerationPerClass()
    {
        return GenerationOptionTypes.CLASS.equals(generation);
    }

    protected boolean isGenerationPerPackage()
    {
        return GenerationOptionTypes.PACKAGE.equals(generation);
    }

    /**
     * Returns class name for the generated file. {0} substituted by package name.
     *
     * @param pak
     * @return                      The GeneratedClassName value
     * @exception XDocletException  Description of Exception
     */
    protected String getGeneratedFileName(XPackage pak) throws XDocletException
    {
        Log log = LogUtil.getLog(JdoXmlMetadataSubTask.class, "getGeneratedFileName");
        String package_structure = null;

        if (isPrefixWithPackageStructure() == true) {
            package_structure = PackageTagsHandler.packageNameAsPathFor(pak);

            int lastSlash = package_structure.lastIndexOf('/');

            if (lastSlash != -1)
                package_structure = package_structure.substring(0, lastSlash);
        }

        String packageName = isPackageSubstitutionInheritanceSupported() == true ? package_structure : null;

        String qualifiedName = pak.getName();
        int lastDot = qualifiedName.lastIndexOf('.');
        String name = qualifiedName.substring(lastDot + 1);

        String destinationFile = MessageFormat.format(getDestinationFile(), new Object[]{name});

        if (log.isDebugEnabled()) {
            log.debug("pak=" + pak);
            log.debug("packageName=" + packageName);
            log.debug("destinationFile=" + destinationFile);
        }

        return new File(packageName, destinationFile).toString();
    }

    protected void setForceGenerationPerPackage(boolean value)
    {
        forceGenerationPerPackage = value;
    }

    protected void startProcessPerPackage() throws XDocletException
    {
        Log log = LogUtil.getLog(JdoXmlMetadataSubTask.class, "startProcessPerPackage");

        if (log.isDebugEnabled()) {
            log.debug("Per package.");
        }

        Collection packages = getXJavaDoc().getSourcePackages();

        for (PackageIterator i = XCollections.packageIterator(packages); i.hasNext(); ) {
            XPackage pakkage = i.next();

            if (log.isDebugEnabled())
                log.debug("Working on " + pakkage);
            generateForPackage(pakkage);
        }
    }

    /**
     * Processed template for pkg and generates output file for pkg.
     *
     * @param pkg                   Description of Parameter
     * @exception XDocletException  Description of Exception
     */
    protected void generateForPackage(XPackage pkg) throws XDocletException
    {
        Log log = LogUtil.getLog(getClass(), "generateForClass");
        String generatedFileName = getGeneratedFileName(pkg);
        File file = new File(getDestDir().toString(), generatedFileName);

        if (log.isDebugEnabled()) {
            log.debug("destDir.toString()=" + getDestDir().toString());
            log.debug("getGeneratedFileName()=" + generatedFileName);
            log.debug("file=" + file);
        }

        if (file.exists()) {
            log.debug("File exists.");

            // Check modification timestamps

            // todo: implement isGenerationNeeded in GenerationManager for (sources in a package) -> generated file
            //boolean isGenerationNeeded = getGenerationManager().isGenerationNeeded(pkg, file, true);
            boolean isGenerationNeeded = true;

            if (!isGenerationNeeded) {
                return;
            }
        }

        file.getParentFile().mkdirs();

        try {
            setCurrentPackage(pkg);
            startEngine(getTemplateURL(), new File(getDestDir(), generatedFileName));
        }
        catch (TemplateException e) {
            if (e instanceof XDocletException) {
                throw (XDocletException) e;
            }
            else {
                log.debug("generateForClass()");
                throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        String generatedFileName = getDestinationFile();

        if (isGenerationPerClass())
            generatedFileName = getGeneratedFileName(getCurrentClass());
        else if (isGenerationPerPackage())
            generatedFileName = getGeneratedFileName(getCurrentPackage());
        System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{generatedFileName}));
    }

    /**
     * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
     * @created   June 14, 20012
     */
    public static class GenerationOptionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String CLASS = "class";
        public final static String PACKAGE = "package";
        public final static String PROJECT = "project";

        /**
         * Gets the Values attribute of the GeneratinOptionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{CLASS, PACKAGE, PROJECT});
        }
    }
}
