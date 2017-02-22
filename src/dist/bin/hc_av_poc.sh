#!/bin/bash

java -classpath ../conf:../../../target/hazelcastHAPOC-0.1-SNAPSHOT-jar-with-dependencies.jar com.lambdazen.hazelcast.CommandExecutor "$@"