/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import xjavadoc.XJavaDoc;
import xdoclet.loader.*;

import xdoclet.util.FileManager;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * The default template engine used by derived SubTasks. It looks for XML-ish <XDoclet:blabla> strings, just like JSP
 * tag libraries. There's no support for scriptlets, because it's cleaner to use and implementing the XML-ish tags is
 * easy, they are not heavyweight like jsp taglib implementations and there's no need for something like taglib.tld.
 *
 * @author    Rickard Oberg (rickard@dreambean.com)
 * @author    Ara Abrahamian (ara_e@email.com)
 * @author    Dmitri Colebatch (dim@bigpond.net.au)
 * @created   July 14, 2001
 * @version   $Revision: 1.36 $
 * @see       #generate(java.lang.String)
 */
public class TemplateEngine
{
    public final static String TAG_MAPPINGS_FILE = "/tagmappings.properties";
    protected static String XDOCLET_PREFIX;
    protected static String XDOCLET_HEAD;
    protected static String XDOCLET_TAIL;
    protected static int XDOCLET_HEAD_LEN;
    protected static int XDOCLET_TAIL_LEN;
    private static TemplateEngine instance = new TemplateEngine();

    /**
     * The PrintWriter used for outputing the generated stuff. {@link xdoclet.template.PrettyPrintWriter} tries to
     * pretty format the generated file by removing redundant spaces/lines.
     *
     * @see   xdoclet.template.PrettyPrintWriter
     */
    protected transient PrettyPrintWriter out;

    protected transient File output = null;

    protected transient String docEncoding = null;

    /**
     * The template file currently being processed.
     */
    private transient URL templateURL = null;

    /**
     * Where we are in the template file. Used for reporting errors.
     */
    private transient int currentLineNum = 0;

    /**
     * The map of tag mappings. String => TemplateTagHandler
     */
    private Map     tagMappings = new HashMap();

    private XJavaDoc _xJavaDoc;

    static {
        XDOCLET_PREFIX = "XD";

        // migration path from "XDoclet:" to "XDt"

        XDOCLET_HEAD = "<" + XDOCLET_PREFIX;
        XDOCLET_TAIL = "</" + XDOCLET_PREFIX;
        XDOCLET_HEAD_LEN = XDOCLET_HEAD.length();
        XDOCLET_TAIL_LEN = XDOCLET_TAIL.length();
    }

    protected TemplateEngine()
    {
        registerTagHandlers();
    }

    /**
     * Gets the EngineInstance attribute of the TemplateEngine class
     *
     * @return   The EngineInstance value
     */
    public static TemplateEngine getEngineInstance()
    {
        return instance;
    }

    /**
     * Skips whitespaces, starting from index i till the first non-whitespace character or end of template and returns
     * the new index.
     *
     * @param template  Description of Parameter
     * @param i         Description of Parameter
     * @return          Description of the Returned Value
     */
    public static int skipWhitespace(String template, int i)
    {
        while (i < template.length() && Character.isWhitespace(template.charAt(i))) {
            i++;
        }

        return i;
    }

    /**
     * Loops over the template content till reaching tillIndex index and returns the number of lines it has encountered.
     *
     * @param template   Description of Parameter
     * @param tillIndex  Description of Parameter
     * @return           The LineNumber value
     */
    protected static int getLineNumber(String template, int tillIndex)
    {
        int NL_LEN = PrettyPrintWriter.LINE_SEPARATOR.length();
        int index = 0;
        int lineNumber = 0;

        do {
            index = template.indexOf(PrettyPrintWriter.LINE_SEPARATOR, index);

            if (index != -1) {
                lineNumber++;
                index += NL_LEN;
            }
            else {
                break;
            }
        } while (index < tillIndex);

        return lineNumber;
    }

    /**
     * Returns current template URL.
     *
     * @return   The TemplateURL value
     * @see      #setTemplateURL(java.net.URL)
     */
    public URL getTemplateURL()
    {
        return templateURL;
    }

    /**
     * Gets the Output attribute of the TemplateEngine object
     *
     * @return   The Output value
     */
    public File getOutput()
    {
        return output;
    }

    /**
     * Gets the CurrentLineNum attribute of the TemplateEngine object
     *
     * @return   The CurrentLineNum value
     */
    public int getCurrentLineNum()
    {
        return currentLineNum;
    }

    /**
     * Get the tag handler for the prefix.
     *
     * @param prefix              The prefix that the tag handler is mapped to
     * @return                    The TemplateTagHandler for the specified prefix. ALways non-null.
     * @throws TemplateException  If there is no tag handler class for the prefix specified.
     */
    public TemplateTagHandler getTagHandlerFor(String prefix) throws TemplateException
    {
        Log log = LogUtil.getLog(TemplateEngine.class, "getTagHandlerFor");

        TemplateTagHandler tagHandler = (TemplateTagHandler) tagMappings.get(prefix);

        if (log.isDebugEnabled()) {
            log.debug("prefix=" + prefix);
            log.debug("tagHandler=" + tagHandler);
        }

        if (tagHandler == null) {
            String msg = Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_NO_TAGHANDLER, new String[]{"'XDt" + prefix + "'"});

            log.error(msg);
            throw new TemplateException(msg);
        }

        return tagHandler;
    }

    /**
     * Returns a Set of Strings that represent the registered namespaces
     *
     * @return
     */
    public Set getNamespaces()
    {
        return tagMappings.keySet();
    }

    public void setXJavaDoc(XJavaDoc xJavaDoc)
    {
        _xJavaDoc = xJavaDoc;
        TemplateTagHandler.setXJavaDoc(_xJavaDoc);
    }

    /**
     * Sets the Writer attribute of the TemplateEngine object
     *
     * @param out  The new Writer value
     */
    public void setWriter(PrettyPrintWriter out)
    {
        this.out = out;
    }

    /**
     * Sets the CurrentLineNum attribute of the TemplateEngine object
     *
     * @param currentLineNum  The new CurrentLineNum value
     */
    public void setCurrentLineNum(int currentLineNum)
    {
        this.currentLineNum = currentLineNum;
    }

    /**
     * A config parameter settable from Ant build file. It sets the current template file to templateURL, so thereafter
     * the new template file is used.
     *
     * @param templateURL  The new TemplateFile value
     * @see                #getTemplateURL()
     */
    public void setTemplateURL(URL templateURL)
    {
        this.templateURL = templateURL;
    }

    /**
     * Sets the Output attribute of the TemplateEngine object
     *
     * @param output  The new Output value
     */
    public void setOutput(File output)
    {
        this.output = output;
    }

    /**
     * Sets the TagHandlerFor attribute of the TemplateEngine object
     *
     * @param prefix                 The new TagHandlerFor value
     * @param tagHandler             The new TagHandlerFor value
     * @exception TemplateException  Describe the exception
     */
    public void setTagHandlerFor(String prefix, TemplateTagHandler tagHandler) throws TemplateException
    {
        Log log = LogUtil.getLog(TemplateEngine.class, "setTagHandlerFor");

        if (log.isDebugEnabled()) {
            log.debug("prefix=" + prefix);
            log.debug("tagHandler=" + tagHandler);
        }

        tagMappings.put(prefix, tagHandler);
    }

    /**
     * set output charset;
     *
     * @param string
     */
    public void setDocEncoding(String string)
    {
        docEncoding = string;
    }

    /**
     * Describe what the method does
     *
     * @param output  Describe what the parameter does
     */
    public final void print(String output)
    {
        if (out != null) {
            out.print(output);
            out.flush();
        }
    }

    /**
     * The main template parsing/processing/running logic. It searches for <XDoclet: string, parses what is found after
     * it till a matching </XDoclet: is found in case of a block tag, or till a /> is found in case of a content tag. It
     * automatically calls the relevent tag implementation method with the correct parameters. If a block tag, then the
     * tag implementation accepts two parameters, the body of the block tag as a string and a Properties object
     * containing all attributes. Note that if the tag doesn't have any attributes the corresponding tag implementation
     * typically only accepts a single string value denoting the block body, though it can also accept a Properties as
     * the second parameter. Tags that may or may not have attributes can safely accept the second Properties object,
     * which will be filled either by nothing or by all the given attributes. Content tag implementation methods have no
     * parameter but should return a String containing the result that should be printed to the generated file. XTag
     * implementation methods should define and throw org.apache.tools.ant.TemplateException if any serious error
     * occurs.
     *
     * @param template               Description of Parameter
     * @exception TemplateException  Description of Exception
     * @see                          #outputOf(java.lang.String)
     */
    public void generate(final String template) throws TemplateException
    {
        int index = 0;
        int prevIndex = 0;

        while (true) {
            // Look for the next tag that we haven't yet handled.
            index = template.indexOf(XDOCLET_HEAD, prevIndex);

            if (index == -1) {
                // we didn't find a tag! print the rest of the template and finish
                print(template.substring(prevIndex));
                break;
            }
            else {
                // we found a tag! print the template up to the tag start, and then handle the tag
                print(template.substring(prevIndex, index));
                prevIndex = handleTag(index, template);
            }
        }
    }

    /**
     * Calls generate() of the specified template content but instead of outputing it to the generated file, it returns
     * the generated content. It's useful for cases where you want to synthesize the result but use it instead of
     * roughly outputing it, for example it's used for the content tags nested inside an attribute value such as:
     * <XDoclet:blabla param1=" <XDoclet:aContentTag/>"/> where we obviously don't want to output the result of
     * aContentTag but use it as the value of the param1 parameter.
     *
     * @param template               Description of Parameter
     * @return                       Description of the Returned Value
     * @exception TemplateException  Description of Exception
     * @see                          #generate(java.lang.String)
     */
    public String outputOf(String template) throws TemplateException
    {
        PrettyPrintWriter oldOut = out;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        out = new PrettyPrintWriter(bout);
        generate(template);
        out.close();
        out = oldOut;

        return new String(bout.toByteArray());
    }

    /**
     * A utility method used for generating the dest_file based on template_file template file.
     *
     * @exception TemplateException  Description of Exception
     */
    public void start() throws TemplateException
    {
        Log log = LogUtil.getLog(TemplateEngine.class, "start");

        output.getParentFile().mkdirs();

        String content = FileManager.getURLContent(getTemplateURL());

        if (content != null) {
            try {
                String encoding = docEncoding;

                if (encoding == null) {
                    encoding = _xJavaDoc.getDocEncoding();
                }

                PrettyPrintWriter out = null;

                if (encoding == null) {
                    out = new PrettyPrintWriter(
                        new BufferedWriter(new FileWriter(output)));
                }
                else {
                    out = new PrettyPrintWriter(
                        new BufferedWriter(
                        new OutputStreamWriter(
                        new FileOutputStream(output),
                        encoding)));
                }

                setWriter(out);
                setCurrentLineNum(0);
                generate(content);
                out.close();
            }
            catch (IOException ex) {
                String msg = Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_ERROR_WRITING_OUTPUT, new String[]{output.toString()});

                log.error(msg, ex);
                throw new TemplateException(ex, msg);
            }
        }
        else {
            String msg = Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_NOT_FOUND, new String[]{getTemplateURL().toString()});

            log.error(msg);
            throw new TemplateException(msg);
        }
    }

    /**
     * Handle the tag that starts at <code>index</code> in the <code>template</code> provided.
     *
     * @param index                  The index that the tag to handle starts at.
     * @param template               The template the tag is in.
     * @return                       The index where the tag finished.
     * @exception TemplateException  Description of Exception
     */
    protected int handleTag(int index, final String template) throws TemplateException
    {
        // the point in the template where we are.  starts at the end of the tag head.
        int i = index + XDOCLET_HEAD_LEN;

        // the command name (tag name)
        StringBuffer cmd = new StringBuffer();

        // tag context (are there more attributes? is it a block or content tag?)
        TagContext tagContext = new TagContext();

        // any attributes the tag has)
        Properties attributes = new Properties();

        // extract the tag name, keeping track of where we are in the template
        i = extractTagName(template, i, cmd);

        // log.debug( "Found " + cmd.toString() + ", line=" + getLineNumber( template, i ) + " of template file: " + getTemplateURL() );

        // do an initial parse of the tag
        i = doInitialTagParse(template, i, tagContext);

        // extract the attributes from the tag, and determine if it is a block or content tag
        i = extractAttributes(tagContext, template, i, attributes);

        if (tagContext.isBlock()) {
            i = handleBlockTag(i, template, cmd.toString(), attributes);
        }
        else {
            invokeContentMethod(cmd.toString(), attributes, template, i);
        }

        return i;
    }

    /**
     * Invokes content tag implementation method named cmd. It first tries with parameters params1, if not successful
     * tries param2. This is used for cases where it's not obvious whether the tag implementation method expects a
     * Properties object or no parameter at all (the tag takes no attributes).
     *
     * @param cmd                    The command to be executed. Everything after the <code>&lt;XDoclet:</code> in the
     *      template.
     * @param params1                Description of Parameter
     * @param params2                Description of Parameter
     * @param template               Description of Parameter
     * @param i                      Description of Parameter
     * @return                       Description of the Returned Value
     * @exception TemplateException  Description of Exception
     * @see                          #invokeBlockMethod(java.lang.String,java.lang.String,java.util.Properties,java.lang.String,int)
     * @see                          #invokeContentMethod(java.lang.String,java.util.Properties,java.lang.String,int)
     */
    protected Object invokeMethod(String cmd, Object[] params1, Object[] params2, String template, int i) throws TemplateException
    {
        Log log = LogUtil.getLog(TemplateEngine.class, "invokeMethod");

        /*
         * During refactoring accept two states:
         * <XDoclet:foo>     will look for the method "foo" in the current class
         * in this case cmd == "oclet:foo"
         * <XDtBar:foo>      will look for the method "foo" in the class mapped as "Bar"
         * in this case cmd == "tBar:foo"
         */
        int colon = cmd.indexOf(':');

        if (colon < 0) {
            log.error("Invoking method failed: XD" + cmd + " is not valid because it does not contain ':' , line=" + getLineNumber(template, i) + " of template file: " + getTemplateURL());
            throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_INVOKE_METHOD_FAILED,
                new String[]{"XD" + cmd, Integer.toString(getLineNumber(template, i)), getTemplateURL().toString()}));
        }

        String prefix = cmd.substring(0, colon);
        String methodName = cmd.substring(colon + 1);

        TemplateTagHandler cmdImplProvider;

        try {
            cmdImplProvider = getTagHandlerFor(prefix.substring(1));
        }
        catch (TemplateException e) {
            log.error("Error occured at/around line " + getLineNumber(template, i) + ", offending template tag: XD" + cmd);
            throw e;
        }

        String className = cmdImplProvider.getClass().getName();

        try {
            Class[] paramTypes = new Class[params1.length];

            for (int j = 0; j < params1.length; j++) {
                paramTypes[j] = params1[j].getClass();
            }

            Method m = cmdImplProvider.getClass().getMethod(methodName, paramTypes);

            return invoke(m, cmdImplProvider, params1);
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof TemplateException) {
                throw (TemplateException) e.getTargetException();
            }
            else {
                log.error("Invoking method failed: " + className + '.' + methodName + ", line=" + getLineNumber(template, i) + " of template file: " + getTemplateURL(), e);
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_INVOKE_METHOD_FAILED,
                    new String[]{className, methodName, Integer.toString(getLineNumber(template, i)), getTemplateURL().toString(), e.getMessage()}));
            }
        }
        catch (IllegalAccessException e) {
            log.error("Invoking method failed: " + className + '.' + methodName + ", line=" + getLineNumber(template, i) + " of template file: " + getTemplateURL(), e);
            throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_INVOKE_METHOD_FAILED,
                new String[]{className, methodName, Integer.toString(getLineNumber(template, i)), getTemplateURL().toString(), e.getMessage()}));
        }
        catch (NoSuchMethodException e) {
            Class[] paramTypes = new Class[params2.length];

            try {
                for (int j = 0; j < params2.length; j++) {
                    paramTypes[j] = params2[j].getClass();
                }

                Method m = cmdImplProvider.getClass().getMethod(methodName, paramTypes);

                return invoke(m, cmdImplProvider, params2);
            }
            catch (NoSuchMethodException nsme) {
                log.error("Could not find method " + className + '.' + methodName + " in class " + cmdImplProvider.getClass().getName());
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_NO_SUCH_METHOD,
                    new String[]{methodName, cmdImplProvider.getClass().getName(), nsme.getMessage()}));
            }
            catch (InvocationTargetException e2) {
                if (e2.getTargetException() instanceof TemplateException) {
                    throw (TemplateException) e2.getTargetException();
                }
                else {
                    log.error("Invoking method failed: " + className + '.' + methodName + ", line=" + getLineNumber(template, i) + " of template file: " + getTemplateURL(), e2);
                    throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_INVOKE_METHOD_FAILED,
                        new String[]{className, methodName, Integer.toString(getLineNumber(template, i)), getTemplateURL().toString(), e2.getMessage()}));
                }
            }
            catch (IllegalAccessException e2) {
                log.error("Invoking method failed: " + className + '.' + methodName + ", line=" + getLineNumber(template, i) + " of template file: " + getTemplateURL(), e2);
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_INVOKE_METHOD_FAILED,
                    new String[]{className, methodName, Integer.toString(getLineNumber(template, i)), getTemplateURL().toString(), e2.getMessage()}));
            }
        }
    }

    /**
     * Invokes content tag implementation method named cmd with the specified set of attributes. If attributes
     * Properties object is not empty it tries to find a method taking one parameter (Properties attributes), otherwise
     * a method with no parameter.
     *
     * @param cmd                    Description of Parameter
     * @param attributes             Description of Parameter
     * @param template               Description of Parameter
     * @param i                      Description of Parameter
     * @exception TemplateException  Description of Exception
     * @see                          #invokeMethod(java.lang.String,java.lang.Object[],java.lang.Object[],java.lang.String,int)
     */
    protected void invokeContentMethod(String cmd, Properties attributes, String template, int i) throws TemplateException
    {
        Object[] params1 = null;
        Object[] params2 = null;

        // probable conditions

        if (attributes.size() > 0) {
            params1 = new Object[]{attributes};
            params2 = new Object[]{};
        }
        else {
            params1 = new Object[]{};
            params2 = new Object[]{attributes};
        }

        String result = (String) invokeMethod(cmd, params1, params2, template, i);

        if (result != null && out != null) {
            print(result);
        }
    }

    /**
     * Describe what the method does
     *
     * @param m                              Describe what the parameter does
     * @param cmdImplProvider                Describe what the parameter does
     * @param params1                        Describe what the parameter does
     * @return                               Describe the return value
     * @exception InvocationTargetException  Describe the exception
     * @exception IllegalAccessException     Describe the exception
     * @exception TemplateException          Describe the exception
     */
    protected Object invoke(Method m, Object cmdImplProvider, Object[] params1)
         throws InvocationTargetException, IllegalAccessException, TemplateException
    {
        return m.invoke(cmdImplProvider, params1);
    }

    private void registerTagHandlers()
    {
        Log log = LogUtil.getLog(TemplateEngine.class, "registerTagHandlers");
        List modules = ModuleFinder.findModules();
        Iterator i = modules.iterator();

        while (i.hasNext()) {
            XDocletModule module = (XDocletModule) i.next();

            log.debug("Registering module " + module);

            // Register tag handlers defined in the module
            List tagHandlerDefinitions = module.getTagHandlerDefinitions();
            Iterator k = tagHandlerDefinitions.iterator();

            while (k.hasNext()) {
                TagHandlerDefinition thd = (TagHandlerDefinition) k.next();

                log.debug("Registering tag handler " + thd.namespace);

                try {
                    TemplateTagHandler handler = (TemplateTagHandler) Class.forName(thd.className).newInstance();

                    // get ANOTHER instance for template parser. So it would not overwrite
                    // template engine settings.
//                    TemplateTagHandler handlerForParser = (TemplateTagHandler) Class.forName(thd.className).newInstance();

                    log.debug("Add tagHandler " + thd.namespace + " (" + thd.className + ')');
                    setTagHandlerFor(thd.namespace, handler);
                    // add it also to template parser, or it will barf
                    // while ettempting to parse files...
                    //TemplateParser.getParserInstance().setTagHandlerFor(thd.namespace, handlerForParser);

                }
                catch (Exception e) {
                    log.error("Couldn't instantiate " + thd.className + " taghandler ", e);
                }
            }
        }
    }

    /**
     * Extract the attributes from the tag and return the index that the tag
     *
     * @param tagContext             Description of Parameter
     * @param template               Description of Parameter
     * @param i                      Description of Parameter
     * @param attributes             Description of Parameter
     * @return                       Description of the Returned Value
     * @exception TemplateException  Description of Exception
     */
    private int extractAttributes(TagContext tagContext, final String template, int i, Properties attributes) throws TemplateException
    {
        while (tagContext.hasMoreAttributes()) {
            i = extractNextAttribute(template, i, tagContext, attributes);
        }

        return i;
    }

    /**
     * Extract the name of the tag starting at index <code>i</code> from the specified <code>template</code> .
     *
     * @param template  The template containing the tag.
     * @param index     The index that the tag is at in the template.
     * @param cmd       The StringBuffer to put the tag name in.
     * @return          The index that the tag name finished at in the template.
     */
    private int extractTagName(final String template, int index, StringBuffer cmd)
    {
        while ((!Character.isWhitespace(template.charAt(index))) && template.charAt(index) != '>' && template.charAt(index) != '/') {
            cmd.append(template.charAt(index));
            index++;
        }

        return index;
    }

    /**
     * Extract the next attribute from the <code>template</code> provided, starting at the <code>index</code> specified.
     *
     * @param template               The template to extract the attribute from.
     * @param index                  The index to start looking for the attribute.
     * @param tagContext             The TagContext - is the tag block/content, are there any more attributes?
     * @param attributes             The attributes collection to add the next attribute to.
     * @return                       The index that the next attribute finished at.
     * @exception TemplateException  Description of Exception
     */
    private int extractNextAttribute(final String template, int index, TagContext tagContext, Properties attributes) throws TemplateException
    {
        StringBuffer attributeName = new StringBuffer();
        StringBuffer attributeValue = new StringBuffer();
        char quoteChar = '"';

        try {
            // read attribute name
            while (template.charAt(index) != '=' && (!Character.isWhitespace(template.charAt(index)))) {
                attributeName.append(template.charAt(index));
                index++;
            }

            index = skipWhitespace(template, index);

            // skip = sign
            if (template.charAt(index) == '=') {
                index++;
            }
            else {
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_EQUALS_EXPECTED, new String[]{Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
            }

            index = skipWhitespace(template, index);

            // skip " sign
            if (template.charAt(index) == '"') {
                index++;
                quoteChar = '"';
            }
            else if (template.charAt(index) == '\'') {
                index++;
                quoteChar = '\'';
            }
            else {
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_QUOTE_EXPECTED, new String[]{Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
            }

            // read attribute value
            while (template.charAt(index) != quoteChar) {
                attributeValue.append(template.charAt(index));
                index++;
            }

            // skip " sign
            index++;
            tagContext.setHasMoreAttributes(true);

            if (attributeValue.toString().indexOf(XDOCLET_HEAD) != -1) {
                attributeValue = new StringBuffer(outputOf(attributeValue.toString()));
            }

            index = skipWhitespace(template, index);

            if (template.charAt(index) == '>') {
                index++;
                tagContext.setBlock(true);
                tagContext.setHasMoreAttributes(false);

                // no more attributes
            }
            else if (template.charAt(index) == '/') {
                index++;

                if (template.charAt(index) == '>') {
                    index++;
                    tagContext.setBlock(false);
                    tagContext.setHasMoreAttributes(false);

                    // no more attributes
                }
                else {
                    throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_GT_EXPECTED, new String[]{Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
                }
            }
        }
        catch (java.lang.StringIndexOutOfBoundsException ex) {
            throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_SYNTAX_ERROR, new String[]{Integer.toString(getLineNumber(template, index)), getTemplateURL().toString(), template}));
        }
        attributes.setProperty(attributeName.toString(), attributeValue.toString());
        return index;
    }

    /**
     * Do an initial parse of the tag at the <code>index</code> specified in the <code>template</code> provided. Look to
     * see if the tag has attributes, and/or is block or content.
     *
     * @param template               The template the tag is contained in.
     * @param index                  The index the tag starts at.
     * @param tagContext             The tag context.
     * @return                       Description of the Returned Value
     * @exception TemplateException  Description of Exception
     */
    private int doInitialTagParse(final String template, int index, TagContext tagContext) throws TemplateException
    {
        while (true) {
            if (template.charAt(index) == '>') {
                index++;
                tagContext.setHasMoreAttributes(false);
                tagContext.setBlock(true);
                return index;
            }
            else if (template.charAt(index) == '/') {
                index++;

                if (template.charAt(index) == '>') {
                    index++;
                    tagContext.setHasMoreAttributes(false);
                    tagContext.setBlock(false);
                    return index;
                }
                else {
                    throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_GT_EXPECTED, new String[]{Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
                }
            }
            else if (Character.isWhitespace(template.charAt(index))) {
                index = skipWhitespace(template, index);
                continue;
            }
            else {
                tagContext.setHasMoreAttributes(true);
                return index;
            }
        }
    }

    /**
     * Handle the block tag starting at the <code>index</code> specified in the <code>template</code> provided.
     *
     * @param index                  The start of the block tag's contents.
     * @param template               The template the block tag is contained in.
     * @param cmd                    The name of the block tag, without XDOCLET_HEAD
     * @param attributes             The attributes in the tag.
     * @return                       The index that the block tag finishes at.
     * @exception TemplateException  Description of Exception
     */
    private int handleBlockTag(int index, String template, String cmd, Properties attributes) throws TemplateException
    {
        int openNestedElemCount = 1;
        String newBody = null;
        int bodyStartIndex = index;
        int bodyEndIndex = -1;

        while (index < template.length()) {
            int fromIndex = index;

            bodyEndIndex = template.indexOf(new StringBuffer(XDOCLET_TAIL).append(cmd).toString(), index);

            if (bodyEndIndex == -1) {
                throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_CLOSE_TAG_MISSING,
                    new String[]{new StringBuffer(XDOCLET_TAIL).append(cmd).append('>').toString(), Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
            }
            else {
                openNestedElemCount--;
            }

            // </XDoclet:cmd
            index = bodyEndIndex + XDOCLET_TAIL_LEN + cmd.length();

            // skip spaces in </XDoclet:cmd   >
            index = skipWhitespace(template, index);

            // trailing >
            index++;

            StringBuffer xdocletPrefixPlusCmd = new StringBuffer(XDOCLET_PREFIX).append(cmd);

            // XXX: I cant remember why I did this like that (using prefix instead of head), it could
            // probably be looked at, but there are other things to do atm, and it works.  dim 13-Oct-01
            int nestedStartIndex = template.indexOf(xdocletPrefixPlusCmd.toString(), fromIndex);

            // has nested elements with the same name, need to loop here for multiple nests.
            while (nestedStartIndex != -1 && nestedStartIndex < bodyEndIndex) {
                // <XDoclet:blabla ...>
                if (template.charAt(nestedStartIndex - 1) == '<') {
                    openNestedElemCount++;
                }
                else {
                    throw new TemplateException(Translator.getString(XDocletTemplateMessages.class, XDocletTemplateMessages.TEMPLATE_CORRESPONDING_TAG_MISSING,
                        new String[]{new StringBuffer(XDOCLET_TAIL).append(cmd).append('>').toString(), Integer.toString(getLineNumber(template, index)), getTemplateURL().toString()}));
                }

                nestedStartIndex = template.indexOf(xdocletPrefixPlusCmd.toString(), nestedStartIndex + 1);
            }

            if (openNestedElemCount == 0) {
                break;
            }
        }

        newBody = template.substring(bodyStartIndex, bodyEndIndex);

        int previousLineNum = currentLineNum;
        int localBodyLineNum = getLineNumber(template, bodyStartIndex);

        currentLineNum += localBodyLineNum;
        if (previousLineNum > 0) {
            currentLineNum--;
        }

        invokeBlockMethod(cmd, newBody, attributes, template, index);

        currentLineNum = previousLineNum;
        return index;
    }

    /**
     * Invokes block tag implementation method named cmd with the specified body block and set of attributes.
     *
     * @param cmd                    Description of Parameter
     * @param block                  Description of Parameter
     * @param attributes             Description of Parameter
     * @param template               Description of Parameter
     * @param i                      Description of Parameter
     * @exception TemplateException  Description of Exception
     * @see                          #invokeMethod(java.lang.String,java.lang.Object[],java.lang.Object[],java.lang.String,int)
     */
    private void invokeBlockMethod(String cmd, String block, Properties attributes, String template, int i) throws TemplateException
    {
        Object[] params1 = null;
        Object[] params2 = null;

        // probable conditions

        if (attributes.size() > 0) {
            params1 = new Object[]{block, attributes};
            params2 = new Object[]{block};
        }
        else {
            params1 = new Object[]{block};
            params2 = new Object[]{block, attributes};
        }

        invokeMethod(cmd, params1, params2, template, i);
    }

    /**
     * A wrapper around the hasMoreAttributes and isBlock status used in {@link TemplateEngine#generate}.
     *
     * @author    Aslak Hellesøy
     * @created   October 14, 2001
     */
    private static class TagContext
    {
        private boolean hasMoreAttributes = false;
        private boolean isBlock = false;

        /**
         * Gets the Block attribute of the TagContext object
         *
         * @return   The Block value
         */
        public boolean isBlock()
        {
            return isBlock;
        }

        /**
         * Sets the HasMoreAttributes attribute of the TagContext object
         *
         * @param attributes  The new HasMoreAttributes value
         */
        public void setHasMoreAttributes(boolean attributes)
        {
            this.hasMoreAttributes = attributes;
        }

        /**
         * Sets the Block attribute of the TagContext object
         *
         * @param block  The new Block value
         */
        public void setBlock(boolean block)
        {
            isBlock = block;
        }

        /**
         * Describe what the method does
         *
         * @return   Describe the return value
         */
        public boolean hasMoreAttributes()
        {
            return hasMoreAttributes;
        }
    }
}
