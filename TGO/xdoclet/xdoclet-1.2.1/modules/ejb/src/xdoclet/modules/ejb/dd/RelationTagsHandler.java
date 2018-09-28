/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dd;

import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.XDocletModulesEjbMessages;
import xdoclet.tagshandler.MethodTagsHandler;

import xdoclet.util.LogUtil;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

/**
 * @author               <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="EjbRel"
 * @version              $Revision: 1.11 $
 */
public class RelationTagsHandler extends EjbTagsHandler
{
    /**
     * A map containing all relations.
     */
    protected static Map relationMap = new HashMap();

    /**
     * The current relation, set by forAllRelationships and used by forAllRelationshipRoles. It somehow is like the
     * current index for the forAllRelationships loop.
     *
     * @see   #forAllRelationships(java.lang.String)
     */
    protected static RelationHolder currentRelation;

    /**
     * Evaluates the body if the left side of this relation has cascade-delete=yes
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     */
    public void ifLeftCascadeDelete(String template) throws XDocletException
    {
        if (currentRelation.isLeftCascadeDelete()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if the right side of this relation has cascade-delete=yes, or the left side has
     * target-relation cascade-delete="yes"
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     */
    public void ifRightCascadeDelete(String template) throws XDocletException
    {
        if (currentRelation.isRightCascadeDelete()) {
            generate(template);
        }
    }

    /**
     * Returns the EJB Name of the left side of this relationship
     *
     * @return                      Left side's EJB name
     * @exception XDocletException
     */
    public String leftEJBName() throws XDocletException
    {
        if (currentRelation.getLeft() != null) {
            return EjbTagsHandler.getEjbNameFor(currentRelation.getLeft());
        }
        else {
            String name = getTagValue(
                FOR_METHOD,
                currentRelation.getRightMethod().getDoc(),
                "ejb:relation",
                "target-ejb",
                null,
                null,
                true,
                !currentRelation.isBidirectional()
                );

            return name;
        }
    }

    /**
     * Returns the EJB Name of the right side of this relationship
     *
     * @return                      Right side's EJB name
     * @exception XDocletException
     */
    public String rightEJBName() throws XDocletException
    {
        if (currentRelation.getRight() != null) {
            return EjbTagsHandler.getEjbNameFor(currentRelation.getRight());
        }
        else {
            String name = getTagValue(
                FOR_METHOD,
                currentRelation.getLeftMethod().getDoc(),
                "ejb:relation",
                "target-ejb",
                null,
                null,
                true,
                !currentRelation.isBidirectional()
                );

            return name;
        }
    }

    /**
     * Evaluates the body if at least one of the classes has an ejb:relation tag, otherwise not.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     */
    public void ifHasRelationships(String template) throws XDocletException
    {
        boolean hasRelationships = hasRelationships();

        if (hasRelationships) {
            generate(template);
        }
    }

    /**
     * Evaluates the body if none of the classes has an ejb:relation tag, otherwise not.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     */
    public void ifNotHasRelationships(String template) throws XDocletException
    {
        boolean hasRelationships = hasRelationships();

        if (!hasRelationships) {
            generate(template);
        }
    }

    /**
     * @return                      the name of the current relation
     * @exception XDocletException
     */
    public String relationName() throws XDocletException
    {
        return currentRelation.getName();
    }

    /**
     * Evaluates the body block for each relationship. Relations are denoted by ejb:relation for the getter method of
     * the cmr-field.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         xdoclet.modules.ejb.entity.CmpTagsHandler#isEntityCmp(xjavadoc.XClass)
     * @see                         xdoclet.modules.ejb.entity.PersistentTagsHandler#isPersistentField(xjavadoc.XMethod)
     * @see                         xdoclet.tagshandler.MethodTagsHandler#isGetter(java.lang.String)
     * @see                         #isSetOrCollection(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void forAllRelationships(String template) throws XDocletException
    {
        Log log = LogUtil.getLog(RelationTagsHandler.class, "forAllRelationships");

        Collection classes = getXJavaDoc().getSourceClasses();

        relationMap.clear();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            Collection methods = clazz.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                setCurrentMethod(method);

                XTag relationTag = method.getDoc().getTag("ejb:relation");

                if (relationTag != null) {
                    String relationName = relationTag.getAttributeValue("name");

                    if (relationName == null) {
                        throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.RELATION_MUST_HAVE_NAME, new String[]{getCurrentClass().getName()}));
                    }

                    RelationHolder relationHolder = (RelationHolder) relationMap.get(relationName);

                    if (relationHolder != null && relationHolder.getLeft() != null && relationHolder.getRight() != null) {
                        String leftSignature = relationHolder.getLeftMethod().getContainingClass().getQualifiedName() + "." + relationHolder.getLeftMethod();
                        String rightSignature = relationHolder.getRightMethod().getContainingClass().getQualifiedName() + "." + relationHolder.getRightMethod();
                        String currentSignature = method.getContainingClass().getQualifiedName() + "." + method;

                        throw new XDocletException(Translator.getString(XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.RELATION_TOO_MANY_NAMES, new String[]{
                            relationName,
                            leftSignature,
                            rightSignature,
                            currentSignature
                            }));
                    }

                    if (relationHolder == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Created new relationship for " + clazz + "." + method);
                        }

                        relationHolder = new RelationHolder();
                        relationHolder.left = clazz;
                        relationHolder.leftMethod = method;
                        relationMap.put(relationName, relationHolder);
                    }
                    else {
                        if (log.isDebugEnabled()) {
                            log.debug("Added " + clazz + " to relationship: " + relationHolder);
                        }
                        relationHolder.right = clazz;
                        relationHolder.rightMethod = method;
                    }
                }
            }
        }

        // Now swap relations so that right is always many (if there is a many side). This may result
        // in left being null, in case of a unidirectional relationship
        // Possible cases are (left-right):
        // 1-->1
        // 1<--1
        // 1-->N
        // 1<--N
        // M-->N
        // N<--M
        // 1<->1
        // 1<->N
        // M<->N
        // Impossible cases are (left-right):
        // N-->1
        // N<--1
        Iterator relations = relationMap.entrySet().iterator();

        while (relations.hasNext()) {
            Map.Entry entry = (Map.Entry) relations.next();
            RelationHolder relationHolder = (RelationHolder) entry.getValue();

            if (relationHolder.isLeftMany() && !relationHolder.isRightMany()) {
                // swap
                if (log.isDebugEnabled()) {
                    log.debug("Swapping left -> right in attempt to make 1-n be 1-n (rather than n-1)");
                }
                relationHolder.swap();
            }
            if (!relationHolder.isBidirectional() &&
                relationHolder.isOne2Many() &&
                relationHolder.getLeftMethod() == null) {
                relationHolder.swap();
            }
        }

        // Loop over all relations
        Iterator relationNameIterator = relationMap.keySet().iterator();

        while (relationNameIterator.hasNext()) {
            // The name is only needed to provide potential error messages.
            String relationName = (String) relationNameIterator.next();

            RelationHolder relationHolder = (RelationHolder) relationMap.get(relationName);

            setCurrentClass(relationHolder.getLeft());
            setCurrentMethod(relationHolder.getLeftMethod());

            currentRelation = relationHolder;

            // Get the target-ejb value (null or not), so we can perform sanity checks.
//			String target = relationHolder.getLeftMethod().doc().tagAttributeValue( "ejb:relation", "target-ejb" );
//			if( relationHolder.getRight() == null )
//			{
//				// Relation has only been declared in one method. That means it is unidirectional and requires
//				// target-ejb to be set. Perform the sanity check here before we give the template
//				// a chance to do something stupid.
//
//				if( target == null )
//				{
//					throw new XDocletException( Translator.getString( XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.RELATION_NEEDS_TARGET,
//						new String[]{relationName, relationHolder.getLeft().name() + "." + relationHolder.getLeftMethod().name()} ) );
//				}
//			}
//			else
//			{
//				// Relation has been declared in exactly two methods. That means target-ejb is forbidden, since its
//				// presence will result in invalid relations element in ejb-jar.xml
//				if( target != null )
//				{
//					throw new XDocletException( Translator.getString( XDocletModulesEjbMessages.class, XDocletModulesEjbMessages.RELATION_NEEDS_NO_TARGET,
//						new String[]{relationName, relationHolder.getLeft().name() + "." + relationHolder.getLeftMethod().name()} ) );
//				}
//			}

            if (log.isDebugEnabled()) {
                log.debug("Generating template for Relation: " + currentRelation);
            }
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifIsLeftMany(String template) throws XDocletException
    {
        if (currentRelation.isLeftMany()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifIsRightMany(String template) throws XDocletException
    {
        if (currentRelation.isRightMany()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String leftFieldName() throws XDocletException
    {
        return MethodTagsHandler.getPropertyNameFor(currentRelation.getLeftMethod());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String rightFieldName() throws XDocletException
    {
        return MethodTagsHandler.getPropertyNameFor(currentRelation.getRightMethod());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String leftFieldType() throws XDocletException
    {
        return MethodTagsHandler.getMethodTypeFor(currentRelation.getLeftMethod());
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String rightFieldType() throws XDocletException
    {
        return MethodTagsHandler.getMethodTypeFor(currentRelation.getRightMethod());
    }

    /**
     * Evaluates the body block if current method's return type is not a java.util.Collection or java.util.Set. Used by
     * forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsNotACollection(String template) throws XDocletException
    {
        if (!isSetOrCollection(MethodTagsHandler.getMethodTypeFor(getCurrentMethod()))) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String relationComment()
    {
        return "<!-- " + currentRelation.toString() + " -->";
    }

    /**
     * Evaluates the body block if the current relationship is a one to one type, meaning, neither side of the relation
     * returns java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsOne2One(String template) throws XDocletException
    {
        if (currentRelation.isOne2One()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if the current relationship IS NOT a one to one type, meaning, at least one side of the
     * relation returns java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifNotIsOne2One(String template) throws XDocletException
    {
        if (!currentRelation.isOne2One()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if the current relationship is a one to many type, meaning, ONLY ONE side of the
     * relation returns java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsOne2Many(String template) throws XDocletException
    {
        if (currentRelation.isOne2Many()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if the current relationship IS NOT a one to many type, meaning, either both sides, or
     * neither side of the relation returns java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifNotIsOne2Many(String template) throws XDocletException
    {
        if (!currentRelation.isOne2Many()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if the current relationship is a many to many type, meaning, both sides of the relation
     * returns java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifIsMany2Many(String template) throws XDocletException
    {
        if (currentRelation.isMany2Many()) {
            generate(template);
        }
    }

    /**
     * Evaluates the body block if the current relationship IS NOT a many to many type, meaning, at least one side of
     * the relation does not return java.util.Collection or java.util.Set. Used by forAllRelationships.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #forAllRelationships(java.lang.String)
     * @doc.tag                     type="block"
     */
    public void ifNotIsMany2Many(String template) throws XDocletException
    {
        if (!currentRelation.isMany2Many()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifIsBidirectional(String template) throws XDocletException
    {
        if (currentRelation.isBidirectional()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifIsUnidirectional(String template) throws XDocletException
    {
        if (!currentRelation.isBidirectional()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifHasLeftRoleName(String template) throws XDocletException
    {
        if (leftRoleName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifHasRightRoleName(String template) throws XDocletException
    {
        if (rightRoleName() != null) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifLeftNavigable(String template) throws XDocletException
    {
        if (currentRelation.isLeftNavigable()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void ifRightNavigable(String template) throws XDocletException
    {
        if (currentRelation.isRightNavigable()) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String leftRoleName() throws XDocletException
    {
        return currentRelation.getLeftRoleName();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String rightRoleName() throws XDocletException
    {
        return currentRelation.getRightRoleName();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String leftMultiplicity() throws XDocletException
    {
        return currentRelation.getLeftMultiplicity();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String rightMultiplicity() throws XDocletException
    {
        return currentRelation.getRightMultiplicity();
    }

    /**
     * Returns true if current method's return type is a java.util.Collection or java.util.Set, false otherwise.
     *
     * @param type  Description of Parameter
     * @return      true if Collection or Set
     */
    protected boolean isSetOrCollection(String type)
    {
        return (type.equals("java.util.Collection") || type.equals("java.util.Set"));
    }

    protected boolean hasRelationships()
    {
        Collection classes = getXJavaDoc().getSourceClasses();
        boolean hasRelationships = false;

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            Collection methods = clazz.getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                setCurrentMethod(method);

                XTag relation = method.getDoc().getTag("ejb:relation");

                if (relation != null) {
                    hasRelationships = true;
                    break;
                }
            }
            if (hasRelationships) {
                break;
            }
        }
        return hasRelationships;
    }

    /**
     * Holds class/method of the two end points of a relationship.
     *
     * @author    Aslak Hellesøy
     * @created   August 28, 2001
     */
    public class RelationHolder
    {
        private final static String ONE = "One";

        private final static String MANY = "Many";

        private XClass left;

        private XMethod leftMethod;

        private XClass right;

        private XMethod rightMethod;

        /**
         * Gets the Bidirectional attribute of the RelationHolder object
         *
         * @return   The Bidirectional value
         */
        public boolean isBidirectional()
        {
            return getRightMethod() != null && getLeftMethod() != null;
        }

        /**
         * Gets the One2One attribute of the RelationHolder object
         *
         * @return                      The One2One value
         * @exception XDocletException
         */
        public boolean isOne2One() throws XDocletException
        {
            return !isLeftMany() && !isRightMany();
        }

        /**
         * Gets the One2Many attribute of the RelationHolder object
         *
         * @return                      The One2Many value
         * @exception XDocletException
         */
        public boolean isOne2Many() throws XDocletException
        {
            return !isLeftMany() && isRightMany();
        }

        /**
         * Gets the Many2Many attribute of the RelationHolder object
         *
         * @return                      The Many2Many value
         * @exception XDocletException
         */
        public boolean isMany2Many() throws XDocletException
        {
            return isLeftMany() && isRightMany();
        }

        /**
         * Gets the LeftMultiplicity attribute of the RelationHolder object
         *
         * @return                      The LeftMultiplicity value
         * @exception XDocletException
         */
        public String getLeftMultiplicity() throws XDocletException
        {
            return isLeftMany() ? MANY : ONE;
        }

        /**
         * Gets the RightMultiplicity attribute of the RelationHolder object
         *
         * @return                      The RightMultiplicity value
         * @exception XDocletException
         */
        public String getRightMultiplicity() throws XDocletException
        {
            return isRightMany() ? MANY : ONE;
        }

        /**
         * Gets the Left attribute of the RelationHolder object
         *
         * @return   The Left value
         */
        public XClass getLeft()
        {
            return left;
        }

        /**
         * Gets the LeftMethod attribute of the RelationHolder object
         *
         * @return   The LeftMethod value
         */
        public XMethod getLeftMethod()
        {
            return leftMethod;
        }

        /**
         * Gets the Right attribute of the RelationHolder object
         *
         * @return   The Right value
         */
        public XClass getRight()
        {
            return right;
        }

        /**
         * Gets the RightMethod attribute of the RelationHolder object
         *
         * @return   The RightMethod value
         */
        public XMethod getRightMethod()
        {
            return rightMethod;
        }

        /**
         * Gets the RightNavigable attribute of the RelationHolder object
         *
         * @return   The RightNavigable value
         */
        public boolean isRightNavigable()
        {
            return getRightMethod() != null;
        }

        /**
         * Gets the LeftNavigable attribute of the RelationHolder object
         *
         * @return   The LeftNavigable value
         */
        public boolean isLeftNavigable()
        {
            return getLeftMethod() != null;
        }

        /**
         * Gets the LeftRoleName attribute of the RelationHolder object
         *
         * @return                      The LeftRoleName value
         * @exception XDocletException
         */
        public String getLeftRoleName() throws XDocletException
        {
            String result = null;

            if (getLeftMethod() != null) {
                result = getLeftMethod().getDoc().getTagAttributeValue("ejb:relation", "role-name", false);
            }
            else {
                result = getRightMethod().getDoc().getTagAttributeValue("ejb:relation", "target-role-name", false);
            }
            return result;
        }

        /**
         * Gets the RightRoleName attribute of the RelationHolder object
         *
         * @return                      The RightRoleName value
         * @exception XDocletException
         */
        public String getRightRoleName() throws XDocletException
        {
            String result = null;

            if (getRightMethod() != null) {
                result = getRightMethod().getDoc().getTagAttributeValue("ejb:relation", "role-name", false);
            }
            else {
                result = getLeftMethod().getDoc().getTagAttributeValue("ejb:relation", "target-role-name", false);
            }
            return result;
        }

        /**
         * Gets the LeftMany attribute of the RelationHolder object
         *
         * @return                      The LeftMany value
         * @exception XDocletException
         */
        public boolean isLeftMany() throws XDocletException
        {
            boolean result;

            if (getLeftMethod() != null) {
                result = isSetOrCollection(getLeftMethod().getReturnType().getType().getQualifiedName());
            }
            else {
                String targetMultiple = getRightMethod().getDoc().getTagAttributeValue("ejb:relation", "target-multiple", false);

                result = TypeConversionUtil.stringToBoolean(targetMultiple, false);
            }
            return result;
        }

        /**
         * Gets the RightMany attribute of the RelationHolder object
         *
         * @return                      The RightMany value
         * @exception XDocletException
         */
        public boolean isRightMany() throws XDocletException
        {
            boolean result;

            if (getRightMethod() != null) {
                result = isSetOrCollection(getRightMethod().getReturnType().getType().getQualifiedName());
            }
            else {
                String targetMultiple = getLeftMethod().getDoc().getTagAttributeValue("ejb:relation", "target-multiple", false);

                result = TypeConversionUtil.stringToBoolean(targetMultiple, false);
            }
            return result;
        }

        /**
         * Gets the LeftCascadeDelete attribute of the RelationHolder object
         *
         * @return                      The LeftCascadeDelete value
         * @exception XDocletException
         */
        public boolean isLeftCascadeDelete() throws XDocletException
        {
            boolean result;

            if (getLeftMethod() != null) {
                result = isCascadeDelete(getLeftMethod(), "cascade-delete");
            }
            else {
                result = isCascadeDelete(getRightMethod(), "target-cascade-delete");
            }
            return result;
        }

        /**
         * Gets the RightCascadeDelete attribute of the RelationHolder object
         *
         * @return                      The RightCascadeDelete value
         * @exception XDocletException
         */
        public boolean isRightCascadeDelete() throws XDocletException
        {
            boolean result;

            if (getRightMethod() != null) {
                result = isCascadeDelete(getRightMethod(), "cascade-delete");
            }
            else {
                result = isCascadeDelete(getLeftMethod(), "target-cascade-delete");
            }
            return result;
        }

        /**
         * @return                      the name of the relation
         * @exception XDocletException
         */
        public String getName() throws XDocletException
        {
            String result = null;

            if (getRightMethod() != null) {
                result = getRightMethod().getDoc().getTagAttributeValue("ejb:relation", "name", false);
            }
            else {
                result = getLeftMethod().getDoc().getTagAttributeValue("ejb:relation", "name", false);
            }
            return result;
        }

        /**
         * Describe what the method does
         */
        public void swap()
        {
            XClass c = right;
            XMethod m = rightMethod;

            right = left;
            rightMethod = leftMethod;
            left = c;
            leftMethod = m;
        }

        /**
         * Describe what the method does
         *
         * @return   Describe the return value
         */
        public int hashCode()
        {
            int result = 17;

            if (getLeft() != null) {
                result = 37 * result + getLeft().hashCode();
            }
            if (getLeftMethod() != null) {
                result = 37 * result + getLeftMethod().hashCode();
            }
            if (getRight() != null) {
                result = 37 * result + getRight().hashCode();
            }
            if (getRightMethod() != null) {
                result = 37 * result + getRightMethod().hashCode();
            }

            return result;
        }

        /**
         * Describe what the method does
         *
         * @return   Describe the return value
         */
        public String toString()
        {
            return new StringBuffer("RelationHolder left=").append(getLeft()).append('.').append(getLeftMethod()).append(" right=").append(getRight()).append('.').append(getRightMethod()).toString();
        }

        /**
         * Describe what the method does
         *
         * @param o  Describe what the parameter does
         * @return   Describe the return value
         */
        public boolean equals(Object o)
        {
            if (o == this) {
                return true;
            }

            if (!(o instanceof RelationHolder)) {
                return false;
            }

            RelationHolder other = (RelationHolder) o;

            return (
                (getLeft() == null ? other.getLeft() == null : getLeft().equals(other.getLeft())) &&
                (getLeftMethod() == null ? other.getLeftMethod() == null : getLeftMethod().equals(other.getLeftMethod())) &&
                (getRight() == null ? other.getRight() == null : getRight().equals(other.getRight())) &&
                (getRightMethod() == null ? other.getRightMethod() == null : getRightMethod().equals(other.getRightMethod())));
        }

        /**
         * Gets the CascadeDelete attribute of the RelationHolder object
         *
         * @param method                Describe what the parameter does
         * @param tag                   Describe what the parameter does
         * @return                      The CascadeDelete value
         * @exception XDocletException
         */
        private boolean isCascadeDelete(XMethod method, String tag) throws XDocletException
        {
            String cd = null;

            cd = getTagValue(
                FOR_METHOD,
                method.getDoc(),
                "ejb:relation",
                tag,
                "yes,no,true,false",
                "no",
                false,
                false
                );
            return TypeConversionUtil.stringToBoolean(cd, false);
        }
    }
}
