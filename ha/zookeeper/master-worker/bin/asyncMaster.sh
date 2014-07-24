#!/bin/bash

ZOOBINDIR=/home/alesha/opt/zookeeper-3.4.6/bin
. "$ZOOBINDIR"/zkEnv.sh

# echo $CLASSPATH

java -cp "$CLASSPATH"./target/master-worker-1.0.jar org.futucode.ha.zk.master.AsyncMaster "$@"

