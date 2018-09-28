/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.io.Serializable;
import java.util.*;

import xjavadoc.*;

import xdoclet.DocletContext;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.template.TemplateException;
import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 14, 2001
 * @xdoclet.taghandler   namespace="Package"
 * @version              $Revision: 1.11 $
 */
public class PackageTagsHandler extends AbstractProgramElementTagsHandler
{
    /**
     * Gets the PackageNameFor attribute of the PackageTagsHandler class
     *
     * @param pak               Describe what the parameter does
     * @param withSubstitution
     * @return                  The PackageNameFor value
     */
    public static String getPackageNameFor(XPackage pak, boolean withSubstitution)
    {
        return getPackageNameFor(pak.getName(), withSubstitution);
    }

    public static String getPackageNameFor(String packageName)
    {
        return getPackageNameFor(packageName, true);
    }

    /**
     * It applies package substitutions.
     *
     * @param packageName
     * @param withSubstitution
     * @return                  The package name after substitutions.
     */
    public static String getPackageNameFor(String packageName, boolean withSubstitution)
    {
        ArrayList packageSubstitutions = getPackageSubstitutions(DocletContext.getInstance().getActiveSubTask().getSubTaskName());

        if (packageSubstitutions == null || !withSubstitution) {
            return packageName;
        }

        for (int i = 0; i < packageSubstitutions.size(); i++) {
            PackageSubstitution ps = (PackageSubstitution) packageSubstitutions.get(i);
            StringTokenizer st = new StringTokenizer(ps.getPackages(), ",", false);

            if (ps.getUseFirst() == false) {
                while (st.hasMoreTokens()) {
                    String packages = st.nextToken();
                    String suffix = "." + packages;

                    if (packageName.endsWith(suffix)) {
                        packageName = packageName.substring(0, packageName.length() - suffix.length()) + '.' + ps.getSubstituteWith();
                        break;
                    }
                }
            }
            else {
                packageName = replaceInline(packageName, ps.getPackages(), ps.getSubstituteWith());
            }
        }

        return packageName;
    }

    /**
     * Gets the PackageSubstitutions attribute of the PackageTagsHandler class
     *
     * @param subtaskName  Describe what the parameter does
     * @return             The PackageSubstitutions value
     */
    public static ArrayList getPackageSubstitutions(String subtaskName)
    {
        // SubTask's packageSubstitutions has precedence over
        // the global packageSubstitutions defined in DocletTask
        ArrayList packageSubstitutions = null;
        boolean supportsPackageSubstitutionInheritance = true;

        Boolean supports = ((Boolean) DocletContext.getInstance().getConfigParam(subtaskName + ".packageSubstitutionInheritanceSupported"));

        if (supports != null) {
            supportsPackageSubstitutionInheritance = supports.booleanValue();
        }

        packageSubstitutions = (ArrayList) DocletContext.getInstance().getConfigParam(subtaskName + ".packageSubstitutions");

        // nothing specified for subtask, inherit the one from DocletTask
        if (supportsPackageSubstitutionInheritance && (packageSubstitutions == null || packageSubstitutions.isEmpty())) {
            packageSubstitutions = (ArrayList) DocletContext.getInstance().getConfigParam("packageSubstitutions");
        }

        return packageSubstitutions;
    }

    /**
     * Returns the current package name as path
     *
     * @param pak  Description of Parameter
     * @return     current package name as path
     * @doc.tag    type="content"
     */
    public static String packageNameAsPathFor(XPackage pak)
    {
        return getPackageNameFor(pak, true).replace('.', '/');
    }

    public static String packageNameAsPathWithoutSubstitutionFor(XPackage pak)
    {
        return getPackageNameFor(pak, false).replace('.', '/');
    }

    /**
     * Returns the current package name as path
     *
     * @param qualifiedName  Description of Parameter
     * @return               current package name as path
     * @doc.tag              type="content"
     */
    public static String packageNameAsPathFor(String qualifiedName)
    {
        String qName = qualifiedName;

        ArrayList pss = getPackageSubstitutions(DocletContext.getInstance().getActiveSubTask().getSubTaskName());

        PackageSubstitution ps;

        for (int i = 0; i < pss.size(); i++) {
            ps = (PackageSubstitution) pss.get(i);
            if (ps.getUseFirst() == true) {
                qName = replaceInline(qName, ps.getPackages(), ps.getSubstituteWith());
            }
        }

        return qName.replace('.', '/');
    }

    /**
     * Replace the first occurance of oldOne in original with newOne Or returns the original string if oldOne is not
     * found
     *
     * @param original  String in which replacement should occour
     * @param oldOne    String to be replaced
     * @param newOne    String that replaces
     * @return          String original string with replacements
     */
    public static String replaceInline(String original, String oldOne, String newOne)
    {
        int index = original.indexOf(oldOne);

        if (index > -1)
            return original.substring(0, index) + newOne + original.substring(index + oldOne.length());
        else
            return original;
    }

    /**
     * Returns the current package name. If we're in the context of a package iteration, this is the name of the current
     * package. If we're in the context of a class iteration without a package iteration, return the name of the current
     * class' package.
     *
     * @return                      current package name
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String packageName() throws XDocletException
    {
        if (getCurrentPackage() != null) {
            // first try to get the name from current package. It exists if
            return getCurrentPackage().getName();
        }
        else {
            return getCurrentClass().getContainingPackage().getName();
        }
    }

    /**
     * Returns the not-full-qualified package name of the full-qualified class name specified in the body of this tag.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void packageOf(String template) throws XDocletException
    {
        try {
            String fullClassName = getEngine().outputOf(template);

            getEngine().print(getPackageNameFor(fullClassName.substring(0, fullClassName.lastIndexOf('.')), true));
        }
        catch (TemplateException ex) {
            throw new XDocletException(ex, Translator.getString(XDocletMessages.class, XDocletMessages.METHOD_FAILED, new String[]{"packageOf"}));
        }
    }

    /**
     * Iterates over all packages loaded by javadoc. Subsequent calls to forAllClasses will only iterate over the
     * classes in the current package.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="abstract" optional="true" values="true,false" description="If true then accept
     *      abstract classes also; otherwise don't."
     * @doc.param                   name="type" optional="true" description="For all classes by the type."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void forAllPackages(String template, Properties attributes) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();
        SortedSet packages = new TreeSet();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            packages.add(clazz.getContainingPackage());
        }

        XPackage currentPackage = null;

        for (Iterator packageIterator = packages.iterator(); packageIterator.hasNext(); ) {
            currentPackage = (XPackage) packageIterator.next();
            setCurrentPackage(currentPackage);
            generate(template);
        }
        // restore current package to null, so subsequent class iterations can
        // perform outside the context of a current package
        setCurrentPackage(null);
    }

    /**
     * Returns the current package name as path
     *
     * @return                      current package name as path
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String packageNameAsPath() throws XDocletException
    {
        return packageNameAsPathFor(packageName());
    }

    /**
     * It's good practice to put interfaces (such as remote/local interfaces, data objects and home interfaces) in a
     * separate "interfaces" package rather than in the EJB bean implementation package. Previous versions of XDoclet
     * dictated this behavior, so if package name of a bean ended with .beans or .ejb interfaces were put into
     * .interfaces package. It's no more the case. You have full control over it. If you don't use a packageSubstitution
     * element, then all interfaces are generated to the same package as the bean implementation class. But if you want
     * to follow the pattern and put interfaces into a separate package you can, by providing the list of package name
     * tails that interfaces of beans inside that packages should be placed into the package you define. For example
     * interfaces of test.ejb.CustomerBean will be placed in test.interfaces by the following packageSubstitution:<p/>
     *
     * <pre><code>
     *&lt;packageSubstitution packages="ejb,beans" substituteWith="interfaces" /&gt;
     *</code></pre> <p/>
     *
     * By using the useFirst attribute, you can tell xdoclet to substitute the first occurence and not the last. <br/>
     * Now if you have a structure like<p/>
     *
     * com.acme.foo.bar.ejb <br/>
     * com.acme.baz.lala.ejb<p/>
     *
     * you want to gather all interfaces under one root/subtree like e.g. <p/>
     *
     * com.acme.interfaces.bar.* <br/>
     * com.acme.interfaces.lala.*<p/>
     *
     * now you can say:<br/>
     * &lt;packagesubstitution packages="foo,baz" substituteWith="interfaces" useFirst="true"/&gt;
     *
     * @created   10. september 2002
     */
    public static class PackageSubstitution implements Serializable
    {
        private String packages = null;
        private String substituteWith = null;
        private boolean useFirst = false;

        /**
         * Gets the Packages attribute of the PackageSubstitution object
         *
         * @return   The Packages value
         */
        public String getPackages()
        {
            return packages;
        }

        /**
         * Gets the SubstituteWith attribute of the PackageSubstitution object
         *
         * @return   The SubstituteWith value
         */
        public String getSubstituteWith()
        {
            return substituteWith;
        }

        /**
         * return the useAny attribute. This attribute specifies if the substitution can only appear at the end of a
         * package (useAny=false) or also in the middle.
         *
         * @return   boolean
         */
        public boolean getUseFirst()
        {
            return this.useFirst;
        }

        /**
         * Sets the Packages attribute of the PackageSubstitution object
         *
         * @param packages  The new Packages value
         */
        public void setPackages(String packages)
        {
            this.packages = packages;
        }

        /**
         * Sets the SubstituteWith attribute of the PackageSubstitution object
         *
         * @param substituteWith  The new SubstituteWith value
         */
        public void setSubstituteWith(String substituteWith)
        {
            this.substituteWith = substituteWith;
        }

        /**
         * Sets the useFirst attribute
         *
         * @param first  should the first occurence be used or not?
         */
        public void setUseFirst(boolean first)
        {
            this.useFirst = first;

        }
    }
}
