/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.exolab.castor.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import xjavadoc.ClassIterator;
import xjavadoc.XClass;
import xjavadoc.XCollections;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.util.LogUtil;

/**
 * @author               <a href="mailto:dim@bigpond.net.au">Dmitri Colebatch</a>
 * @created              Aug 23, 2002
 * @xdoclet.taghandler   namespace="Castor"
 * @version              $Revision: 1.5 $
 */
public class CastorTagsHandler extends XDocletTagSupport
{
    public void forAllClasses(String template, Properties attributes) throws XDocletException
    {
        Log log = LogUtil.getLog(CastorTagsHandler.class, "forAllClasses");
        Collection classes = getOrderedClasses();

        for (ClassIterator i = XCollections.classIterator(classes); i.hasNext(); ) {
            XClass currentClass = i.next();

            setCurrentClass(currentClass);
            log.debug("currentClass=" + currentClass);
            generate(template);
        }
    }

    private Collection getOrderedClasses() throws XDocletException
    {
        Log log = LogUtil.getLog(CastorTagsHandler.class, "getAllClasses");

        log.debug("ordering classes");

        List orderedClasses = new ArrayList();

        while (!gatherClasses(orderedClasses))
            ;
        return orderedClasses;
    }

    private boolean gatherClasses(List orderedClasses) throws XDocletException
    {
        Log log = LogUtil.getLog(CastorTagsHandler.class, "gatherClasses");

        boolean addedAll = true;

        for (ClassIterator i = XCollections.classIterator(getXJavaDoc().getSourceClasses()); i.hasNext(); ) {
            XClass currentClass = i.next();
            String depends = getTagValue(FOR_CLASS, currentClass.getDoc(), "depends", "", "", null, false, false);

            if (depends == null) {
                orderedClasses.add(currentClass);
                log.debug("Adding " + currentClass);
            }
            else {
                boolean addedClass = checkOrderedClasses(orderedClasses, depends, currentClass);

                log.debug("Added " + currentClass + " with depends='" + depends + "' because " + depends + " already added.");
                addedAll = addedAll && addedClass;
            }
        }
        return addedAll;
    }

    private boolean checkOrderedClasses(List orderedClasses, String depends, XClass currentClass)
    {
        Log log = LogUtil.getLog(CastorTagsHandler.class, "checkOrderedClasses");

        for (Iterator iterator = orderedClasses.iterator(); iterator.hasNext(); ) {
            XClass xClass = (XClass) iterator.next();

            if (xClass.getName().equals(depends)) {
                orderedClasses.add(currentClass);
                log.debug("Found " + depends);
                return true;
            }
        }
        log.debug("Couldn't find " + depends);
        return false;
    }

}
