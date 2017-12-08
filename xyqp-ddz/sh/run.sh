#!/bin/sh

kill -9 `ps -ef | grep xyqp-ddz.jar | grep java | awk '{print $2}'`;
sleep 2;

nohup java -jar xyqp-ddz.jar >/dev/null 2>&1 &