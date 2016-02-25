#!/bin/bash

cdir=$(cd "$(dirname "$0")"; pwd)

RUNJAVA=/usr/sbin/manerfan/jdk1.8.0_65/bin/java
JAVA_OPTS=-server -Xms64M -Xmx512M -XX:MaxNewSize=256M

CLASSPATH=./resources
WEB_CONTENT=webapp

CONTEXT_PATH=/
PORT=80

start $RUNJAVA$ -Xbootclasspath/a:$CLASSPATH$ $JAVA_OPTS$ -jar mblog.jar -cp $CONTEXT_PATH$ -wc $WEB_CONENT$ -p $PORT$