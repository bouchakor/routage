#!/bin/bash

clear
echo
JAVA=`find org -iname "*.java"`
echo "compiling..."
javac $JAVA 2> errlog.javac
echo "done. Errors are reported in errlog.javac"
CLASS=`find org -iname "*.class" -o -iname "*.properties"`
echo "building graphstream.jar file..."
jar cvf graphstream.jar $CLASS > /dev/null 2> errlog.jar 
echo "done. Errors are reported in errlog.jar"
echo 
echo "Thanks for using graphstream"
