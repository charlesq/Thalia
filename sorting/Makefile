#!/bin/bash
.PHONY:PACKAGE 
.PHONY: TEST
export BIN := bin
export TEST := test
export JAR := ~/jar
export TMP := TestSorting.class TestQuickSort.class TestHeapSort.class
export SRC := src
all: PACKAGE TEST  

TEST: $(TEST)/TestSorting.java $(TEST)/TestQuickSort.java $(TEST)/TestHeapSort.java $(TEST)/TestSuiteSort.java
	javac -g -d  $(BIN) $^; \
	cd $(BIN);\
 
PACKAGE: $(SRC)/Sort.java $(SRC)/QuickSort.java $(SRC)/HeapSort.java
	rm $(BIN)/* -fr; \
	rm $(JAR)/sorting.jar;\
	javac -g -d $(BIN) $^; \
	cd $(BIN);\
	jar cvf $(JAR)/sorting.jar *;\
	rm * -fr;
	 


