/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.jmx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

import xdoclet.tagshandler.ClassTagsHandler;
import xdoclet.tagshandler.PropertyTagsHandler;

import xdoclet.template.TemplateException;

/**
 * @author               <a href="mailto:mnewcomb@users.sourceforge.net">Michael Newcomb</a>
 * @created              April 17, 2003
 * @xdoclet.taghandler   namespace="JMX"
 */
public class JMXTagsHandler
     extends ClassTagsHandler
{
    private final static Map INTERNAL_TYPES;
    static {
        Map m = new HashMap();

        m.put(Byte.TYPE.getName(), "B");
        m.put(Short.TYPE.getName(), "S");
        m.put(Integer.TYPE.getName(), "I");
        m.put(Long.TYPE.getName(), "J");
        m.put(Float.TYPE.getName(), "F");
        m.put(Double.TYPE.getName(), "D");
        m.put(Boolean.TYPE.getName(), "Z");
        m.put(Character.TYPE.getName(), "C");

        INTERNAL_TYPES = Collections.unmodifiableMap(m);
    }


    public String managedAttributeType()
         throws XDocletException
    {
        Properties attributes = new Properties();

        attributes.setProperty("tagName", "jmx.managed-attribute");

        // lookup the PropertyTagsHandler
        //
        PropertyTagsHandler pth = null;

        try {
            pth = (PropertyTagsHandler) ((TemplateSubTask) getDocletContext().getActiveSubTask()).getEngine().getTagHandlerFor("Property");
        }
        catch (TemplateException te) {
            throw new XDocletException(te, "there's some funky shiat going on!");
        }

        // get the normal type
        //
        String type = pth.propertyTypeWithTag(attributes);

        // if it is an array, we need to re-arrange it into psuedo internal java
        // type so java.lang.String[] will become [java.lang.String
        //
        int indexOfOpenBracket = type.indexOf('[');

        if (indexOfOpenBracket != -1) {
            String originalType = type;

            type = originalType.substring(0, indexOfOpenBracket);

            // replace primitives with their internal java type and surround classes
            // with L<class>;
            //
            type =
                INTERNAL_TYPES.containsKey(type) ? (String) INTERNAL_TYPES.get(type) :
                ("L" + type + ";");

            // add a '[' for each dimension
            //
            for (int index = indexOfOpenBracket; index != -1;
                index = originalType.indexOf('[', index + 1)) {
                type = '[' + type;
            }
        }

        return type;
    }
}
