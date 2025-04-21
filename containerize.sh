#!/bin/bash


docker build -t KafkaProduce:0.10.0 -f Dockerfile-Produce .


docker build -t KafkaConsume:0.10.0 -f Dockerfile-Consume .