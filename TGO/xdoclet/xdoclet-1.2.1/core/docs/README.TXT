TASK/SUBTASK/TAG DOCO HOWTO

All the information in the HTML documents in this directory should be "moved" to the following places:

When all the info is moved, please CVS remove the html file.

*************** Task/SubTask doco *************** 
To get an overview of how the Task/SubTask doco generation works, compare 
http://users.pandora.be/ees/xdoclet/ant/xdoclet/modules/jboss/ejb/JBossSubTask.html
with
http://cvs.xdoclet.sourceforge.net/cgi-bin/viewcvs.cgi/xdoclet/xdoclet/modules/jboss/src/xdoclet/modules/jboss/ejb/JBossSubTask.java?rev=1.13&content-type=text/vnd.viewcvs-markup

As you see, javadocs (and special @ant tags) end up in the Maven docs. Inherited attributes and nested elements
will be documented too. This is cool stuff :-) The intermediate result is
xml files (xdoc syntax) that Maven turns into HTML. These will be written to xdoclet\target\generated-xdocs\ant

The information in the "upper" part of each old HTML document describing the usage 
of a task/subtask should be moved
to the corresponding java file. All information in the "description" column should be moved to
general javadoc comments in the setXXX() method of the corresponding "attribute". 

The contents of the "Required" column should be moved to a @ant.required tag. 
See JBossSubTask for an example illustrating how it's done.
If the @ant.required tag is missing, "No." will be the output. 
If present, but empty, "Yes." If present with content,
the content of the tag will be put in the cell.

Merge files can be documented with one or more class-level 
@xdoclet.merge-file file="foobar.xdt" description="yadda" tags.

setXxx methods that should not be exposed to the docs as Ant attributes can be excluded from docs by adding a
@ant.ignore tag.

***************  Tag doco (mostly done now) *************** 
The information in the "lower" part of each document describing the usage of the tags should be moved to
the xtags.xml file of the corresponding module.