#!/bin/bash


docker build -t k8s_produce:0.10.0 -f Dockerfile-K8s-Produce .


docker build -t k8s_consume:0.10.0 -f Dockerfile-K8s-Consume .