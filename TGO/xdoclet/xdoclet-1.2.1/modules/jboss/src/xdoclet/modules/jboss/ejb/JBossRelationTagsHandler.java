/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.jboss.ejb;

import java.util.*;
import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.modules.ejb.dd.RelationTagsHandler;

/**
 * @author               <a href="mailto:david@nustiu.net">David Budworth</a>
 * @created              Feb 4, 2002
 * @xdoclet.taghandler   namespace="JBEjbRel"
 * @version              $Revision: 1.9 $
 */
public class JBossRelationTagsHandler extends RelationTagsHandler
{
    private String  currentFKRelField = null;
    private String  currentFKCol = null;

    public String relationTableAttribute(Properties attributes) throws XDocletException
    {
        String attribute = null;

        String paramName = attributes.getProperty("paramName");

        XMethod leftMethod = currentRelation.getLeftMethod();
        XMethod rightMethod = currentRelation.getRightMethod();

        attribute = leftMethod == null ? null :
            leftMethod.getDoc().getTagAttributeValue("jboss.relation-table", paramName, false);

        attribute = attribute != null ? attribute : rightMethod == null ? null :
            rightMethod.getDoc().getTagAttributeValue("jboss.relation-table", paramName, false);

        return attribute;
    }

    public void ifHasRelationTableAttribute(String template, Properties attributes) throws XDocletException
    {
        if (relationTableAttribute(attributes) != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifNotLeftHasFK(String template) throws XDocletException
    {
        if (!hasFK(true)) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifNotRightHasFK(String template) throws XDocletException
    {
        if (!hasFK(false)) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifLeftHasFK(String template) throws XDocletException
    {
        if (hasFK(true)) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifRightHasFK(String template) throws XDocletException
    {
        if (hasFK(false)) {
            generate(template);
        }
    }

    public void ifLeftHasReadAhead(String template) throws XDocletException
    {
        if (readAheadStrategy(true) != null) {
            generate(template);
        }
    }

    public void ifRightHasReadAhead(String template) throws XDocletException
    {
        if (readAheadStrategy(false) != null) {
            generate(template);
        }
    }

    public void ifLeftHasReadAheadPageSize(String template) throws XDocletException
    {
        if (readAheadPageSize(true) != null) {
            generate(template);
        }
    }

    public void ifRightHasReadAheadPageSize(String template) throws XDocletException
    {
        if (readAheadPageSize(false) != null) {
            generate(template);
        }
    }

    public void ifLeftHasReadAheadEagerLoadGroup(String template) throws XDocletException
    {
        if (readAheadEagerLoadGroup(true) != null) {
            generate(template);
        }
    }

    public void ifRightHasReadAheadEagerLoadGroup(String template) throws XDocletException
    {
        if (readAheadEagerLoadGroup(false) != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllLeftForeignKeys(String template) throws XDocletException
    {
        forAllForeignKeys(template, true);
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllRightForeignKeys(String template) throws XDocletException
    {
        forAllForeignKeys(template, false);
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String relatedPKField()
    {
        return currentFKRelField;
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String fkColumn()
    {
        return currentFKCol;
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifLeftHasFKConstraint(String template) throws XDocletException
    {
        if (hasFKConstraint(true)) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifRightHasFKConstraint(String template) throws XDocletException
    {
        if (hasFKConstraint(false)) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String leftFKConstraint() throws XDocletException
    {
        return fkConstraint(true);
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String rightFKConstraint() throws XDocletException
    {
        return fkConstraint(false);
    }

    public String leftReadAheadStrategy() throws XDocletException
    {
        return readAheadStrategy(true);
    }

    public String rightReadAheadStrategy() throws XDocletException
    {
        return readAheadStrategy(false);
    }

    public String leftReadAheadPageSize() throws XDocletException
    {
        return readAheadPageSize(true);
    }

    public String rightReadAheadPageSize() throws XDocletException
    {
        return readAheadPageSize(false);
    }

    public String leftReadAheadEagerLoadGroup() throws XDocletException
    {
        return readAheadEagerLoadGroup(true);
    }

    public String rightReadAheadEagerLoadGroup() throws XDocletException
    {
        return readAheadEagerLoadGroup(false);
    }

    public void ifIsRelationTableMapping(String template) throws XDocletException
    {
        if (isRelationTableMapping())
            generate(template);
    }

    public void ifNotIsRelationTableMapping(String template) throws XDocletException
    {
        if (!isRelationTableMapping())
            generate(template);
    }

    public void ifIsForeignKeyMapping(String template) throws XDocletException
    {
        if (isForeignKeyMapping())
            generate(template);
    }

    public void ifNotIsForeignKeyMapping(String template) throws XDocletException
    {
        if (!isForeignKeyMapping())
            generate(template);
    }

    /**
     * Checks if the current relation should use a relation table mapping. This can occurs if the current relation is
     * many-to-many or the programmer has explicitly set jboss.relation-mapping style="relation-table"
     *
     * @return                      true if relation-table-mapping should be used
     * @exception XDocletException
     */
    private boolean isRelationTableMapping() throws XDocletException
    {
        if (currentRelation.isMany2Many())
            return true;
        if (currentRelation.getLeftMethod() != null)
            if ("relation-table".equals(currentRelation.getLeftMethod().getDoc().getTagAttributeValue("jboss.relation-mapping", "style")))
                return true;
        if (currentRelation.getRightMethod() != null)
            if ("relation-table".equals(currentRelation.getRightMethod().getDoc().getTagAttributeValue("jboss.relation-mapping", "style")))
                return true;

        return false;
    }

    /**
     * Checks if the current relation should use a foreign key mapping. This occurs if the programmer has explicitly set
     * jboss.relation-mapping style="foreign-key"
     *
     * @return                      true if foreign-key-mapping should be used
     * @exception XDocletException
     */
    private boolean isForeignKeyMapping() throws XDocletException
    {
        if (currentRelation.getLeftMethod() != null)
            if ("foreign-key".equals(currentRelation.getLeftMethod().getDoc().getTagAttributeValue("jboss.relation-mapping", "style")))
                return true;
        if (currentRelation.getRightMethod() != null)
            if ("foreign-key".equals(currentRelation.getRightMethod().getDoc().getTagAttributeValue("jboss.relation-mapping", "style")))
                return true;

        return false;
    }

    private String fkConstraint(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().getTagAttributeValue("jboss.relation", "fk-constraint");
        else
            return methodB.getDoc().getTagAttributeValue("jboss.target-relation", "fk-constraint");
    }

    private String readAheadStrategy(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().getTagAttributeValue("jboss.relation-read-ahead", "strategy");
        else
            return methodB.getDoc().getTagAttributeValue("jboss.target-relation-read-ahead", "strategy");
    }

    private String readAheadPageSize(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().getTagAttributeValue("jboss.relation-read-ahead", "page-size");
        else
            return methodB.getDoc().getTagAttributeValue("jboss.target-relation-read-ahead", "page-size");
    }

    private String readAheadEagerLoadGroup(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().getTagAttributeValue("jboss.relation-read-ahead", "eager-load-group");
        else
            return methodB.getDoc().getTagAttributeValue("jboss.target-relation-read-ahead", "eager-load-group");
    }

    /**
     * Checks if the current relation has a foreign key constraint.
     *
     * @param left  if TRUE the left side of the relation will be checked, otherwise the right side will be.
     * @return      TRUE if the current relation has a foreign key constraint.
     */
    private boolean hasFKConstraint(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().getTagAttributeValue("jboss.relation", "fk-constraint") != null;
        else
            return methodB.getDoc().getTagAttributeValue("jboss.target-relation", "fk-constraint") != null;
    }


    /**
     * Checks if the current relation has a foreign key declaration tag (jboss.relation/jboss.target-relation).
     *
     * @param left  if TRUE the left side of the relation will be checked, otherwise the right side will be.
     * @return      TRUE if the relation has a foreign key declaration tag.
     */
    private boolean hasFK(boolean left)
    {
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            return methodA.getDoc().hasTag("jboss.relation");
        else
            return methodB.getDoc().hasTag("jboss.target-relation");
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param left                  Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    private void forAllForeignKeys(String template, boolean left) throws XDocletException
    {
        Collection fktags = null;
        XMethod methodA = null;
        XMethod methodB = null;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
        }

        if (methodA != null)
            fktags = methodA.getDoc().getTags("jboss.relation");
        else
            fktags = methodB.getDoc().getTags("jboss.target-relation");

        // maybe we should throw here?
        if (fktags == null) {
            return;
        }

        for (TagIterator i = XCollections.tagIterator(fktags); i.hasNext(); ) {
            XTag fktag = i.next();

            currentFKRelField = fktag.getAttributeValue("related-pk-field");
            currentFKCol = fktag.getAttributeValue("fk-column");
            generate(template);
        }
    }
}
