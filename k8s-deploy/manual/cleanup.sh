#!/bin/bash


docker stop k8s-zookeeper; docker rm k8s-zookeeper
docker stop k8s-kafka; docker rm k8s-kafka
docker stop topic-init; docker rm topic-init