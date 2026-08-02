#!/bin/bash

# Compile source files
find documentprocessor -name "*.java" > sources.txt
javac -d out --class-path "lib/junit-jupiter-api-5.9.1.jar" @sources.txt

# Run tests
java -jar lib/junit-platform-console-standalone-1.9.1.jar --class-path out --scan-class-path
