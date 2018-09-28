/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.mvcsoft.ejb;

import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;
import xdoclet.tagshandler.ClassTagsHandler;

import xdoclet.util.Translator;

/**
 * @author               Daniel OConnor (docodan@mvcsoft.com)
 * @created              November 1, 2001
 * @xdoclet.taghandler   namespace="MVCSoft"
 */
public class MVCSoftTagsHandler extends ClassTagsHandler
{
    protected static XParameter currentQueryMethodParameter;

    protected static StringTokenizer currentAliases;

    protected static String currentRoleName;

    protected static String currentFieldName;

    protected static String currentColName;

    protected static String nested;
    private final static String CMP_FIELDS = "CMP Fields";

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String methodParamType() throws XDocletException
    {
        StringBuffer type = new StringBuffer(currentQueryMethodParameter.getType().getQualifiedName());

        for (int iter = 0; iter < currentQueryMethodParameter.getDimension(); iter++) {
            type.append("[]");
        }
        return type.toString();
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllQueryMethodParams(String template) throws XDocletException
    {
        List parameters = Arrays.asList(getCurrentMethod().getParameters().toArray());

        for (int i = 0; i < (parameters.size() - 2); i++) {
            currentQueryMethodParameter = (XParameter) parameters.get(i);

            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String fieldName() throws XDocletException
    {
        String token = currentToken(new Properties());
        int idx = token.indexOf(' ');

        if (idx == -1) {
            return token;
        }
        else {
            return token.substring(0, idx).trim();
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String sortDirection() throws XDocletException
    {
        String token = currentToken(new Properties()).trim();
        int idx = token.indexOf(' ');

        if (idx == -1) {
            return "";
        }
        else {
            String sort = token.substring(idx, token.length()).trim();

            if (sort.equalsIgnoreCase("desc")) {
                return "Descending";
            }
            else {
                return "Ascending";
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void ifHasSortDirection(String template) throws XDocletException
    {
        if (!sortDirection().equals("")) {
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forEachRoleMapping(String template) throws XDocletException
    {
        String aliases = getKeyAliases();

        if (aliases == null) {
            return;
        }

        String leftRole = null;
        String rightRole = null;
        int idx = aliases.indexOf(';');

        if (idx == -1) {
            leftRole = aliases;
        }
        else {
            leftRole = aliases.substring(0, idx);
            rightRole = aliases.substring(idx + 1);
        }
        doForExtendedRole(leftRole, template);
        if (rightRole != null) {
            doForExtendedRole(rightRole, template);
        }
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forSingleRoleMapping(String template) throws XDocletException
    {
        String aliases = getKeyAliases();

        if (aliases == null) {
            return;
        }
        currentAliases = new StringTokenizer(aliases, ",", false);
        generate(template);
    }

    /**
     * Describe what the method does
     *
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    public void forAllAliases(String template) throws XDocletException
    {
        while (currentAliases.hasMoreTokens()) {
            String token = currentAliases.nextToken();
            int idx = token.indexOf('=');

            if (idx == -1) {
                throw new XDocletException(Translator.getString(XDocletModulesMvcsoftEjbMessages.class, XDocletModulesMvcsoftEjbMessages.ALIAS_FORM));
            }
            currentFieldName = token.substring(0, idx).trim();
            currentColName = token.substring(idx + 1).trim();
            generate(template);
        }
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String roleName() throws XDocletException
    {
        return currentRoleName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String keyFieldName() throws XDocletException
    {
        return currentFieldName;
    }

    /**
     * Describe what the method does
     *
     * @return                      Describe the return value
     * @exception XDocletException  Describe the exception
     */
    public String colName() throws XDocletException
    {
        return currentColName;
    }

    // 1.1 features

    public void forNestedFaultGroups(String template) throws XDocletException
    {
        Properties prop = new Properties();

        prop.setProperty("tagName", "mvcsoft:fault-group");
        prop.setProperty("paramName", "nested");
        nested = classTagValue(prop);
        //"mvcsoft:fault-group", "nested", 0, null, null, true, false );
        generate(template);
    }

    public String nestedFaultGroups() throws XDocletException
    {
        if ((nested == null) || nested.equals(""))
            return "";
        StringBuffer buffer = new StringBuffer();
        Map map = parseNested(nested);
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            if (key.equals(CMP_FIELDS))
                continue;
            buffer.append(getFaultGroupRelationshipLinks(key, (Map) map.get(key)));
        }
        return buffer.toString();
    }

    /**
     * Gets the KeyAliases attribute of the MVCSoftTagsHandler object
     *
     * @return   The KeyAliases value
     */
    private String getKeyAliases()
    {
        return getCurrentMethod().getDoc().getTagAttributeValue("mvcsoft:relation", "key-aliases", false);
    }

    private Map getSubMap(Map mapNested, String cmrPath)
    {
        StringTokenizer pathTokenizer = new StringTokenizer(cmrPath, ".", false);

        while (pathTokenizer.hasMoreTokens()) {
            String token = pathTokenizer.nextToken();
            Map mapNew = (Map) mapNested.get(token);

            if (mapNew == null) {
                mapNew = new HashMap();
                mapNested.put(token, mapNew);
            }
            mapNested = mapNew;
        }
        return mapNested;
    }

    private String getFaultGroupRelationshipLinks(String cmrField, Map mapNested)
    {
        StringBuffer buffer = new StringBuffer("<fault-group-relationship-link>");

        buffer.append("<cmr-field>");
        buffer.append(cmrField);
        buffer.append("</cmr-field>");
        buffer.append("<loading-strategy>SeparateQuery</loading-strategy>");

        List cmpFields = (List) mapNested.get(CMP_FIELDS);

        if (cmpFields != null) {
            for (Iterator iterCmp = cmpFields.iterator(); iterCmp.hasNext(); ) {
                String cmp = (String) iterCmp.next();

                buffer.append("<field-name>");
                buffer.append(cmp);
                buffer.append("</field-name>");
            }
        }

        Iterator iterator = mapNested.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            if (key.equals(CMP_FIELDS))
                continue;
            buffer.append(getFaultGroupRelationshipLinks(key, (Map) mapNested.get(key)));
        }
        buffer.append("</fault-group-relationship-link>");
        return buffer.toString();
    }

    /**
     * Describe what the method does
     *
     * @param token                 Describe what the parameter does
     * @param template              Describe what the parameter does
     * @exception XDocletException  Describe the exception
     */
    private void doForExtendedRole(String token, String template) throws XDocletException
    {
        int idx = token.indexOf(':');

        if (idx == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesMvcsoftEjbMessages.class, XDocletModulesMvcsoftEjbMessages.KEY_ALIAS_FORM));
        }

        currentRoleName = token.substring(0, idx);
        currentAliases = new StringTokenizer(token.substring(idx + 1), ",", false);
        generate(template);
    }

    private Map parseNested(String nestedFaultGroups)
         throws XDocletException
    {
        HashMap mapNested = new HashMap();
        StringTokenizer tokenizer = new StringTokenizer(nestedFaultGroups, ";", false);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int idx = token.indexOf(":");

            if (idx == -1) {
                throw new XDocletException(Translator.getString(XDocletModulesMvcsoftEjbMessages.class, XDocletModulesMvcsoftEjbMessages.NESTED_FAULT_GROUP_FORM));
            }

            String cmrPath = token.substring(0, idx);
            StringTokenizer cmpFields = new StringTokenizer(token.substring(idx + 1), ",", false);
            Map subMap = getSubMap(mapNested, cmrPath);
            List fields = new LinkedList();

            while (cmpFields.hasMoreTokens()) {
                fields.add(cmpFields.nextToken());
            }
            subMap.put(CMP_FIELDS, fields);
        }
        return mapNested;
    }
}
