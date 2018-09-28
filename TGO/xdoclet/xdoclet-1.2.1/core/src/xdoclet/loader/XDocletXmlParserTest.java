/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.loader;

import java.io.FileInputStream;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author    <a href="mailto:aslak.nospam@users.sf.net">Aslak Hellesøy</a>
 * @created   4 mei 2002
 * @version   $Revision: 1.5 $
 */
public class XDocletXmlParserTest extends TestCase
{
    public XDocletXmlParserTest(String name)
    {
        super(name);
    }

    public void testParse() throws Exception
    {
        FileInputStream wls = new FileInputStream("test/src/xdoclet-xmlparsertest.xml");
        XDocletXmlParser parser = new XDocletXmlParser();
        XDocletModule wlsModule = parser.parse(new InputSource(wls));
    }
}
