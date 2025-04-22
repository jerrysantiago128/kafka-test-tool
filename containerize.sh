#!/bin/bash


docker build -t kafka_produce:0.10.0 -f Dockerfile-Produce .


docker build -t kafka_consume:0.10.0 -f Dockerfile-Consume .