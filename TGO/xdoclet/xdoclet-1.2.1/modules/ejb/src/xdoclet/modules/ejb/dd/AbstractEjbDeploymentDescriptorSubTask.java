/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dd;

import java.util.Collection;
import java.util.Iterator;

import xjavadoc.*;
import xdoclet.XDocletException;
import xdoclet.XmlSubTask;
import xdoclet.modules.ejb.entity.CmpTagsHandler;

/**
 * Base class for all subtasks that generate xml-ish ejb deployment descriptors.
 *
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   Dec 11, 2001
 * @version   $Revision: 1.6 $
 */
public abstract class AbstractEjbDeploymentDescriptorSubTask extends XmlSubTask
{
    private final static int DONT_CARE = 0;
    private final static int CMP_2_X = 1;
    private final static int CMP_1_X = 2;

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean atLeastOneCmpEntityBeanExists() throws XDocletException
    {
        return atLeastOneCmpEntityBeanExists(DONT_CARE);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean atLeastOneCmp1EntityBeanExists() throws XDocletException
    {
        return atLeastOneCmpEntityBeanExists(CMP_1_X);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean atLeastOneCmp2EntityBeanExists() throws XDocletException
    {
        return atLeastOneCmpEntityBeanExists(CMP_2_X);
    }

    /**
     * Describe what the method does
     *
     * @param cmpVersion
     * @return                      Describe the return value
     * @exception XDocletException
     */
    private boolean atLeastOneCmpEntityBeanExists(int cmpVersion) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            if (CmpTagsHandler.isEntityCmp(clazz) &&
                (cmpVersion == DONT_CARE ||
                (cmpVersion == CMP_2_X && CmpTagsHandler.isUsingCmp2Impl(clazz)) ||
                (cmpVersion == CMP_1_X && !CmpTagsHandler.isUsingCmp2Impl(clazz)))) {
                return true;
            }
        }

        return false;
    }
}
