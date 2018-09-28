/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.externalizer;

import java.util.*;
import xdoclet.DocletContext;
import xdoclet.DocletTask;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xdoclet.util.Translator;

/**
 * @author               Ara Abrahamian (ara_e_w@yahoo.com)
 * @created              May 30, 2002
 * @xdoclet.taghandler   namespace="Externalizer"
 * @version              $Revision: 1.4 $
 */
public class ExternalizerTagsHandler extends XDocletTagSupport
{
    private String  currentKey;
    private String  currentValue;

    /**
     * Evaluate the body for all field tags. Works only in context of ExternalizerSubTask.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @doc.tag                     type="block"
     */
    public void forAllFieldTags(String template) throws XDocletException
    {
        ExternalizerSubTask.Combination combination = ((ExternalizerSubTask) getDocletContext().getActiveSubTask()).getCurrentCombination();

        for (int i = 0; i < combination.keys.size(); i++) {
            currentKey = (String) combination.keys.get(i);
            currentValue = (String) combination.values.get(i);

            generate(template);
        }

        currentKey = null;
        currentValue = null;
    }

    /**
     * current resource bundle name, will be called by TranslatorSubTask
     *
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String bundleKey() throws XDocletException
    {
        if (DocletContext.getInstance().isSubTaskDefined(DocletTask.getSubTaskName(ExternalizerSubTask.class))) {
            return ((ExternalizerSubTask) DocletContext.getInstance().getSubTaskBy(DocletTask.getSubTaskName(ExternalizerSubTask.class))).getBundleKey(getCurrentClass());
        }
        else {
            throw new XDocletException(Translator.getString("xdoclet.modules.externalizer.ExternalizerSubtaskMessages",
                ExternalizerSubtaskMessages.TRANSLATOR_DEPENDS_ON_EXTERNALIZER));
        }
    }

    /**
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String key() throws XDocletException
    {
        return currentKey;
    }

    /**
     * @return
     * @exception XDocletException
     * @doc.tag                     type="content"
     */
    public String value() throws XDocletException
    {
        return currentValue;
    }
}
