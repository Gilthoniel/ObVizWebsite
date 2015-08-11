#!/usr/bin/env bash

file0="RUNNING_PID"
file1="bin/obviz-app"
appDirectory="target/universal/stage"

# Java 8 is required
export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"

if [ -e "$appDirectory" ]
then

    cd "$appDirectory";

    if [ -e "$file0" ]
    then
        # Get the PID of the process
        pid=$(cat "$file0")

        # Kill the process
        kill $pid
        rm "$file0"

        echo "Old process killed."
    else
        echo "Server is currently not running."
    fi

    if [ -e "$file1" ]
    then
        echo "The server is starting ..."
        bash $file1 &
    else
        echo "Script is not found. Have you stage the project ?"
    fi

else
    echo "Can't find the directory where the staged project should be present"
fi