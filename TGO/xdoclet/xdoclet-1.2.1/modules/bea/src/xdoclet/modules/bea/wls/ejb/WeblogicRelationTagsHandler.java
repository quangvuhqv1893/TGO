/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.bea.wls.ejb;

import java.util.*;

import xjavadoc.*;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.dd.RelationTagsHandler;

import xdoclet.util.Translator;

/**
 * This tag handler handles tags needed for Weblogic relations
 *
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created              12 mei 2002
 * @xdoclet.taghandler   namespace="WlEjbRel"
 * @version              $Revision: 1.10 $
 */
public class WeblogicRelationTagsHandler extends RelationTagsHandler
{
    private Collection columnMapTags = null;
    private TagIterator columnMapTagIterator = null;
    private XClass  pkClass;
    private String  currentKeyColumn = null;
    private String  currentForeignKeyColumn = null;

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param left
     * @exception XDocletException  Describe the exception
     */
    public void forAllColumnMaps(String template, boolean left) throws XDocletException
    {
        XMethod methodA;
        XMethod methodB;
        XClass classA;
        XClass classB;

        if (left) {
            methodA = currentRelation.getLeftMethod();
            methodB = currentRelation.getRightMethod();
            classA = currentRelation.getLeft();
            classB = currentRelation.getRight();
        }
        else {
            methodA = currentRelation.getRightMethod();
            methodB = currentRelation.getLeftMethod();
            classA = currentRelation.getRight();
            classB = currentRelation.getLeft();
        }

        if (methodA != null) {
            // bidirectional
            columnMapTags = methodA.getDoc().getTags("weblogic.column-map");

            columnMapTagIterator = XCollections.tagIterator(columnMapTags);
            pkClass = classB;
            if (pkClass == null) {
                pkClass = EjbTagsHandler.getEjb(methodA.getDoc().getTagAttributeValue("ejb.relation", "target-ejb"));
            }
            if (columnMapTags.size() == 0) {
                //throw new XDocletException( currentRelation.getRightMethod().name() + " should have at least one @weblogic:column-map tag (B)" );
            }
        }
        else {
            // unidirectional
            columnMapTags = methodB.getDoc().getTags("weblogic:target-column-map");

            columnMapTagIterator = XCollections.tagIterator(columnMapTags);
            pkClass = classB;
            if (columnMapTags.size() == 0) {
                //throw new XDocletException( currentRelation.getLeftMethod().name() + " should have at least one @weblogic:target-column-map tag (C)" );
            }
        }

        while (columnMapTagIterator.hasNext()) {
            XTag columnMapTag = columnMapTagIterator.next();

            currentKeyColumn = columnMapTag.getAttributeValue("key-column");
            currentForeignKeyColumn = columnMapTag.getAttributeValue("foreign-key-column");
            generate(template);
        }
    }

    public void forAllLeftColumnMaps(String template) throws XDocletException
    {
        forAllColumnMaps(template, true);
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllRightColumnMaps(String template) throws XDocletException
    {
        forAllColumnMaps(template, false);
    }

    /**
     * Makes sure the column-map tags are on the right side and target-column-map tags left
     */
    public void ensureColumnMapTagsRight()
    {
        boolean rightHasTargetColumnMapTags = false;
        boolean leftHasColumnMapTags = false;

        if (currentRelation.getLeftMethod() != null) {
            rightHasTargetColumnMapTags = currentRelation.getLeftMethod().getDoc().getTags("weblogic.target-column-map").size() != 0;
        }
        if (currentRelation.getRightMethod() != null) {
            leftHasColumnMapTags = currentRelation.getRightMethod().getDoc().getTags("weblogic.column-map").size() != 0;
        }
        if (rightHasTargetColumnMapTags || leftHasColumnMapTags) {
            currentRelation.swap();
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String joinTableName() throws XDocletException
    {
        // We say mandatory is false (last parameter), so we can provide a more detailed error message.
        String leftJoinTableName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue("weblogic:relation", "join-table-name", false);
        String joinTableName = leftJoinTableName;
        String rightJoinTableName = null;

        // do some sanity checking
        if (leftJoinTableName == null && currentRelation.getRightMethod() != null) {
            rightJoinTableName = currentRelation.getRightMethod().getDoc().getTagAttributeValue("weblogic:relation", "join-table-name", false);
            joinTableName = rightJoinTableName;
        }
        if (leftJoinTableName != null && rightJoinTableName != null) {
            throw new XDocletException(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.JOIN_TABLE_NAME_ONLY_ONE_SIDE));
        }
        if (leftJoinTableName == null && rightJoinTableName == null) {
            throw new XDocletException(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.JOIN_TABLE_NAME_NEEDED, new String[]{relationName()}));
        }
        return joinTableName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String leftGroupName() throws XDocletException
    {
        if (currentRelation.getLeftMethod() != null) {
            return currentRelation.getLeftMethod().getDoc().getTagAttributeValue("weblogic:relation", "group-name", false);
        }
        else {
            return null;
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifHasLeftGroupName(String template) throws XDocletException
    {
        if (leftGroupName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String rightGroupName() throws XDocletException
    {
        String groupName = null;

        if (currentRelation.getRightMethod() != null) {
            // bidirectional
            groupName = currentRelation.getRightMethod().getDoc().getTagAttributeValue("weblogic:relation", "group-name", false);
        }
        else {
            // unidirectional
            groupName = currentRelation.getLeftMethod().getDoc().getTagAttributeValue("weblogic:relation", "group-name", false);
        }
        return groupName;
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifHasRightGroupName(String template) throws XDocletException
    {
        if (rightGroupName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifHasKeyColumn(String template) throws XDocletException
    {
        if (keyColumn() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String keyColumn() throws XDocletException
    {
        if (currentKeyColumn == null) {
            // Not specified. Look on related bean
            if (columnMapTags.size() > 1) {
                throw new XDocletException(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.CANT_GUESS_PK_FIELD));
            }

            Collection methods = pkClass.getMethods();

            for (MethodIterator i = XCollections.methodIterator(methods); i.hasNext(); ) {
                XMethod method = i.next();
                boolean isPk = method.getDoc().getTag("ejb.pk-field") != null;
                String tmpKeyColumn = method.getDoc().getTagAttributeValue("ejb.persistence", "column-name");

                // see that we only have one pk field
                if (isPk) {
                    if (currentKeyColumn != null) {
                        throw new XDocletException(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.MORE_THAN_ONE_PK_FIELD, new String[]{pkClass.getQualifiedName()}));
                    }
                    else {
                        currentKeyColumn = tmpKeyColumn;
                    }
                }
            }
            if (currentKeyColumn == null) {
                throw new XDocletException(Translator.getString(XDocletModulesBeaWlsEjbMessages.class, XDocletModulesBeaWlsEjbMessages.NO_PK_FIELD, new String[]{pkClass.getQualifiedName()}));
            }
        }
        return currentKeyColumn;
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String foreignKeyColumn()
    {
        return currentForeignKeyColumn;
    }
}
