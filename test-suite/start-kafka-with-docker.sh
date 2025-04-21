#!/bin/bash

# Usage: ./create_topic.sh <topic_name> <bootstrap_server>
if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <topic_name> <bootstrap_server>"
  exit 1
fi

export TOPIC_NAME="$1"
export BOOTSTRAP_SERVER="$2"

echo "Cleaning up environment"

docker stop zookeeper-test; docker rm zookeeper-test 
docker stop kafka-main; docker rm kafka-main 
docker stop topic-init; docker rm topic-init

echo "Creating topic '$TOPIC_NAME' on bootstrap server '$BOOTSTRAP_SERVER'..."

# Run the topic-init service; --rm removes the container after execution
docker compose run --rm topic-init

