
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Node.java \
	PairPOJO.java \
	DataNode.java \
	IndexNode.java \
	BPlusTree.java \
 	treesearch.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class