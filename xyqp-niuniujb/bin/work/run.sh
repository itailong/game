#!/bin/sh

kill -9 `ps -ef | grep xyqp-niuniujb.jar | grep java | awk '{print $2}'`;
sleep 2;

nohup java -jar xyqp-niuniujb.jar >/dev/null 2>&1 &