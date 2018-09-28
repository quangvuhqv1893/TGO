/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.ant.modulesbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.DTDLocation;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.XMLCatalog;

/**
 * Loops over all modules and builds each one. It builds modules the module depends on first. The module dependency is
 * specified in a module.xml file in the root of each module. It's based on Ant's dependency checking code. Refer to
 * that code for more details.
 *
 * @author    Ara Abrahamian (ara_e_w@yahoo.com)
 * @created   Jun 9, 2002
 * @version   $Revision: 1.11 $
 */
public class ModulesGrandBuilderTask extends Task
{
    /**
     * Constant for the "visiting" state, used when traversing a DFS of target dependencies.
     */
    private final static String VISITING = "VISITING";
    /**
     * Constant for the "visited" state, used when traversing a DFS of target dependencies.
     */
    private final static String VISITED = "VISITED";

    private static ModuleXmlParser parser = new ModuleXmlParser();

    private String  target = null;

    /**
     * for resolving entities such as dtds
     */
    private XMLCatalog xmlCatalog = new XMLCatalog();

    private static boolean isModule(File file)
    {
        File module_build_xml = new File(file, "build.xml");

        return (!file.getName().equalsIgnoreCase("build")) && (!file.getName().equalsIgnoreCase("cvs")) && module_build_xml.exists();
    }

    private static BuildException makeCircularException(String end, Stack stk)
    {
        StringBuffer sb = new StringBuffer("Circular dependency: ");

        sb.append(end);

        String c;

        do {
            c = (String) stk.pop();
            sb.append(" <- ");
            sb.append(c);
        } while (!c.equals(end));

        return new BuildException(new String(sb));
    }

    /**
     * set the name of the target to be called in each of the modules' build files
     *
     * @param target  the target name
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    public final Vector topoSort(String root, Hashtable modules) throws BuildException
    {
        Vector ret = new Vector();
        Hashtable state = new Hashtable();
        Stack visiting = new Stack();

        // We first run a DFS based sort using the root as the starting node.
        // This creates the minimum sequence of Modules to the root node.
        // We then do a sort on any remaining unVISITED modules.
        // This is unnecessary for doing our build, but it catches
        // circular dependencies or missing Modules on the entire
        // dependency tree, not just on the Modules that depend on the
        // build Module.
        tsort(root, modules, state, visiting, ret);

        for (Enumeration en = modules.keys(); en.hasMoreElements(); ) {
            String cur_module = (String) en.nextElement();
            String st = (String) state.get(cur_module);

            if (st == null) {
                tsort(cur_module, modules, state, visiting, ret);
            }
            else if (st == VISITING) {
                throw new RuntimeException("Unexpected node in visiting state: " + cur_module);
            }
        }

        return ret;
    }

    /**
     * add an XMLCatalog as a nested element; optional.
     *
     * @param catalog  The feature to be added to the ConfiguredXMLCatalog attribute
     */
    public void addConfiguredXMLCatalog(XMLCatalog catalog)
    {
        xmlCatalog.addConfiguredXMLCatalog(catalog);
    }

    /**
     * Create a DTD location record; optional. This stores the location of a DTD. The DTD is identified by its public
     * Id.
     *
     * @return
     */
    public DTDLocation createDTD()
    {
        DTDLocation dtdLocation = new DTDLocation();

        xmlCatalog.addDTD(dtdLocation);
        return dtdLocation;
    }

    /**
     * Initialize internal instance of XMLCatalog
     *
     * @exception BuildException
     */
    public void init() throws BuildException
    {
        super.init();
        xmlCatalog.setProject(project);
    }

    public void execute() throws BuildException
    {
        File base_dir = this.getProject().getBaseDir();
        File[] files = base_dir.listFiles();
        Hashtable modules = new Hashtable();

        parser.setEntityResolver(xmlCatalog);

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory() && isModule(file)) {
                Module module = createModule(file);

                modules.put(module.getName(), module);
            }
        }

        //for all modules, one by one, execute each one, but first sort the list based on dependency path of each module
        for (Enumeration enum = modules.elements(); enum.hasMoreElements(); ) {
            Module module = (Module) enum.nextElement();
            Vector sorted_modules = topoSort(module.getName(), modules);

            int curidx = 0;
            Module cur_module;

            do {
                cur_module = (Module) sorted_modules.elementAt(curidx++);

                if (cur_module.isExecuted() == false) {
                    executeModule(cur_module);
                    cur_module.setExecuted(true);
                }
            } while (!cur_module.getName().equals(module.getName()));
        }
    }

    /**
     * @param root
     * @param targets
     * @param state
     * @param visiting
     * @param ret
     * @exception BuildException
     * @todo                      i18n
     */
    private final void tsort(String root, Hashtable targets, Hashtable state, Stack visiting, Vector ret) throws BuildException
    {
        state.put(root, VISITING);
        visiting.push(root);

        Module module = (Module) targets.get(root);

        // Make sure we exist
        if (module == null) {
            StringBuffer sb = new StringBuffer("Module `");

            sb.append(root);
            sb.append("' does not exist. ");

            visiting.pop();
            if (!visiting.empty()) {
                String parent = (String) visiting.peek();

                sb.append("It is used from module `");
                sb.append(parent);
                sb.append("'.");
            }

            throw new BuildException(new String(sb));
        }

        for (Enumeration en = module.getDependencies(); en.hasMoreElements(); ) {
            String cur = (String) en.nextElement();
            String m = (String) state.get(cur);

            if (m == null) {
                // Not been visited
                tsort(cur, targets, state, visiting, ret);
            }
            else if (m == VISITING) {
                // Currently visiting this node, so have a cycle
                throw makeCircularException(cur, visiting);
            }
        }

        String p = (String) visiting.pop();

        if (root != p) {
            throw new RuntimeException("Unexpected internal error: expected to " + "pop " + root + " but got " + p);
        }

        state.put(root, VISITED);
        ret.addElement(module);
    }

    private void executeModule(Module module)
    {
        Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN), null);

        exe.setAntRun(project);
        exe.setWorkingDirectory(module.getBaseDir());

        CommandlineJava cmdl = new CommandlineJava();

        Environment.Variable ant_home = new Environment.Variable();

        //set ant_home
        String env_ant_home = project.getProperty("env.ANT_HOME");

        if (env_ant_home != null) {
            ant_home.setKey("ant.home");
            ant_home.setValue(env_ant_home);
            cmdl.addSysproperty(ant_home);
        }

        Path classpath = cmdl.createClasspath(project);

        classpath.setPath(System.getProperty("java.class.path"));

        //set the class name
        cmdl.setClassname("org.apache.tools.ant.Main");

        // add the Ant target name, if specified
        if (target != null) {
            cmdl.createArgument().setValue(target);
        }

        // pass on all the properties
        Hashtable props = getProject().getProperties();
        Enumeration prop_keys = props.keys();

        while (prop_keys.hasMoreElements()) {
            String arg = prop_keys.nextElement().toString();

            if (argumentShouldntBePassedOn(arg))
                continue;

            String value = props.get(arg).toString();

            //don't try to set a property if it will mess up a windows command line due to spaces.
            //Patch by Adrian Brock.
            if (value == null || value.indexOf(" ") == -1) {
                cmdl.createArgument().setValue("-D" + arg + "=" + value);
            }
            // end of if ()

        }

        exe.setCommandline(cmdl.getCommandline());

        try {
            int exit = exe.execute();

            if (exe.killedProcess()) {
                log("Timeout: killed the sub-process", Project.MSG_WARN);
            }
            if (exit != 0) {
                throw new BuildException("" + exit, location);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BuildException(e, location);
        }
    }

    private boolean argumentShouldntBePassedOn(String arg)
    {
        return "basedir".equals(arg) || "ant.file".equals(arg) || arg.startsWith("java.") || arg.startsWith("sun.");
    }

    private Module createModule(File file)
    {
        //load dependency info
        File module_xml = new File(file, "module.xml");
        Module module = null;

        if (module_xml.exists()) {
            try {
                FileInputStream module_xml_in = new FileInputStream(module_xml);

                module = parser.parse(module_xml_in);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            module = new Module();
        }

        module.setName(file.getName());
        module.setBaseDir(file);

        return module;
    }

}
