/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.tagshandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="Parameter"
 * @version              $Revision: 1.18 $
 */
public class ParameterTagsHandler extends AbstractProgramElementTagsHandler
{

    /**
     * The current method's current parameter. forAllMethodParams sets the value while looping over the parameters of
     * current method.
     *
     * @see   #forAllMethodParams(java.lang.String)
     */
    protected static XParameter currentMethodParameter;

    /**
     * The <code>currentMethodParamTag</code> holds the current ParamTag corresponding to the current Parameter.
     *
     * @see   #forAllMethodParams(java.lang.String)
     */
    protected static XTag currentMethodParamTag;
    protected String currentName;

    public static String getMethodParamTypeFor(XParameter param)
    {
        return param.getType().getQualifiedName() + param.getDimensionAsString();
    }

    /**
     * Returns the type of the current method parameter, current method parameter is set inside a forAllMethodParams tag
     * in each iteration. Do not forget to add array dimensions if any.
     *
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodParamType(Properties attributes) throws XDocletException
    {
        return getMethodParamTypeFor(currentMethodParameter);
    }

    /**
     * The <code>methodParamDescription</code> method returns the comment text associated with the ParamTag for the
     * current Parameter
     *
     * @return                      a <code>String</code> value
     * @exception XDocletException  if an error occurs
     * @doc.tag                     type="content"
     */
    public String methodParamDescription() throws XDocletException
    {
        if (currentMethodParamTag == null) {
            return "no description";
        }
        // end of if ()

        return currentMethodParamTag.getValue();
    }

    /**
     * Returns the name of the current method parameter, current method parameter is set inside a forAllMethodParams tag
     * in each iteration.
     *
     * @return                      name of the current method parameter
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodParamName() throws XDocletException
    {
        return currentMethodParameter.getName();
    }

    /**
     * Iterates over all parameters of current method and evaluates the body of the tag for each method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void forAllMethodParams(String template) throws XDocletException
    {
        forAllParams(getCurrentMethod(), template);
    }

    /**
     * Iterates over all parameters of current constructor and evaluates the body of the tag for each method.
     *
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     */
    public void forAllConstructorParams(String template) throws XDocletException
    {
        forAllParams(getCurrentConstructor(), template);
    }

    /**
     * Evaluates the body of the tag if current method/constructor has parameters.
     *
     * @param template              The body of the block tag
     * @param attributes
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="forConstructor" optional="true" values="true,false" description="If true, then
     *      look for parameters of current constructor instead of current method"
     */
    public void ifHasParams(String template, Properties attributes) throws XDocletException
    {
        String constr = (String) attributes.get("forConstructor");

        Collection parameters;

        if ("true".equals(constr)) {
            parameters = getCurrentConstructor().getParameters();
        }
        else {
            parameters = getCurrentMethod().getParameters();
        }

        if (parameters != null && parameters.size() > 0)
            generate(template);
    }

    /**
     * Iterates over all parameters in current method and returns a string containing definition of all those
     * parameters.
     *
     * @param attributes            The attributes of the template tag
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="includeDefinition" optional="true" values="true,false" description="If true
     *      then include the parameter type of parameters in the composed string."
     * @doc.param                   name="forConstructor" optional="true" values="true,false" description="If true, then
     *      look for parameters of current constructor instead of current method"
     */
    public String parameterList(Properties attributes) throws XDocletException
    {
        boolean incl = TypeConversionUtil.stringToBoolean(attributes.getProperty("includeDefinition"), true);
        boolean constr = TypeConversionUtil.stringToBoolean(attributes.getProperty("forConstructor"), false);

        Collection parameters;

        if (constr == true) {
            parameters = getCurrentConstructor().getParameters();
        }
        else {
            parameters = getCurrentMethod().getParameters();
        }

        StringBuffer sbuf = new StringBuffer();
        String type = null;
        String name = null;

        boolean comma = false;

        for (Iterator i = parameters.iterator(); i.hasNext(); ) {
            XParameter parameter = (XParameter) i.next();

            type = getMethodParamTypeFor(parameter);

            name = parameter.getName();
            if (type == null) {
                throw new XDocletException("FATAL:" + name);
            }

            if (comma) {
                sbuf.append(',');
            }

            if (incl == true) {
                sbuf.append(type).append(' ').append(name);
            }
            else {
                sbuf.append(name);
            }

            comma = true;
        }

        String result = sbuf.toString();

        return result;
    }

    /**
     * Gets the value of the parameter specified by paramName of current tag, and assuming the value has the format of a
     * typical method definition extracts of parameter types out of it and evaluates the body for each parameter type.
     * current parameter type can be accessed as &lt;XDtParameter:currentToken/&gt;. Also gives back parameter name as
     * &lt;XDtParameter:currentName/&gt;
     *
     * @param attributes            The attributes of the template tag
     * @param template              The body of the block tag
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="block"
     * @doc.param                   name="paramName" optional="false" description="The parameter name that its value is
     *      used for extracting parameter types out of it."
     */
    public void forAllParameterTypes(String template, Properties attributes) throws XDocletException
    {
        String paramName = attributes.getProperty("paramName");
        String value = getCurrentClassTag().getAttributeValue(paramName);
        String oldToken = currentToken;

        // findAll(int p1, int p2) -> int p1, int p2
        value = value.substring(value.indexOf('(') + 1, value.lastIndexOf(')'));

        StringTokenizer st = new StringTokenizer(value, ",", false);
        int tokenNr = 0;

        while (st.hasMoreTokens()) {
            tokenNr++;
            currentToken = st.nextToken().trim();

            int spaceposBetweenTypeAndName = currentToken.lastIndexOf(' ');

            spaceposBetweenTypeAndName = spaceposBetweenTypeAndName == -1 ? currentToken.lastIndexOf('\t') : spaceposBetweenTypeAndName;

            if (spaceposBetweenTypeAndName != -1) {
                currentName = currentToken.substring(spaceposBetweenTypeAndName).trim();
                currentToken = currentToken.substring(0, spaceposBetweenTypeAndName).trim();
            }
            else {
                currentName = "param" + tokenNr;
            }

            generate(template);
        }

        currentToken = oldToken;
    }

    /**
     * return name of parameter currently being iterated - ugly hack...
     *
     * @return
     * @doc.tag   type="content"
     */
    public String currentName()
    {
        return currentName;
    }

    /**
     * Describe what the method does
     *
     * @param member                Describe what the parameter does
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    private void forAllParams(XExecutableMember member, String template) throws XDocletException
    {
        Collection parameters = member.getParameters();
        Collection paramTags = member.getDoc().getTags("param");

        for (Iterator k = parameters.iterator(); k.hasNext(); ) {
            currentMethodParameter = (XParameter) k.next();
            currentMethodParamTag = null;
            for (Iterator tagIterator = paramTags.iterator(); tagIterator.hasNext(); ) {
                // find @param xxx
                XTag paramTag = (XTag) tagIterator.next();
                String paramTagValue = paramTag.getValue();
                StringTokenizer st = new StringTokenizer(paramTagValue);
                String paramTagParam = null;

                if (st.hasMoreTokens()) {
                    paramTagParam = st.nextToken();
                }

                if (currentMethodParameter.getName().equals(paramTagParam)) {
                    currentMethodParamTag = paramTag;
                    break;
                }
            }
            generate(template);
        }
    }
}
