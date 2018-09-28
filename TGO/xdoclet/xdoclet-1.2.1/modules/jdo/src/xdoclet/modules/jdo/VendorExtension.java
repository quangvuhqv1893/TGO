/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jdo;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author    Ludovic Claude (ludovicc@users.sourceforge.net)
 * @created   10 October 2002
 * @version   $Revision: 1.1 $
 */
public class VendorExtension
{

    private String  vendor;
    private String  key;
    private String  value;
    private Collection nestedExtensions;

    /**
     * Constructor for VendorExtension.
     *
     * @param vendor
     * @param key
     * @param value
     */
    public VendorExtension(String vendor, String key, String value)
    {
        super();
        this.vendor = vendor;
        this.key = key;
        this.value = value;
    }

    public String getVendor()
    {
        return vendor;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    public Collection getNestedExtensions()
    {
        return nestedExtensions;
    }

    public boolean hasNestedExtensions()
    {
        return nestedExtensions != null && nestedExtensions.size() > 0;
    }

    public Iterator nestedExtensionsIterator()
    {
        return nestedExtensions.iterator();
    }

    public void addNestedExtension(VendorExtension nestedExtension)
    {
        if (nestedExtensions == null) {
            nestedExtensions = new ArrayList();
        }
        nestedExtensions.add(nestedExtension);
    }
}
