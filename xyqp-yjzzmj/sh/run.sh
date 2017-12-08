#!/bin/sh

kill -9 `ps -ef | grep xyqp-yjzzmj.jar | grep java | awk '{print $2}'`;
sleep 2;

nohup java -jar xyqp-yjzzmj.jar >/dev/null 2>&1 &