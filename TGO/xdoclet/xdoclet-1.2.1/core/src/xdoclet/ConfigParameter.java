/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet;

import java.io.Serializable;

/**
 * Allows to set configuration parameters that will be included in the element as attribute value pair.<br/>
 * The following input<p/>
 *
 * &lt;configParam name="foo" value="bar"/&gt;<p/>
 *
 * will give <p/>
 *
 * &lt;foo&gt;bar&lt;/foo&gt;
 *
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       Jan 21, 2002
 * @version       $Revision: 1.6 $
 * @ant.element   name="configParam"
 */
public final class ConfigParameter implements Serializable
{
    private String  name = null;
    private String  value = null;

    /**
     * Gets the Name attribute of the ConfigParameter object
     *
     * @return   The Name value
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the Value attribute of the ConfigParameter object
     *
     * @return   The Value value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the Name attribute of the ConfigParameter object
     *
     * @param name  The new Name value
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the Value attribute of the ConfigParameter object
     *
     * @param value  The new Value value
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
