*********************************************
** XDoclet: Attribute-Oriented Programming **
*********************************************

XDoclet is a code generation engine. It enables Attribute-Oriented Programming 
for java. In short, this means that you can add more significance to your code 
by adding meta data (attributes) to your java sources. This is done in special 
JavaDoc tags.

XDoclet will parse your source files and generate many artifacts such as XML 
descriptors and/or source code from it. These files are generated from templates 
that use the information provided in the source code and its JavaDoc tags.

---------------------------------
-- Obtaining and Documentation --
---------------------------------

The latest version and documentation of XDoclet is available here:

    http://xdoclet.sourceforge.net
        or
    http://sourceforge.net/projects/xdoclet/

---------------------------------
-- Building XDoclet            --
---------------------------------

In order to build XDoclet, you should have the CVS modules "xdoclet" (where this
file is) and "xjavadoc" side by side. If you check out the "xdoclet-all" module,
you'll get it that way.

Then you have to install Jakarta Ant 1.5.1 (http://ant.apache.org/) or
later. Put xalan.jar into the Jakarta Ant lib directory. If you want to build
the docs too, you also need Maven (http://maven.apache.org/) installed.

Then you just cd to the xdoclet folder (where this file is). And type:

    ant

More information is in the documentation, which you'll also find online:

    http://xdoclet.sourceforge.net/install.html

---------------------------------
-- Credits                     --
---------------------------------

Thank these guys:

    http://xdoclet.sourceforge.net/team-list.html