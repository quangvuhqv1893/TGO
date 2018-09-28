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
import xdoclet.modules.ejb.entity.FacadeTagsHandler;

import xdoclet.tagshandler.PackageTagsHandler;
import xdoclet.util.LogUtil;

import xdoclet.util.Translator;

/**
 * this subtask is stage 2 of remote facade generation.
 *
 * @author        Konstantin Pribluda (kpriblouda@yahoo.com)
 * @created       September 10, 2002
 * @ant.element   display-name="Facade" name="remotefacade" parent="xdoclet.modules.ejb.EjbDocletTask"
 * @version       $Revision: 1.3 $
 */

public class RemoteFacadeSubTask extends AbstractEjbCodeGeneratorSubTask
{
    public final static String DEFAULT_REMOTE_FACADE_CLASS_PATTERN = "{0}Remote";
    protected static String DEFAULT_TEMPLATE_FILE = "resources/remotefacade.xdt";

    /**
     * A configuration parameter for specifying the entity bean facade EJB class name pattern. By default the value is
     * used for deciding the entity bean facade class name. {0} in the value mean current class's symbolic name which
     * for an EJBean is the EJB name.
     */
    protected String remoteFacadeClassPattern;

    /**
     * a configuration parameter for specifying facade ejb names pattern {0} means ejb name
     */
    protected String remoteFacadeEjbNamePattern;

    public RemoteFacadeSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(getRemoteFacadeClassPattern() + ".java");
        addOfType("javax.ejb.SessionBean");

    }

    /**
     * Returns the configuration parameter for specifying the entity bean facade class name pattern. By default the
     * value is used for deciding the concrete CMP entity bean class name. {0} in the value mean current class's
     * symbolic name which for an EJBean is the EJB name. If nothing explicitly specified by user then "{0}FacadeEJB" is
     * used by default.
     *
     * @return   The EntityCmpClassPattern value
     */
    public String getRemoteFacadeClassPattern()
    {
        if (remoteFacadeClassPattern != null) {
            return remoteFacadeClassPattern;
        }
        else {
            return DEFAULT_REMOTE_FACADE_CLASS_PATTERN;
        }
    }

    /**
     * Sets the Pattern attribute of the DataObjectSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern)
    {
        remoteFacadeClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getRemoteFacadeClassPattern() == null || getRemoteFacadeClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"pattern"}));
        }

        if (getRemoteFacadeClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }

    /**
     * Gets the GeneratedFileName attribute of the RemoteFacadeSubTask object
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The GeneratedFileName value
     * @exception XDocletException
     */
    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        return PackageTagsHandler.packageNameAsPathFor(FacadeTagsHandler.getRemoteFacadeClassFor(getCurrentClass())) + ".java";
    }

    /**
     * we accept session beans generated on first stage runs.
     *
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     * @todo                        refactor/merge this method with matchesGenerationRules from EntityBmpSubTask
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException
    {
        Log log = LogUtil.getLog(RemoteFacadeSubTask.class, "matchesGenerationRules");

        System.out.println("remote facade tests: " + clazz.getQualifiedName());
        if (super.matchesGenerationRules(clazz) == false) {
            System.out.println("super failed");
            log.debug("Skip bean " + clazz.getQualifiedName() + " because super.matchesGenerationRules() returned false.");
            return false;
        }

        if (SessionTagsHandler.isSession(getCurrentClass()) == false) {
            System.out.println("session failed");

            log.debug("Skip bean " + clazz.getQualifiedName() + " because of it's not session bean.");
            return false;
        }

        String generate = clazz.getDoc().getTagAttributeValue("ejb:bean", "generate", false);

        if ((generate != null) && (generate.equals("false") || generate.equals("no"))) {
            System.out.println("generate failed");
            log.debug("Skip remote facade for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        if (!clazz.getDoc().hasTag("ejb.remote-facade")) {
            System.out.println("remote-facade failed");
            log.debug("Skip facade class for " + clazz.getQualifiedName() + " because ejb.remote-facade is absent");
            return false;
        }

        String facade = clazz.getDoc().getTagAttributeValue("ejb.remote-facade", "generate", false);

        if ((facade != null) && (facade.equals("false") || facade.equals("no"))) {
            System.out.println("remote-facade generate failed");
            log.debug("Skip facade class for " + clazz.getQualifiedName() + " because of generate=" + generate + " flag.");
            return false;
        }

        if (EjbTagsHandler.isAConcreteEJBean(getCurrentClass())) {
            return true;
        }
        else {
            System.out.println("not concrete failed");
            log.debug("Skip bean " + clazz.getQualifiedName() + " because it's not a concrete bean.");
            return false;
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.GENERATING_REMOTE_FACADE_FOR,
            new String[]{getCurrentClass().getQualifiedName()}));
    }

}

