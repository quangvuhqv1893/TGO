/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.web;

import xdoclet.XDocletException;
import xdoclet.XDocletMessages;

import xdoclet.XmlSubTask;
import xdoclet.util.Translator;

/**
 * Generates taglib.tld deployment descriptor for JSP taglibs.
 *
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              July 12, 2001
 * @ant.element          display-name="JSP Taglib" name="jsptaglib" parent="xdoclet.modules.web.WebDocletTask"
 * @version              $Revision: 1.13 $
 * @xdoclet.merge-file   file="taglib-settings.xml" relates-to="taglib.tld" description="Unparsed XML entity containing
 *      (tlib-version, jsp-version, short-name, uri?, display-name?, small-icon?, large-icon?, description?) elements."
 * @xdoclet.merge-file   file="taglib-validator.xml" relates-to="taglib.tld" description="XML document containing the
 *      validator element, used to include a validator which isn't processed by XDoclet."
 * @xdoclet.merge-file   file="taglib-listener.xml" relates-to="taglib.tld" description="Unparsed XML entity containing
 *      additional listener elements, for including listeners not processed by XDoclet."
 * @xdoclet.merge-file   file="taglib-tag.xml" relates-to="taglib.tld" description="Unparsed XML entity containing
 *      additional tag elements, for including tags not processed by XDoclet."
 */
public class JspTaglibSubTask extends XmlSubTask
{
    private static String DEFAULT_TEMPLATE_FILE = "resources/taglib_tld.xdt";

    private static String TLD_PUBLICID_1_2 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN";

    private static String TLD_PUBLICID_1_1 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN";

    private static String TLD_SYSTEMID_1_2 = "http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd";

    private static String TLD_SYSTEMID_1_1 = "http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd";

    private static String TLD_DTD_FILE_NAME_1_2 = "resources/web-jsptaglibrary_1_2.dtd";

    private static String TLD_DTD_FILE_NAME_1_1 = "resources/web-jsptaglibrary_1_1.dtd";

    protected String taglibversion = "1.0";

    protected String jspversion = JspVersionTypes.VERSION_1_2;

    protected String shortname = "";

    protected String uri = "";

    protected String displayname = "";

    protected String smallicon = "";

    protected String largeicon = "";

    protected String description = "";

    protected String filename = "taglib.tld";

    /**
     * Describe what the JspTaglibSubTask constructor does
     */
    public JspTaglibSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(filename);

        setPublicId(TLD_PUBLICID_1_2);
        setSystemId(TLD_SYSTEMID_1_2);
        setDtdURL(getClass().getResource(TLD_DTD_FILE_NAME_1_2));
    }

    /**
     * Gets the Jspversion attribute of the JspTaglibSubTask object
     *
     * @return   The Jspversion value
     */
    public String getJspversion()
    {
        return jspversion;
    }

    /**
     * Gets the Taglibversion attribute of the JspTaglibSubTask object
     *
     * @return   The Taglibversion value
     */
    public String getTaglibversion()
    {
        return taglibversion;
    }

    /**
     * Gets the Shortname attribute of the JspTaglibSubTask object
     *
     * @return   The Shortname value
     */
    public String getShortname()
    {
        return shortname;
    }

    /**
     * Gets the Uri attribute of the JspTaglibSubTask object
     *
     * @return   The Uri value
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * Gets the Displayname attribute of the JspTaglibSubTask object
     *
     * @return   The Displayname value
     */
    public String getDisplayname()
    {
        return displayname;
    }

    /**
     * Gets the Smallicon attribute of the JspTaglibSubTask object
     *
     * @return   The Smallicon value
     */
    public String getSmallicon()
    {
        return smallicon;
    }

    /**
     * Gets the Largeicon attribute of the JspTaglibSubTask object
     *
     * @return   The Largeicon value
     */
    public String getLargeicon()
    {
        return largeicon;
    }

    /**
     * Gets the Description attribute of the JspTaglibSubTask object
     *
     * @return   The Description value
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the Filename attribute of the JspTaglibSubTask object
     *
     * @return   The Filename value
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Sets the Jspversion attribute of the JspTaglibSubTask object
     *
     * @param jspversion  The new Jspversion value
     */
    public void setJspversion(JspVersionTypes jspversion)
    {
        this.jspversion = jspversion.getValue();
    }

    /**
     * Sets the Taglibversion attribute of the JspTaglibSubTask object
     *
     * @param taglibversion  The new Taglibversion value
     */
    public void setTaglibversion(String taglibversion)
    {
        this.taglibversion = taglibversion;
    }

    /**
     * Sets the Shortname attribute of the JspTaglibSubTask object
     *
     * @param shortname  The new Shortname value
     */
    public void setShortname(String shortname)
    {
        this.shortname = shortname;
    }

    /**
     * Sets the Uri attribute of the JspTaglibSubTask object
     *
     * @param uri  The new Uri value
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /**
     * Sets the Displayname attribute of the JspTaglibSubTask object
     *
     * @param new_display_name  The new Displayname value
     */
    public void setDisplayname(String new_display_name)
    {
        displayname = new_display_name;
    }

    /**
     * Sets the Smallicon attribute of the JspTaglibSubTask object
     *
     * @param new_icon  The new Smallicon value
     */
    public void setSmallicon(String new_icon)
    {
        smallicon = new_icon;
    }

    /**
     * Sets the Largeicon attribute of the JspTaglibSubTask object
     *
     * @param new_icon  The new Largeicon value
     */
    public void setLargeicon(String new_icon)
    {
        largeicon = new_icon;
    }

    /**
     * Sets the Description attribute of the JspTaglibSubTask object
     *
     * @param new_description  The new Description value
     */
    public void setDescription(String new_description)
    {
        description = new_description;
    }

    /**
     * Sets the Filename attribute of the JspTaglibSubTask object
     *
     * @param new_filename  The new Filename value
     */
    public void setFilename(String new_filename)
    {
        filename = new_filename;
        setDestinationFile(filename);
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException
     */
    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getShortname() == null || getShortname().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"shortName"}));
        }
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    public void execute() throws XDocletException
    {
        if (getJspversion().equals(JspVersionTypes.VERSION_1_1)) {
            setPublicId(TLD_PUBLICID_1_1);
            setSystemId(TLD_SYSTEMID_1_1);
            setDtdURL(getClass().getResource(TLD_DTD_FILE_NAME_1_1));
        }
        else {
            setPublicId(TLD_PUBLICID_1_2);
            setSystemId(TLD_SYSTEMID_1_2);
            setDtdURL(getClass().getResource(TLD_DTD_FILE_NAME_1_2));
        }

        startProcess();
    }

    /**
     * Describe what the method does
     *
     * @exception XDocletException
     */
    protected void engineStarted() throws XDocletException
    {
        System.out.println(Translator.getString(XDocletMessages.class, XDocletMessages.GENERATING_SOMETHING, new String[]{getDestinationFile().toString()}));
    }

    /**
     * @author    Aslak Hellesøy
     * @created   July 28, 2001
     */
    public static class ContextParam implements java.io.Serializable
    {
        private String paramName = null;
        private String paramValue = null;
        private String description = "";

        /**
         * Gets the Name attribute of the ContextParam object
         *
         * @return   The Name value
         */
        public String getName()
        {
            return paramName;
        }

        /**
         * Gets the Value attribute of the ContextParam object
         *
         * @return   The Value value
         */
        public String getValue()
        {
            return paramValue;
        }

        /**
         * Gets the Description attribute of the ContextParam object
         *
         * @return   The Description value
         */
        public String getDescription()
        {
            return description;
        }

        /**
         * Sets the Name attribute of the ContextParam object
         *
         * @param name  The new Name value
         */
        public void setName(String name)
        {
            paramName = name;
        }

        /**
         * Sets the Value attribute of the ContextParam object
         *
         * @param value  The new Value value
         */
        public void setValue(String value)
        {
            paramValue = value;
        }

        /**
         * Sets the Description attribute of the ContextParam object
         *
         * @param desc  The new Description value
         */
        public void setDescription(String desc)
        {
            description = desc;
        }
    }

    /**
     * @author    Aslak Hellesøy
     * @created   July 28, 2001
     */
    public static class TagLib implements java.io.Serializable
    {
        private String taglibURI = null;
        private String taglibLocation = null;

        /**
         * Gets the URI attribute of the TagLib object
         *
         * @return   The URI value
         */
        public String getURI()
        {
            return taglibURI;
        }

        /**
         * Gets the Location attribute of the TagLib object
         *
         * @return   The Location value
         */
        public String getLocation()
        {
            return taglibLocation;
        }

        /**
         * Sets the URI attribute of the TagLib object
         *
         * @param uri  The new URI value
         */
        public void setURI(String uri)
        {
            taglibURI = uri;
        }

        /**
         * Sets the Location attribute of the TagLib object
         *
         * @param location  The new Location value
         */
        public void setLocation(String location)
        {
            taglibLocation = location;
        }
    }

    /**
     * @author    Ara Abrahamian (ara_e@email.com)
     * @created   July 19, 2001
     */
    public static class JspVersionTypes extends org.apache.tools.ant.types.EnumeratedAttribute
    {
        public final static String VERSION_1_1 = "1.1";
        public final static String VERSION_1_2 = "1.2";
        public final static String VERSION_2_0 = "2.0";

        // We're getting there!

        /**
         * Gets the Values attribute of the JspVersionTypes object
         *
         * @return   The Values value
         */
        public String[] getValues()
        {
            return (new String[]{VERSION_1_1, VERSION_1_2, VERSION_2_0});
        }
    }

}
