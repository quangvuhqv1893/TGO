/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.modules.ejb.dd;

import java.util.*;

import xjavadoc.*;

import xdoclet.XDocletException;

import xdoclet.modules.ejb.EjbTagsHandler;
import xdoclet.modules.ejb.intf.InterfaceTagsHandler;
import xdoclet.util.DocletUtil;

/**
 * @author               Ara Abrahamian (ara_e@email.com)
 * @created              Oct 15, 2001
 * @xdoclet.taghandler   namespace="EjbSec"
 * @version              $Revision: 1.7 $
 */
public class SecurityTagsHandler extends EjbTagsHandler
{
    /**
     * The current security role name, set by forAllSecurityRoles and returned by securityRoleName. It somehow is like
     * the current index for the forAllSecurityRoles loop.
     *
     * @see   #forAllSecurityRoles(java.lang.String)
     * @see   #securityRoleName()
     */
    protected transient String currentSecurityRoleName;

    /**
     * Returns current security role name set by the containing forAllSecurityRoles.
     *
     * @return                      Current security role name
     * @exception XDocletException
     * @see                         #forAllSecurityRoles(java.lang.String)
     * @doc.tag                     type="content"
     */
    public String securityRoleName() throws XDocletException
    {
        return currentSecurityRoleName;
    }

    /**
     * Evaluates the body block for each ejb:permission defined in class level or method level.
     *
     * @param template              The body of the block tag
     * @exception XDocletException
     * @see                         #hasPermission(xjavadoc.XDoc)
     * @see                         #securityRoleName()
     * @doc.tag                     type="block"
     */
    public void forAllSecurityRoles(String template) throws XDocletException
    {
        Collection classes = getXJavaDoc().getSourceClasses();

        Set roleSet = new HashSet();

        for (Iterator i = classes.iterator(); i.hasNext(); ) {
            XClass clazz = (XClass) i.next();

            setCurrentClass(clazz);

            // Get roles from class
            if (hasPermission(getCurrentClass().getDoc())) {
                Collection permissions = getCurrentClass().getDoc().getTags("ejb.permission");

                for (Iterator k = permissions.iterator(); k.hasNext(); ) {
                    XTag tag = (XTag) k.next();
                    String roleName = tag.getAttributeValue("role-name");

                    if (roleName != null) {
                        roleSet.addAll(Arrays.asList(DocletUtil.tokenizeDelimitedToArray(roleName, ",")));
                    }
                }
                // Add roles to set
            }

            // Get roles from methods
            Collection methods = getCurrentClass().getMethods();

            for (Iterator j = methods.iterator(); j.hasNext(); ) {
                XMethod method = (XMethod) j.next();

                setCurrentMethod(method);

                if (hasPermission(getCurrentMethod().getDoc()) && InterfaceTagsHandler.isInterfaceMethod(getCurrentMethod())) {
                    Collection permissions = getCurrentMethod().getDoc().getTags("ejb.permission");

                    for (Iterator k = permissions.iterator(); k.hasNext(); ) {
                        XTag tag = (XTag) k.next();
                        String role_name = tag.getAttributeValue("role-name");

                        if (role_name != null) {
                            roleSet.addAll(Arrays.asList(DocletUtil.tokenizeDelimitedToArray(role_name, ",")));
                        }
                    }
                    // Add role to set
                }
            }

            // get roles from finders
            Collection finders = getCurrentClass().getDoc().getTags("ejb:finder");

            for (Iterator j = finders.iterator(); j.hasNext(); ) {
                XTag tag = (XTag) j.next();
                String roleName = tag.getAttributeValue("role-name");

                if (roleName != null) {
                    roleSet.addAll(Arrays.asList(DocletUtil.tokenizeDelimitedToArray(roleName, ",")));
                }
            }

            // and from pk field ( if any )
            Collection pk = getCurrentClass().getDoc().getTags("ejb:pk");

            for (Iterator j = pk.iterator(); j.hasNext(); ) {
                XTag tag = (XTag) j.next();
                String roleName = tag.getAttributeValue("role-name");

                if (roleName != null) {
                    roleSet.addAll(Arrays.asList(DocletUtil.tokenizeDelimitedToArray(roleName, ",")));
                }
            }
        }

        // Output set of roles
        Iterator roleEnum = roleSet.iterator();

        while (roleEnum.hasNext()) {
            currentSecurityRoleName = (String) roleEnum.next();

            generate(template);
        }
    }

    /**
     * Returns true if class/method denoted by doc has ejb:permission tag, false otherwise.
     *
     * @param doc  Description of Parameter
     * @return     Description of the Returned Value
     * @todo       Shouldn't this method rather be in SecurityTagsHandler?
     */
    private boolean hasPermission(XDoc doc)
    {
        return doc.hasTag("ejb:permission");
    }
}
