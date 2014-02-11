#!/bin/bash
.PHONY:PACKAGE 
.PHONY: TEST
export BIN := bin
export TEST := test
export JAR := ~/jar
export TMP := 
export SRC := src
all: PACKAGE TEST  

TEST: $(TEST)/TestSearch.java $(TEST)/GraphTests.java $(TEST)/TestMST.java 
	javac -g -d  $(BIN) $^; \
	cp graph.txt $(BIN)/.; \
	cd $(BIN);
 
PACKAGE: $(SRC)/Graph.java $(SRC)/DFS.java $(SRC)/Action.java $(SRC)/BFS.java $(SRC)/Edge.java $(SRC)/MST.java $(SRC)/Prim.java $(SRC)/DisjointedSet.java $(SRC)/Kruskal.java
	mkdir -p  $(BIN); \
	rm $(BIN)/* -fr; \
	rm $(JAR)/graph.jar;\
	javac -g -d $(BIN) $^; \
	cd $(BIN);\
	jar cvf $(JAR)/graph.jar *;\
	rm * -fr;
	 


