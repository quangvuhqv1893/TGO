/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.doc.info;

import java.io.File;
import java.util.*;

import xjavadoc.ClassIterator;

import xjavadoc.XCollections;
import xjavadoc.XPackage;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.modules.doc.XDocletModulesDocMessages;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.FileManager;
import xdoclet.util.Translator;

/**
 * Extracts tag values from classes and method docs and generates an HTML report that summarizes all occurrences of this
 * tag in a source tree. This task can be used to generate TODO lists or any list with metrics about the occurrence of a
 * certain tag.
 *
 * @author        <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created       September 18, 2001
 * @ant.element   display-name="Info/Todo" name="info" parent="xdoclet.modules.doc.DocumentDocletTask"
 * @todo          use DocletTask as parent instead. should be enough.
 * @version       $Revision: 1.13 $
 */
public class InfoSubTask extends TemplateSubTask
{
    private final Properties properties = new Properties();

    private String  header = null;

    private String  projectName = null;

    /**
     * @todo   blabla (this is yet another test)
     */
    public InfoSubTask()
    {
        // Set default values
        setHeader("Todo list");
        setTag("todo");

        // Use the Ant project's name
        // setProjectname(documentDocletTask.getProject().getName());

        // Don't want tags from superclasses
        properties.setProperty("superclasses", "false");
    }

    /**
     * Gets the Header attribute of the InfoSubTask object
     *
     * @return   The Header value
     */
    public String getHeader()
    {
        return header;
    }

    /**
     * Gets the Projectname attribute of the InfoSubTask object
     *
     * @return   The Projectname value
     */
    public String getProjectname()
    {
        return projectName;
    }

    /**
     * Sets the Header attribute of the InfoSubTask object
     *
     * @param header  The new Header value
     */
    public void setHeader(String header)
    {
        this.header = header;
    }

    /**
     * Sets the Tag attribute of the InfoSubTask object
     *
     * @param tag  The new Tag value
     */
    public void setTag(String tag)
    {
        properties.setProperty("tagName", tag);
    }

    /**
     * Sets the Projectname attribute of the InfoSubTask object
     *
     * @param projectname  The new Projectname value
     */
    public void setProjectname(String projectname)
    {
        this.projectName = projectname;
    }

    /**
     * @exception XDocletException  Description of Exception
     * @todo                        generate an overview summary html too? (the default right page). It could be the old
     *      todo file, a bit modified.
     */
    public void execute() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesDocMessages.class, XDocletModulesDocMessages.CREATE_INFO_LISTS_FOR,
            new String[]{properties.getProperty("tagName")}));

        // first, generate the general stuff on the root.

        // copy the static stuff
        try {
            FileManager.writeURLContent(getClass().getResource("resources/class.gif"), new File(getDestDir(), "class.gif"));
            FileManager.writeURLContent(getClass().getResource("resources/field.gif"), new File(getDestDir(), "field.gif"));
            FileManager.writeURLContent(getClass().getResource("resources/constructor.gif"), new File(getDestDir(), "constructor.gif"));
            FileManager.writeURLContent(getClass().getResource("resources/method.gif"), new File(getDestDir(), "method.gif"));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }

        // Although stylesheet could be copied out with above method, use template engine.
        // We might want to parameterise colors/fonts with tags
        setTemplateURL(getClass().getResource("resources/info.css"));
        setDestinationFile("info.css");
        startProcess();

        setTemplateURL(getClass().getResource("resources/index.xdt"));
        setDestinationFile("index.html");
        startProcess();

        setTemplateURL(getClass().getResource("resources/all-classes.xdt"));
        setDestinationFile("all-classes.html");
        startProcess();

        setTemplateURL(getClass().getResource("resources/all-packages.xdt"));
        setDestinationFile("all-packages.html");
        startProcess();

        setTemplateURL(getClass().getResource("resources/overview-packages.xdt"));
        setDestinationFile("overview-packages.html");
        startProcess();

        // now loop over all packages and classes
        Collection classes = getXJavaDoc().getSourceClasses();
        SortedSet packages = new TreeSet();

        for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
            packages.add(i.next().getContainingPackage());
        }

        XPackage currentPackage = null;

        for (Iterator packageIterator = packages.iterator(); packageIterator.hasNext(); ) {
            currentPackage = (XPackage) packageIterator.next();
            setCurrentPackage(currentPackage);

            File oldDestDir = getDestDir();
            File dir = new File(getDestDir(), PackageTagsHandler.packageNameAsPathFor(currentPackage));

            setDestDir(dir);
            setTemplateURL(getClass().getResource("resources/classes-list.xdt"));
            setDestinationFile("classes-list.html");
            startProcess();

            classes = currentPackage.getClasses();
            setTemplateURL(getClass().getResource("resources/class-details.xdt"));
            for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
                setCurrentClass(i.next());
                setDestinationFile(getCurrentClass().getName() + "-details.html");
                startProcess();
            }

            setDestDir(oldDestDir);
        }

        // restore current package to null, so subsequent class iterations can
        // perform outside the context of a current packages
        setCurrentPackage(null);
    }

    /**
     * validate options - noop here
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
    }

    /**
     * Gets the Properties attribute of the InfoSubTask object
     *
     * @return   The Properties value
     */
    protected Properties getProperties()
    {
        return properties;
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException  Describe the exception
     */
    protected void engineStarted() throws XDocletException
    {
    }

}
