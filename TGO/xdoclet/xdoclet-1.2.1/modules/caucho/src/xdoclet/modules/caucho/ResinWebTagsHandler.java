/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.caucho;

import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import xjavadoc.XDoc;
import xjavadoc.XProgramElement;
import xjavadoc.XTag;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;

/**
 * Template tags handler for Resin web.xml generation.
 *
 * @author               Yoritaka Sakakura (yori@teardrop.org)
 * @created              June 5, 2002
 * @xdoclet.taghandler   namespace="Resin"
 */
public class ResinWebTagsHandler extends XDocletTagSupport
{
    private final static int WAIT_NAME = 0;
    private final static int IN_NAME = 1;
    private final static int WAIT_VALUE = 2;
    private final static int IN_VALUE = 3;

    private String  name;
    private String  value;


    private static boolean isDelimiter(char c)
    {
        return c == '"' || c == '=';
    }

    private static boolean isSpace(char c)
    {
        return Character.isSpaceChar(c);
    }

    private static boolean isSpecial(char c)
    {
        return isDelimiter(c) || isSpace(c);
    }


    /**
     * Iterates over all parameters of the current javadoc tag.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllCurrentTagParams(String template)
         throws XDocletException
    {
        clear();

        int state = WAIT_NAME;
        final StringBuffer name = new StringBuffer();
        final StringBuffer value = new StringBuffer();

        final String text = getCurrentTag().getValue();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            switch (state) {
            case WAIT_NAME:
                if (!isSpecial(c)) {
                    name.append(c);
                    state = IN_NAME;
                }
                break;
            case IN_NAME:
                if (c == '=')
                    state = WAIT_VALUE;
                else if (isSpace(c)) {
                    // peek ahead
                    int index = i + 1;

                    while (index < text.length()) {
                        char c2 = text.charAt(index);

                        if (isSpace(c2))
                            index++;
                        else if (c2 == '=') {
                            i = index;
                            state = WAIT_VALUE;
                            break;
                        }
                        else {
                            name.delete(0, name.length());
                            state = WAIT_NAME;
                            i = index - 1;
                            break;
                        }
                    }
                }
                else
                    name.append(c);
                break;
            case WAIT_VALUE:
                if (c == '"')
                    state = IN_VALUE;
                else if (!isSpace(c)) {
                    name.delete(0, name.length());
                    state = WAIT_NAME;
                }
                break;
            case IN_VALUE:
                if (c == '"') {
                    // empty values ok
                    this.name = name.toString();
                    this.value = value.toString();
                    name.delete(0, name.length());
                    value.delete(0, value.length());
                    state = WAIT_NAME;
                    generate(template);
                    clear();
                }
                else
                    value.append(c);
            }
        }
    }

    /**
     * Writes the current javadoc parameter as an xml element of the form: <PRE>
     * <parameter-name>
     *
     * parameter-value</parameter-name> </PRE> If the parameter maps to a non-empty value in the attributes, value is
     * used as the xml element name instead of the javadoc parameter name.
     *
     * @param props
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterAsElement(Properties props)
         throws XDocletException
    {
        String name = props.getProperty(parameterName());

        if (name == null || name.length() < 1)
            name = parameterName();

        StringBuffer buffer = new StringBuffer();

        buffer.append('<');
        buffer.append(name);
        buffer.append('>');
        buffer.append(parameterValue());
        buffer.append("</");
        buffer.append(name);
        buffer.append('>');

        return buffer.toString();
    }

    /**
     * Writes the current javadoc parameter as an xml element of the form: <PRE>
     * <init-param parameter-name="parameter-value"/>
     * </PRE>
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterAsInitParam()
         throws XDocletException
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<init-param ");
        buffer.append(parameterName());
        buffer.append("=\"");
        buffer.append(parameterValue());
        buffer.append("\"/>");

        return buffer.toString();
    }

    /**
     * Writes the current javadoc parameter as an element or an init-param, depending on the tag attributes; if the
     * parameter name is contained in the attributes, the 'element' form is used, else the 'init-parm' form.
     *
     * @param props
     * @return
     * @exception XDocletException
     * @see                         #parameterAsElement(java.util.Properties)
     * @see                         #parameterAsInitParam()
     * @doc.tag                     type="content"
     */
    public String parameterAsXml(Properties props)
         throws XDocletException
    {
        if (props.containsKey(parameterName()))
            return parameterAsElement(props);
        else
            return parameterAsInitParam();
    }

    /**
     * Returns the current javadoc parameter name.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterName()
         throws XDocletException
    {
        if (name == null)
            throw L.error(L.NO_CURRENT_PARAMETER);
        else
            return name;
    }

    /**
     * Returns the current javadoc parameter value.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String parameterValue()
         throws XDocletException
    {
        if (value == null)
            throw L.error(L.NO_CURRENT_PARAMETER);
        else
            return value;
    }

    /**
     * Writes an xml comment indicating the current method or class name.
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String sourceComment()
         throws XDocletException
    {
        XProgramElement doc = getCurrentMethod();

        if (doc == null)
            doc = getCurrentClass();
        if (doc == null)
            return "";
        else
            return "<!-- " + L.l(L.GENERATED_FROM, new String[]{doc.getName()}) + " -->";
    }

    private XTag getCurrentTag()
         throws XDocletException
    {
        XTag tag = getCurrentMethodTag();

        if (tag == null)
            tag = getCurrentClassTag();
        if (tag == null)
            throw L.error(L.NO_CURRENT_JAVADOC_TAG);
        return tag;
    }


    private void clear()
    {
        name = null;
        value = null;
    }
}
