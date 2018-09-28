/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.DocletContext;
import xdoclet.SubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.template.PrettyPrintWriter;
import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Oct 15, 2001
 * @version   $Revision: 1.15 $
 */
public abstract class AbstractProgramElementTagsHandler extends XDocletTagSupport
{
    /**
     * The current token. Currently forAllParameterTypes and forAllClassTagTokens set it and currentToken returns the
     * value. Tokens are computed for cases where the value should be tokenized by a set of delimiters.
     */
    protected static String currentToken;

    /**
     * The StringTokenizer object doing the tokenization. It should be shared by different tag implementations, that's
     * why its defined class-level.
     */
    protected static StringTokenizer tagTokenizer;

    /**
     * Template can use matchPattern as a place where they can put volatile variable. You can set the value somewhere in
     * the template and later use the value.
     */
    protected static String matchPattern;

    /**
     * Returns the not-full-qualified name of the current class without the package name.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @doc.tag      type="content"
     */
    public static String getClassNameFor(XClass clazz)
    {
        return clazz.getName();
    }

    /**
     * Returns the full-qualified name of the current class with the package name.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @doc.tag      type="content"
     */
    public static String getFullClassNameFor(XClass clazz)
    {
        return clazz.getQualifiedName();
    }

    /**
     * Returns the full-qualified name of the superclass of the current class.
     *
     * @param clazz  Description of Parameter
     * @return       Description of the Returned Value
     * @doc.tag      type="content"
     */
    public static String getFullSuperclassNameFor(XClass clazz)
    {
        if (clazz.getSuperclass() != null) {
            return clazz.getSuperclass().getQualifiedName();
        }
        else {
            return "java.lang.Object";
        }
    }

    /**
     * Utility method to get classes for iteration used by various methods. The result depends on the context: are we
     * within a forAllPackages iteration or not.
     *
     * @return   An array with all classes in that context in it.
     */
    public static Collection getAllClasses()
    {
        if (DocletContext.getInstance().getActiveSubTask().getCurrentPackage() == null) {
            // not in a forAllPackages context
            return getXJavaDoc().getSourceClasses();
        }
        else {
            return DocletContext.getInstance().getActiveSubTask().getCurrentPackage().getClasses();
        }
    }

    /**
     * @param clazz
     * @param executableMemberName
     * @param parameters
     * @param setCurrentExecutableMember
     * @param forType
     * @return
     * @exception XDocletException
     * @todo                              Remove. For archeologists only
     */
    protected static boolean hasExecutableMember_OLD(XClass clazz, String executableMemberName, String[] parameters, boolean setCurrentExecutableMember, int forType)
         throws XDocletException
    {
        Log log = LogUtil.getLog(AbstractProgramElementTagsHandler.class, "hasExecutableMember");

        while (clazz != null) {
            Collection executableMembers = null;

            switch (forType) {
            case FOR_CONSTRUCTOR:
                executableMembers = clazz.getConstructors();
                break;
            case FOR_METHOD:
                executableMembers = clazz.getMethods();
                break;
            default:
                throw new XDocletException("Bad type: " + forType);
            }

            int j = 0;

            loop :
            for (Iterator m = executableMembers.iterator(); m.hasNext(); ) {
                XExecutableMember executableMember = (XExecutableMember) m.next();

                if (executableMember.getName().equals(executableMemberName)) {
                    // All parameters must be equal too
                    if (parameters != null) {
                        Collection params = executableMember.getParameters();

                        log.debug("params.length=" + params.size());

                        for (Iterator p = params.iterator(); p.hasNext(); ) {
                            XParameter param = (XParameter) p.next();

                            String paramType = new StringBuffer(param.getType().getQualifiedName()).append(param.getDimensionAsString()).toString();

                            if (log.isDebugEnabled()) {
                                log.debug("params[j]=" + paramType);
                                log.debug("parameters[j]=" + parameters[j]);
                            }
                            if (paramType.equals(parameters[j++])) {
                                continue loop;
                            }
                        }
                    }

                    // The class has the given executable member
                    if (setCurrentExecutableMember) {
                        switch (forType) {
                        case FOR_CONSTRUCTOR:
                            setCurrentConstructor((XConstructor) executableMember);
                            break;
                        case FOR_METHOD:
                            setCurrentMethod((XMethod) executableMember);
                            break;
                        default:
                            throw new XDocletException("Bad type: " + forType);
                        }
                    }
                    return true;
                }
            }

            // Check super class info
            clazz = clazz.getSuperclass();
        }

        return false;
    }

    /**
     * Used to protect returned arrays from being modified (sorted, reordered for example).
     *
     * @param objects
     * @return
     */
    protected static Object[] makeCopyOfArray(Object[] objects)
    {
        Object[] objects_copy = (Object[]) java.lang.reflect.Array.newInstance(objects.getClass().getComponentType(), objects.length);

        System.arraycopy(objects, 0, objects_copy, 0, objects.length);

        return objects_copy;
    }

    protected static boolean hasExecutableMember(XClass clazz, String executableMemberName, String[] parameters, boolean setCurrentExecutableMember, int forType)
         throws XDocletException
    {
        Log log = LogUtil.getLog(AbstractProgramElementTagsHandler.class, "hasExecutableMember");

        StringBuffer executableMemberNameWithSignature = new StringBuffer(executableMemberName).append("(");
        boolean comma = false;

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (comma) {
                    executableMemberNameWithSignature.append(',');
                }
                executableMemberNameWithSignature.append(getXJavaDoc().getXClass(parameters[i]).getQualifiedName());
            }
        }
        executableMemberNameWithSignature.append(')');

        XExecutableMember executableMember = null;

        switch (forType) {
        case FOR_CONSTRUCTOR:
            executableMember = clazz.getConstructor(executableMemberNameWithSignature.toString());
            break;
        case FOR_METHOD:
            executableMember = clazz.getMethod(executableMemberNameWithSignature.toString(), true);
            break;
        default:
            throw new XDocletException("Bad type: " + forType);
        }
        if (setCurrentExecutableMember && executableMember != null) {
            switch (forType) {
            case FOR_CONSTRUCTOR:
                setCurrentConstructor((XConstructor) executableMember);
                break;
            case FOR_METHOD:
                setCurrentMethod((XMethod) executableMember);
                break;
            default:
                throw new XDocletException("Bad type: " + forType);
            }
        }

        return executableMember != null;
    }

    /**
     * Sets the vaSoulue of match variable.
     *
     * @param template              The body of the block tag
     * @param attributes            The attributes of the template tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     * @doc.param                   name="value" optional="false" description="The new value for matchPattern."
     */
    public void setMatchValue(String template, Properties attributes) throws XDocletException
    {
        matchPattern = attributes.getProperty("value");
        generate(template);
        matchPattern = null;
    }

    /**
     * Returns the value of match variable. Match variable serves as a variable for templates, you set it somewhere in
     * template and look it up somewhere else in template.
     *
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String matchValue() throws XDocletException
    {
        return matchPattern;
    }

    /**
     * Returns current token inside forAllClassTagTokens.
     *
     * @param attributes            The attributes of the template tag
     * @return                      value of currently processed token
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String currentToken(Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(SubTask.class, "currentToken");

        log.debug("current token:  " + currentToken);

        if (currentToken == null) {
            log.error("null token found");
            return "";
        }
        else {
            return currentToken;
        }
    }

    /**
     * Skips current token. Returns empty string.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Empty string
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String skipToken(Properties attributes) throws XDocletException
    {
        if (tagTokenizer.hasMoreTokens()) {
            tagTokenizer.nextToken();
        }

        return "";
    }

    /**
     * Gets the XExecutableMemberForMemberName attribute of the AbstractProgramElementTagsHandler object
     *
     * @param memberName            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @return                      The XExecutableMemberForMemberName value
     * @exception XDocletException  Describe the exception
     */
    protected XExecutableMember getXExecutableMemberForMemberName(String memberName, int forType) throws XDocletException
    {
        if (memberName != null) {
            return extractXExecutableMember(getCurrentClass(), memberName, forType);
        }

        return null;
    }

    /**
     * Searches for the XExecutableMember of the member with name methodName and returns it.
     *
     * @param superclasses          Search superclasses.
     * @param memberName
     * @param forType
     * @return                      The XMethod for the method named value
     * @exception XDocletException
     */
    protected XExecutableMember getXExecutableMemberForMemberName(String memberName, boolean superclasses, int forType) throws XDocletException
    {
        if (!superclasses) {
            return getXExecutableMemberForMemberName(memberName, forType);
        }

        for (XClass clazz = getCurrentClass(); clazz != null; clazz = clazz.getSuperclass()) {
            XExecutableMember member = extractXExecutableMember(clazz, memberName, forType);

            if (member != null) {
                return member;
            }
        }
        return null;
    }

    /**
     * A utility method to get the blank space characters used for indenting comments.
     *
     * @param attributes  The attributes of the template tag
     * @return            The IndentChars value
     * @see               MethodTagsHandler#methodComment(java.util.Properties)
     * @see               ClassTagsHandler#classComment(java.util.Properties)
     */
    protected char[] getIndentChars(Properties attributes)
    {
        String indentStr = attributes.getProperty("indent");

        if (indentStr == null) {
            return new char[0];
        }

        int indent = new Integer(indentStr).intValue();
        char[] spaces = new char[indent];

        Arrays.fill(spaces, ' ');
        return spaces;
    }

    /**
     * Describe what the method does
     *
     * @param attributes            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    protected String exceptionList(Properties attributes, int forType) throws XDocletException
    {
        String skipExceptions = attributes.getProperty("skip");
        String appendExceptions = attributes.getProperty("append");
        String memberName = null;
        Collection exceptions = null;

        XExecutableMember executableMember = null;

        switch (forType) {
        case FOR_CONSTRUCTOR:
            executableMember = getCurrentConstructor();
            memberName = attributes.getProperty("constructor");
            break;
        case FOR_METHOD:
            executableMember = getCurrentMethod();
            memberName = attributes.getProperty("method");
            break;
        default:
            throw new XDocletException("Can't forAll for type " + forType);
        }

        if (executableMember == null && memberName == null) {
            exceptions = new ArrayList();
        }

        if (memberName == null) {
            exceptions = executableMember.getThrownExceptions();
        }
        else {
            executableMember = getXExecutableMemberForMemberName(memberName, true, forType);

            // no member with the specified name found in class
            if (executableMember != null) {
                exceptions = executableMember.getThrownExceptions();
            }
            else {
                exceptions = new ArrayList();
            }
        }

        StringBuffer sbuf = new StringBuffer();
        String type = null;

        for (Iterator i = exceptions.iterator(); i.hasNext(); ) {
            type = ((XClass) i.next()).getQualifiedName();

            if (isInSkipExceptionsList(skipExceptions, type) == false &&
                isInAppendExceptionsList(appendExceptions, type) == false) {
                appendException(sbuf, type);
            }
        }

        // append all exceptions specfied to be always appended by default
        if (appendExceptions != null) {
            appendException(sbuf, appendExceptions);
        }

        return sbuf.toString();
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @param for_type              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    protected void forAllMemberTagTokens(String template, Properties attributes, int for_type) throws XDocletException
    {
        Log log = LogUtil.getLog(MethodTagsHandler.class, "forAllMemberTagTokens");

        String tagValue;

        tagValue = getTagValue(attributes, for_type);

        String delimiter = attributes.getProperty("delimiter");
        String skipStr = attributes.getProperty("skip");
        int skip;

        try {
            skip = Integer.valueOf(skipStr).intValue();
        }
        catch (Throwable t) {
            skip = 0;
        }

        if (delimiter == null) {
            log.debug("got null delimiter - forAllMemberTagTokens");
            delimiter = PARAMETER_DELIMITER;
        }

        log.debug("Tag Value = " + tagValue);

        tagTokenizer = new StringTokenizer(tagValue, delimiter, false);
        currentToken = "";
        matchPattern = null;

        for (int i = 0; tagTokenizer.hasMoreTokens() && i < skip; i++) {
            tagTokenizer.nextToken();
        }

        while (tagTokenizer.hasMoreTokens()) {
            currentToken = tagTokenizer.nextToken();

            log.debug("generate current token: " + currentToken);

            generate(template);
        }

        currentToken = null;
        tagTokenizer = null;
        matchPattern = null;
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @param resourceKey           Describe what the parameter does
     * @param arguments             Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    protected void forAllMemberTags(String template, Properties attributes, int forType, String resourceKey, String[] arguments) throws XDocletException
    {
        Log log = LogUtil.getLog(AbstractProgramElementTagsHandler.class, "forAllMemberTags");
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), true);
        XMember member = null;

        switch (forType) {
        case FOR_FIELD:
            member = getCurrentField();
            break;
        case FOR_CONSTRUCTOR:
            member = getCurrentConstructor();
            break;
        case FOR_METHOD:
            member = getCurrentMethod();
            break;
        default:
            throw new XDocletException("Bad type " + forType);
        }

        if (member == null) {
            throw new XDocletException(Translator.getString(XDocletTagshandlerMessages.class, resourceKey, arguments));
        }

        /*
         * If the tagName contains a "|" it's to support deprecated tags.
         * If we find an occurrance for the first tag, we won't loop over the second one.
         */
        StringTokenizer st = new StringTokenizer(attributes.getProperty("tagName"), "|");
        boolean found = false;

        while (st.hasMoreTokens() && !found) {
            String tagName = st.nextToken();
            Collection tags = member.getDoc().getTags(tagName, superclasses);

            for (Iterator i = tags.iterator(); i.hasNext(); ) {
                found = true;

                XTag tag = (XTag) i.next();

                if (forType == FOR_METHOD || forType == FOR_CONSTRUCTOR) {
                    setCurrentMethodTag(tag);
                }
                else {
                    setCurrentFieldTag(tag);
                }

                String m = getTagValue(attributes, forType);

                if (log.isDebugEnabled()) {
                    log.debug((getCurrentMethod() == null) ? "<no current method>" : getCurrentMethod().getName() + " ** Tag/Param = "
                        + attributes.getProperty("tagName") + '/' + attributes.getProperty("paramName")
                        + "  ** Value = " + m
                        + " MatchPattern = " + matchPattern);
                }

                if (matchPattern == null) {
                    generate(template);
                }
                else if (matchPattern != null && (matchPattern.equals(m) || m.equals("*"))) {
                    generate(template);
                }
            }

            if (forType == FOR_METHOD || forType == FOR_CONSTRUCTOR) {
                setCurrentMethodTag(null);
            }
            else {
                setCurrentFieldTag(null);
            }
        }
    }

    /**
     * @param attributes            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     * @todo                        There is similar functionality in xjavadoc.XDoc. Use that instead (needs a little
     *      rework to be more flexible).
     */
    protected String memberComment(Properties attributes, int forType) throws XDocletException
    {
        String noCommentSigns = attributes.getProperty("no-comment-signs");

        XMember member = null;

        switch (forType) {
        case FOR_FIELD:
            member = getCurrentField();
            break;
        case FOR_CONSTRUCTOR:
            member = getCurrentConstructor();
            break;
        case FOR_METHOD:
            member = getCurrentMethod();
            break;
        default:
            throw new XDocletException("Bad type " + forType);
        }

        if (noCommentSigns != null && noCommentSigns.equalsIgnoreCase("true")) {
            return member.getDoc().getCommentText();
        }

        char[] spaces = getIndentChars(attributes);
        Collection memberTags = member.getDoc().getTags();

        if (memberTags.size() > 0) {
            StringBuffer sbuf = new StringBuffer();

            // add user comments
            StringTokenizer st = new StringTokenizer(member.getDoc().getCommentText().trim(), "\n", false);

            if (st.countTokens() > 0) {
                sbuf.append(spaces).append("/**").append(PrettyPrintWriter.LINE_SEPARATOR);
                while (st.hasMoreTokens()) {
                    sbuf.append(spaces).append(" * ").append(st.nextToken().trim()).append(PrettyPrintWriter.LINE_SEPARATOR);
                }

                for (Iterator i = memberTags.iterator(); i.hasNext(); ) {
                    XTag memberTag = (XTag) i.next();
                    // all of our xdoclet-specific tags have a ":" or "."
                    String memberTagName = memberTag.getName();

                    if (memberTagName.indexOf(':') == -1 && memberTagName.indexOf('.') == -1
                        && getDocletContext().getExcludedTags().indexOf(memberTagName) == -1) {
                        sbuf.append(spaces).append(" * ")
                            .append('@').append(memberTag.getName()).append(' ')
                            .append(memberTag.getValue());

                        // for all lines but not the last line
                        if (i.hasNext()) {
                            sbuf.append(PrettyPrintWriter.LINE_SEPARATOR);
                        }
                    }
                }
                sbuf.append(spaces).append(" */");
            }

            return sbuf.toString();
        }
        else {
            return "";
        }
    }

    /**
     * Describe what the method does
     *
     * @param member                Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    protected String firstSentenceDescriptionOfCurrentMember(XMember member) throws XDocletException
    {
        return member.getDoc().getFirstSentence() != null ? member.getDoc().getFirstSentence() : "";
    }

    /**
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @todo                        the already Set contains XMember objects. equals/hashCode should be defined in
     *      XMember and be implemented in all of the implementing classes.
     */
    protected void forAllMembers(String template, Properties attributes, int forType) throws XDocletException
    {
        boolean superclasses = TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"), true);
        boolean sort = TypeConversionUtil.stringToBoolean(attributes.getProperty("sort"), true);
        XClass currentClass = getCurrentClass();

        if (currentClass == null) {
            throw new XDocletException("currentClass == null!!!");
        }

        Collection members = null;

        switch (forType) {
        case FOR_FIELD:
            members = currentClass.getFields(superclasses);
            break;
        case FOR_CONSTRUCTOR:
            members = currentClass.getConstructors();
            break;
        case FOR_METHOD:
            members = currentClass.getMethods(superclasses);
            break;
        default:
            throw new XDocletException("Bad type: " + forType);
        }

        if (sort) {
            // sort fields, but we should make a copy first, because members is not a new copy, it's shared by all
            List sortedMembers = new ArrayList(members);

            members = sortedMembers;
        }

        for (Iterator j = members.iterator(); j.hasNext(); ) {
            XMember member = (XMember) j.next();

            switch (forType) {
            case FOR_FIELD:
                setCurrentField((XField) member);
                break;
            case FOR_CONSTRUCTOR:
                setCurrentConstructor((XConstructor) member);
                break;
            case FOR_METHOD:
                setCurrentMethod((XMethod) member);
                break;
            default:
                throw new XDocletException("Bad type: " + forType);
            }

            setCurrentClass(member.getContainingClass());
            generate(template);
        }
        setCurrentClass(currentClass);

    }

    /**
     * A utility method used by firstSentenceDescription to replace end of line by space.
     *
     * @param pText  Description of Parameter
     * @return       Description of the Returned Value
     */
    protected String checkForWrap(String pText)
    {
        int lIndex = pText.indexOf(PrettyPrintWriter.LINE_SEPARATOR);

        while (lIndex >= 0) {
            if (lIndex < pText.length() - 1) {
                pText = new StringBuffer(pText.substring(0, lIndex).trim()).append(' ').append(pText.substring(lIndex + 1).trim()).toString();
            }
            else {
                pText = pText.substring(0, lIndex);
            }

            lIndex = pText.indexOf(PrettyPrintWriter.LINE_SEPARATOR);
        }

        // Avoid any trailing spaces
        return pText.trim();
    }

    /**
     * Gets the InAppendExceptionsList attribute of the AbstractProgramElementTagsHandler object
     *
     * @param appendExceptions  Describe what the parameter does
     * @param type              Describe what the parameter does
     * @return                  The InAppendExceptionsList value
     */
    private boolean isInAppendExceptionsList(String appendExceptions, String type)
    {
        if (appendExceptions == null) {
            return false;
        }
        else {
            return appendExceptions.indexOf(type) != -1;
        }
    }

    /**
     * Gets the InSkipExceptionsList attribute of the AbstractProgramElementTagsHandler object
     *
     * @param skipExceptions  Describe what the parameter does
     * @param type            Describe what the parameter does
     * @return                The InSkipExceptionsList value
     */
    private boolean isInSkipExceptionsList(String skipExceptions, String type)
    {
        if (skipExceptions == null) {
            return false;
        }
        else {
            return skipExceptions.indexOf(type) != -1;
        }
    }

    /**
     * Describe what the method does
     *
     * @param sbuf  Describe what the parameter does
     * @param type  Describe what the parameter does
     */
    private void appendException(StringBuffer sbuf, String type)
    {
        if (sbuf.length() == 0) {
            sbuf.append("throws ").append(type);
        }
        else {
            sbuf.append(", ").append(type);
        }
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @param memberName            Describe what the parameter does
     * @param forType               Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    private XExecutableMember extractXExecutableMember(XClass clazz, String memberName, int forType) throws XDocletException
    {
        Collection executableMembers = null;

        switch (forType) {
        case FOR_CONSTRUCTOR:
            executableMembers = clazz.getConstructors();
            break;
        case FOR_METHOD:
            executableMembers = clazz.getMethods();
            break;
        default:
            throw new XDocletException("Bad type: " + forType);
        }

        for (Iterator i = executableMembers.iterator(); i.hasNext(); ) {
            XExecutableMember executableMember = (XExecutableMember) i.next();

            if (executableMember.getName().equals(memberName)) {
                return executableMember;
            }
        }

        return null;
    }
}
