# Set the file name of your jar package:
JAR_PKG = QINQ

#Set name of class to run
MAIN_CLASS = qinq.application.Main
BINDIR = bin/

JAVAC = javac
JAVA = java
JFLAGS = -encoding UTF-8
CLASSPATH = resource/*:src
SRCS = $(shell find src/ -type f -name '*java')
RFILES = $(shell find src/ -type f -not -name '*class' -not -name '*java')

all: jar clean

compile:
	javac -cp $(CLASSPATH) $(JFLAGS) $(SRCS)

jar: compile $(BINDIR)
	jar cvfe $(BINDIR)/$(JAR_PKG).jar $(MAIN_CLASS) -C resource "" $(shell find resource/ -type f -name '*class') -C src "" $(RFILES) $(shell find src/ -type f -name '*class')
	chmod +x $(BINDIR)/$(JAR_PKG).jar

$(BINDIR):
	mkdir $(BINDIR)

clean:
	rm src/*/*/*class

cleanall: clean
	rm $(BINDIR)/$(JAR_PKG).jar