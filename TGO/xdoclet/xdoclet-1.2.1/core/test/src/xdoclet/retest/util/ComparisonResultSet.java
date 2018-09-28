package xdoclet.retest.util;

import xdoclet.util.Translator;
import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author    Vincent Harcq (vincent.harcq@hubmethods.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.3 $
 */
public class ComparisonResultSet
{
    protected List messages;

    public ComparisonResultSet()
    {
        messages = new ArrayList();
    }

    public String[] getMessages()
    {
        return (String[])messages.toArray(new String[messages.size()]);
    }

    public void addError(String message, String[] params)
    throws XDocletException
    {
        messages.add(Translator.getString(XDocletRetestMessages.class, message, params));
    }

    public boolean error()
    {
        return messages.size() > 0;
    }

    public String toString()
    {
        String ret="";
        if (messages.size() > 0)
        {
            ret = ret + "==== " + messages.size() + " error(s) ====\n";
            Iterator it = messages.iterator();
            while (it.hasNext())
            {
                ret = ret + "========== " + (String)it.next() + "\n";
            }
        }
        return ret;
    }


}
