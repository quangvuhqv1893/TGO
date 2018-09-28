/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.mockobjects;


// Apache commons import
import org.apache.commons.logging.Log;

// XJavadoc import
import xjavadoc.XClass;

// XDoclet import
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.util.LogUtil;

/**
 * Subtask for mockobject.
 *
 * @author        Joe Walnes
 * @author        Stig J&oslash;rgensen
 * @created       5. februar 2003
 * @ant.element   name="mockobjects" display-name="Mockmaker" parent="xdoclet.modules.mockobjects.MockObjectDocletTask"
 */
public class MockObjectSubTask extends TemplateSubTask
{
    static String   DEFAULT_TEMPLATE_FILE = "resources/mockobject.xdt";
    static String   DEFAULT_MOCKCLASS_PATTERN = "{0}Mock";

    String          mockClassPattern = DEFAULT_MOCKCLASS_PATTERN;

    /**
     * Initialize the default behaviour.
     */
    public MockObjectSubTask()
    {
        setAcceptAbstractClasses(true);
        setAcceptInterfaces(true);

        // If the templates is not set in the Ant task, use the default ones
        setDestinationFile(DEFAULT_MOCKCLASS_PATTERN + ".java");
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
    }

    /**
     * Returns the mockClassPattern.
     *
     * @return   String the mockClassPattern.
     */
    public String getMockClassPattern()
    {
        return mockClassPattern;
    }

    /**
     * Sets the mockClassPattern.
     *
     * @param mockClassPattern  The mockClassPattern to set
     */
    public void setMockClassPattern(String mockClassPattern)
    {
        this.mockClassPattern = mockClassPattern;
    }

    /**
     * Gets the GeneratedFileName attribute of the DaoSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return javaFile(MockObjectTagsHandler.getMockClassFor(getCurrentClass()));
    }

    /**
     * @param clazz
     * @return
     * @exception XDocletException
     * @see                         xdoclet.TemplateSubTask#matchesGenerationRules(xjavadoc.XClass)
     */
    protected boolean matchesGenerationRules(XClass clazz)
         throws XDocletException
    {

        Log log = LogUtil.getLog(MockObjectSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip class " + clazz.getQualifiedName() +
                " because super.matchesGenerationRules() returned false.");

            return false;
        }

        if (!getCurrentClass().isInterface()) {
            log.debug("Skip class " + clazz.getQualifiedName() +
                " because it is not an interface.");

            return false;
        }

        if (!getCurrentClass().getDoc().hasTag("mock:generate")) {
            log.debug("Skip class " + clazz.getQualifiedName() +
                " because the mock:generate tag does not exist.");

            return false;
        }
        return true;
    }
}
