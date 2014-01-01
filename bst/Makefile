#!/bin/bash
.PHONY:PACKAGE 
.PHONY: TEST
export BIN := bin
export TEST := test
export JAR := ~/jar
export TMP := 
export SRC := src
all: PACKAGE TEST  

TEST: $(TEST)/AllTests.java $(TEST)/TestBST.java
	javac -g -d  $(BIN) $^; \
	cd $(BIN);
 
PACKAGE: $(SRC)/Node.java $(SRC)/BST.java
	rm $(BIN)/* -fr; \
	rm $(JAR)/bst.jar;\
	javac -g -d $(BIN) $^; \
	cd $(BIN);\
	jar cvf $(JAR)/bst.jar *;\
	rm * -fr;
	 


