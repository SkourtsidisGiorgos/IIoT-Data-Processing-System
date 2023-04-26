#!/bin/bash

APP_NAME=streaming-iot
#start=`date +%s`
printf -v start '%(%Y-%m-%d %H:%M:%S)T' -1
echo "Starting ${APP_NAME} application: startTime=${start}"

if [ $# -ne 1 ]
  then
    echo "There are missing arguments. Please provide the profile name (dev, test, prod)"
    exit 0
fi

profile=$1
echo "Provided CLI arguements: profile=${profile}"
echo "Exporting parameters"

export JAVA_HOME="~/torun/java/openjdk/jdk-11.0.16.1/bin/java"
export PATH=$JAVA_HOME/bin:$PATH

echo "Checking java version"
java_version=$(java --version | grep "openjdk")
echo "Java version=${java_version}"
#if [ -z "$java_version" ]
#	then
#		echo "You have not given me the correct java version."
#		echo "Use java 11"
#		exit 0
#fi

jmx_port=10095

echo "JMX host:port=`hostname`:${jmx_port}"

log_config="logback.xml"

echo "Will use log config=${log_config}"

echo "Executing jar"

nohup java \
  -Xlog:gc*=info:file=logs/gc_${APP_NAME}_`date +%Y%m%d_%H%M%S`.log:time,level,tags:filecount=10,filesize=10M \
  -XX:+UseParallelGC -XX:ParallelGCThreads=4 \
  -Xms500m -Xmx2g \
  -Dspring.profiles.active=${profile} \
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.port=${jmx_port} \
  -Djava.rmi.server.hostname=`hostname` \
  -jar streaming-iot.jar \
  --logging.config=config/${log_config} \
  >> logs/nohup_${APP_NAME}-`date +%Y%m%d_%H%M%S`.log 2>&1 &
  
