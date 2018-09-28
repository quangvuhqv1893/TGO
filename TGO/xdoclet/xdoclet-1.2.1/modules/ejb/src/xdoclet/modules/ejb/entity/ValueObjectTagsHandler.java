/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.entity;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;

import xjavadoc.*;
import xdoclet.DocletContext;

import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.entity.ValueObjectSubTask;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.LogUtil;

import xdoclet.util.TypeConversionUtil;

/**
 * @author               Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created              13. juni 2002
 * @xdoclet.taghandler   namespace="EjbValueObj"
 * @version              $Revision: 1.21 $
 */
public class ValueObjectTagsHandler
     extends EjbTagsHandler
{
    private XTag    currentTag;

    private String  currentValueObjectClass;

    private String  currentValueObjectAttribute;

    private String  currentValueObjectMatch;

    // For aggregation
    private String  currentAggregateType;

    private String  currentAggregateName;

    private String  currentRelationBeanClass;

    /**
     * Checks if a method is a value object relation (aggregate or compose) matching a certain valueObject
     *
     * @param method
     * @param valueObject
     * @return
     */
    public static boolean isValueObjectRelation(XMethod method, String valueObject)
    {
        Log log = LogUtil.getLog(ValueObjectTagsHandler.class, "isValueObjectRelation");
        boolean ret = method.getDoc().hasTag("ejb:value-object");

        if (log.isDebugEnabled())
            log.debug(method.getName() + " has a ejb:value-object Tag " + ret);
        if (ret) {
            Collection tags = method.getDoc().getTags("ejb:value-object");

            if (tags.size() == 0 && !"*".equals(valueObject)) {
                ret = false;
            }
            else {
                ret = false;

                boolean excluded = true;

                for (Iterator i = tags.iterator(); i.hasNext(); ) {
                    XTag tag = (XTag) i.next();
                    String exclude = tag.getAttributeValue("exclude");
                    String aggreg = tag.getAttributeValue("aggregate");
                    String comp = tag.getAttributeValue("compose");

                    if ("true".equals(exclude) || (aggreg == null && comp == null)) {
                        excluded = true;
                        ret = false;
                        break;
                    }

                    String value = tag.getAttributeValue("match");

                    if (log.isDebugEnabled())
                        log.debug(method.getName() + " Match=" + value + "==" + valueObject);
                    if (value == null) {
                        if ("*".equals(valueObject))
                            ret = true;
                        else
                            ret = false;
                        break;
                    }
                    else if (value.equals(valueObject) || value.equals("*") || "*".equals(valueObject)) {
                        ret = true;
                        break;
                    }
                }
                if ("*".equals(valueObject) && !excluded) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * Gets the GenerationNeeded attribute of the ValueObjectTagsHandler class
     *
     * @param clazz  Describe what the parameter does
     * @return       The GenerationNeeded value
     */
    public static boolean isGenerationNeeded(XClass clazz)
    {
        return true;
        //TODO
    }

    /**
     * Gets the CurrentValueObjectClass attribute of the ValueObjectTagsHandler class
     *
     * @param clazz                 Describe what the parameter does
     * @param tag                   Describe what the parameter does
     * @return                      The CurrentValueObjectClass value
     * @exception XDocletException
     */
    public static String getCurrentValueObjectClass(XClass clazz, XTag tag)
         throws XDocletException
    {
        String name = getCurrentValueObjectName(tag);

        String _currentValueObjectClass;

        _currentValueObjectClass = MessageFormat.format(getSubTask().getValueObjectClassPattern(),
            new Object[]{(name != null) ? name : getShortEjbNameFor(clazz)});

        String packageName = clazz.getContainingPackage().getName();

        packageName = choosePackage(packageName, null, DocletTask.getSubTaskName(ValueObjectSubTask.class));

        _currentValueObjectClass = packageName + '.' + _currentValueObjectClass;

        return _currentValueObjectClass;
    }

    /**
     * Gets the CurrentValueObjectName attribute of the ValueObjectTagsHandler class
     *
     * @param tag                   Describe what the parameter does
     * @return                      The CurrentValueObjectName value
     * @exception XDocletException
     */
    public static String getCurrentValueObjectName(XTag tag) throws XDocletException
    {
        String name = tag.getAttributeValue("name");

        if (name != null) {
            return name;
        }

        // name is null, must look whether it's defined in current class level
        XClass clazz = getCurrentClass();

        while (clazz != null) {
            for (Iterator i = clazz.getDoc().getTags(tag.getName()).iterator(); i.hasNext(); ) {
                if (tag.equals(i.next())) {
                    // ok, we are defined here return defined ejb name
                    name = EjbTagsHandler.getShortEjbNameFor(clazz);
                    if (name == null) {
                        throw new XDocletException("unable to determine value object name in class " + clazz.getName());
                    }
                    return name;
                }
            }
            // not found on current level. try with superclass
            clazz = clazz.getSuperclass();
        }
        throw new XDocletException("class defining value object is not EJB");
    }

    /**
     * Gets the CurrentValueObjectAttribute attribute of the ValueObjectTagsHandler class
     *
     * @param tag                   Describe what the parameter does
     * @return                      The CurrentValueObjectAttribute value
     * @exception XDocletException
     */
    public static String getCurrentValueObjectAttribute(XTag tag) throws XDocletException
    {
        String name = getCurrentValueObjectName(tag);

        String _currentValueObjectAttribute;

        if (name == null) {
            _currentValueObjectAttribute = "Value";
        }
        else {
            _currentValueObjectAttribute = MessageFormat.format(getSubTask().getValueObjectClassPattern(), new Object[]{name});
        }

        return _currentValueObjectAttribute;
    }

    /**
     * Gets the CurrentValueObjectMatch attribute of the ValueObjectTagsHandler class
     *
     * @param tag  Describe what the parameter does
     * @return     The CurrentValueObjectMatch value
     */
    public static String getCurrentValueObjectMatch(XTag tag)
    {
        String match = tag.getAttributeValue("match");

        if (match == null) {
            match = "*";
        }
        return match;
    }

    public static String getCurrentValueObjectImplements(XTag tag)
    {
        String toImplement = tag.getAttributeValue("implements");

        return toImplement != null ? "," + toImplement : "";
    }

    /**
     * Gets the SubTask attribute of the ValueObjectTagsHandler class
     *
     * @return   The SubTask value
     */
    private static ValueObjectSubTask getSubTask()
    {
        ValueObjectSubTask subtask = ((ValueObjectSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(ValueObjectSubTask.class)));

        return subtask;
    }

    public boolean isAbstractValueObject(String valueObjectName,
        XClass currentClass)
         throws XDocletException
    {
        boolean isAbstract = false;

        Collection valueObjectTags =
            currentClass.getDoc().getTags("ejb:value-object");

        for (Iterator i = valueObjectTags.iterator();
            i.hasNext() && !isAbstract; ) {
            XTag tag = (XTag) i.next();

            isAbstract = isAbstractValueObject(valueObjectName, tag);
        }

        return isAbstract;
    }

    /**
     * @param clazz                 Description of Parameter
     * @return                      the full qualified data-object class name
     * @exception XDocletException
     */
    public String getValueMostSuperObjectClass(XClass clazz) throws XDocletException
    {
        String currentDataClass = currentValueObjectClass;
        // Begin at the first super class

        XClass cur_clazz = clazz.getSuperclass();

        do {
            // Find if we have an abstract data class definition to generate
            Collection methods = cur_clazz.getMethods();
            boolean found = false;

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                if (method.getName().equals("get" + currentValueObjectAttribute)) {
                    found = true;
                    currentDataClass = getCurrentValueObjectClass(cur_clazz, currentTag);
                }

                if (found) {
                    break;
                }
            }
            cur_clazz = cur_clazz.getSuperclass();
        } while (cur_clazz != null);

        return currentDataClass;
    }

    /**
     * @param template
     * @param attributes
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifGeneratePKConstructor(String template, Properties attributes)
         throws XDocletException
    {
        if (getSubTask().getGeneratePKConstructor()) {
            generate(template);
        }
    }

    public void ifIsAbstractValueObject(String template)
         throws XDocletException
    {
        if (isAbstractValueObject(valueObjectName(), getCurrentClass())) {
            generate(template);
        }
    }

    public void ifNotIsAbstractValueObject(String template)
         throws XDocletException
    {
        if (!isAbstractValueObject(valueObjectName(), getCurrentClass())) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String valueObjectClass() throws XDocletException
    {
        return getSubTask().getCurrentValueObjectClass();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String valueObjectName() throws XDocletException
    {
        return getSubTask().getCurrentValueObjectName();
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException
     */
    public String valueObjectMatch() throws XDocletException
    {
        return getSubTask().getCurrentValueObjectMatch();
    }

    /**
     * Returns the name of the class dataobject class extends.
     *
     * @param attributes
     * @return                      The name of generated PK class.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String extendsFrom(Properties attributes) throws XDocletException
    {
        String extendsFrom = null;

        // get the value object's name we are checking extendsFrom for
        //
        String valueObjectName = attributes.getProperty("valueobject", valueObjectName());

        // go through all the value-object tags until we find the one for our value object
        //
        XClass currentClass = getCurrentClass();
        Collection valueObjectTags = currentClass.getDoc().getTags("ejb:value-object");

        for (Iterator i = valueObjectTags.iterator();
            i.hasNext() && extendsFrom == null; ) {
            XTag tag = (XTag) i.next();

            String attr = tag.getAttributeValue("extends");

            if (valueObjectName.equals(tag.getAttributeValue("name")) &&
                attr != null) {
                // only extend if our value-object has an extends parameter
                //
                extendsFrom = attr;
            }
        }

        return extendsFrom != null ? extendsFrom : "java.lang.Object";
    }

    /**
     * @param pTemplate
     * @exception XDocletException
     */
    public void forAllValueObjects(String pTemplate) throws XDocletException
    {
        Log log = LogUtil.getLog(ValueObjectTagsHandler.class, "forAllValueObjects");
        Collection dos = getCurrentClass().getDoc().getTags("ejb:value-object", true);

        if (log.isDebugEnabled()) {
            log.debug("Number of tags ejb:value-object in " + getCurrentClass() + " = " + dos.size());
        }
        for (Iterator i = dos.iterator(); i.hasNext(); ) {
            currentTag = (XTag) i.next();

            if (!isAbstractValueObject(getCurrentValueObjectName(currentTag),
                currentTag)) {

                currentValueObjectClass = getCurrentValueObjectClass(getCurrentClass(), currentTag);
                currentValueObjectAttribute = getCurrentValueObjectAttribute(currentTag);
                currentValueObjectMatch = getCurrentValueObjectMatch(currentTag);
                if (log.isDebugEnabled()) {
                    log.debug("Generate for " + currentValueObjectClass + " attr=" + currentValueObjectAttribute + " match=" + currentValueObjectMatch);
                }

                generate(pTemplate);
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String currentValueObjectClass()
    {
        return currentValueObjectClass;
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String currentValueObjectAttribute()
    {
        return currentValueObjectAttribute;
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String currentValueObjectMatch()
    {
        return currentValueObjectMatch;
    }

    /**
     * Describe what the method does
     *
     * @param attributes  Describe what the parameter does
     * @return            Describe the return value
     */
    public String currentAggregateType(Properties attributes)
    {
        String sh = attributes.getProperty("short");
        String ret = "";

        if (sh == null) {
            ret = currentAggregateType;
        }
        else {
            StringTokenizer st = new StringTokenizer(currentAggregateType, ".");

            while (st.hasMoreTokens()) {
                ret = st.nextToken();
            }
        }
        return ret;
    }

    /**
     * return interfaces to be implemented
     *
     * @return    The name of generated PK class.
     * @doc.tag   type="content"
     */
    public String valueObjectImplements()
    {
        return getSubTask().getCurrentValueObjectImplements();
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String currentAggregateName()
    {
        return currentAggregateName;
    }

    /**
     * Describe what the method does
     *
     * @return   Describe the return value
     */
    public String currentRelationBeanClass()
    {
        return currentRelationBeanClass;
    }

    /**
     * Type of the constructor for aggregates or compositions.
     *
     * @return                      Type of the constructor for aggregates or compositions.
     * @exception XDocletException
     */
    public String concreteCollectionType() throws XDocletException
    {
        String currentReturnType = getCurrentMethod().getReturnType().getType().getQualifiedName();
        String res = null;

        if (currentReturnType.equals("java.util.Collection")) {
            res = "java.util.ArrayList";
        }
        else if (currentReturnType.equals("java.util.Set")) {
            res = "java.util.HashSet";
        }
        else {
            throw new XDocletException("Invalid return type (" +
                currentReturnType +
                " on aggregate or composition.");
        }
        return res;
    }


    /**
     * Returns the data-object class name highest in the hierarchy of derived beans. Because of possible inheritance
     * between entity bean, the type of the generated getData method must be the one of the most super class of the
     * current entity bean. The current Data class must extend the corresponding super Data class.
     *
     * @return                      The data-object class name highest in the hierarchy of derived beans.
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String valueMostSuperObjectClass() throws XDocletException
    {
        return getValueMostSuperObjectClass(getCurrentClass());
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException
     */
    public void forAllSuperSetValue(String template) throws XDocletException
    {
        XClass oldClass = getCurrentClass();

        XClass superclass = null;

        do {
            Collection dos = getCurrentClass().getDoc().getTags("ejb:value-object", false);

            for (Iterator i = dos.iterator(); i.hasNext(); ) {
                currentTag = (XTag) i.next();
                currentValueObjectClass = getCurrentValueObjectClass(getCurrentClass(), currentTag);
                currentValueObjectAttribute = getCurrentValueObjectAttribute(currentTag);
                currentValueObjectMatch = getCurrentValueObjectMatch(currentTag);

                forAllSetters(template, "set" + currentValueObjectAttribute);
            }

            superclass = getCurrentClass().getSuperclass();

            if (superclass != null) {
                pushCurrentClass(superclass);
            }
        } while (superclass != null);

        setCurrentClass(oldClass);

    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException
     */
    public void forAllAggregates(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        String valueObject = attributes.getProperty("valueobject");

        forAllRelations(template, superclasses, valueObject, "aggregate");
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException
     */
    public void forAllComposes(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        String valueObject = attributes.getProperty("valueobject");

        forAllRelations(template, superclasses, valueObject, "compose");
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException
     */
    public void forAllRelations(String template, Properties attributes) throws XDocletException
    {
        String superclasses_str = attributes.getProperty("superclasses");
        boolean superclasses = TypeConversionUtil.stringToBoolean(superclasses_str, true);

        String valueObject = attributes.getProperty("valueobject");

        forAllRelations(template, superclasses, valueObject, "aggregate");
        forAllRelations(template, superclasses, valueObject, "compose");
    }

    /**
     * Evaluate the body block if Value Object subtask being used.
     *
     * @param template
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void ifUsingValueObject(String template) throws XDocletException
    {
        if (DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(ValueObjectSubTask.class))) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @param methodName            Describe what the parameter does
     * @exception XDocletException
     */
    protected void forAllSetters(String template, String methodName) throws XDocletException
    {
        Log log = LogUtil.getLog(ValueObjectTagsHandler.class, "forAllSetters");

        if (log.isDebugEnabled()) {
            log.debug(methodName);
        }

        // Find if we have an abstract data class definition to generate
        Collection methods = getCurrentClass().getMethods();
        XMethod methodFound = null;

        for (Iterator j = methods.iterator(); j.hasNext(); ) {
            XMethod method = (XMethod) j.next();

            if (method.getName().equals(methodName)) {
                methodFound = method;
            }

            if (methodFound != null) {
                if (log.isDebugEnabled()) {
                    log.debug(methodFound);
                }
                break;
            }
        }

        if (methodFound != null) {
            generate(template);
        }

    }

    private boolean isAbstractValueObject(String valueObjectName, XTag tag)
    {
        boolean isAbstract = false;
        String attr = tag.getAttributeValue("abstract");

        if (valueObjectName.equals(tag.getAttributeValue("name")) &&
            attr != null) {
            isAbstract = TypeConversionUtil.stringToBoolean(attr, false);
        }
        return isAbstract;
    }

    /**
     * @param template              Describe what the parameter does
     * @param superclasses          Describe what the parameter does
     * @param valueObject           Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @exception XDocletException
     * @todo                        (Aslak) use a HashSet instead of HashMap for foundFields
     */
    /*
     * private java.util.List[] extractDocs( String class_name ) throws XDocletException
     * {
     * Map foundFields = new HashMap();
     * java.util.List ext_fields = new java.util.ArrayList();
     * java.util.List ext_methods = new java.util.ArrayList();
     * java.util.List ext_constructors = new java.util.ArrayList();
     * XClass cur_class = getCurrentClass();
     * ArrayList full_constructor_params = new ArrayList();
     * do
     * {
     * XMethod[] methods = cur_class.methods();
     * for( int j = 0; j < methods.length; j++ )
     * {
     * if( PersistentTagsHandler.isPersistentField( methods[j] ) && MethodTagsHandler.isGetter( methods[j].name() ) && !foundFields.containsKey( methods[j].name() ) )
     * {
     * / Store that we found this field so we don't add it twice
     * foundFields.put( methods[j].name(), methods[j].name() );
     * String method_name_without_prefix = MethodTagsHandler.getMethodNameWithoutPrefixFor( methods[j] );
     * String field_name = Introspector.decapitalize( method_name_without_prefix );
     * XField field = new XFieldImpl( field_name, Modifier.PROTECTED, methods[j].returnType() );
     * ext_fields.add( field );
     * /getter method
     * ext_methods.add( new XMethodImpl( methods[j].name(), Modifier.PUBLIC, new ParameterImpl[0], methods[j].returnType() ) );
     * /setter method
     * ext_methods.add( new XMethodImpl( "set" + method_name_without_prefix, Modifier.PUBLIC, new ParameterImpl[]{new ParameterImpl( methods[j].returnType(), field_name )}, new TypeImpl( "void" ) ) );
     * full_constructor_params.add( new ParameterImpl( methods[j].returnType(), field_name ) );
     * }
     * }
     * / Add super class info
     * cur_class = cur_class.superclass();
     * }while ( cur_class != null );
     * /fields:
     * int modifiers = Modifier.STATIC | Modifier.FINAL;
     * ext_fields.add( new XFieldImpl( "serialVersionUID", modifiers, new TypeImpl( "long" ) ) );
     * if( BmpTagsHandler.useSoftLocking( getCurrentClass() ) )
     * ext_fields.add( new XFieldImpl( "_version", Modifier.PRIVATE, new TypeImpl( "long" ) ) );
     * / TODO
     * /methods:
     * /       if( hasDataEquals( getCurrentClass() ) )
     * /           ext_methods.add( new XMethodImpl( "equals", Modifier.PUBLIC, new ParameterImpl[0], new TypeImpl( "Object", "", getDocletContext().getRoot().classNamed( "java.lang.Object" ) ) ) );
     * ext_methods.add( new XMethodImpl( "toString", Modifier.PUBLIC, new ParameterImpl[0], new TypeImpl( "int" ) ) );
     * if( BmpTagsHandler.useSoftLocking( getCurrentClass() ) )
     * {
     * ext_methods.add( new XMethodImpl( "getVersion", Modifier.PUBLIC, new ParameterImpl[0], new TypeImpl( "long" ) ) );
     * ext_methods.add( new XMethodImpl( "setVersion", Modifier.PUBLIC, new ParameterImpl[]{new ParameterImpl( new TypeImpl( "long" ), "version" )}, new TypeImpl( "void" ) ) );
     * }
     * /constructors:
     * modifiers = Modifier.PUBLIC;
     * ext_constructors.add( new XConstructorImpl( class_name, modifiers, new ParameterImpl[0] ) );
     * ext_constructors.add( new XConstructorImpl( class_name, modifiers, new ParameterImpl[]{new ParameterImpl( new TypeImpl( class_name ), "otherData" )} ) );
     * ext_constructors.add( new XConstructorImpl( class_name, modifiers, ( ParameterImpl[] ) full_constructor_params.toArray( new ParameterImpl[0] ) ) );
     * return new java.util.List[]{ext_fields, ext_methods, ext_constructors};
     * }
     */
    /**
     * @param template              Describe what the parameter does
     * @param superclasses          Describe what the parameter does
     * @param valueObject           Describe what the parameter does
     * @param type                  Describe what the parameter does
     * @exception XDocletException
     * @todo                        (Aslak) use a HashSet instead of HashMap for foundFields
     */
    private void forAllRelations(String template, boolean superclasses, String valueObject, String type) throws XDocletException
    {
        Log log = LogUtil.getLog(ValueObjectTagsHandler.class, "forAllRelations");
        Map foundFields = new HashMap();
        XClass currentClass = getCurrentClass();
        XMethod oldMethod = getCurrentMethod();

        if (log.isDebugEnabled())
            log.debug("*** forAllRelations on class=" + currentClass.getName() + " valueobject=" + valueObject);
        do {
            pushCurrentClass(currentClass);

            if (log.isDebugEnabled()) {
                log.debug("****** CurrentClass=" + getCurrentClass());
            }

            Collection methods = getCurrentClass().getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                setCurrentMethod(method);

                if (MethodTagsHandler.isGetter(getCurrentMethod().getName()) &&
                    !foundFields.containsKey(getCurrentMethod().getName()) &&
                    isValueObjectRelation(getCurrentMethod(), valueObject)) {

                    boolean ret = getCurrentMethod().getDoc().hasTag("ejb:value-object");

                    if (log.isDebugEnabled())
                        log.debug("****** Method " + getCurrentMethod().getName() + " has VO tag : " + ret);
                    if (ret) {
                        Collection tags = getCurrentMethod().getDoc().getTags("ejb:value-object");

                        for (Iterator i = tags.iterator(); i.hasNext(); ) {
                            XTag tag = (XTag) i.next();
                            String aggreg = tag.getAttributeValue(type);
                            String aggregName = tag.getAttributeValue(type + "-name");

                            if (log.isDebugEnabled()) {
                                log.debug("********* " + method.getName() + " ejb:value-object Tag - Type = " + type + " - Value = " + aggreg + " - Name = " + aggregName);
                            }

                            if (aggreg != null) {
                                String currentReturnType = getCurrentMethod().getReturnType().getType().getQualifiedName();

                                if (log.isDebugEnabled()) {
                                    log.debug("********* METHOD=" + getCurrentMethod().getName() + " " + currentReturnType);
                                }

                                // Store that we found this field so we don't add it twice
                                foundFields.put(getCurrentMethod().getName(), getCurrentMethod().getName());
                                if (currentReturnType.equals("java.util.Collection") ||
                                    currentReturnType.equals("java.util.Set")) {

                                    if (log.isDebugEnabled()) {
                                        log.debug("********* Type Collection or Set");
                                    }
                                    currentAggregateType = aggreg;
                                    currentAggregateName = aggregName;

                                    String relationInterface = tag.getAttributeValue("members");

                                    if (log.isDebugEnabled()) {
                                        log.debug(relationInterface);
                                    }
                                    if (relationInterface != null && !relationInterface.equals("")) {
                                        currentRelationBeanClass = InterfaceTagsHandler.getBeanClassNameFromInterfaceNameFor(relationInterface);
                                    }
                                }
                                else {
                                    if (log.isDebugEnabled()) {
                                        log.debug("********* Type " + getCurrentMethod().getReturnType().getType().toString());
                                    }
                                    currentAggregateType = aggreg;
                                    currentAggregateName = aggregName;
                                    currentRelationBeanClass = InterfaceTagsHandler.getBeanClassNameFromInterfaceNameFor(getCurrentMethod().getReturnType().getType().getQualifiedName());
                                }
                                generate(template);
                                currentAggregateType = null;
                                currentAggregateName = null;
                                currentRelationBeanClass = null;
                                break;
                            }
                        }
                    }
                }
            }

            // Add super class info
            XClass superclass = getCurrentClass().getSuperclass();

            if (superclass == null) {
                popCurrentClass();
                break;
            }

            if (superclass.getQualifiedName().equals("java.lang.Object")) {
                popCurrentClass();
                break;
            }

            popCurrentClass();

            // superclasses is true for *CMP/BMP/Session
            if (superclasses == true) {
                currentClass = currentClass.getSuperclass();
            }
            else {
                if (shouldTraverseSuperclassForDependentClass(currentClass.getSuperclass(), getDependentClassTagName())) {
                    currentClass = currentClass.getSuperclass();
                }
                else {
                    break;
                }
            }
        } while (true);

        setCurrentMethod(oldMethod);

        log.debug("Finished.");
    }
}
