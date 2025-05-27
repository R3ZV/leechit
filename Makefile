run: build
	java -cp out Main .:lib/sqlite-jdbc.jar 

build:
	javac -d out $(shell find src -name "*.java") || exit 1
