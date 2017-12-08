#!/bin/sh

kill -9 `ps -ef | grep xyqp-pdk.jar | grep java | awk '{print $2}'`;
sleep 2;

nohup java -jar xyqp-pdk.jar >/dev/null 2>&1 &