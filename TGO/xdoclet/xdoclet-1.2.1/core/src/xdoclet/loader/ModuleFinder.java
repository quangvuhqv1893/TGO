/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.apache.commons.logging.Log;

import org.apache.tools.ant.AntClassLoader;
import org.xml.sax.InputSource;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;

/**
 * Finds xdoclet modules.
 *
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created   7. april 2002
 * @todo      Use reflection to call AntClassLoader.getClasspath to remove dependency to Ant. This package should be Ant
 *      independent
 * @version   $Revision: 1.15 $
 */
public class ModuleFinder
{
    private final static FileFilter jarFilter =
        new FileFilter()
        {
            public boolean accept(File file)
            {
                return file.getName().endsWith(".jar");
            }
        };
    private static String classpath;
    private static List modules = null;

    public static String getClasspath()
    {
        return classpath;
    }

    /**
     * Get the newest Jar file on the classpath. This method is used to see if a generation is needed because one of the
     * xdoclet jars might have changed since the last generation.
     *
     * @return   The timestamp of the newest jar file on the classpath
     */
    public static File getNewestFileOnClassPath()
    {
        List moduleFiles = findModuleFiles();
        long newest = Long.MIN_VALUE;
        File newestFile = null;

        Iterator i = moduleFiles.iterator();

        while (i.hasNext()) {
            File moduleFile = (File) i.next();

            if (moduleFile.lastModified() >= newest) {
                newestFile = moduleFile;
                newest = moduleFile.lastModified();
            }
        }
        return newestFile;
    }

    public static void setClasspath(String classpath)
    {
        ModuleFinder.classpath = classpath;
    }

    /**
     * Initialises the classpath. If a system property named <code>xdoclet.class.path</code>, then that value will be
     * used. If not, we'll try to cast the clazz' class loader to an AntClassLoader and get classpath from there. If
     * that fails (happens if the clazz was loaded by the system class loader), we'll use java.class.path.
     *
     * @param clazz  the class used to find classpath.
     */
    public static void initClasspath(Class clazz)
    {
        if (System.getProperty("xdoclet.class.path") == null) {
            try {
                classpath = ((AntClassLoader) clazz.getClassLoader()).getClasspath();
            }
            catch (ClassCastException e) {
                classpath = System.getProperty("java.class.path");
            }
        }
        else {
            classpath = System.getProperty("xdoclet.class.path");
        }
    }

    /**
     * Returns a List of XDocletModule objects
     *
     * @return
     */
    public static List findModules()
    {
        if (modules == null) {
            modules = new ArrayList();

            Log log = LogUtil.getLog(ModuleFinder.class, "findModules");

            log.debug(Translator.getString(LoaderMessages.class, LoaderMessages.REGISTERING_MODULES));

            XDocletXmlParser parser = new XDocletXmlParser();

            List moduleFiles = findModuleFiles();
            Iterator moduleFileIterator = moduleFiles.iterator();

            while (moduleFileIterator.hasNext()) {
                File file = (File) moduleFileIterator.next();

                if (file.exists()) {
                    try {
                        InputStream xdocletXmlIs = null;
                        URL xtagsURL = null;

                        if (file.isDirectory()) {
                            xdocletXmlIs = new FileInputStream(new File(file, "META-INF" + File.separator + "xdoclet.xml"));
                            xtagsURL = (new File(file, "META-INF" + File.separator + "xtags.xml")).toURL();
                        }
                        else {
                            JarFile jar = new JarFile(file);
                            JarEntry xdocletXml = jar.getJarEntry("META-INF/xdoclet.xml");

                            xtagsURL = new URL(new URL("jar:" + file.toURL() + "!/"), "META-INF/xtags.xml");

                            if (xdocletXml != null) {
                                log.debug(Translator.getString(LoaderMessages.class, LoaderMessages.PARSING_XDOCLET_XML, new String[]{file.getAbsolutePath()}));

                                xdocletXmlIs = jar.getInputStream(xdocletXml);
                            }
                            else {
                                log.debug(Translator.getString(LoaderMessages.class, LoaderMessages.SKIP_NO_XDOCLET_XML, new String[]{file.getAbsolutePath()}));
                            }
                        }

                        if (xdocletXmlIs != null) {
                            InputSource in = new InputSource(xdocletXmlIs);

                            in.setSystemId(xtagsURL.toString());

                            XDocletModule module = parser.parse(in);

                            if (module != null) {
                                module.setXTagsDefinitionURL(xtagsURL);
                                modules.add(module);
                            }
                            else {
                                log.warn(Translator.getString(LoaderMessages.class, LoaderMessages.BAD_XDOCLET_XML, new String[]{file.getAbsolutePath()}));
                            }
                        }
                    }
                    catch (ZipException ze) {
                        // Zip Exceptions gets only a warning, so invalid zip files
                        // are just skipped by the module finder.
                        log.warn(Translator.getString(LoaderMessages.class, LoaderMessages.INVALID_ZIP_FILE, new String[]{file.getName()}));
                    }
                    catch (IOException e) {
                        throw new IllegalStateException(Translator.getString(LoaderMessages.class, LoaderMessages.LOAD_MODULE_ERROR, new String[]{e.getMessage()}));
                    }
                }
                else {
                    log.warn(Translator.getString(LoaderMessages.class, LoaderMessages.NONEXISTANT_CLASSPATH_ENTRY, new String[]{file.getAbsolutePath()}));
                }
            }

            log.debug(Translator.getString(LoaderMessages.class, LoaderMessages.DONE_REGISTERING_MODULES, new String[]{String.valueOf(modules.size())}));
        }
        return modules;
    }

    public static void resetFoundModules()
    {
        modules = null;
    }

    private static List findModuleFiles()
    {
        if (classpath == null) {
            throw new IllegalStateException(Translator.getString(LoaderMessages.class, LoaderMessages.INIT_CLASSPATH_NOT_CALLED));
        }

        ArrayList result = new ArrayList();

        StringTokenizer pathTokenizer = new StringTokenizer(classpath, System.getProperty("path.separator"));

        while (pathTokenizer.hasMoreTokens()) {
            File file = new File(pathTokenizer.nextToken());

            if (file.isDirectory()) {
                // a module doesn't have to be a jar. can be a straight directory too.
                // required in order to solve the chicken and egg for xdoclet itself (externalizer)
                if (new File(file, "META-INF" + File.separator + "xdoclet.xml").exists()) {
                    result.add(file);
                }
            }
            else if (jarFilter.accept(file)) {
                result.add(file);
            }
        }
        return result;
    }
}
