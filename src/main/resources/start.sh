#!/bin/bash

cdir=$(cd "$(dirname "$0")"; pwd)

RUNJAVA=/usr/sbin/manerfan/jdk1.8.0_65/bin/java
# -Xms 最小堆空间
# -Xmx 最大堆空间
# -XX:MaxNewSize 最大新生代空间
# -XX:MaxMetaspaceSize 最大元空间(Java8 使用元空间取代持久代)
JAVA_OPTS=-server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Xms64M -Xmx512M -XX:MaxNewSize=256M -XX:MaxMetaspaceSize=128M

CLASSPATH=./resources
WEB_CONTENT=webapp

CONTEXT_PATH=/
PORT=80

start $RUNJAVA$ -Xbootclasspath/a:$CLASSPATH$ $JAVA_OPTS$ -jar mblog.jar -cp $CONTEXT_PATH$ -wc $WEB_CONTENT$ -p $PORT$