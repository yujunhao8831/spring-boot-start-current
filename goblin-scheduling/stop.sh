#!/bin/bash
PID=$(ps -ef | grep goblin-scheduling.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo "服务已关闭"
else
    echo kill $PID
    kill $PID
fi
