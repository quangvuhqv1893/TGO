/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.tagshandler.TypeTagsHandler;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;
import xdoclet.template.XDocletTemplateMessages;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Generates The template file specified in templateFile configuration parameter. It operates in two modes: per class
 * and single output.
 *
 * @author          Ara Abrahamian (ara_e@email.com)
 * @created         Sep 25, 2001
 * @ant.element     name="template" parent="xdoclet.DocletTask" display-name="Standard Subtask"
 * @version         $Revision: 1.75 $
 * @ant.attribute   name="destinationFile" description="The destination file name. If a {0} is found it's assumed that a
 *      per class output generation is needed, so {0} is substituted with class name; otherwise a single file is
 *      generated with the specified name." required="Yes"
 * @ant.attribute   name="extent" description="You can control the extent in which the type search occurs. Valid values
 *      are: <i>concrete-type</i> , <i> superclass</i> and <i>hierarchy</i> ." required="No. Default is \"hierarchy\""
 */
public class TemplateSubTask extends SubTask
{
    private final static String SUBTASK_NAME = "template";

    private URL     templateURL = null;

    private transient TemplateEngine engine = null;

    private boolean prefixWithPackageStructure = true;

    private boolean acceptInterfaces = true;
    private boolean acceptAbstractClasses = true;
    private String  docEncoding = null;

    /**
     * @see   #setDestinationFile(java.lang.String)
     * @see   #getDestinationFile()
     */
    private String  destinationFile = null;

    /**
     * Applicable only to per class output generation. Specify full qualified class name from which classes should be
     * derived. Output generation is performed only for classes of one of the types defined in this list. You can
     * control the extent in which the type search occures by using 'extent' property.
     *
     * @see   #addOfType(java.lang.String)
     * @see   #getOfType()
     * @see   #setExtent(TemplateSubTask.ExtentTypes)
     * @see   #getExtent()
     */
    private List    ofType = new ArrayList();

    /**
     * You can control the extent in which the type search occurs. Valid values are: <i>concrete-type</i> , <i>
     * superclass</i> and <i>hierarchy</i> which is the default.
     *
     * @see   #setExtent(TemplateSubTask.ExtentTypes)
     * @see   #getExtent()
     * @see   #addOfType(java.lang.String)
     * @see   #getOfType()
     */
    private String  extent = "hierarchy";

    /**
     * A class should have the class tag defined in this property in order to be processed and output generated for.
     *
     * @see   #setHavingClassTag(java.lang.String)
     * @see   #getHavingClassTag()
     */
    private String  havingClassTag = null;

    /**
     * The subtask class.
     */
    private String  subTaskClassName = null;

    private ArrayList packageSubstitutions = new ArrayList();

    private boolean packageSubstitutionInheritanceSupported = true;

    private GenerationManager generationManager;

    public TemplateSubTask()
    {
        super();
        DocletTask.registerSubTaskName(this, SUBTASK_NAME);
    }

    /**
     * Converts the full qualified class name to a valid path with File.separator characters instead of . characters and
     * class name postfixed by a ".java".
     *
     * @param className  Description of Parameter
     * @return           Description of the Returned Value
     */
    protected static String javaFile(String className)
    {
        return className.replace('.', '/') + ".java";
    }

    public boolean getAcceptInterfaces()
    {
        return acceptInterfaces;
    }

    public boolean getAcceptAbstractClasses()
    {
        return acceptAbstractClasses;
    }

    public GenerationManager getGenerationManager()
    {
        return generationManager;
    }

    /**
     * Gets the PackageSubstitutions attribute of the TemplateSubTask object
     *
     * @return   The PackageSubstitutions value
     */
    public ArrayList getPackageSubstitutions()
    {
        return packageSubstitutions;
    }

    /**
     * By default supports, but some subtasks may not support because global packageSubstitution is for public
     * interfaces/classes, not good for impl classes.
     *
     * @return   true
     */
    public boolean isPackageSubstitutionInheritanceSupported()
    {
        return packageSubstitutionInheritanceSupported;
    }

    /**
     * Gets the SubTaskClassName attribute of the TemplateSubTask object
     *
     * @return   The SubTaskClassName value
     */
    public String getSubTaskClassName()
    {
        return subTaskClassName;
    }

    /**
     * Gets the Engine attribute of the TemplateSubTask object
     *
     * @return   The Engine value
     */
    public TemplateEngine getEngine()
    {
        if (engine == null) {
            throw new IllegalStateException("Engine is null?!");
        }
        return engine;
    }

    /**
     * Gets the TemplateURL attribute of the TemplateSubTask object
     *
     * @return   The TemplateURL value
     */
    public URL getTemplateURL()
    {
        return templateURL;
    }

    /**
     * Gets the DestinationFile attribute of the TemplateSubTask object
     *
     * @return   The DestinationFile value
     */
    public String getDestinationFile()
    {
        return destinationFile;
    }

    /**
     * Gets the OfType attribute of the TemplateSubTask object
     *
     * @return   The OfType value
     */
    public String[] getOfType()
    {
        String[] result = new String[ofType.size()];

        for (int i = 0; i < ofType.size(); i++) {
            result[i] = ((OfType) ofType.get(i)).getType();
        }

        return result;
    }

    /**
     * Gets the Extent attribute of the TemplateSubTask object
     *
     * @return   The Extent value
     */
    public String getExtent()
    {
        return extent;
    }

    /**
     * Gets the HavingClassTag attribute of the TemplateSubTask object
     *
     * @return   The HavingClassTag value
     */
    public String getHavingClassTag()
    {
        return havingClassTag;
    }

    /**
     * Gets the PrefixWithPackageStructure attribute of the TemplateSubTask object
     *
     * @return   The PrefixWithPackageStructure value
     */
    public boolean isPrefixWithPackageStructure()
    {
        return prefixWithPackageStructure;
    }

    /**
     * Indicates whether or not to generate for interfaces.
     *
     * @param acceptInterfaces
     * @ant.not-required        No, default is "true"
     */
    public void setAcceptInterfaces(boolean acceptInterfaces)
    {
        this.acceptInterfaces = acceptInterfaces;
    }

    /**
     * Indicates whether or not to generate for abstract classes.
     *
     * @param acceptAbstractClasses
     * @ant.not-required             No, default is "true"
     */
    public void setAcceptAbstractClasses(boolean acceptAbstractClasses)
    {
        this.acceptAbstractClasses = acceptAbstractClasses;
    }

    /**
     * Sets the PackageSubstitutions attribute of the TemplateSubTask object
     *
     * @param packageSubstitutions  The new PackageSubstitutions value
     */
    public void setPackageSubstitutions(ArrayList packageSubstitutions)
    {
        this.packageSubstitutions = packageSubstitutions;
    }

    /**
     * Indicates whether or not package substitution should be inherited
     *
     * @param packageSubstitutionInheritanceSupported  The new PackageSubstitutionInheritanceSupported value
     * @ant.not-required                               No, default is "true"
     */
    public void setPackageSubstitutionInheritanceSupported(boolean packageSubstitutionInheritanceSupported)
    {
        this.packageSubstitutionInheritanceSupported = packageSubstitutionInheritanceSupported;
    }

    /**
     * Sets a different name for the subtask which will be seen in the log messages.
     *
     * @param subTaskClassName  The new SubTaskClassName value
     */
    public void setSubTaskClassName(String subTaskClassName)
    {
        this.subTaskClassName = subTaskClassName;
    }

    /**
     * Indicates whether or not to prefix with package structure.
     *
     * @param prefixWithPackageStructure  The new PrefixWithPackageStructure value
     * @ant.not-required                  No, default is "true"
     */
    public void setPrefixWithPackageStructure(boolean prefixWithPackageStructure)
    {
        this.prefixWithPackageStructure = prefixWithPackageStructure;
    }

    /**
     * Sets the Engine attribute of the TemplateSubTask object
     *
     * @param engine  The new Engine value
     * @ant.ignore
     */
    public void setEngine(TemplateEngine engine)
    {
        this.engine = engine;
    }

    /**
     * The destination file name. If a {0} is found it's assumed that a per class output generation is needed, so {0} is
     * substituted with class name; otherwise a single file is generated with the specified name.
     *
     * @param destinationFile  The new DestinationFile value
     * @ant.ignore
     */
    public void setDestinationFile(String destinationFile)
    {
        this.destinationFile = destinationFile;
    }

    /**
     * This method should be called to set a template file programmatically. The URL is typically obtained with a
     * getClass().getResource( templateName )
     *
     * @param templateURL
     * @ant.ignore
     */
    public void setTemplateURL(URL templateURL)
    {
        this.templateURL = templateURL;
    }

    /**
     * Sets the name of the template file to use for generation
     *
     * @param templateFile          the file name (real file!) of the template
     * @exception XDocletException
     * @ant.not-required            Yes if its a nested <template/> element.
     */
    public void setTemplateFile(File templateFile) throws XDocletException
    {
        if (templateFile.exists()) {
            try {
                setTemplateURL(templateFile.toURL());
            }
            catch (MalformedURLException e) {
                throw new XDocletException(e.getMessage());
            }
        }
        else {
            throw new XDocletException("Couldn't find template: " + templateFile.getAbsolutePath());
        }
    }

    /**
     * You can control the extent in which the type search occurs. Valid values are:
     * <ul>
     *   <li> "concrete-type": the class is itself one of the classes listed in ofType attribute. </li>
     *   <li> "superclass": the class is itself one of the classes listed in ofType attribute or its superclass is one
     *   of the listed ones </li>
     *   <li> "hierarchy" which is the default: anywhere in its hierarchy it derives from one of the listed classes.
     *   </li>
     * </ul>
     * There's no distinction between a class and an interface.
     *
     * @param extent  The new Extent value
     * @ant.ignore
     */
    public void setExtent(ExtentTypes extent)
    {
        this.extent = extent.getValue();
    }

    /**
     * Sets the HavingClassTag attribute of the TemplateSubTask object
     *
     * @param havingClassTag  The new HavingClassTag value
     */
    public void setHavingClassTag(String havingClassTag)
    {
        this.havingClassTag = havingClassTag;
    }

    public void setOfType(String ofType)
    {
        StringTokenizer st = new StringTokenizer(ofType, ",");

        while (st.hasMoreTokens()) {
            String type = st.nextToken();

            OfType new_oftype = new OfType();

            new_oftype.setType(type);
            addOfType(new_oftype);
        }
    }

    public void addOfType(OfType ofType)
    {
        this.ofType.add(ofType);
    }

    /**
     * Substitutes the package of the generated files.
     *
     * @param ps  The feature to be added to the Fileset attribute
     */
    public void addPackageSubstitution(xdoclet.tagshandler.PackageTagsHandler.PackageSubstitution ps)
    {
        packageSubstitutions.add(ps);
    }

    /**
     * Describe what the method does
     *
     * @param src  Describe what the parameter does
     */
    public void copyAttributesFrom(TemplateSubTask src)
    {
        setTemplateURL(src.getTemplateURL());
        setPrefixWithPackageStructure(src.isPrefixWithPackageStructure());
        setAcceptInterfaces(src.getAcceptInterfaces());
        setAcceptAbstractClasses(src.getAcceptAbstractClasses());
        setDestinationFile(src.getDestinationFile());
        for (int i = 0; i < src.ofType.size(); i++) {
            addOfType((OfType) src.ofType.get(i));
        }
        setExtentValue(src.getExtent());
        setHavingClassTag(src.getHavingClassTag());
        setSubTaskClassName(src.getSubTaskClassName());
        for (int i = 0; i < src.packageSubstitutions.size(); i++) {
            addPackageSubstitution((PackageTagsHandler.PackageSubstitution) src.packageSubstitutions.get(i));
        }
        setPackageSubstitutionInheritanceSupported(src.isPackageSubstitutionInheritanceSupported());
        setGenerationManager(src.getGenerationManager());
    }

    /**
     * Describe what the method does
     *
     * @param xJavaDoc
     * @exception XDocletException  Describe the exception
     */
    public void init(XJavaDoc xJavaDoc) throws XDocletException
    {
        super.init(xJavaDoc);
        TemplateEngine.getEngineInstance().setXJavaDoc(getXJavaDoc());
        setEngine(TemplateEngine.getEngineInstance());
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getTemplateURL() == null) {
            throw new XDocletException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_PARAMETER_MISSING, new String[]{"templateFile"}));
        }

        if (getDestinationFile() == null) {
            throw new XDocletException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_PARAMETER_MISSING, new String[]{"destinationFile"}));
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    public void execute() throws XDocletException
    {
        startProcess();
    }

    /**
     * Returns class name for the generated file. {0} substituted by class name.
     *
     * @param clazz                 Description of Parameter
     * @return                      The GeneratedClassName value
     * @exception XDocletException  Description of Exception
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "getGeneratedFileName");

        XPackage pak = clazz.getContainingPackage();
        String package_structure = null;

        if (isPrefixWithPackageStructure() == true)
            // This will do package substitution too
            package_structure = PackageTagsHandler.packageNameAsPathFor(pak);
        else
            package_structure = null;

        String packageName = isPackageSubstitutionInheritanceSupported() == true ? package_structure : null;

        String name = clazz.getTransformedName();

        String destinationFile = MessageFormat.format(getDestinationFile(), new Object[]{name});

        if (log.isDebugEnabled()) {
            log.debug("clazz.getName()=" + clazz.getName());
            log.debug("clazz.getQualifiedName()=" + clazz.getQualifiedName());
            log.debug("pak=" + pak);
            log.debug("packageName=" + packageName);
            log.debug("destinationFile=" + destinationFile);
        }

        return new File(packageName, destinationFile).toString();
    }

    /**
     * Sets the GenerationManager attribute of the TemplateSubTask object
     *
     * @param gM  The new GenerationManager value
     */
    protected void setGenerationManager(GenerationManager gM)
    {
        this.generationManager = gM;
    }

    /**
     * Sets the ExtentValue attribute of the TemplateSubTask object
     *
     * @param extent  The new ExtentValue value
     */
    protected void setExtentValue(String extent)
    {
        this.extent = extent;
    }

    /**
     * Describe what the method does
     *
     * @param templateURL            Describe what the parameter does
     * @param outputFile             Describe what the parameter does
     * @exception TemplateException  Describe the exception
     */
    protected final void startEngine(URL templateURL, File outputFile) throws TemplateException
    {
        engineStarted();

        getEngine().setOutput(outputFile);
        getEngine().setTemplateURL(templateURL);

        getEngine().start();

        engineFinished();
    }

    protected void addOfType(String ofType)
    {
        OfType new_oftype = new OfType();

        new_oftype.setType(ofType);

        this.ofType.add(new_oftype);
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void startProcess() throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "startProcess");

        if (log.isDebugEnabled()) {
            log.debug("docEncoding=" + docEncoding);
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
        else {
            startProcessForAll();
        }
    }

    protected void startProcessForAll() throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "startProcessForAll");

        if (log.isDebugEnabled()) {
            log.debug("Not per class.");
        }

        getEngine().setTemplateURL(getTemplateURL());

        File outputFile = new File(getDestDir(), getDestinationFile());

        try {
            setGenerationManager(new GenerationManager(getXJavaDoc(), this));
            if (generationManager.isGenerationNeeded(outputFile)) {
                startEngine(getTemplateURL(), outputFile);
            }
        }
        catch (TemplateException e) {
            if (e instanceof XDocletException) {
                throw (XDocletException) e;
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("startProcess()");
                }
                throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
            }
        }
    }

    /**
     * Subclasses should override this method and return true if they want startProcessPerClass() to process inner
     * classes too.
     *
     * @return
     */
    protected boolean processInnerClasses()
    {
        return false;
    }

    protected void startProcessPerClass() throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "startProcessPerClass");

        if (log.isDebugEnabled()) {
            log.debug("Per class.");
        }

        /*
         * loop over all classes. false->use readonly classes. subtask decides whether inner classes
         * should be processed too.
         * ideally, getSourceClasses should always return inner classes too (with no optional inner argument),
         * but we have to think about bwc too.
         */
        Collection classes = getXJavaDoc().getSourceClasses(false, processInnerClasses());

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);
            if (matchesGenerationRules(clazz)) {
                if (log.isDebugEnabled()) {
                    log.debug("Working on " + clazz);
                }
                generateForClass(clazz);
            }
        }
    }

    /**
     * Returns true if output not already generated for clazz, and is of the specified type and has the specified class
     * tag; false otherwise. If returned false, no output file is generated for clazz.
     *
     * @param clazz                 Description of Parameter
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "matchesGenerationRules");

        // skip inner classes
        if (clazz.getContainingClass() != null && !processInnerClasses()) {
            return false;
        }

        // skip automatically generated classes
        if (isDocletGenerated(clazz)) {
            if (log.isDebugEnabled()) {
                log.debug("Reject file '" + clazz.getQualifiedName() + "' because it is a doclet generated one.");
            }
            return false;
        }

        if (acceptInterfaces == false && clazz.isInterface()) {
            if (log.isDebugEnabled()) {
                log.debug("Reject file '" + clazz.getQualifiedName() + "' because it is an interface and acceptInterfaces=false.");
            }
            return false;
        }

        if (acceptAbstractClasses == false && clazz.isAbstract()) {
            if (log.isDebugEnabled()) {
                log.debug("Reject file '" + clazz.getQualifiedName() + "' because it is an abstract class and acceptAbstractClasses=false.");
            }
            return false;
        }

        if (ofType.size() > 0 && classIsntOfOneOfTheTypes(clazz, log))
            return false;

        if (getHavingClassTag() != null) {
            if (!clazz.getDoc().hasTag(getHavingClassTag(), false)) {
                if (log.isDebugEnabled()) {
                    log.debug("Reject class '" + clazz.getQualifiedName() + "' because it doesn't have class tag '" + getHavingClassTag() + "'.");
                }
                return false;
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("Accept class '" + clazz.getQualifiedName() + "' because it has class tag '" + getHavingClassTag() + "'.");
                }
            }
        }

        return true;
    }

    /**
     * Processed template for clazz and generates output file for clazz.
     *
     * @param clazz                 Description of Parameter
     * @exception XDocletException  Description of Exception
     */
    protected void generateForClass(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "generateForClass");

        File file = new File(getDestDir().toString(), getGeneratedFileName(clazz));

        if (log.isDebugEnabled()) {
            log.debug("destDir.toString()=" + getDestDir().toString());
            log.debug("getGeneratedFileName()=" + getGeneratedFileName(clazz));
            log.debug("file=" + file);
        }

        if (file.exists()) {
            log.debug("File exists.");

            // Check modification timestamps
            setGenerationManager(new GenerationManager(getXJavaDoc(), this));

            boolean isGenerationNeeded = generationManager.isGenerationNeeded(clazz, file, true);

            if (!isGenerationNeeded) {
                return;
            }
        }

        file.getParentFile().mkdirs();

        try {
            setCurrentPackage(clazz.getContainingPackage());
            setCurrentClass(clazz);
            startEngine(getTemplateURL(), new File(getDestDir(), getGeneratedFileName(clazz)));
        }
        catch (TemplateException e) {
            if (e instanceof XDocletException) {
                throw (XDocletException) e;
            }
            else {
                log.debug("generateForClass()");
                e.printStackTrace();
                throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
        Log log = LogUtil.getLog(TemplateSubTask.class, "engineStarted");

        if (isGenerationPerClass()) {
            log.info(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_TEMPLATE_OUTPUT_PER_CLASS, new String[]{getCurrentClass().getQualifiedName(), getTemplateURL().toString()}));
        }
        else {
            log.info(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_TEMPLATE_OUTPUT_SINGLE_FILE, new String[]{getDestinationFile(), getTemplateURL().toString()}));
        }

        if (log.isDebugEnabled()) {
            log.debug("getCurrentClass()=" + getCurrentClass());
            log.debug("isGenerationPerClass()=" + isGenerationPerClass());
        }
    }

    /**
     * Describe what the method does
     *
     * @exception TemplateException  Describe the exception
     */
    protected void engineFinished() throws TemplateException
    {
    }

    /**
     * Gets the GenerationPerClass attribute of the TemplateSubTask object
     *
     * @return   The GenerationPerClass value
     */
    private boolean isGenerationPerClass()
    {
        return getDestinationFile().indexOf("{0}") != -1;
    }

    private boolean classIsntOfOneOfTheTypes(XClass clazz, Log log)
    {
        boolean match = false;
        String type = null;
        int extent_value = TypeTagsHandler.extractExtentType(getExtent());

        for (Iterator it = ofType.iterator(); it.hasNext(); ) {
            type = ((OfType) it.next()).getType();

            if (TypeTagsHandler.isOfType(clazz, type, extent_value) == true) {

                match = true;
                break;
            }
        }

        if (!match) {
            if (log.isDebugEnabled()) {
                log.debug("Reject class '" + clazz.getQualifiedName() + "' because it is not of the specified types according to extent='" + getExtent() + "'.");
            }

            return true;
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Accept class '" + clazz.getQualifiedName() + "' because it is of type '" + type + "'.");
            }

            return false;
        }
    }

    /**
     * @author    Ara Abrahamian (ara_e@email.com)
     * @created   July 19, 2001
     */
    public final static class ExtentTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String EXTENT_TYPE_CONCRETE_TYPE = "concrete-type";
        public final static String EXTENT_TYPE_SUPERCLASS = "superclass";
        public final static String EXTENT_TYPE_HIERARCHY = "hierarchy";

        public java.lang.String[] getValues()
        {
            return (new java.lang.String[]{EXTENT_TYPE_CONCRETE_TYPE, EXTENT_TYPE_SUPERCLASS, EXTENT_TYPE_HIERARCHY});
        }
    }

    /**
     * Applicable only to per class output generation. Specify full qualified class name from which classes should be
     * derived. Output generation is performed only for classes of one of the types defined in this property, it's a
     * comma-separated list. You can control the extent in which the type search occurs by using 'extent' property.
     *
     * @created   10. september 2002
     */
    public final static class OfType
    {
        private String type;

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
    }
}
