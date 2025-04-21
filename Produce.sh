#!/bin/bash

dir=$(pwd)
jar=$(find "$dir" -type f -name "*.jar")

java -cp $dir/target/kafka-test-tool-0.10.0.jar msps.test.kafka.Producer "$@"
