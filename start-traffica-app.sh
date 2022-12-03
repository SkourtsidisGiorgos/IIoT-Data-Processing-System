#!/bin/bash

#start=`date +%s`
printf -v start '%(%Y-%m-%d %H:%M:%S)T' -1
echo "Starting traffica application: startTime=${start}"


if [ $# -ne 1 ]
  then
    echo "There are missing arguments."
    exit 0
fi

profile=$1

echo "Provided CLI arguements: profile=${profile}"
echo "Exporting parameters"

export JAVA_HOME="/shared/cosmote/tools/jdk/jdk-11.0.16.1"
export PATH=$JAVA_HOME/bin:$PATH

echo "Checking java version"
java_version=$(java --version | grep "openjdk 11.")
echo "Java version=${java_version}"
if [ -z "$java_version" ]
	then
		echo "You have not given me the correct java version."
		echo "Use java 11"
		exit 0
fi

if [ $profile == "voice" ]
then
  jmx_port=11582
else
  jmx_port=11583
fi

echo "JMX host:port=`hostname`:${jmx_port}"

log_config="log4j2_${profile}.xml"

echo "Will use log config=${log_config}"

echo "Executing jar"

nohup java \
  -Xlog:gc*=info:file=logs/gc_${profile}_`date +%Y%m%d_%H%M%S`.log:time,level,tags:filecount=10,filesize=10M \
  -XX:+UseParallelGC -XX:ParallelGCThreads=4 \
  -Xms500m -Xmx2g \
  -Dspring.profiles.active=${profile} \
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.port=${jmx_port} \
  -Djava.rmi.server.hostname=`hostname` \
  -jar traffica-app.jar \
  --logging.config=config/${log_config} \
  >> logs/nohup_traffica-${profile}-`date +%Y%m%d_%H%M%S`.log 2>&1 &
  
