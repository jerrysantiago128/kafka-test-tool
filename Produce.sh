#!/bin/bash

dir=$(pwd)
jar=$(find "$dir" -type f -name "*.jar")

java -cp $dir/target/kafka-test-1.0-SNAPSHOT.jar com.test.kafka.Producer "$@"
