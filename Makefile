.PHONY: all build run clean

CLASSPATH := out:lib/sqlite-jdbc.jar

MAIN_CLASS := main.Main

all: build run

run: build
	@echo "Running Java application..."
	java -cp $(CLASSPATH) $(MAIN_CLASS)

build:
	@echo "Compiling Java source files..."
	mkdir -p out
	javac -d out -cp lib/sqlite-jdbc.jar $(shell find src -name "*.java")
