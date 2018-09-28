/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc;

import java.beans.Introspector;

import java.util.*;

import xjavadoc.*;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.template.TemplateEngine;
import xdoclet.template.TemplateException;

/**
 * Generates ant docs. Introspects classes and looks for nested elements. Generates one single file for each element.
 * All generated element docs also links nested elements' docs.
 *
 * @author        Aslak Hellesøy
 * @created       21. juli 2002
 * @ant.element   display-name="Ant documentation" name="antdocs" parent="xdoclet.DocletTask"
 * @todo          use DocletTask as parent instead. should be enough.
 * @version       $Revision: 1.17 $
 */
public class AntdocSubTask extends TemplateSubTask
{
    private static String ANTDOC_TEMPLATE_FILE = "resources/antdoc-xdoc.xdt";

    /**
     * Usually, one class corresponds to only one XML element, but in some rare cases it might correspond to more. This
     * is if the same class is used in e.g. several create methods: <code>Gee createFoo()</code>, <code>Gee createBar()</code>
     * <p>
     *
     * The Gee class corresponds to &lt;foo&gt; AND &lt;bar&gt;. This map's values are Sets containing AntElements.</p>
     */
    protected final Map classToAntElementMap = new HashMap();

    private final Set alreadyRecursed = new HashSet();

    public String getDestinationFile()
    {
        return "{0}.xml";
    }

    public void init(XJavaDoc xJavaDoc) throws XDocletException
    {
        super.init(xJavaDoc);

        if (getTemplateURL() == null) {
            setTemplateURL(getClass().getResource(ANTDOC_TEMPLATE_FILE));
        }

        // Discover the top elements
        discoverTasks();

        // Link from bottom and up. Dynamic elements know who their parents are
        discoverDynamicElements();

        // Link from top and down. Look for addBlaBla(), addConfiguredBlaBla() and createBlaBla()
        discoverChildElements();
    }

    public void validateOptions() throws XDocletException
    {
        // super.validateOptions();
    }

    protected boolean processInnerClasses()
    {
        return true;
    }

    protected void generateForClass(XClass clazz) throws XDocletException
    {
        Element element = (Element) classToAntElementMap.get(clazz);
        AntdocTagsHandler antdocTagsHandler = null;

        try {
            antdocTagsHandler = (AntdocTagsHandler) TemplateEngine.getEngineInstance().getTagHandlerFor("Antdoc");
        }
        catch (TemplateException e) {
            throw new XDocletException(e.getMessage());
        }
        antdocTagsHandler.setDocElement(element);
        super.generateForClass(clazz);
    }

    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        // We only want to generate for classes that correspond to AntElements
        boolean result = classToAntElementMap.containsKey(clazz);

        return result;
    }

    /**
     * Gets an AntElement. If the element doesn't exist, it is created.
     *
     * @param clazz  the class to use for the element
     * @return
     */
    private Element getElement(XClass clazz)
    {
        Element element = (Element) classToAntElementMap.get(clazz);

        if (element == null) {
            element = new Element(clazz);

            classToAntElementMap.put(clazz, element);
        }
        return element;
    }

    /**
     * Scans all sources to discover what classes can be interfaced by Ant. Starts by looking at all classes passed to
     * XDoclet (Should be Ant tasks only), then by introspecting the addBlaBla, addConfiguredBlaBla and createBlaBla
     * methods to discover additonal classes that represent nested elements.
     */
    private void discoverTasks()
    {
        // Register the "top" elements, that is, regular Ant tasks
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if ((clazz.isA("org.apache.tools.ant.Task") || clazz.isA("org.apache.tools.ant.types.DataType")) && !clazz.isAbstract() && clazz.isPublic()) {
                getElement(clazz);
                // This will register the element in the global map
            }
        }
    }

    /**
     * Discovers dynamic elements.
     */
    private void discoverDynamicElements()
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass child = (XClass) i.next();
            XDoc doc = child.getDoc();

            String parentClassName = doc.getTagAttributeValue("ant.element", "parent");
            String elementName = doc.getTagAttributeValue("ant.element", "name");

            if (parentClassName != null) {
                XClass parent = getXJavaDoc().getXClass(parentClassName);

                if (parent.isA("org.apache.tools.ant.DynamicConfigurator")) {
                    // Both clazz and parentClazz are Ant elements
                    Element childElement = getElement(child);
                    Element parentElement = getElement(parent);
                    SubElement subElement = new SubElement(childElement, null, elementName);

                    parentElement.addSubElement(subElement);
                }
            }
        }
    }

    private void discoverChildElements()
    {
        // Iterate over the elements we've discovered so far.
        // Can't use an iterator because antElements will be modified
        List soFar = Arrays.asList(classToAntElementMap.values().toArray());

        for (Iterator i = soFar.iterator(); i.hasNext(); ) {
            Element element = (Element) i.next();

            addChidrenRecursive(element);
        }
    }

    private void addChidrenRecursive(Element element)
    {
        Collection methods = element.getXClass().getMethods(true);

        for (Iterator i = methods.iterator(); i.hasNext(); ) {
            XMethod method = (XMethod) i.next();

            addChildElementMaybe(element, method);
        }
    }

    private void addChildElementMaybe(Element parentElement, XMethod method)
    {
        XClass clazz = null;
        String name = null;

        if (method.isPublic()) {
            if (method.getName().startsWith("create") && method.getParameters().size() == 0 && method.getReturnType().getDimension() == 0) {
                // public AntElementClass createFoo()
                clazz = method.getReturnType().getType();
                name = Introspector.decapitalize(method.getName().substring(6));
            }
            else if (method.getName().startsWith("addConfigured") && method.getParameters().size() == 1 && method.getReturnType().getType().getQualifiedName().equals("void")) {
                // public void addFoo(AntElementClass ne)
                clazz = ((XParameter) method.getParameters().iterator().next()).getType();
                name = Introspector.decapitalize(method.getName().substring(13));
            }
            else if (method.getName().startsWith("add") && method.getParameters().size() == 1 && method.getReturnType().getType().getQualifiedName().equals("void")) {
                // public void addFoo(AntElementClass ne)
                clazz = ((XParameter) method.getParameters().iterator().next()).getType();
                name = Introspector.decapitalize(method.getName().substring(3));
            }
        }
        if (clazz != null) {

            Element element = getElement(clazz);
            SubElement subElement = new SubElement(element, method, name);

            parentElement.addSubElement(subElement);

            // Avoid infinite cycles if an element can contain itself, like PatternSet
            if (!alreadyRecursed.contains(clazz)) {
                alreadyRecursed.add(clazz);
                addChidrenRecursive(element);
            }
        }
    }

    /**
     * This class corresponds to an XML element in an Ant build file. This can be a task, or a nested element at any
     * level.
     *
     * @created   21. juli 2002
     */
    public class Element
    {
        private final String TASK = "task";
        private final String SUBTASK = "subtask";
        private final SortedSet subElements = new TreeSet();

        private XClass clazz;

        /**
         * @param clazz  the bean class "implementing" the element
         */
        public Element(XClass clazz)
        {
            this.clazz = clazz;
        }

        public Collection getSubElements()
        {
            return subElements;
        }

        public XClass getXClass()
        {
            return clazz;
        }

        public String getName()
        {
            String elementName = getXClass().getDoc().getTagAttributeValue("ant.element", "name");

            if (elementName == null) {
                String classNameDecapitalized = clazz.getName().toLowerCase();

                if (classNameDecapitalized.endsWith(SUBTASK)) {
                    elementName = classNameDecapitalized.substring(0, classNameDecapitalized.length() - SUBTASK.length());
                }
                else if (classNameDecapitalized.endsWith(TASK)) {
                    elementName = classNameDecapitalized.substring(0, classNameDecapitalized.length() - TASK.length());
                }
                else {
                    elementName = classNameDecapitalized;
                }
            }
            return elementName;
        }

        public void addSubElement(SubElement subElement)
        {
            subElements.add(subElement);
        }

        public String toString()
        {
            return clazz.getQualifiedName() + "(" + clazz.getClass().getName() + ")";
        }

        public boolean equals(Object o)
        {

            return o == this;
        }

        public int hashCode()
        {
            return toString().hashCode();
        }
    }

    /**
     * @created   29. august 2002
     */
    public class SubElement implements Comparable
    {
        private final Element subject;
        private final String name;
        private final XMethod method;

        public SubElement(Element subject, XMethod method, String name)
        {
            this.subject = subject;
            this.name = name;
            this.method = method;
        }

        public Element getSubject()
        {
            return subject;
        }

        public String getName()
        {
            return name;
        }

        public boolean isDynamicSubElement()
        {
            return method == null;
        }

        public XClass getXClass()
        {
            return getSubject().getXClass();
        }

        public String getDescription()
        {
            if (isDynamicSubElement() == false) {
                // hard sub element
                return method.getDoc().getCommentText();
            }
            else {
                // dynamic sub element
                return getXClass().getDoc().getFirstSentence();
            }
        }

        public int compareTo(Object o)
        {
            SubElement other = (SubElement) o;

            return getName().compareTo(other.getName());
        }
    }
}
