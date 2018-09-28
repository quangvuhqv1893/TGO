/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.externalizer;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

import xjavadoc.*;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.template.TemplateException;

/**
 * Externalizes key-value paired tags to whatever configured file (be it xml or properties file). It's useful for simple
 * key-value properties or xml files. For now only properties files are supported. It supports i18n and l10n, so
 * generated files follow ResourceBundle naming convention. Parameters "language", "country" and "variant" are reserved
 * and used for this purpose.
 *
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       May 7, 2002
 * @ant.element   display-name="Externalizer" name="externalizer" parent="xdoclet.DocletTask"
 * @version       $Revision: 1.10 $
 */
public class ExternalizerSubTask extends TemplateSubTask
{
    public final static String GENERATED_FILE_NAME = "{0}Messages{1}.properties";

    private static String DEFAULT_TEMPLATE_FILE = "resources/properties.xdt";

    private String  tagName = "msg.message";
    private String  valueParamName = "msg";
    private Map     combinations;
    private Combination currentCombination;

    public ExternalizerSubTask()
    {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
    }


    public String getTagName()
    {
        return tagName;
    }

    public String getValueParamName()
    {
        return valueParamName;
    }

    public Combination getCurrentCombination()
    {
        return currentCombination;
    }

    public String getKeyParamName()
    {
        return "";
    }


    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public void setValueParamName(String valueParamName)
    {
        this.valueParamName = valueParamName;
    }

    public void setKeyParamName(String p)
    {
    }

    public void validateOptions() throws XDocletException
    {
        super.validateOptions();

        if (getTagName() == null) {
            throw new XDocletException("tagName parameter is missing.");
        }
        if (getValueParamName() == null) {
            throw new XDocletException("valueParamName parameter is missing.");
        }
    }

    protected String getBundleKey(XClass clazz)
    {
        String result = MessageFormat.format(getDestinationFile(), new Object[]{clazz.getQualifiedName().replace('.', '/'), ""});

        return result.substring(0, result.lastIndexOf('.'));
    }

    protected String getGeneratedFileName(XClass clazz) throws XDocletException
    {
        String result = MessageFormat.format(getDestinationFile(), new Object[]{clazz.getQualifiedName().replace('.', '/'), currentCombination.getCombinationNameAsResourceBundleName()});

        return result;
    }

    protected void generateForClass(XClass clazz) throws XDocletException
    {
        setupResourceBundleCombinationsForClass(clazz);

        Set entries = combinations.entrySet();

        for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Combination combination = (Combination) entry.getValue();

            currentCombination = combination;

            super.generateForClass(clazz);
        }

        currentCombination = null;
    }

    private void setupResourceBundleCombinationsForClass(XClass clazz) throws XDocletException
    {
        combinations = new HashMap();

        Collection fields = clazz.getFields();

        for (FieldIterator j = XCollections.fieldIterator(fields); j.hasNext(); ) {
            XField field = j.next();
            XDoc doc = field.getDoc();

            if (doc != null && doc.hasTag(tagName)) {
                Collection tags = doc.getTags(tagName);

                for (TagIterator i = XCollections.tagIterator(tags); i.hasNext(); ) {
                    XTag tag = i.next();
                    String language = tag.getAttributeValue("language");
                    String country = tag.getAttributeValue("country");
                    String variant = tag.getAttributeValue("variant");
                    String combination_name = language + country + variant;
                    Combination combination = (Combination) combinations.get(combination_name);

                    if (combination == null) {
                        combination = new Combination(language, country, variant);

                        combinations.put(combination_name, combination);
                    }

                    combination.keys.add(field.getName().toLowerCase());
                    combination.values.add(tag.getAttributeValue(valueParamName));
                }
            }
            else {
                //throw new XDocletException("Couldn't find any field tags named @" + tagName + " in " + field.getContainingClass().getQualifiedName() + "." + field.getQualifiedName());
            }
        }
    }

    /**
     * @created   May 31, 2002
     */
    final static class Combination
    {
        public String language;
        public String country;
        public String variant;
        public List keys = new ArrayList();
        public List values = new ArrayList();

        public Combination(String language, String country, String variant)
        {
            this.country = country;
            this.language = language;
            this.variant = variant;
        }

        public String getCombinationNameAsResourceBundleName()
        {
            String name = "";

            if (language != null) {
                name += language;
            }

            if (country != null) {
                name += "_";
                name += country;
            }

            if (variant != null) {
                name += "_";
                name += variant;
            }

            if (name.length() > 0)
                return "_" + name;
            else
                return name;
        }

        public int hashCode()
        {
            return (language + country + variant).hashCode();
        }

        public boolean equals(Object obj)
        {
            if (!(obj instanceof Combination))
                return false;

            Combination other_combination = (Combination) obj;

            return this.language.equals(other_combination.language) &&
                this.country.equals(other_combination.country) &&
                this.variant.equals(other_combination.variant);
        }
    }
}
