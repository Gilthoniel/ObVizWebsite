#!/usr/bin/env bash

file0="RUNNING_PID"
appDirectory="target/universal/stage"

if [ -e "$appDirectory" ]
then

    cd "$appDirectory";

    if [ -e "$file0" ]
    then
        # Get the PID of the process
        pid=$(cat "$file0")

        # Kill the process
        kill $pid

        echo "Old process killed."
    else
        echo "Server is currently not running."
    fi

else
    echo "Can't find the directory where the staged project should be present"
fi