run: build
	java -cp out Main $(ARGS)

test:
	javac -d out/tests $(shell find tests -name "*.java") || exit 1
	gradle test

build:
	javac -d out $(shell find src -name "*.java") || exit 1

