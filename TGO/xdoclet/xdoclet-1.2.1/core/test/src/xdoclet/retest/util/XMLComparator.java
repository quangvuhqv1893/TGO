package xdoclet.retest.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import java.util.Stack;

import xdoclet.XDocletException;
import xdoclet.retest.XDocletRetestMessages;

/**
 * @author    David Jencks (davidjencks@directvinternet.com)
 * @created   Mars 5, 2002
 * @version   $Revision: 1.5 $
 */
public class XMLComparator
        extends AbstractComparator
{

    private Stack              refStack = new Stack();
	private Stack              compStack = new Stack();
    private Node one;
    private Node two;
    private boolean attributeValidation = true;

    /**
     * Define the Node names (eg &lt;something&gt;) that are rejected during the
     * comparison.
     * Rejected values are
     * <ul>
     * <li>&lt;description&gt; : EJB and web deployment descriptor does not need an
     * equality to be equal
     * </ul>
     */
    static final String[] REJECTED = {
        "description"
    };

    /**
     * Default constructor where you specify the two Node to compare
     *
     * @param one the first node
     * @param two the second node
     */
    public XMLComparator( Node one, Node two )
	{
		this.one = one;
        this.two = two;
        resultSet = new ComparisonResultSet();
	}

    /**
     * Start the comparison
     *
     * @return a <code>xdoclet.retest.utilComparisonResultSet</code> containing
     * potential differences.
     */
    public ComparisonResultSet compare()
	throws XDocletException
    {
		compare( one, two, new Counter() );
		return resultSet;
	}

    /**
     * In EJB deployment descripor we do not have to take care of Attribute
     * value.  Attribute is something like &lt;tag attribute=&quot;value&quot;&gt;
     *
     * @param value specify if you want attribute validation
     */
    public void setAttributeValidation(boolean value)
    {
        this.attributeValidation = value;
    }

	private boolean valueDiffers( String r, String c )
	{
		if( r == null && c == null )
		{
			return false;
		}
		if( r != null && c != null )
		{
			return !r.equals( c );
		}
		return true;
	}

	private Node skipEmpty( Node node )
	{
		Node n = node;

		while( n != null && !nodeHasContentOrAttributes( n ) )
		{
			n = n.getNextSibling();
		}
		return n;
	}

	private boolean nodeHasContentOrAttributes( Node n )
	{
		if( n == null )
		{
			return false;
		}
        // The # test is to consider #text and #cdata-section like nothing
		if( n.getNodeValue() != null
                && !n.getNodeValue().trim().equals( "" )
                && !n.getNodeName().equals("#cdata-section")
                && !n.getNodeName().equals("#comment"))

		{
            //System.out.println(n.getNodeName() + "---" + n.getNodeValue());
			return true;
		}

        if (attributeValidation){
            NamedNodeMap AttList = n.getAttributes();

            if( AttList != null && AttList.getLength() > 0 )
            {
                return true;
            }
        }

		Node c = n.getFirstChild();

		while( c != null )
		{
			if( nodeHasContentOrAttributes( c ) )
			{
                //System.out.println("222 "+n.getNodeName() + "---" + n.getNodeValue());
				return true;
			}
			c = c.getNextSibling();
		}
		return false;
	}

	private void compare( Node reference, Node comp, Counter c )
	throws XDocletException
    {
		if( reference == null )
		{
			if( comp == null )
			{
				return;
			}
			else
			{
				logDifference( "null reference node unmatched" );
				return;
			}
		}
		if( comp == null )
		{
			logDifference( "null comparison node unmatched" );
			return;
		}
		if( reference instanceof Element )
		{
			refStack.push( ( ( Element ) reference ).getTagName() + c.incRefCount() );
		}
		if( comp instanceof Element )
		{
			compStack.push( ( ( Element ) comp ).getTagName() + c.incCompCount() );
		}

            if( valueDiffers( reference.getNodeName(), comp.getNodeName() ) )
            {
                logDifference( "Mismatch NODE NAME, reference : " + reference.getNodeName() +
                        " differs from new: " + comp.getNodeName() );
            }

        if ( ! rejectedNodeName(reference.getNodeName()) &&  ! rejectedNodeName(comp.getNodeName())){
            if( valueDiffers( reference.getNodeValue(), comp.getNodeValue() ) )
            {
                logDifference( "Mismatch NODE VALUE, reference : " + reference.getNodeName()+"."+reference.getNodeValue() +
                    " differs from new: " + comp.getNodeName()+"."+comp.getNodeValue() );
            }
        }

        if (attributeValidation)
        {
            NamedNodeMap refAttList = reference.getAttributes();
            NamedNodeMap compAttList = comp.getAttributes();
            int refAttListLength = ( refAttList == null ) ? 0 : refAttList.getLength();
            int compAttListLength = ( compAttList == null ) ? 0 : compAttList.getLength();

            if( refAttListLength != compAttListLength )
            {
                logDifference( "Differing attribute count, reference : " + refAttListLength +
                    " differs from new: " + compAttListLength );
            }
            for( int i = 0; i < Math.min( refAttListLength, compAttListLength ); i++ )
            {
                compare( refAttList.item( i ), compAttList.item( i ), c );
            }
        }

		Node refChild = skipEmpty( reference.getFirstChild() );
		Node compChild = skipEmpty( comp.getFirstChild() );
		Counter subC = new Counter();

		while( refChild != null && compChild != null )
		{
            while( refChild != null && refChild.getNodeType() == 8)
            {
                refChild = skipEmpty( refChild.getNextSibling() );
            }

            while( compChild != null && compChild.getNodeType() == 8)
            {
                compChild = skipEmpty( compChild.getNextSibling() );
            }

			compare( refChild, compChild, subC );
			if (refChild != null) refChild = skipEmpty( refChild.getNextSibling() );
			if (compChild != null)compChild = skipEmpty( compChild.getNextSibling() );
		}

        while(refChild != null && refChild.getNodeType() == 8)
		{
			refChild = skipEmpty( refChild.getNextSibling() );
		}

        while( compChild != null && compChild.getNodeType() == 8)
		{
			compChild = skipEmpty( compChild.getNextSibling() );
		}

		//check for leftovers
		while( refChild != null )
		{
			logDifference( "unmatched in reference : " + refChild );
			refChild = skipEmpty( refChild.getNextSibling() );
		}
		while( compChild != null )
		{
			logDifference( "unmatched in compare : " + compChild );
			compChild = skipEmpty( compChild.getNextSibling() );
		}

		if( reference instanceof Element )
		{
			refStack.pop();
		}
		if( comp instanceof Element )
		{
			compStack.pop();
		}
	}

	private void logDifference( String message )
	throws XDocletException
    {
        resultSet.addError(XDocletRetestMessages.XML_DIFF,new String[]{message});
	}

    private boolean rejectedNodeName(String s)
    {
        for (int i = 0 ; i < REJECTED.length ; i++)
        {
            if (s.equals(REJECTED[i])) return true;
        }
            return false;
    }

	private class Counter
	{
		private int       refCount = 0;
		private int       compCount = 0;
		int incRefCount()
		{
			return ++refCount;
		}
		int incCompCount()
		{
			return ++compCount;
		}
	}


}
