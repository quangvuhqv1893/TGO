/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.session;

import org.apache.commons.logging.Log;

import xjavadoc.XClass;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.ejb.AbstractEjbCodeGeneratorSubTask;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;

import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @ant.element          display-name="Session Bean" name="session" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version              $Revision: 1.7 $
 * @xdoclet.merge-file   file="session-custom.xdt" relates-to="{0}Session.java" description="A text file containing
 *      custom template and/or java code to include in the EJB session class."
 */
public class SessionSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public static String DEFAULT_SESSION_CLASS_PATTERN = "{0}Session";

    protected static String DEFAULT_TEMPLATE_FILE = "resources/session.xdt";

    /**
     * A configuration parameter for specifying the concrete session bean class name pattern. By default the value is
     * used for deciding the concrete session bean class name. {0} in the value mean current class's symbolic name which
     * for an EJBean is the EJB name.
     *
     * @see   #getSessionClassPattern()
     */
    protected String sessionClassPattern;


    /**
     * Describe what the SessionSubTask constructor does
     */
    public SessionSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getSessionClassPattern() + ".java");
        addOfType("javax.ejb.SessionBean");
        setPackageSubstitutionInheritanceSupported(false);
    }

    /**
     * Returns the configuration parameter for specifying the concrete session bean class name pattern. By default the
     * value is used for deciding the concrete session bean class name. {0} in the value mean current class's symbolic
     * name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}Session" is used by
     * default.
     *
     * @return   The SessionClassPattern value
     * @see      #sessionClassPattern
     */
    public String getSessionClassPattern()
    {
        if (sessionClassPattern != null) {
            return sessionClassPattern;
        }
        else {
            return DEFAULT_SESSION_CLASS_PATTERN;
        }
    }


    /**
     * Sets the Pattern attribute of the SessionSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        sessionClassPattern = new_pattern;
    }


    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getSessionClassPattern() == null || getSessionClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getSessionClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }


    /**
     * Gets the GeneratedFileName attribute of the SessionSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(SessionTagsHandler.getSessionClassFor(getCurrentClass())) + ".java";
    }


    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_SESSION_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }


    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(SessionSubTask.class, "matchesGenerationRules");

        if (super.matchesGenerationRules(clazz) == false) {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        String generate = getCurrentClass().getDoc().getTagAttributeValue("ejb:bean", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            log.debug("Skip session class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        if (EjbTagsHandler.isAConcreteEJBean(getCurrentClass())) {
            return true;
        }
        else {
            log.debug("Skip bean " + clazz.getQualifiedName() + " because it's not a concrete bean.");
            return false;
        }
    }

}
