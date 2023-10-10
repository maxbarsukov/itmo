JAVAC=javac
sources = $(wildcard *.java) # all .java files
classes = $(sources:.java=.class)

.PHONY: clean jar run-jar

all: program

program: $(classes)

clean:
	rm -f *.class

%.class: %.java
	$(JAVAC) $<

jar: $(classes)
	jar -cvfm lab1.jar MANIFEST.mf $(classes) 

run-jar: jar
	java -jar lab1.jar
