/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.util;

import java.util.*;

import xdoclet.*;
import xdoclet.util.Translator;

/**
 * This taghandler puts together some utility tags useful during template processing. For now, contains only a mechanism
 * to manage collections of strings
 *
 * @author               Marcus Brito (pazu@animegaiden.com.br)
 * @created              25 Jun 2002
 * @xdoclet.taghandler   namespace="Collection"
 * @version              $Revision: 1.2 $
 */
public class CollectionTagsHandler extends XDocletTagSupport
{
    // This will be a map of collections
    private Map     collections = new HashMap();

    /**
     * Obtains one value contained in the collection. This tag only apply to map valued collections, and an xdoclet
     * exception will be throw if the specified collection is not a map.
     *
     * @param attributes         The attributes of the template tag
     * @return                   The requested value or null if no such value exists.
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="content"
     * @doc.param                name="key" optional="false" description="The collection to operate on."
     * @doc.param                name="name" optional="false" description="The key to retrive."
     */
    public String get(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String key = attributes.getProperty("key");

        // Makes sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Makes key is specified
        if (key == null || key.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"key"}));
        }

        // Makes sure the name exists
        if (!collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_NOT_DEFINED, new String[]{name}));
        }

        // Make sure the name is a map
        if (!(collections.get(name) instanceof Map)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_IS_NOT_MAP, new String[]{name}));
        }

        return (String) ((Map) collections.get(name)).get(key);
    }


    /**
     * Creates a new utility collection that will store template data. If a collection with the specified name already
     * exists, an XDocletException will be thrown.
     *
     * @param attributes         The attributes of the template tag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="content"
     * @doc.param                name="name" optional="false" description="The name for the newly created collection"
     * @doc.param                name="type" optional="true" values="map,set" description="The type of the collection to
     *      create. Default value is set"
     */
    public void create(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String type = attributes.getProperty("type");

        // Make sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Prevent collection overwriting
        if (collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_ALREADY_EXISTS, new String[]{name}));
        }

        // Creates a new map or set, as requested
        if ("map".equals(type))
            collections.put(name, new HashMap());
        else
            collections.put(name, new HashSet());
    }

    /**
     * Puts a new element into the specified collection. If the collection is a set, only the 'name' and 'value'
     * attributes should be specified. If the collection is a map, the 'key' value should also be specified. If the
     * 'key' is specified and the collection is a set, or if 'key' is not specified and the collection is a map, an
     * XDocletException will be thrown.
     *
     * @param attributes         The attributes of the template tag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="content"
     * @doc.param                name="name" optional="false" description="The name of the collection to operate on. If
     *      the collection does not exists, an execption will be thrown."
     * @doc.param                name="key" optional="true" description="The key to the new value. Should only be
     *      specified if the collection is a map."
     * @doc.param                name="value" optional="false" description="The value to put into the collection."
     */
    public void put(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String key = attributes.getProperty("key");
        String value = attributes.getProperty("value");

        // Makes sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Makes sure value is specified
        if (value == null || value.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"value"}));
        }

        // Make sure the collection exists
        if (!collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_NOT_DEFINED, new String[]{name}));
        }

        // Puts the value into the collection, as requested
        if (collections.get(name) instanceof Map) {
            if (key == null || key.length() == 0) {
                throw new XDocletException(Translator.getString(XDocletMessages.class,
                    XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"key"}));
            }

            ((Map) collections.get(name)).put(key, value);
        }
        else {
            // Throws an exception if key is specified
            if (key != null) {
                throw new XDocletException(Translator.getString(CollectionMessages.class,
                    CollectionMessages.COLLECTION_IS_NOT_MAP, new String[]{name}));
            }

            ((Set) collections.get(name)).add(value);
        }
    }


    /**
     * Removes an element from the specified collection. One of 'key' or 'value' attributes should be specified,
     * depending if the collection is a map or a set.
     *
     * @param attributes         The attributes of the template tag
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="content"
     * @doc.param                name="name" optional="false" description="The name of the collection to operate on. If
     *      the collection does not exists, an execption will be thrown."
     * @doc.param                name="key" optional="true" description="The key to remove from the map. Invalid if the
     *      collection is a set."
     * @doc.param                name="value" optional="true" description="The value to remove from the set. Invalid if
     *      the collection is a map."
     */
    public void remove(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String key = attributes.getProperty("key");
        String value = attributes.getProperty("value");

        // Makes sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Make sure name exists
        if (!collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_NOT_DEFINED, new String[]{name}));
        }

        // Removes the key from the name, as requested
        if (collections.get(name) instanceof Map) {
            // Makes sure key is specified
            if (key == null || key.length() == 0) {
                throw new XDocletException(Translator.getString(XDocletMessages.class,
                    XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"key"}));
            }

            ((Map) collections.get(name)).remove(key);
        }
        else {
            // Makes sure value is specified
            if (value == null || value.length() == 0) {
                throw new XDocletException(Translator.getString(XDocletMessages.class,
                    XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"value"}));
            }

            ((Set) collections.get(name)).remove(key);
        }
    }

    /**
     * Generates the contained template code if the specified collection contains the key or value passed as attributes.
     * If the collection is a set, only the 'value' attribute should be specified. If the collection is a map, the 'key'
     * attribute should be specifed and if the 'value' attribute is also specified, an additional check for equality
     * will be made.
     *
     * @param template              The block tag contents
     * @param attributes            The attributes of the tag template
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The collection to operate on."
     * @doc.param                   name="key" optional="true" description="The key to check, if the collection is a
     *      map."
     * @doc.param                   name="value" optional="true" description="The valu to check, if the collection is a
     *      set. If the collection is a map, the value to check for equality."
     */
    public void ifContains(String template, Properties attributes) throws XDocletException
    {
        if (contains(attributes))
            generate(template);
    }

    /**
     * Generates the contained template code if the specified collection doesn't contain the key or value passed as
     * attributes. If the collection is a set, only the 'value' attribute should be specified. If the collection is a
     * map, the 'key' attribute should be specifed and if the 'value' attribute is also specified, an additional check
     * for equality will be made.
     *
     * @param template              The block tag contents
     * @param attributes            The attributes of the tag template
     * @exception XDocletException
     * @doc.tag                     type="block"
     * @doc.param                   name="name" optional="false" description="The collection to operate on."
     * @doc.param                   name="key" optional="true" description="The key to check, if the collection is a
     *      map."
     * @doc.param                   name="value" optional="true" description="The valu to check, if the collection is a
     *      set. If the collection is a map, the value to check for equality."
     */
    public void ifDoesntContain(String template, Properties attributes) throws XDocletException
    {
        if (!contains(attributes))
            generate(template);
    }

    /**
     * Destroys the specified collection. The collection must exists or an exception will be thrown.
     *
     * @param attributes         The attributes of the tag template
     * @throws XDocletException  if something goes wrong
     * @doc.tag                  type="content"
     * @doc.param                name="name" description="The collection to destroy."
     */
    public void destroy(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");

        // Makes sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Makes sure name exists
        if (!collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_NOT_DEFINED, new String[]{name}));
        }

        collections.remove(name);
    }


    /**
     * Checks if the specified collection contains a certain key/value. If the collection is a set, only the 'value'
     * attribute should be specified. If the collection is a map, the 'key' value should be specified and if the 'value'
     * attribute is also specified, an additional check for equality will be made.
     *
     * @param attributes            The attributes of the template tag
     * @return
     * @exception XDocletException
     */
    private boolean contains(Properties attributes) throws XDocletException
    {
        String name = attributes.getProperty("name");
        String key = attributes.getProperty("key");
        String value = attributes.getProperty("value");

        // Makes sure name is specified
        if (name == null || name.length() == 0) {
            throw new XDocletException(Translator.getString(XDocletMessages.class,
                XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"name"}));
        }

        // Make sure name exists
        if (!collections.containsKey(name)) {
            throw new XDocletException(Translator.getString(CollectionMessages.class,
                CollectionMessages.COLLECTION_NOT_DEFINED, new String[]{name}));
        }

        // Checks if the collection contains the key/value specified
        if (collections.get(name) instanceof Map) {
            // Makes sure key is specified
            if (key == null || key.length() == 0) {
                throw new XDocletException(Translator.getString(XDocletMessages.class,
                    XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"key"}));
            }

            // If value is specified, checks for equality
            if (value == null)
                return ((Map) collections.get(name)).containsKey(key);
            else
                return value.equals(((Map) collections.get(name)).get(key));
        }
        else {
            // Makes sure value is specified
            if (value == null || value.length() == 0) {
                throw new XDocletException(Translator.getString(XDocletMessages.class,
                    XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[]{"value"}));
            }

            return ((Set) collections.get(name)).contains(value);
        }
    }
}
