/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jdo;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import xjavadoc.*;

import xdoclet.SubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;

/**
 * @author               Ludovic Claude (ludovicc@users.sourceforge.net)
 * @author               David Jencks (d_jencks@users.sourceforge.net)
 * @created              June 11, 20012
 * @xdoclet.taghandler   namespace="Jdo"
 * @version              $Revision: 1.8 $
 */
public class JdoTagsHandler extends XDocletTagSupport
{

    private Collection _extensions;
    private VendorExtension _currentExtension;

    /**
     * The <code>forAllPackages</code> iterates through all packages, and generates the template if the
     * jdo.persistence-capable tag is present in at least one class in the package.
     *
     * @param template              a <code>String</code> value
     * @param attributes            a <code>Properties</code> value
     * @exception XDocletException  if an error occurs
     * @doc:tag                     type="block"
     * @doc:param                   name="tagName" optional="false" description="The tag name that must be present in at
     *      least one class in the package in order that the template be generated."
     */
    public void forAllPackages(String template, Properties attributes) throws XDocletException
    {
        if (getCurrentPackage() == null) {
            Collection packages = getXJavaDoc().getSourcePackages();

            for (PackageIterator i = XCollections.packageIterator(packages); i.hasNext(); ) {
                XPackage pakkage = i.next();

                setCurrentPackage(pakkage);

                Collection classes = getCurrentPackage().getClasses();

                for (ClassIterator j = XCollections.classIterator(classes); j.hasNext(); ) {
                    XDoc doc = j.next().getDoc();

                    if (doc.hasTag("jdo.persistence-capable")) {
                        generate(template);
                        break;
                    }
                }
                setCurrentPackage(null);
            }
        }
        else {
            Collection classes = getCurrentPackage().getClasses();

            for (ClassIterator j = XCollections.classIterator(classes); j.hasNext(); ) {
                XDoc doc = j.next().getDoc();

                if (doc.hasTag("jdo.persistence-capable")) {
                    generate(template);
                    break;
                }
            }
        }
    }

    /**
     * The <code>forAllClassesInPackage</code> method iterates through all the classes in the current package.
     *
     * @param template              a <code>String</code> value
     * @param attributes            a <code>Properties</code> value
     * @exception XDocletException  if an error occurs
     * @doc:tag                     type="block"
     */
    public void forAllClassesInPackage(String template, Properties attributes) throws XDocletException
    {
        if (getCurrentClass() == null) {

            Collection classes = getCurrentPackage().getClasses();

            for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
                setCurrentClass(i.next());
                generate(template);
            }
        }
        else {
            generate(template);
        }

    }

    /**
     * Generates the tags for the vendor extensions
     *
     * @param attributes
     * @param template
     * @exception XDocletException
     */
    public void forAllVendorExtensions(String template, Properties attributes) throws XDocletException
    {
        String level = attributes.getProperty("level");
        SubTask[] subtasks = getDocletContext().getSubTasks();

        _extensions = new ArrayList();
        for (int i = 0; i < subtasks.length; i++) {
            if (subtasks[i] instanceof VendorExtensionsSubTask) {
                VendorExtensionsSubTask vendorExtensionsTask = (VendorExtensionsSubTask) subtasks[i];

                _extensions.addAll(vendorExtensionsTask.getExtensions(level));
            }
        }

        for (Iterator i = _extensions.iterator(); i.hasNext(); ) {
            setCurrentVendorExtension((VendorExtension) i.next());
            generate(template);
        }
    }

    public String vendorExtension(Properties attributes)
    {
        StringBuffer tags = new StringBuffer();

        tags.append("<extension vendor-name=\"");
        tags.append(_currentExtension.getVendor());
        tags.append("\" key=\"");
        tags.append(_currentExtension.getKey());
        tags.append("\" value=\"");
        tags.append(_currentExtension.getValue());
        tags.append("\"");
        if (_currentExtension.hasNestedExtensions()) {
            tags.append(">");
            for (Iterator i = _currentExtension.nestedExtensionsIterator(); i.hasNext(); ) {
                VendorExtension popVendorExtension = _currentExtension;

                setCurrentVendorExtension((VendorExtension) i.next());
                tags.append(vendorExtension(attributes));
                setCurrentVendorExtension(popVendorExtension);
            }
            tags.append("</extension>");
        }
        else {
            tags.append("/>");
        }
        return tags.toString();
    }

    protected void setCurrentVendorExtension(VendorExtension vendorExtension)
    {
        _currentExtension = vendorExtension;
    }

}
