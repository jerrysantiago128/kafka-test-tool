#!/bin/bash

# Usage: ./create_topic.sh <topic_name> <bootstrap_server>
if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <topic_name> <bootstrap_server>"
  exit 1
fi

export BOOTSTRAP_SERVER="$1"
export TOPIC_NAME="$2"


echo "Cleaning up environment"

docker stop zookeeper-test; docker rm zookeeper-test 
docker stop k8s-test; docker rm k8s-test
docker stop topic-init; docker rm topic-init


echo "Creating topic '$TOPIC_NAME' on bootstrap server '$BOOTSTRAP_SERVER'..."

#docker compose -f k8s-compose.yml up -d 

# Run the topic-init service; --rm removes the container after execution
docker compose -f k8s-compose.yml run --rm topic-init

