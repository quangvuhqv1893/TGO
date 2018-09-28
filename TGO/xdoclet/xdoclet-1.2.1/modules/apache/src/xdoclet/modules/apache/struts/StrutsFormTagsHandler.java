/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.apache.struts;

import java.beans.Introspector;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import xjavadoc.MethodIterator;
import xjavadoc.TagIterator;
import xjavadoc.XClass;
import xjavadoc.XCollections;
import xjavadoc.XMethod;
import xjavadoc.XTag;
import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.apache.struts.ejb.StrutsFormSubTask;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.entity.PersistentTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.LogUtil;

/**
 * @author               Dmitri Colebatch (dim@bigpond.net.au)
 * @created              Oct 19, 2001
 * @xdoclet.taghandler   namespace="StrutsForm"
 * @version              $Revision: 1.11 $
 */
public class StrutsFormTagsHandler extends EjbTagsHandler
{
    /**
     * Gets the StrutsFormClassFor attribute of the StrutsFormTagsHandler class.
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The StrutsFormClassFor value
     * @exception XDocletException  Describe the exception
     */
    public static String getStrutsFormClassFor(XClass clazz) throws XDocletException
    {
        String packageName = clazz.getContainingPackage().getName();

        packageName = choosePackage(packageName, null, DocletTask.getSubTaskName(StrutsFormSubTask.class));
        return packageName + '.' + getStrutsFormClassName(clazz);
    }

    /**
     * Gets the StrutsFormClassName attribute of the StrutsFormTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @return                      The StrutsFormClassName value
     * @exception XDocletException  Describe the exception
     */
    public static String getStrutsFormClassName(XClass clazz) throws XDocletException
    {
        XTag currentTag = ((StrutsFormSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(StrutsFormSubTask.class))).getCurrentFormTag();

        // check if there is a name parameter
        String name = currentTag.getAttributeValue("name");

        if (name == null) {
            return getShortEjbNameFor(clazz) + "Form";
        }
        else {
            return MessageFormat.format(getStrutsFormClassPattern(), new Object[]{getShortEjbNameFor(clazz), name});
        }
    }

    /**
     * Return true if at least one struts:form tag is defined.
     *
     * @param clazz                 Class to check
     * @return                      whether class has struts:form tag defined
     * @exception XDocletException  Description of Exception
     */
    public static boolean hasFormDefinition(XClass clazz) throws XDocletException
    {
        return clazz.getDoc().hasTag("struts:form", false);
    }

    /**
     * Gets the StrutsFormClassPattern attribute of the StrutsFormTagsHandler class
     *
     * @return   The StrutsFormClassPattern value
     */
    protected static String getStrutsFormClassPattern()
    {
        return ((StrutsFormSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(StrutsFormSubTask.class))).getStrutsFormClassPattern();
    }

    /**
     * Return the class name for the current class.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String strutsFormClass() throws XDocletException
    {
        return getStrutsFormClassFor(getCurrentClass());
    }

    /**
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String strutsFormName() throws XDocletException
    {
        XTag currentTag = ((StrutsFormSubTask) getDocletContext().getSubTaskBy(DocletTask.getSubTaskName(StrutsFormSubTask.class))).getCurrentFormTag();
        String formName = currentTag.getAttributeValue("name");

        if (formName == null || formName.trim().length() == 0) {
            return Introspector.decapitalize(getEjbNameFor(getCurrentClass()) + "Form");
        }
        else {
            return Introspector.decapitalize(getEjbNameFor(getCurrentClass()) + '.' + formName);
        }
    }

    /**
     * Evaluates body for all fields included in form generation
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void forAllFormFields(String template) throws XDocletException
    {
        // all fields carrying @struts:form-field form-name="<bla>" where <bla> is current
        // form name, or all persistent fields if  include-all="true" is set
        // pk fields are included implicitly, unless include-pk="false" is specified.

        Log log = LogUtil.getLog(StrutsFormTagsHandler.class, "forAllFormFields");
        XClass currentClass = getCurrentClass();
        Map foundFields = new HashMap();

        if (log.isDebugEnabled()) {
            log.debug("BEGIN-----------------------------------------");
        }

        do {
            pushCurrentClass(currentClass);

            if (log.isDebugEnabled()) {
                log.debug("-----CLASS=" + getCurrentClass().getName() + "----------------");
            }

            Collection methods = getCurrentClass().getMethods();

            for (MethodIterator j = XCollections.methodIterator(methods); j.hasNext(); ) {
                setCurrentMethod(j.next());
                // We are interested in persistent fields and fields marked as a form-field.
                if (MethodTagsHandler.isGetter(getCurrentMethod().getName()) &&
                    !foundFields.containsKey(getCurrentMethod().getName()) &&
                    useMethodInForm(getCurrentMethod())) {
                    if (useMethodInForm(getCurrentMethod())) {
                        if (log.isDebugEnabled()) {
                            log.debug("METHOD(I=" + getCurrentMethod().getName());
                        }
                        // Store that we found this field so we don't add it twice
                        foundFields.put(getCurrentMethod().getName(), getCurrentMethod().getName());

                        generate(template);
                    }
                }
            }

            // Add super class info
            if (getCurrentClass().getSuperclass().getQualifiedName().equals("java.lang.Object")) {
                popCurrentClass();
                break;
            }

            popCurrentClass();
            currentClass = currentClass.getSuperclass();
        } while (true);

        if (log.isDebugEnabled()) {
            log.debug("END-------------------------------------------");
        }
    }

    /**
     * Evaluates the body if the method belongs in a given form.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifUseMethodInForm(String template) throws XDocletException
    {
        if (useMethodInForm(getCurrentMethod()))
            generate(template);
    }

    /**
     * Check that method has struts:form-field tag with valid name, or is pk field (and pk fields are included) or
     * include-all="true".
     *
     * @param method                Description of Parameter
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     */
    protected boolean useMethodInForm(XMethod method) throws XDocletException
    {
        // check for include-all
        XTag currentTag = ((StrutsFormSubTask) getDocletContext().getSubTaskBy(DocletTask.getSubTaskName(StrutsFormSubTask.class))).getCurrentFormTag();
        String value = currentTag.getAttributeValue("include-all");

        // by default, include all is false
        if (value != null && value.equals("true")) {
            return true;
        }

        // include all pk fields unless include-pk="false"
        value = currentTag.getAttributeValue("include-pk");
        if (PersistentTagsHandler.isPkField(method) && (value != null && value.equals("true"))) {
            return true;
        }

        // check for explicit inclusion
        Collection mTags = method.getDoc().getTags("struts:form-field");

        for (TagIterator i = XCollections.tagIterator(mTags); i.hasNext(); ) {
            XTag mTag = i.next();
            String pname = mTag.getAttributeValue("form-name");
            String fname = currentTag.getAttributeValue("name");

            if (pname != null && fname != null && fname.equals(pname)) {
                return true;
            }
        }

        // no need in such field...
        return false;
    }
}
